package com.qingcheng.tcpudp.tcp;


import com.qingcheng.tcpudp.utils.L;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by wanghuan on 2017/7/6.
 */
public class TCPClientTesting {

    public static void main(String[] args){
        TCPClientTesting testing = new TCPClientTesting();
        testing.run();
    }

    public void run(){
        com.qingcheng.tcpudp.utils.L.i("start  tcp client");

        try {
            Socket socket = new Socket("localhost" , 10090);
            L.d("连接建立");
            socket.setSoTimeout(10000);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            PrintStream output = new PrintStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            boolean flag = true;
            while (flag){
                L.i("等待输入信息：");
                String text = input.readLine();
                L.d("向服务器发送消息：" + text);
                output.println(text);
                if("bye".equalsIgnoreCase(text)){
                    flag = false;
                    L.d("发送完毕 准备退出");
                }else {
                    L.i("等待服务器下发消息...");
                    String readText = reader.readLine();
                    L.w("收到服务器消息：" + readText);
                }
            }
            input.close();
            if(socket != null){
                socket.close();
            }
            L.d("退出程序");
        } catch (IOException e) {
            L.f("异常退出");
            e.printStackTrace();
        }
    }

}
