/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 *
 * @author T. Takeuchi
 */
public class SocketServer implements Runnable {

    private final int port;
    private final Thread thread;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public SocketServer(int port) throws IOException {
        this.port = port;

        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            for (SelectionKey selectionKey : selector.selectedKeys()) {
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel;
                    try {
                        socketChannel = serverSocketChannel.accept();
                        if (socketChannel == null) continue;
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        socketChannel = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

                    Charset charset = Charset.forName("US-ASCII");
                    ByteBuffer byteBuffer = ByteBuffer.allocate(8192);

                    try {
                        switch (socketChannel.read(byteBuffer)) {
                        case -1:
                            socketChannel.close();
                            break;
                        case 0:
                            continue;
                        default:
                            byteBuffer.flip();
                            System.out.print("EEE: " + charset.decode(byteBuffer));

                            socketChannel.write(charset.encode("Good bye!\r\n"));

                            socketChannel.close();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public int getPort() {
        return port;
    }
}
