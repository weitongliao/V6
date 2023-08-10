import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
//现在实现了client给server发送具有header的消息，需要server进行处理并返回有效信息，client需要对信息进行处理并更新node对象的邻居
public class ReceiveHandler implements Runnable {
    private DatagramPacket packet;
    private Node node;

    public ReceiveHandler(DatagramPacket packet, Node node) {
        this.packet = packet;
        this.node = node;
    }

    @Override
    public void run() {
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getData());
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);

            // Deserialize the received object (dictionary)

            Object receivedObject = objectStream.readObject();
            if (receivedObject instanceof java.util.HashMap) {
                java.util.HashMap<?, ?> receivedMap = (java.util.HashMap<?, ?>) receivedObject;
                System.out.println("Received dictionary: " + receivedMap);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
