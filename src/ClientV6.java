import java.io.*;
import java.net.*;

public class ClientV6 {
    public static void main(String[] args) throws IOException {
        String serverIPv6Address = "fe80::25c6:397:7e73:9775";  // Replace with actual server's IPv6 address

        Socket socket = new Socket(serverIPv6Address, 12345);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String ownIPv6Address = socket.getLocalAddress().getHostAddress();
        int ownPort = socket.getLocalPort();

        System.out.println("Connected to server");
        System.out.println("Own info: " + ownIPv6Address + " " + ownPort);

        // Send own info to server
        out.println(ownIPv6Address + " " + ownPort);

        // Receive peer info from server
        String peerInfo = in.readLine();
        String[] peerParts = peerInfo.split(" ");
        String peerIPv6Address = peerParts[0];
        int peerPort = Integer.parseInt(peerParts[1]);

        System.out.println("Peer info: " + peerIPv6Address + " " + peerPort);

        // Start P2P communication
        try {
            Socket peerSocket = new Socket(peerIPv6Address, 23456);
            System.out.println("connected");

            Thread sendThread = new Thread(new SendMessageHandler(peerSocket.getOutputStream()));
            Thread receiveThread = new Thread(new ReceiveMessageHandler(peerSocket.getInputStream()));

            sendThread.start();
            receiveThread.start();

            sendThread.join();
            receiveThread.join();

            peerSocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        socket.close();
    }
}

