import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Client {
    private static final String server_address = "fe80::25c6:397:7e73:9775";
    private static final int max_count_in_ring = 12; // dimension is 13, max count is 12

    public static void main(String[] args) {
        Node node = new Node();

        // Current node resource amount setting from 0-7 each category
        int cpu = 2;
        int gpu = 3;
        int ram = 4;

        startService(node, cpu, gpu, ram);
    }

    public static void startService(Node node, int cpu, int gpu, int ram) {
        String CPU = Integer.toBinaryString(cpu);
        String GPU = Integer.toBinaryString(gpu);
        String RAM = Integer.toBinaryString(ram);
        String resourceIdentifier = String.format("%1$" + 3 + "s", CPU).replace(' ', '0')
                + String.format("%1$" + 3 + "s", GPU).replace(' ', '0')
                + String.format("%1$" + 3 + "s", RAM).replace(' ', '0');

        String serverAddress = server_address;
        int serverPort = 12345;
        int listenPort = 23456;

        // data是资源量的信息
        sendMsg("j", resourceIdentifier, null, null, null, serverAddress, serverPort);
        try{
            DatagramSocket socket = new DatagramSocket(listenPort);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.setSoTimeout(5000);
            socket.receive(receivePacket);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(receivePacket.getData());

            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            // Deserialize the received object (dictionary)
            java.util.HashMap<?, ?> receivedMap = null;
            Object receivedObject = objectStream.readObject();
            if (receivedObject instanceof java.util.HashMap) {
                receivedMap = (java.util.HashMap<?, ?>) receivedObject;
                System.out.println("Received dictionary: " + receivedMap);
                node.setNodeId((String) receivedMap.get("D"));
                String[] parts = ((String) receivedMap.get("D")).split(",");
                node.setNodeId(parts[0]);
                node.setIp(parts[1]);
            }
            socket.close();
        } catch (SocketTimeoutException e){
            System.out.println("connect to serve time out");
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        // Initialize
        Thread listenerThread = new Thread(new ListenerThread(listenPort, node));
        listenerThread.start();


        // update neighbor info constantly
        Thread communicationThread = new Thread(new CommunicationToServerThread(serverAddress, serverPort, node));
        communicationThread.start();

        Thread publishRequirementThread = new Thread(new PublishRequirement(node));
        publishRequirementThread.start();
    }

    public static void sendMsg(String header, String data, String sourceID, String senderID, String destinationID, String destinationIP, int port){
        HashMap<String, String> dictionary = new HashMap<>();
        dictionary.put("H", header); // H for header, u for update neighbour, j for join, r for server reply to update from server, f for find node (routing), e for echo from destination, i for return id
        dictionary.put("D", data); // D for data
        dictionary.put("S", sourceID); // S for source ID
        dictionary.put("DE", destinationID); // DE for destination
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
            Client.sendMsg("u", null, node.getNodeId(), node.getNodeId(), null, serverAddress, serverPort);
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

class PublishRequirement implements Runnable {
    private Node node;

    public PublishRequirement(Node node) {
        this.node = node;
    }
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("CPU0-7 GPU0-7 RAM0-7. Enter 'exit' to quit.");

        // 循环从键盘接收输入
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting input thread...");
                break;
            }
            String CPU = Integer.toBinaryString(Integer.parseInt(input.substring(0, 1)));
            String GPU = Integer.toBinaryString(Integer.parseInt(input.substring(1, 2)));
            String RAM = Integer.toBinaryString(Integer.parseInt(input.substring(2, 3)));
            String resourceIdentifier = String.format("%1$" + 3 + "s", CPU).replace(' ', '0')
                    + String.format("%1$" + 3 + "s", GPU).replace(' ', '0')
                    + String.format("%1$" + 3 + "s", RAM).replace(' ', '0');

            // TODO: 2023/8/16 data中不知道填什么
            Random random = new Random();
            node.routing("f", node.getNodeId(), resourceIdentifier+"0"+ RequestHandler.generateRandomBinary(3) + String.format("%02d", random.nextInt(13)), "");
//            System.out.println("Input received: " + resourceIdentifier+"0"+ RequestHandler.generateRandomBinary(3) + String.format("%02d", randomNumber));
        }

        scanner.close();
    }
}

