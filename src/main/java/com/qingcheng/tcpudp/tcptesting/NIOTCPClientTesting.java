package com.qingcheng.tcpudp.tcptesting;

import com.qingcheng.tcpudp.utils.L;

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
public class NIOTCPClientTesting {

    public static void main(String[] args){
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            boolean isConnected = socketChannel.connect(new InetSocketAddress("localhost" , 9099));
            if(!isConnected){
                while (!socketChannel.finishConnect()){
                    L.t("等待连接完成");
                    Thread.sleep(500);
                }
            }
            L.t("连接成功");
            String msg = "你好";
            ByteBuffer writeBuffer = ByteBuffer.wrap(msg.getBytes("utf-8"));
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            if(writeBuffer.hasRemaining()){
                L.d("向服务器发送数据...");
                socketChannel.write(writeBuffer);
                L.d("发送完毕");
            }
            boolean flag = true;
            while (flag){
                int read = socketChannel.read(readBuffer);
                L.d("read status : " + read);
                if(read == -1){
                    flag = false;
                    L.d("接收完毕 准备退出");
                }
                L.d("接收到服务器消息 : " + new String(readBuffer.array()));
                if(read == 0){
                    L.d("向服务器发送数据...");
                    socketChannel.write(writeBuffer);
                    L.d("发送完毕");
                }
                Thread.sleep(5000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
