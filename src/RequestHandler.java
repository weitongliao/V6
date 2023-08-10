import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;

public class RequestHandler implements Runnable {
    private DatagramPacket packet;

    public RequestHandler(DatagramPacket packet) {
        this.packet = packet;
    }

    @Override
    public void run() {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getData());
        ObjectInputStream objectStream = null;
        try {
            objectStream = new ObjectInputStream(byteStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize the received object (dictionary)
        try {
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