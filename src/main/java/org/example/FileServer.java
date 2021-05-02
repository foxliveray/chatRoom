package org.example;// FileServer.java
// TCP/IP Server - opens and reads a requested file and
// send the file. The client will request a file to read,
// open it and send the file over the socket, or an error.

import java.net.*;
import java.io.*;

// This is the lab template - fill in the two methods that
// get the filename from the requesting client, and send
// the file

class FileServer {


    // Method for receiving the file name from the client
    // Takes the socket created upon accept and returns the
    // filename as a String

    public static String getFileName(Socket s1)
            throws IOException {
        // FILL IN THIS METHOD - NOTE: The return type is String

        DataInputStream in = null;  //读取socket输入流
        try {
            //访问Socket对象的getInputStream方法取得客户端发送过来的数据流
            in = new DataInputStream(new BufferedInputStream(s1.getInputStream()));
            String fileName = in.readUTF();  //获取文件名
            System.out.println("fileName:" + fileName);
//      File file = new File(fileName);
//      if(file.exists()){
//          return fileName;
//      }
            return fileName;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return "";
    }


    // Send file method - opens the file name requested by
    // the client and checks if:
    // a) the file exists, and
    // b) if the server has permission to read the file,
    //    then returns the result or the file to the client

    public static void sendFileToClient(
            Socket s1, String sfile)
            throws IOException {

        // FILL IN THIS METHOD - NOTE: the code below is
        // provided as an example of returning a error result
        // to the client.  The \n is required for the client
        // to properly read the string returned
        DataOutputStream s1out = null; //写Socket的输出流
        DataInputStream dis = null;  //读取本地文件的输入流

        try {
            s1out = new DataOutputStream(s1.getOutputStream());
            // Open the file for reading
            // Use file to determine if permissions are correct
            File f = new File(sfile);

            // Does the file exist?
            if (f.exists() != true) {
                String error =
                        new String(
                                "File " + sfile + " does not exist...\n");
                int len = error.length();
                for (int i = 0; i < len; i++) {
                    s1out.write((int) error.charAt(i));
                }
                System.out.println(error);
                return;
            }
            System.out.println(f.getAbsolutePath());
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
            //s1out.writeUTF(sfile);
            //清空缓存，将文件名发送出去
            //s1out.flush();
            //开始发送文件
            byte buf[] = new byte[100];
            int num = 0;
            while((num=dis.read(buf))!=-1){
                s1out.write(buf, 0, num);
            }
            s1out.flush();
            System.out.println("文件发送成功！");
        } catch (IOException e){
            System.out.println("文件传输错误！");
            s1out.close();
            dis.close();
        } finally {
            s1out.close();
            dis.close();
        }

    }

    // You need to fill in the listen/accept loop below to
    // establish the requested connection and then call
    // the sendFileToClient() method.

    public static void main(String args[]) {
        ServerSocket s = (ServerSocket) null;
        Socket s1;
        byte inbuf[] = new byte[100];
        String fileName;

        // Setup our server on socket 4321
        // (wait for 30 seconds before timing out connections)
        try {
            s = new ServerSocket(4321, 1);
            System.out.println("Server listen at port:4321");
        } catch (IOException e) {
            System.out.println("\nServer timed out!");
            System.exit(-1);
        }

        // Run the listen/accept loop forever
        while (true) {
            try {

                // Wait here and listen for a connection
                // After the connection is made, send the
                // file to the  client
                s1 = s.accept();
                System.out.println("get connection at:" + s1.getInetAddress() + ", port is:" + s1.getPort());
                fileName= getFileName(s1);
                if(!"".equals(fileName)){
                    sendFileToClient(s1, fileName);
                }
            } catch (IOException e) {
                System.out.println("Error - " + e.toString());
            }
        }
    }
}
