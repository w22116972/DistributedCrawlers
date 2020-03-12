package example.netty.transportserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.nio.NioEventLoopGroup;

import java.io.IOException;
import java.nio.charset.Charset;

public class NettyNioServer {

    public void server(int port) throws IOException {
        try {
            final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi", Charset.forName("UTF-8")));
            NioEventLoopGroup masterGroup = new NioEventLoopGroup(1);
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap b = new ServerBootstrap();
            b.group(masterGroup, workerGroup);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
