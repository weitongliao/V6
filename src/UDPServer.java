import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class UDPServer {
    public static void main(String[] args) {
        Map<String, InetSocketAddress> clients = new HashMap<>();
        DatagramSocket serverSocket = null;

        try {
            serverSocket = new DatagramSocket(12345, Inet6Address.getByName("::"));

            byte[] receiveData = new byte[1024];

            System.out.println("Server started, waiting for connections...");

            while (clients.size() < 2) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String clientInfo = clientAddress.getHostAddress() + " " + clientPort;

                if (!clients.containsKey(clientInfo)) {
                    System.out.println("Connected to client: " + clientInfo);
                    clients.put(clientInfo, new InetSocketAddress(clientAddress, clientPort));
                }
            }

            // Send peer info to each client
            for (Map.Entry<String, InetSocketAddress> entry : clients.entrySet()) {
                String peerInfo = clients.get(entry.getKey().equals(clients.keySet().toArray()[0]) ?
                        clients.keySet().toArray()[1] : clients.keySet().toArray()[0]).toString();
                DatagramPacket sendPacket = new DatagramPacket(peerInfo.getBytes(), peerInfo.getBytes().length,
                        entry.getValue().getAddress(), entry.getValue().getPort());
                serverSocket.send(sendPacket);
            }

            // Start P2P communication
            new UDPP2PHandler(clients.get(clients.keySet().toArray()[0]), clients.get(clients.keySet().toArray()[1])).start();
            new UDPP2PHandler(clients.get(clients.keySet().toArray()[1]), clients.get(clients.keySet().toArray()[0])).start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}

class UDPP2PHandler extends Thread {
    private InetSocketAddress sender;
    private InetSocketAddress receiver;

    public UDPP2PHandler(InetSocketAddress sender, InetSocketAddress receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        try {
            DatagramSocket senderSocket = new DatagramSocket();

            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                senderSocket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from client: " + message);

                DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.getBytes().length,
                        receiver.getAddress(), receiver.getPort());
                senderSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
