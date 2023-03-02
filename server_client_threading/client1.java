package server_client_threading;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class client1 {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("Client Started....");

        Socket socket = new Socket("127.0.0.1", 22222);
        System.out.println("Client Connected....");

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        while (true) {

            Scanner sc = new Scanner(System.in);
            String message = sc.nextLine();

            oos.writeObject(message);


            if (message.equals("exit")) break;
            String serverReply = (String) ois.readObject();


            System.out.println("From Server: " + serverReply);

        }
        socket.close();
    }
}