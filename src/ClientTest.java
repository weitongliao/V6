import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.HashMap;

public class ClientTest {


    public static void main(String[] args) {
        Node node = new Node();

        int cpu = 2;
        int gpu = 3;
        int ram = 4;
        String CPU = Integer.toBinaryString(cpu);
        String GPU = Integer.toBinaryString(gpu);
        String RAM = Integer.toBinaryString(ram);
        System.out.println(CPU + GPU + RAM);

        String serverAddress = "fe80::25c6:397:7e73:9775";
        int serverPort = 12345;
        int listenPort = 23456;

        // Initialize
        Thread listenerThread = new Thread(new ListenerThread(listenPort, node));
        listenerThread.start();
        // Todo: data要是资源量的信息
        sendMsg("j", CPU + GPU + RAM, null, null,serverAddress, serverPort);

        // update neighbor info constantly
        Thread communicationThread = new Thread(new CommunicationToServerThread(serverAddress, serverPort, node));
        communicationThread.start();
    }

    public static void sendMsg(String header, String data, String sourceID, String senderID, String destinationIP, int port){
        HashMap<String, String> dictionary = new HashMap<>();
        dictionary.put("H", header); // H for header, u for update neighbour, j for join, r for server reply to update from server, f for find node (routing)
        dictionary.put("D", data); // D for data
        dictionary.put("S", sourceID); // S for source ID
        dictionary.put("DE", sourceID); // DE for destination
        dictionary.put("E", senderID); // Sender
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            // Serialize and send the dictionary
            objectStream.writeObject(dictionary);
            byte[] sendData = byteStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Inet6Address.getByName(destinationIP), port);
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.send(sendPacket);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class CommunicationToServerThread implements Runnable {
    private final String serverAddress;
    private final int serverPort;
    private Node node;

    public CommunicationToServerThread(String serverAddress, int serverPort, Node node) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.node = node;
    }

    @Override
    public void run() {
        while (true){
            // Update Neighbors
            ClientTest.sendMsg("u", null, node.getNodeId(), node.getNodeId(), serverAddress, serverPort);
            try {
                Thread.sleep(10000); //update neighbor every 10 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

class ListenerThread implements Runnable {
    private int listenPort;
    private Node node;

    public ListenerThread(int listenPort, Node node) {
        this.listenPort = listenPort;
        this.node = node;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(listenPort);

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                Thread receiveHandler = new Thread(new ReceiveHandler(receivePacket, node));
                receiveHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

