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

            oos.writeObject(count_Vowels(message));


            if (message.equals("exit")) break;
            String serverReply = (String) ois.readObject();


            System.out.println("From Server: Vowel number is --> " + serverReply);

        }
        socket.close();
    }
    public static String count_Vowels(String str)
    {
        int count = 0;
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == 'a' || str.charAt(i) == 'e' || str.charAt(i) == 'i'
                    || str.charAt(i) == 'o' || str.charAt(i) == 'u')
            {
                count++;
            }
        }

        return String.valueOf(count);
    }
}