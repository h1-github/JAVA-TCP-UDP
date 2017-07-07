package com.qingcheng.tcpudp.tcptesting;

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
public class TCPThreadPoolServerTesting {

    private final static int MAX_THREAD_POOL_SIZE = 2;

    public static void main(String[] args){
        TCPThreadPoolServerTesting testing = new TCPThreadPoolServerTesting();
        testing.run();
    }

    private void run(){
        try {
            final ServerSocket serverSocket = new ServerSocket(9192);
            for(int i = 0 ; i < MAX_THREAD_POOL_SIZE; i++){
                final Thread thread = new Thread(){
                    @Override
                    public void run() {
                        L.w("启动线程实例：" + this.getName() + " id -> " + this.getId());
                        try {
                            Thread.sleep(20 * 30*1024);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        while (true){
                            try {
                                L.f("等待客户端连接... ");
                                Socket client = serverSocket.accept();
                                L.d("新的客户端连接建立：" + client.getRemoteSocketAddress().toString());

                                PrintStream output = new PrintStream(client.getOutputStream());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                boolean readFlag = true;
                                while (readFlag){
                                    String readText = reader.readLine();
                                    if(readText == null || readText.equalsIgnoreCase("bye")){
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
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
