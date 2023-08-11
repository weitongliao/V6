import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RequestHandler implements Runnable {
    private static int clientPort = 23456;
    private DatagramPacket packet;
    private ConcurrentHashMap<String, String> nodes;

    public RequestHandler(DatagramPacket packet, ConcurrentHashMap<String, String> nodes) {
        this.packet = packet;
        this.nodes = nodes;
    }

    @Override
    public void run() {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getData());
        java.util.HashMap<?, ?> receivedMap = null;
        try {

            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            // Deserialize the received object (dictionary)

            Object receivedObject = objectStream.readObject();
            if (receivedObject instanceof java.util.HashMap) {
                receivedMap = (java.util.HashMap<?, ?>) receivedObject;
                System.out.println("Received dictionary: " + receivedMap);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        assert receivedMap != null;
        String header = (String) receivedMap.get("H");
        if (Objects.equals(header, "j")){
            nodes.put("11010", "1.1.1.2");;
        } else if (Objects.equals(header, "u")){
            // reply update request
            String id = (String) receivedMap.get("E");
            List<String> results = new ArrayList<>();
            results.add(ServerTest.getLeftOutsideLeaf(nodes, id));
            results.add(ServerTest.getRightOutsideLeaf(nodes, id));
            results.add(ServerTest.getLeftCyclicNeighbor(nodes, id));
            results.add(ServerTest.getRightCyclicNeighbor(nodes, id));
            results.add(ServerTest.getCubicalNeighbor(nodes, id));
            results.addAll(ServerTest.getInnerLeaf(nodes, id));
            for (String nid : results) {
                results.add(nodes.get(nid));
            }
            sendMsg("r", results.toString(), null, null, nodes.get(id), clientPort);
        }
    }

    public static void sendMsg(String header, String data, String sourceID, String senderID, String destination, int port){
        HashMap<String, String> dictionary = new HashMap<>();
        dictionary.put("H", header); // H for header, u for update neighbour, j for join, r for server reply to update from server, f for find node (routing)
        dictionary.put("D", data); // D for data
        dictionary.put("S", sourceID); // S for source ID
        dictionary.put("E", senderID); // Sender
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            // Serialize and send the dictionary
            objectStream.writeObject(dictionary);
            byte[] sendData = byteStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Inet6Address.getByName(destination), port);
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.send(sendPacket);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}