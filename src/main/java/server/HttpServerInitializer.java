package server;

import com.github.vanbv.num.json.JsonParserDefault;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerKeepAliveHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;

	public HttpServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}
		p.addLast(new HttpServerCodec())
		.addLast("HttpServerKeepAlive", new HttpServerKeepAliveHandler())
		//p.addLast("decoder", new HttpRequestDecoder());
        //p.addLast("encoder", new HttpResponseEncoder());
		//p.addLast(new HttpServerExpectContinueHandler());
		//p.addLast(new HttpHelloWorldServerHandler());
		.addLast("HttpObjectAggregator", new HttpObjectAggregator(10 * 1024 * 102, true))
		.addLast("HttpChunkedWrite", new ChunkedWriteHandler())
        .addLast(new HttpMappedHandler(new JsonParserDefault()));
	}

}
