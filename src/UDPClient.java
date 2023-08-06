import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        String serverIPv6Address = "fe80::25c6:397:7e73:977"; // Replace with actual server's IPv6 address
        int serverPort = 12345;

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            socket.connect(new InetSocketAddress(serverIPv6Address, serverPort));

            String ownIPv6Address = socket.getLocalAddress().getHostAddress();
            int ownPort = socket.getLocalPort();

            System.out.println("Connected to server");
            System.out.println("Own info: " + ownIPv6Address + " " + ownPort);

            byte[] sendData = (ownIPv6Address + " " + ownPort).getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(serverIPv6Address), serverPort);
            socket.send(sendPacket);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String peerInfo = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] peerParts = peerInfo.split(" ");
            String peerIPv6Address = peerParts[0];
            int peerPort = Integer.parseInt(peerParts[1]);

            System.out.println("Peer info: " + peerIPv6Address + " " + peerPort);

            // Start P2P communication
            new UDPClientP2PHandler(socket, peerIPv6Address, peerPort).start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}

class UDPClientP2PHandler extends Thread {
    private DatagramSocket socket;
    private String peerIPv6Address;
    private int peerPort;

    public UDPClientP2PHandler(DatagramSocket socket, String peerIPv6Address, int peerPort) {
        this.socket = socket;
        this.peerIPv6Address = peerIPv6Address;
        this.peerPort = peerPort;
    }

    @Override
    public void run() {
        try {
            DatagramSocket peerSocket = new DatagramSocket();
            peerSocket.connect(new InetSocketAddress(peerIPv6Address, peerPort));

            byte[] sendData;
            byte[] receiveData = new byte[1024];

            while (true) {
                sendData = ("Message from client " + socket.getLocalAddress().getHostAddress()).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                        InetAddress.getByName(peerIPv6Address), peerPort);
                peerSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                peerSocket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from peer: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
