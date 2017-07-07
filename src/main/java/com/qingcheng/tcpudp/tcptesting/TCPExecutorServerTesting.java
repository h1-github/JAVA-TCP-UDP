package com.qingcheng.tcpudp.tcptesting;

import com.qingcheng.tcpudp.utils.L;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wanghuan on 2017/7/6.
 */
public class TCPExecutorServerTesting {

    private final static int MAX_THREAD_POOL_SIZE = 2;

    public static void main(String[] args){
        TCPExecutorServerTesting testing = new TCPExecutorServerTesting();
        testing.run();
    }

    private void run(){
        try {
            final ServerSocket serverSocket = new ServerSocket(9192);
            Executor executor = Executors.newSingleThreadExecutor();
            boolean flag = true;
            while (flag){
                L.d("等待客户端连接...");
                final Socket client = serverSocket.accept();
                L.d("客户端已经建立连接！");
                executor.execute(new Runnable() {
                    public void run() {
                        try {
                            PrintStream output = new PrintStream(client.getOutputStream());
                            InputStream inputStream = client.getInputStream();

                            while (true){
                                int read = inputStream.read();
                                L.d("read -> " + read);
                                output.println("over : " + read);
                                Thread.sleep(500);
                            }


//                            int BUFF_SIZE = 10;
//                            byte[] buff = new byte[BUFF_SIZE];
//                            inputStream.read(buff , 0 , BUFF_SIZE);
//                            L.d("原始数据 ：" + new String(buff));
//                            for (int i = 0; i < buff.length; i++){
//                                L.d("buff["+i+"] : " + buff[i]);
//                            }
//                            output.println("over");
//                            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                            boolean readFlag = true;
//                            while (readFlag){
//                                String readText = reader.readLine();
//                                if(readText == null || readText.equalsIgnoreCase("close")){
//                                    readFlag = false;
//                                    L.d("完毕 退出服务");
//                                }else {
//                                    String response = "收到客户端发来的信息：" + readText;
//                                    L.d(response);
//                                    output.println(response);
//                                }
//                            }
//                            output.close();
//                            reader.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
