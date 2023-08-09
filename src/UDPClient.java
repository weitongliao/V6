import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        String serverIPv4Address = "fe80::25c6:397:7e73:9775"; // Replace with actual server's IPv4 address
        int serverPort = 12345;

        try {
            DatagramSocket clientSocket = new DatagramSocket();

            String message = "Hello from client!";
            byte[] sendData = message.getBytes();

            InetAddress serverAddress = InetAddress.getByName(serverIPv4Address);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

            clientSocket.send(sendPacket);
            System.out.println("Message sent to server");

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            clientSocket.receive(receivePacket);

            String responseMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Response from server: " + responseMessage);

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
