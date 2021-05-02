package org.example;// ReadFile.java
// TCP/IP simple client that reads a file on the server  
// Accepts at least one argument, the name of a file on 
// the server to read. Opens a connection with the server 
// and displays the resultant file or an error result

import java.awt.*;
import java.net.*;
import java.io.*;

// This is the lab template. 
// Fill in the code for the two methods called from main

class ReadFile {

  // This method will send the file name to read to the 
  // server. You need to set up your own data output 
  // stream to pass the filename to the server (which 
  // should be reading a data input stream and expecting 
  // a string).

  public static void sendFileName(
          Socket s, String fileName)
          throws IOException {

    // FILL IN THIS METHOD - Hint: create a stream to
    // send the filename to the server
    DataOutputStream dos = new DataOutputStream(s.getOutputStream()); //写Socket的输出流
    dos.writeUTF(fileName);
    receiveFile(s, "example.txt");
    dos.flush();
  }

  // This method will receive the file from the Server,
  // or the result of the attempt to read the file.

  public static void receiveFile(Socket s, String file_path)
          throws IOException {

    // FILL IN THIS METHOD - Hint: look at the
    // simpleClient code
    DataInputStream inputStream=null;
    DataOutputStream outputStream=null;
    try{
      inputStream  = new DataInputStream(new BufferedInputStream(s.getInputStream()));
      outputStream = new DataOutputStream(new FileOutputStream(file_path));
      byte[] buf = new byte[100];
      int len = 0;
      while((len=inputStream.read(buf))!=-1){
        outputStream.write(buf, 0, len);
      }
      outputStream.flush();
      System.out.println("file received!");
    }catch (Exception e){
      System.out.println(e.getMessage());
    }finally {
      inputStream.close();
      outputStream.close();
    }
  }

  public static void main(String args[]) {
    Socket s;
    int port = 4321;

    // Did we receive the correct number of arguments?
    if (args.length != 2) {
      System.out.println(
              "Usage: java ReadFile <server> <file>");
      System.exit (-1);
    }

    try {

      // Open our connection to args[0]
      s = new Socket(args[0], port);
      // Send the file name to the Server
      System.out.println("connect success！");
      sendFileName (s, args[1]);

      // Output the file (or result of the request)
      receiveFile (s, "example.txt");

      // When the EOF is reached, just close the 
      // connection and exit
      s.close();

    } catch (IOException e) {
      // ignore
    }
  }
}
