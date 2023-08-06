import java.io.*;
import java.net.*;

public class ServerV6 {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(12345);

        System.out.println("Server started, waiting for connections...");

        try {
            Socket[] clientSockets = new Socket[2];
            String[] clientInfo = new String[2];

            for (int i = 0; i < 2; i++) {
                clientSockets[i] = serverSocket.accept();
                InetAddress clientAddress = clientSockets[i].getInetAddress();
                int clientPort = clientSockets[i].getPort();
                clientInfo[i] = clientAddress.getHostAddress() + " " + clientPort;
                System.out.println("Connected to client: " + clientInfo[i]);
            }

            // Send peer info to each client
            for (int i = 0; i < 2; i++) {
                PrintWriter out = new PrintWriter(clientSockets[i].getOutputStream(), true);
                out.println(clientInfo[(i + 1) % 2]);
            }

            // Start P2P communication
            Thread client1Thread = new Thread(new P2PHandler(clientSockets[0], clientSockets[1]));
            Thread client2Thread = new Thread(new P2PHandler(clientSockets[1], clientSockets[0]));

            client1Thread.start();
            client2Thread.start();

            client1Thread.join();
            client2Thread.join();
        } finally {
            serverSocket.close();
        }
    }
}

