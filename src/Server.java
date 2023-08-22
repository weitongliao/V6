import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    // TODO: 2023/8/16 验证是否可以模糊查询 节点离开 安卓
    // TODO: 2023/8/16 模糊查询时节点生成随机id 前9位是所需资源量的标识符 加上0 所以前10位不是随机的，后面位数是随机的 加随机三位 加上0到12最大环中个数的随机数
    // TODO: 2023/8/16 目前还无法解决节点离开问题
    // ID and IP mapping
    // ConcurrentHashMap有什么缺陷吗？
    //ConcurrentHashMap 是设计为非阻塞的。
    // 在更新时会局部锁住某部分数据，但不会把整个表都锁住。
    // 同步读取操作则是完全非阻塞的。
    // 好处是在保证合理的同步前提下，效率很高。坏处是严格来说读取操作不能保证反映最近的更新
    static ConcurrentHashMap<String, String> nodes = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
    static int cubicLength = 13;

    public static void main(String[] args) {
        int port = 12345;

        try {
            DatagramSocket socket = new DatagramSocket(port, Inet6Address.getByName("::"));
            System.out.println("Server listening on port " + port);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);
//                InetAddress senderAddress = packet.getAddress();

//                String request = new String(packet.getData(), 0, packet.getLength());
//                System.out.println("Received request: " + request);

                Thread requestHandler = new Thread(new RequestHandler(packet, nodes, counts));
                requestHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLeftOutsideLeaf(ConcurrentHashMap<String, String> nodes, String currentNodeID) {
        String leftOutsideLeaf = null;
        int minDifference = Integer.MAX_VALUE;

        int k = getCyclicIndex(currentNodeID);
        int cubicalIndex = getCubicalIndex(currentNodeID);
//        System.out.println(cubicalIndex);
        int diff = 0;

        String neighborCubic = null;
        for(String id: nodes.keySet()){
            diff = cubicalIndex - getCubicalIndex(id);
            if(diff > 0 && diff < minDifference){
                minDifference = diff;
//                neighborCubic = String.valueOf(getCubicalIndex(id));
                neighborCubic = id.substring(0, id.length()-2);
            }
        }
        if(neighborCubic != null){
            int maxCyclic = -1;
            for(String id: nodes.keySet()){
                if(id.substring(0, id.length()-2).equals(neighborCubic)){
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
                neighborCubic = id.substring(0, id.length()-2);
            }
        }
        if(neighborCubic != null){
            int maxCyclic = -1;
            for(String id: nodes.keySet()){
                if(id.substring(0, id.length()-2).equals(neighborCubic)){
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
            MSDB = Node.findHighestDifferentBit(padString(Integer.toBinaryString(getCubicalIndex(key)), cubicLength), padString(Integer.toBinaryString(cubicalIndex), cubicLength));
            diff = cubicalIndex - getCubicalIndex(key);
            if(getCyclicIndex(key) == k - 1 && MSDB <= k - 1 && diff >= 0){
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
        int minDifference = Integer.MAX_VALUE;

        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String key = entry.getKey();
            MSDB = Node.findHighestDifferentBit(padString(Integer.toBinaryString(getCubicalIndex(key)), cubicLength), padString(Integer.toBinaryString(cubicalIndex), cubicLength));
//            System.out.println(MSDB+" "+Integer.toBinaryString(getCubicalIndex(key))+" "+Integer.toBinaryString(cubicalIndex));
            diff = cubicalIndex - getCubicalIndex(key);
            if(getCyclicIndex(key) == k - 1 && MSDB <= k - 1 && diff <= 0){
                if(rightCyclicNeighbor == null || diff < minDifference){
                    rightCyclicNeighbor = key;
                    minDifference = diff;
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
        leaf.add(prefix.concat(String.format("%02d", findSmallerOrMax(matchingKeys, lastDigit))));
        leaf.add(prefix.concat(String.format("%02d", findLargerOrMin(matchingKeys, lastDigit))));

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
//        System.out.println(id.substring(0, id.length()-2));
        return Integer.parseInt(id.substring(0, id.length()-2), 2);
    }
    public static int getCyclicIndex(String id){
        return Integer.parseInt(id.substring(id.length()-2), 10);
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
