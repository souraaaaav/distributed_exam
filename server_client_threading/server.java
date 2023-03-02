package server_client_threading;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ServerSocket serverSocket = new ServerSocket(22222);
        System.out.println("Server Started.....");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client Connected.....");
            new ServerThread(socket);
        }
    }

}

class ServerThread implements Runnable {

    Socket clientSocket;
    Thread t;

    ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());

            while (true) {

                String message = (String) ois.readObject();
                if (message == null) break;
                System.out.println("Message Received: " + message);

                System.out.println("Received message from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                oos.writeObject(message.toUpperCase());

            }


        } catch (IOException | ClassNotFoundException e) {

            throw new RuntimeException(e);
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}