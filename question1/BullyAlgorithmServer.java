package question1;

import java.io.*;
import java.net.*;
import java.util.*;

public class BullyAlgorithm {
    // Port number for communication
    private static final int PORT = 5000;

    // Timeout period for leader response (in milliseconds)
    private static final int TIMEOUT = 5000;

    // List of processes in the network
    private List<Process> processes;

    // ID of this process
    private int processId;

    // Whether this process is currently the leader
    private boolean isLeader;

    // Socket for sending and receiving messages
    private DatagramSocket socket;

    public BullyAlgorithm(int processId, List<Process> processes) throws SocketException {
        this.processId = processId;
        this.processes = processes;
        this.isLeader = false;
        this.socket = new DatagramSocket(PORT);
    }

    // Starts the election process
    public void startElection() throws IOException {
        System.out.println("Process " + processId + " initiates election");

        // Send an election message to all processes with higher IDs
        for (int i = processId + 1; i < processes.size(); i++) {
            send(i, "ELECTION");
        }

        // Wait for responses from higher ID processes
        boolean receivedResponse = false;
        for (int i = processId + 1; i < processes.size(); i++) {
            String response = receive(TIMEOUT);
            if (response != null && response.equals("OK")) {
                receivedResponse = true;
                break;
            }
        }

        // If no responses were received, this process is the new leader
        if (!receivedResponse) {
            System.out.println("Process " + processId + " is the new leader");
            isLeader = true;
            // Send a leader message to all processes
            for (int i = 0; i < processes.size(); i++) {
                if (i != processId) {
                    send(i, "LEADER");
                }
            }
        }
    }

    // Handles an election message from a lower ID process
    public void handleElection(int senderId) throws IOException {
        System.out.println("Process " + processId + " receives election from " + senderId);

        // Send an OK message to acknowledge the election
        send(senderId, "OK");

        // Start a new election to determine the leader
        startElection();
    }

    // Handles a leader message from the current leader
    public void handleLeader() {
        System.out.println("Process " + processId + " acknowledges leader");
        isLeader = false;
    }

    // Sends a message to a process
    private void send(int receiverId, String message) throws IOException {
        Process receiver = processes.get(receiverId);
        byte[] sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(receiver.getAddress()), receiver.getPort());
        socket.send(sendPacket);
    }

    // Receives a message
    private String receive(int timeout) throws IOException {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.setSoTimeout(timeout);
        try {
            socket.receive(receivePacket);
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            return message;
        } catch (SocketTimeoutException e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        // Initialize the list of processes
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("localhost", 5001));
        processes.add(new Process("localhost", 5002));
        processes.add(new Process("localhost", 5003));
        processes.add(new Process("localhost", 5004));
        processes.add(new Process("localhost", 5005));

        // Initialize the BullyAlgorithm instance
        BullyAlgorithm algorithm = new BullyAlgorithm(0, processes);

        // Start the election process
        algorithm.startElection();

        // Wait for messages
        while (true) {
            String message = algorithm.receive(0);
            if (message != null) {
                String[] parts = message.split(",");
                int senderId = Integer.parseInt(parts[0]);
                String messageType = parts[1];
                if (messageType.equals("ELECTION")) {
                    algorithm.handleElection(senderId);
                } else if (messageType.equals("LEADER")) {
                    algorithm.handleLeader();
                }
            }
        }
    }

    // Represents a process in the network
    private static class Process {
        private String address;
        private int port;

        public Process(String address, int port) {
            this.address = address;
            this.port = port;
        }

        public String getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }
    }
}

