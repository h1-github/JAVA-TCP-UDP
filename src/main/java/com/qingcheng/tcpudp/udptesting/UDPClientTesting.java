package com.qingcheng.tcpudp.udptesting;

import com.qingcheng.tcpudp.tcptesting.TCPClientTesting;
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
public class UDPClientTesting {

    public static void main(String[] args) {
        UDPClientTesting testing = new UDPClientTesting();
        testing.run();
    }

    public void run() {
        L.i("start  udptesting client");
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            boolean flag = true;
            while (flag) {
                L.d("等待输入信息：");
                String readText = input.readLine();
                if (readText == null || readText.equalsIgnoreCase("bye")) {
                    flag = false;
                    L.d("退出客户端");
                } else {
                    DatagramSocket ds = new DatagramSocket(10091);
                    InetAddress address = InetAddress.getLocalHost();

                    DatagramPacket dp_send = new DatagramPacket(readText.getBytes("utf-8"), readText.getBytes("utf-8").length, address, 10092);
                    byte[] buff = new byte[1024];
                    DatagramPacket dp_read = new DatagramPacket(buff, 1024);
                    ds.setSoTimeout(10000);
                    int times = 0;
                    boolean receiveResponse = false;
                    while (!receiveResponse && times < 3) {
                        L.i("第 " + times + " 次向服务器发送数据: " + new String(dp_send.getData()));
                        ds.send(dp_send);
                        try {
                            L.i("等待服务器返回数据:");
                            ds.receive(dp_read);
                            receiveResponse = true;
                        }catch (Exception e){
                            times++;
                            L.d("超时没有接收到服务返回：第 " + times + " 次");
                        }
                    }
                    if(receiveResponse){

                        byte[] data = dp_read.getData();
                        int length = dp_read.getLength();
                        L.d("length -> "+ length);
                        byte[] realData = new byte[length];
                        if(length > 0){
                            for (int i = 0; i < length; i++){
                                realData[i] = data[i];
                            }
                        }

                        String response = new String(realData);
                        L.i("接收到服务器返回 ：" + response);
                    }else {
                        L.w("超次数没有收到反馈");
                    }
                    ds.close();
                }
            }
        input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
