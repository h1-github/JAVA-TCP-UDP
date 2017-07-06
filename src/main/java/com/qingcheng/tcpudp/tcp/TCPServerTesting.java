package com.qingcheng.tcpudp.tcp;

import com.qingcheng.tcpudp.utils.L;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wanghuan on 2017/7/6.
 */
public class TCPServerTesting {

    public static void main(String[] args){
        TCPServerTesting testing = new TCPServerTesting();
        testing.run();
    }

    private void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(10090);
            L.i("启动 ServerSocket ...");
            Socket client = null;
            boolean flag = true;
            while (flag){
                L.i("ServerSocket 等待连接 ...");
                client = serverSocket.accept();
                L.i("一个 client 建立连接 : " + client.getSoTimeout());
                new HandlerClientThread(client).start();
            }
            serverSocket.close();
            L.d("退出程序");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class HandlerClientThread extends Thread{

        Socket client = null;

        public HandlerClientThread(Socket socket) {
            this.client = socket;
        }

        @Override
        public void run() {
            try {
                L.w("在独立线程中处理 client 交互 ");
                PrintStream output = new PrintStream(client.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                boolean readFlag = true;
                while (readFlag){
                    String readText = reader.readLine();
                    if(readText == null || readText.equalsIgnoreCase("")){
                        readFlag = false;
                        L.d("接收完毕");
                    }else {
                        String response = "收到客户端发来的信息：" + readText;
                        L.d(response);
                        output.println(response);
                    }
                }
                output.close();
                reader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
