package server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import temp.Mapped;
import temp.ReflectionTools;
import temp.UriHandlerBased;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class HttpHelloWorldServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
	private HttpRequest request;
	private final StringBuilder buf = new StringBuilder();
	private Map<String, UriHandlerBased> handlers = new HashMap<String, UriHandlerBased>();
	
	public HttpHelloWorldServerHandler() {
        if (handlers.size()==0) {
            try {
                for (Class c : ReflectionTools.getClasses(getClass().getPackage().getName() + ".handlers")) {
                    Annotation annotation = c.getAnnotation(Mapped.class);
                    if (annotation!=null) {
                        handlers.put(((Mapped) annotation).uri(), (UriHandlerBased)c.newInstance());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
		UriHandlerBased handler = null;
        if (msg instanceof HttpRequest) {
        	System.out.println("ITS REQUEST");
            HttpRequest request = this.request = (HttpRequest) msg;
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
            buf.setLength(0);
            String context = queryStringDecoder.path();
            handler = handlers.get(context);
            if (handler!=null) {
                handler.process(request, buf);
                System.out.println(buf.toString() + "-BUFWORK!!!");
            }
        }
        
        if (msg instanceof LastHttpContent) {
        	System.out.println("ITS LASTHTTPCONTENT, BUF CONTENT: "+buf.toString());
            FullHttpResponse response = new DefaultFullHttpResponse (
                    HttpVersion.HTTP_1_1,
                    ((LastHttpContent) msg).decoderResult().isSuccess()? OK : HttpResponseStatus.BAD_REQUEST,
                    Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8)
            );
            response.headers().set(CONTENT_TYPE, handler!=null ? handler.getContentType() : "text/plain; charset=UTF-8");

            if (HttpUtil.isKeepAlive(request)) {
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            }
            ctx.write(response);
        }
		
		
		
//		if (msg instanceof HttpRequest) {
//			HttpRequest req = (HttpRequest) msg;
//
//			boolean keepAlive = HttpUtil.isKeepAlive(req);
//			FullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), OK,
//					Unpooled.wrappedBuffer(CONTENT));
//			response.headers().set(CONTENT_TYPE, TEXT_PLAIN).setInt(CONTENT_LENGTH, response.content().readableBytes());
//
//			if (keepAlive) {
//				if (!req.protocolVersion().isKeepAliveDefault()) {
//					response.headers().set(CONNECTION, KEEP_ALIVE);
//				}
//			} else {
//				// Tell the client we're going to close the connection.
//				response.headers().set(CONNECTION, CLOSE);
//			}
//
//			ChannelFuture f = ctx.write(response);
//
//			if (!keepAlive) {
//				f.addListener(ChannelFutureListener.CLOSE);
//			}
//		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
