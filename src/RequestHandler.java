import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RequestHandler implements Runnable {
    // 环里最大的
    // TODO: 2023/8/15 服务器还要处理节点离开 写出节点发布资源需求的方法 
    private static final int max_count_in_ring = 13;
    private static int clientPort = 23456;
    private DatagramPacket packet;
    private ConcurrentHashMap<String, String> nodes;
    private ConcurrentHashMap<String, Integer> counts;

    public RequestHandler(DatagramPacket packet, ConcurrentHashMap<String, String> nodes, ConcurrentHashMap<String, Integer> counts) {
        this.packet = packet;
        this.nodes = nodes;
        this.counts = counts;
    }

    @Override
    public void run() {
        InetAddress senderAddress = packet.getAddress();
        String senderIpv6Address = ((Inet6Address) senderAddress).getHostAddress();
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
            //cpu 000 gpu 000 ram 000 大间距0 随机簇000 index00 12位必须 加三位随机 15
            String resourceID = (String) receivedMap.get("D");
            String id = "";

            for (int i = 0; i < 8; i++) {
                String key = resourceID+"0"+padString(Integer.toBinaryString(i), 3);
                if(counts.containsKey(key)){
                    int count = counts.get(key);
                    if(count < max_count_in_ring){
                        counts.put(key, count + 1);
                        id = key + padString(String.valueOf(max_count_in_ring - count - 1), 2);
                        break;
                    }
                }else {
                    counts.put(key, 1);
                    id = key + padString(String.valueOf(max_count_in_ring - 1), 2);
                    break;
                }
            }

//            while(true){
//                String randomPad = generateRandomBinary(3);
//                if(counts.containsKey(resourceID+"0"+randomPad)){
//                    int count = counts.get(resourceID+"0"+randomPad);
//                    if (count < 15){
//                        counts.put(resourceID+"0"+randomPad, count + 1);
//
//                        id = resourceID+"0"+randomPad+String.format("%02d", (max_count_in_ring-count));
//                        break;
//                    }
//                }else{
//                    counts.put(resourceID+"0"+randomPad, 1);
//                    id = resourceID+"0"+randomPad+String.format("%02d", max_count_in_ring);
//                    break;
//                }
//            }
            if(id.equals("")){
                System.out.println("Generating id error");
            }else {
                nodes.put(id, senderIpv6Address);
//            System.out.println(counts);
//            System.out.println(nodes);
                sendMsg("i", id+","+senderIpv6Address, null, null, senderIpv6Address, clientPort);
            }

//            nodes.put("11010", "1.1.1.2");
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
            for (int i = 0; i < 7; i++) {
                if(results.get(i) == null){
                    results.add(null);
                }else {
                    results.add(nodes.get(results.get(i)));
                }
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

    public static String generateRandomBinary(int bitCount) {
        if (bitCount <= 0) {
            throw new IllegalArgumentException("Bit count must be positive.");
        }

        Random random = new Random();
        StringBuilder binaryBuilder = new StringBuilder();

        for (int i = 0; i < bitCount; i++) {
            int bit = random.nextInt(2); // 生成0或1
            binaryBuilder.append(bit);
        }

        return binaryBuilder.toString();
    }

    public static String padString(String input, int length) {
        if (input.length() >= length) {
            return input; // 字符串长度已达到或超过目标长度，不需要补全
        }

        int paddingLength = length - input.length();
        String padding = String.join("", Collections.nCopies(paddingLength, "0")); // 使用0进行补全

        return padding + input;
    }
}