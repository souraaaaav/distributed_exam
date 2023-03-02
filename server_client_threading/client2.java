package server_client_threading;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class client2 {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("Client Started....");

        Socket socket = new Socket("127.0.0.1", 22222);
        System.out.println("Client Connected....");

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        while (true) {

            Scanner sc = new Scanner(System.in);
            String message = sc.nextLine();

            oos.writeObject(countWords(message));


            if (message.equals("exit")) break;
            String serverReply = (String) ois.readObject();


            System.out.println("From Server: Article number is --> " + serverReply);

        }
        socket.close();
    }
    public static String countWords(String str) {
        int aCount = 0;
        int anCount = 0;
        int theCount = 0;

        // split the string into words
        String[] words = str.split("\\s+");

        // iterate over the words and count occurrences of "a", "an", and "the"
        for (String word : words) {
            if (word.equalsIgnoreCase("a")) {
                aCount++;
            } else if (word.equalsIgnoreCase("an")) {
                anCount++;
            } else if (word.equalsIgnoreCase("the")) {
                theCount++;
            }
        }
        return "\n'a' -> "+aCount+"\n'an' -> "+anCount+"\n'the' -> "+theCount;
    }
}