package com.qingcheng.tcpudp.udptesting;

import com.qingcheng.tcpudp.utils.L;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by wanghuan on 2017/7/6.
 */
public class UDPServerTesting {

    public static void main(String[] args) {
        UDPServerTesting testing = new UDPServerTesting();
        testing.run();
    }

    public void run() {
        L.i("start  udptesting server");
        try {
            DatagramSocket ds = new DatagramSocket(10092);

            byte[] buff = new byte[1024];
            DatagramPacket dp_read = new DatagramPacket(buff , 1024);
            L.t("UDP 服务器已经启动完毕 等待客户端连接");
            boolean flag = true;
            while (flag){
                ds.receive(dp_read);
                byte[] data = dp_read.getData();
                int length = dp_read.getLength();
                L.d("length -> "+ length);
                byte[] realData = new byte[length];
                if(length > 0){
                    for (int i = 0; i < length; i++){
                        realData[i] = data[i];
                    }
                }

                String text = new String(realData);
                L.i("接收到来自客户端的消息：" + text);
                String response = "回复：" + text;
                if(text == null || "close".equals(text)){
                    flag = false;
                    L.d("远程关闭了服务");
                    response = "即将关闭服务器";
                }
                DatagramPacket dp_send = new DatagramPacket(response.getBytes(), response.getBytes().length, dp_read.getAddress(), dp_read.getPort());
                ds.send(dp_send);
                dp_read.setLength(1024);
            }
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
