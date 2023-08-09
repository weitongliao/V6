import java.net.*;

public class UDPServer {
    public static void main(String[] args) {
        int serverPort = 12345;

        try {
            DatagramSocket serverSocket = new DatagramSocket(serverPort);

            System.out.println("UDP Server started, waiting for connections...");

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                serverSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                System.out.println("Connected to client: " + clientAddress.getHostAddress() + ":" + clientPort);

                Thread clientThread = new ClientHandler(serverSocket, receivePacket);
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private DatagramSocket socket;
    private DatagramPacket receivePacket;

    public ClientHandler(DatagramSocket socket, DatagramPacket receivePacket) {
        this.socket = socket;
        this.receivePacket = receivePacket;
    }

    @Override
    public void run() {
        try {
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            String th = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Message from client: " + th);

            byte[] sendData;
            String responseMessage = "Hello from server!";
            sendData = responseMessage.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            socket.send(sendPacket);

            System.out.println("Response sent to client: " + clientAddress.getHostAddress() + ":" + clientPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
