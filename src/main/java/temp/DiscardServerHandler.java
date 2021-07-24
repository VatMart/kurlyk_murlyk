package temp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		// Discard the received data silently.
		// System.out.println(msg);

		((ByteBuf) msg).release(); // (3)
		// ctx.channel().writeAndFlush(ctx.alloc().buffer().writeBytes("eng:ok
		// rus:��������������\n".getBytes(Charset.forName("UTF-8"))));
		ByteBuf buf = Unpooled.buffer();
		buf.writeCharSequence("rus:�������", Charset.forName("UTF-8"));
		ctx.channel().write(buf);
		
		// ByteBuf in = (ByteBuf) msg;
		// ctx.write(msg); // (1)
		// ctx.flush(); // (2)
		try {
//			while (in.isReadable()) { // (1)
//				System.out.print((char) in.readByte());
//				System.out.flush();
//			}
			// System.out.println(in.toString(io.netty.util.CharsetUtil.UTF_8));
		} finally {
			// ReferenceCountUtil.release(msg); // (2)
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) { // (1) ���������� ��� ������ ���������� �����������
		// final ByteBuf time = ctx.alloc().buffer(4); // �������� ����� �� ��������� ��
		// 4 �����
		// time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

		// final ChannelFuture f = ctx.writeAndFlush(time); // (3)
		// f.addListener(ChannelFutureListener.CLOSE); // ��������� ���������� �����
		// ������� �������� ���������
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		// ctx.close();
	}
}