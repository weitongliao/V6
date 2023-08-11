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
        int port = 12345;

        try {
            DatagramSocket socket = new DatagramSocket(port, Inet6Address.getByName("::"));
            System.out.println("Server listening on port " + port);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

//                String request = new String(packet.getData(), 0, packet.getLength());
//                System.out.println("Received request: " + request);

                Thread requestHandler = new Thread(new RequestHandler(packet, nodes));
                requestHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Node t = new Node(new NodeID("1111"),
//                new NodeID(3, "0000", "1.1.1.1", 12345),
//                new NodeID(2, "0001", "1.1.1.1", 12345),
//                new NodeID(1, "0010", "1.1.1.1", 12345),
//                new NodeID(0, "0110", "1.1.1.1", 12345),
//                new NodeID(1, "0100", "1.1.1.1", 12345),
//                new NodeID(1, "1000", "1.1.1.1", 12345),
//                new NodeID(2, "1011", "1.1.1.1", 12345));

//        System.out.println(t.getNodeId().getCubicalIndex());

        nodes.put("11002", "1.1.1.1");
        nodes.put("11003", "1.1.1.2");
        nodes.put("11014", "1.1.1.2");
        nodes.put("11005", "1.1.1.2");
        nodes.put("10005", "1.1.1.2");
        nodes.put("11115", "1.1.1.2");
        nodes.put("11105", "1.1.1.2");
        nodes.put("11006", "1.1.1.2");
        nodes.put("11116", "1.1.1.2");
//        nodes.put("11118", "1.1.1.2");
        nodes.put("11100", "1.1.1.2");
        nodes.put("11010", "1.1.1.2");
//        nodes.put("11013", "1.1.1.2");

        String currentNodeID = "11011";
//        target 111xx
//        String currentNodeID = "111100005";

        System.out.println("Left Outside Leaf: " + getLeftOutsideLeaf(nodes, currentNodeID));
        System.out.println("Right Outside Leaf: " + getRightOutsideLeaf(nodes, currentNodeID));
        System.out.println("Left Cyclic Neighbor: " + getLeftCyclicNeighbor(nodes, currentNodeID));
        System.out.println("Right Cyclic Neighbor: " + getRightCyclicNeighbor(nodes, currentNodeID));
        System.out.println("Cubical Neighbor: " + getCubicalNeighbor(nodes, currentNodeID));
        System.out.println(getInnerLeaf(nodes, currentNodeID));

    }

    public static String getLeftOutsideLeaf(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String leftOutsideLeaf = null;

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            if (key.substring(0, key.length() - 1).compareTo(currentNodeID.substring(0, key.length() - 1)) < 0) {
                if (leftOutsideLeaf == null || key.charAt(key.length() - 1) > leftOutsideLeaf.charAt(key.length() - 1)) {
                    leftOutsideLeaf = key;
                }
            }
        }

        return leftOutsideLeaf;
    }

    public static String getRightOutsideLeaf(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String rightOutsideLeaf = null;

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            if (key.substring(0, key.length() - 1).compareTo(currentNodeID.substring(0, key.length() - 1)) > 0) {
                if (rightOutsideLeaf == null || key.charAt(key.length() - 1) > rightOutsideLeaf.charAt(key.length() - 1)) {
                    rightOutsideLeaf = key;
                }
            }
        }

        return rightOutsideLeaf;
    }

    public static String getLeftCyclicNeighbor(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String leftCyclicNeighbor = null;

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            if (key.substring(0, key.length() - 1).compareTo(currentNodeID.substring(0, key.length() - 1)) <= 0 && key.charAt(key.length() - 1) == currentNodeID.charAt(key.length() - 1) - 1) {
                if (leftCyclicNeighbor == null || key.compareTo(leftCyclicNeighbor) > 0) {
                    leftCyclicNeighbor = key;
                }
            }
        }

        return leftCyclicNeighbor;
    }

    public static String getRightCyclicNeighbor(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String rightCyclicNeighbor = null;

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            if (key.substring(0, key.length() - 1).compareTo(currentNodeID.substring(0, key.length() - 1)) >= 0 && key.charAt(key.length() - 1) == currentNodeID.charAt(key.length() - 1) - 1) {
                if (rightCyclicNeighbor == null || key.compareTo(rightCyclicNeighbor) < 0) {
                    rightCyclicNeighbor = key;
                }
            }
        }

        return rightCyclicNeighbor;
    }

    public static String getCubicalNeighbor(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String cubicalNeighbor = null;
        int id_length = currentNodeID.length();
        char last = currentNodeID.charAt(id_length-1);
        int lastDigit = Integer.parseInt(String.valueOf(last));

        int k_th_digit = Integer.parseInt(String.valueOf(currentNodeID.charAt(id_length - lastDigit - 2)));

        String firstKDigit = currentNodeID.substring(0, id_length - lastDigit - 2);

//        System.out.println(firstKDigit);
        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            char keyLast = key.charAt(id_length-1);
            int keyLastDigit = Integer.parseInt(String.valueOf(keyLast));
//            System.out.println(keyLastDigit);

            if (keyLastDigit == lastDigit - 1) {
                if(key.substring(0, id_length - lastDigit - 2).equals(firstKDigit)){
                    if(Integer.parseInt(String.valueOf(key.charAt(id_length - lastDigit - 2))) + k_th_digit == 1){
                        cubicalNeighbor = key;
                        break;
                    }
                }
            }
        }

        return cubicalNeighbor;
    }

    public static List<String> getInnerLeaf(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        int id_length = currentNodeID.length();
        List<String> matchingKeys = new ArrayList<>();
        String prefix = currentNodeID.substring(0, id_length-1);

        char last = currentNodeID.charAt(id_length-1);
        int lastDigit = Integer.parseInt(String.valueOf(last));

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            if (key.charAt(id_length-1) != currentNodeID.charAt(id_length-1) && key.substring(0, id_length-1).equals(currentNodeID.substring(0, id_length-1))) {
                matchingKeys.add(String.valueOf(key.charAt(id_length-1)));
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
}
