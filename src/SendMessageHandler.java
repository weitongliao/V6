import java.io.*;

class SendMessageHandler implements Runnable {
    private OutputStream outputStream;

    public SendMessageHandler(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(outputStream, true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String message;
            while ((message = reader.readLine()) != null) {
                out.println(message);
                System.out.println("send " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
