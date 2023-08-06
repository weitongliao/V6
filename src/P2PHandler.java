import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class P2PHandler implements Runnable {
    private Socket sender;
    private Socket receiver;

    public P2PHandler(Socket sender, Socket receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(sender.getInputStream()));
            PrintWriter out = new PrintWriter(receiver.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from client: " + message);
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
