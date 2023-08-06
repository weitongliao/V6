import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class ReceiveMessageHandler implements Runnable {
    private InputStream inputStream;

    public ReceiveMessageHandler(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from peer: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
