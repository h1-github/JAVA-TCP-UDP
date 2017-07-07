package com.qingcheng.tcpudp.tcptesting;

import com.qingcheng.tcpudp.utils.L;

import java.io.BufferedReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by wanghuan on 2017/7/7.
 */
public class NIOTCPServerTesting {

    public static void main(String[] args){
        try {
            Selector selector = Selector.open();
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            socketChannel.socket().bind(new InetSocketAddress(9099));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector , SelectionKey.OP_ACCEPT);

            while (true){
                L.w("开始循环");

                if(selector.select(3000) == 0){
                    L.i("没有什么信息");
                    continue;
                }

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()){
                    SelectionKey selectionKey = keyIterator.next();
                    if(selectionKey.isAcceptable()){
                        L.i("= isAcceptable =");
                        SocketChannel channel = ((ServerSocketChannel)selectionKey.channel()).accept();
                        ByteBuffer byteBuffer = ByteBuffer.wrap("连接建立".getBytes("utf-8"));
                        channel.write(byteBuffer);
                        channel.configureBlocking(false);
                        channel.register(selectionKey.selector() , SelectionKey.OP_READ , ByteBuffer.allocate(1024));
                    }
                    if(selectionKey.isReadable()){
                        L.i("= isReadable =");
                        SocketChannel channel = (SocketChannel)selectionKey.channel();
                        ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();
                        long size = channel.read(byteBuffer);
                        if(size == -1){
                            channel.close();
                        }else {
                            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        }
                    }
                    if(selectionKey.isConnectable()){
                        L.i("= isConnectable =");
                    }
                    if(selectionKey.isValid()){
                        L.i("= isValid =");
                    }
                    if(selectionKey.isWritable()){
                        L.i("= isWritable =");
                        ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();
                        byteBuffer.flip();
                        SocketChannel channel = (SocketChannel)selectionKey.channel();
                        channel.write(byteBuffer);
                        if(!byteBuffer.hasRemaining()){
                            selectionKey.interestOps(SelectionKey.OP_READ);
                        }
                        byteBuffer.compact();
                    }
                    keyIterator.remove();
                }
                Thread.sleep(3000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
