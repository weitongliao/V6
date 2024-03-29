import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// 现在实现了client给server发送具有header的消息，需要server进行处理并返回有效信息，
// client需要对信息进行处理并更新node对象的邻居
public class ReceiveHandler implements Runnable {
    private DatagramPacket packet;
    private Node node;

    public ReceiveHandler(DatagramPacket packet, Node node) {
        this.packet = packet;
        this.node = node;
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
        if (Objects.equals(header, "r")){
            String receivedResult = (String) receivedMap.get("D");
            List<String> receivedList = Arrays.asList(receivedResult.substring(1, receivedResult.length() - 1).split(", "));
            if(!Objects.equals(receivedList.get(0), "null")){
                node.setLeftOutsideLeaf(new NodeID(receivedList.get(0), receivedList.get(7)));
            }
            if(!Objects.equals(receivedList.get(1), "null")){
                node.setRightOutsideLeaf(new NodeID(receivedList.get(1), receivedList.get(8)));
            }
            if(!Objects.equals(receivedList.get(2), "null")){
                node.setLeftCyclicNeighbor(new NodeID(receivedList.get(2), receivedList.get(9)));
            }
            if(!Objects.equals(receivedList.get(3), "null")){
                node.setRightCyclicNeighbor(new NodeID(receivedList.get(3), receivedList.get(10)));
            }
            if(!Objects.equals(receivedList.get(4), "null")){
                node.setCubicalNeighbor(new NodeID(receivedList.get(4), receivedList.get(11)));
            }
            if(!Objects.equals(receivedList.get(5), "null")){
                node.setLeftInsideLeaf(new NodeID(receivedList.get(5), receivedList.get(12)));
            }
            if(!Objects.equals(receivedList.get(6), "null")){
                node.setRightInsideLeaf(new NodeID(receivedList.get(6), receivedList.get(13)));
            }

        } else if (Objects.equals(header, "f") || Objects.equals(header, "e")){
            node.routing(header, (String) receivedMap.get("S"), (String) receivedMap.get("DE"), (String) receivedMap.get("D"));
        }else if (Objects.equals(header, "i")){
            // no longer effect
            String receiveData = (String) receivedMap.get("D");
            String[] parts = receiveData.split(",");
            node.setNodeId(parts[0]);
            node.setIp(parts[1]);
        }
    }
}
