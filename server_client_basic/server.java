package server_client_basic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

    public static void main(String args[]) throws IOException, ClassNotFoundException{

        ServerSocket serverSocket = new ServerSocket(22222);
        System.out.println("Server Started.....");

        while(true){

            Socket socket = serverSocket.accept();
            System.out.println("Client Connected.....");

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());


            String message = (String) ois.readObject();
            System.out.println("Message Received: " + message);

            System.out.println("Received message from client1 " + socket.getInetAddress() + ":" + socket.getPort());


            oos.writeObject("Hi Client "+message.toUpperCase());

            ois.close();
            oos.close();
            socket.close();
            //terminate the server if client1 sends exit request
            if(message.equalsIgnoreCase("exit")) break;
        }
        System.out.println("Shutting down Socket server!!");

        serverSocket.close();
    }

}
