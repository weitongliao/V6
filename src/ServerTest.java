import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerTest {
    //    ID and IP mapping
    // ConcurrentHashMap有什么缺陷吗？
    //ConcurrentHashMap 是设计为非阻塞的。
    // 在更新时会局部锁住某部分数据，但不会把整个表都锁住。
    // 同步读取操作则是完全非阻塞的。
    // 好处是在保证合理的同步前提下，效率很高。坏处是严格来说读取操作不能保证反映最近的更新
    static ConcurrentHashMap<String, String> nodes = new ConcurrentHashMap<>();

    public static void main(String[] args) {
//        int port = 12345;
//
//        try {
//            DatagramSocket socket = new DatagramSocket(port, Inet6Address.getByName("::"));
//            System.out.println("Server listening on port " + port);
//
//            while (true) {
//                byte[] buffer = new byte[1024];
//                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//
//                socket.receive(packet);
//
////                String request = new String(packet.getData(), 0, packet.getLength());
////                System.out.println("Received request: " + request);
//
//                Thread requestHandler = new Thread(new RequestHandler(packet, nodes));
//                requestHandler.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Node t = new Node(new NodeID("1111"),
//                new NodeID(3, "0000", "1.1.1.1", 12345),
//                new NodeID(2, "0001", "1.1.1.1", 12345),
//                new NodeID(1, "0010", "1.1.1.1", 12345),
//                new NodeID(0, "0110", "1.1.1.1", 12345),
//                new NodeID(1, "0100", "1.1.1.1", 12345),
//                new NodeID(1, "1000", "1.1.1.1", 12345),
//                new NodeID(2, "1011", "1.1.1.1", 12345));

//        System.out.println(t.getNodeId().getCubicalIndex());

        nodes.put("11111001104", "1.1.1.1");
        nodes.put("11110101104", "1.1.1.1");

        nodes.put("11111100104", "1.1.1.1");
        nodes.put("11111101004", "1.1.1.1");

        nodes.put("11011100004", "1.1.1.1");
        nodes.put("11110100004", "1.1.1.1");
        nodes.put("11100100004", "1.1.1.1");
        nodes.put("11110110004", "1.1.1.1");

        nodes.put("11111100003", "1.1.1.1");
        nodes.put("11111100002", "1.1.1.1");

        nodes.put("11111011108", "1.1.1.1");
        nodes.put("11111011104", "1.1.1.1");

        nodes.put("11111111004", "1.1.1.1");
        nodes.put("11111100108", "1.1.1.1");
        nodes.put("11111100107", "1.1.1.1");
        nodes.put("11111101107", "1.1.1.1");
//        nodes.put("11111100006", "1.1.1.1");
//        nodes.put("11111100007", "1.1.1.1");
//        nodes.put("111113", "1.1.1.2");
//        nodes.put("010141", "1.1.1.2");
//        nodes.put("101141", "1.1.1.2");


        String currentNodeID = "11111100005";


        System.out.println("Left Outside Leaf: " + getLeftOutsideLeaf(nodes, currentNodeID));
        System.out.println("Right Outside Leaf: " + getRightOutsideLeaf(nodes, currentNodeID));
        System.out.println("Left Cyclic Neighbor: " + getLeftCyclicNeighbor(nodes, currentNodeID));
        System.out.println("Right Cyclic Neighbor: " + getRightCyclicNeighbor(nodes, currentNodeID));
        System.out.println("Cubical Neighbor: " + getCubicalNeighbor(nodes, currentNodeID));
        System.out.println(getInnerLeaf(nodes, currentNodeID));

    }

    public static String getLeftOutsideLeaf(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String leftOutsideLeaf = null;
        int minDifference = Integer.MAX_VALUE;

        int k = getCyclicIndex(currentNodeID);
        int cubicalIndex = getCubicalIndex(currentNodeID);
        int diff = 0;

        String neighborCubic = null;
        for(String id: nodes.keySet()){
            diff = cubicalIndex - getCubicalIndex(id);
            if(diff > 0 && diff < minDifference){
                minDifference = diff;
                neighborCubic = String.valueOf(getCubicalIndex(id));
            }
        }
        if(neighborCubic != null){
            int maxCyclic = -1;
            for(String id: nodes.keySet()){
                if(String.valueOf(getCubicalIndex(id)).equals(neighborCubic)){
                    if (getCyclicIndex(id) > maxCyclic){
                        maxCyclic = getCyclicIndex(id);
                    }
                }
            }
            leftOutsideLeaf = neighborCubic + String.format("%02d", maxCyclic);
        }


        return leftOutsideLeaf;
    }

    public static String getRightOutsideLeaf(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String leftOutsideLeaf = null;
        int minDifference = Integer.MAX_VALUE;

        int k = getCyclicIndex(currentNodeID);
        int cubicalIndex = getCubicalIndex(currentNodeID);
        int diff = 0;

        String neighborCubic = null;
        for(String id: nodes.keySet()){
            diff =  getCubicalIndex(id) - cubicalIndex;
            if(diff > 0 && diff < minDifference){
                minDifference = diff;
                neighborCubic = String.valueOf(getCubicalIndex(id));
            }
        }
        if(neighborCubic != null){
            int maxCyclic = -1;
            for(String id: nodes.keySet()){
                if(String.valueOf(getCubicalIndex(id)).equals(neighborCubic)){
                    if (getCyclicIndex(id) > maxCyclic){
                        maxCyclic = getCyclicIndex(id);
                    }
                }
            }
            leftOutsideLeaf = neighborCubic + String.format("%02d", maxCyclic);
        }


        return leftOutsideLeaf;
    }

    public static String getLeftCyclicNeighbor(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String leftCyclicNeighbor = null;
        int MSDB = 0;
        int diff = 0;
        int minDifference = Integer.MAX_VALUE;

        int k = getCyclicIndex(currentNodeID);
        int cubicalIndex = getCubicalIndex(currentNodeID);

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            MSDB = Node.findHighestDifferentBit(String.valueOf(getCubicalIndex(key)), String.valueOf(cubicalIndex));
            diff = cubicalIndex - getCubicalIndex(key);
            if(getCyclicIndex(key) == k - 1 && MSDB <= k - 1 && diff > 0){
                if(leftCyclicNeighbor == null || diff < minDifference){
                    leftCyclicNeighbor = key;
                    minDifference = diff;
                }
            }
        }

        return leftCyclicNeighbor;
    }

    public static String getRightCyclicNeighbor(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        int k = getCyclicIndex(currentNodeID);
        int cubicalIndex = getCubicalIndex(currentNodeID);
        String rightCyclicNeighbor = null;
        int MSDB = 0;
        int diff = 0;
        int maxDifference = Integer.MIN_VALUE;

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            MSDB = Node.findHighestDifferentBit(String.valueOf(getCubicalIndex(key)), String.valueOf(cubicalIndex));
            diff = cubicalIndex - getCubicalIndex(key);
            if(getCyclicIndex(key) == k - 1 && MSDB <= k - 1 && diff < 0){
                if(rightCyclicNeighbor == null || diff > maxDifference){
                    rightCyclicNeighbor = key;
                    maxDifference = diff;
                }
            }
        }

        return rightCyclicNeighbor;
    }

    public static String getCubicalNeighbor(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String cubicalNeighbor = null;
        int k = getCyclicIndex(currentNodeID);

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            int k_this = getCyclicIndex(key);
            if(k - 1 == k_this){
                if(key.substring(0, currentNodeID.length()-3-k).equals(currentNodeID.substring(0, currentNodeID.length()-3-k))){
                    if(key.charAt(currentNodeID.length()-3-k) != currentNodeID.charAt(currentNodeID.length()-3-k)){
                        cubicalNeighbor = key;
                    }
                }
            }
        }

        return cubicalNeighbor;
    }

    public static List<String> getInnerLeaf(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        int id_length = currentNodeID.length();
        List<String> matchingKeys = new ArrayList<>();
        String prefix = currentNodeID.substring(0, id_length-2);

        int lastDigit = Integer.parseInt(currentNodeID.substring(id_length-2));

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            if (!key.substring(id_length - 2).equals(currentNodeID.substring(id_length - 2)) && key.substring(0, id_length-2).equals(currentNodeID.substring(0, id_length-2))) {
                matchingKeys.add(key.substring(id_length - 2));
            }
        }


//        for (String key : matchingKeys) {
//            System.out.println(findSmallerOrMax(matchingKeys, lastDigit));
//        }
        if (matchingKeys.isEmpty()){
            List<String> leaf = new ArrayList<>();
            leaf.add(null);
            leaf.add(null);
            return leaf;
        }

        List<String> leaf = new ArrayList<>();
        leaf.add(prefix.concat(String.valueOf(findSmallerOrMax(matchingKeys, lastDigit))));
        leaf.add(prefix.concat(String.valueOf(findLargerOrMin(matchingKeys, lastDigit))));

        return leaf;
    }

    public static int findSmallerOrMax(List<String> matchingKeys, int targetValue) {
        int maxSmallerValue = Integer.MIN_VALUE;

        for (String key : matchingKeys) {
            int keyIntValue = Integer.parseInt(key);
            if (keyIntValue < targetValue && keyIntValue > maxSmallerValue) {
                maxSmallerValue = keyIntValue;
            }
        }

        if (maxSmallerValue == Integer.MIN_VALUE) {
            int maxKeyValue = Integer.MIN_VALUE;
            for (String key : matchingKeys) {
                int keyIntValue = Integer.parseInt(key);
                if (keyIntValue > maxKeyValue) {
                    maxKeyValue = keyIntValue;
                }
            }
            return maxKeyValue;
        } else {
            return maxSmallerValue;
        }
    }

    public static int findLargerOrMin(List<String> matchingKeys, int targetValue) {
        int minLargerValue = Integer.MAX_VALUE;

        for (String key : matchingKeys) {
            int keyIntValue = Integer.parseInt(key);
            if (keyIntValue > targetValue && keyIntValue < minLargerValue) {
                minLargerValue = keyIntValue;
            }
        }

        if (minLargerValue == Integer.MAX_VALUE) {
            int minKeyValue = Integer.MAX_VALUE;
            for (String key : matchingKeys) {
                int keyIntValue = Integer.parseInt(key);
                if (keyIntValue < minKeyValue) {
                    minKeyValue = keyIntValue;
                }
            }
            return minKeyValue;
        } else {
            return minLargerValue;
        }
    }

    public static int getCubicalIndex(String id){
        return Integer.parseInt(id.substring(0, id.length()-2));
    }
    public static int getCyclicIndex(String id){
        return Integer.parseInt(id.substring(id.length()-2));
    }
}
