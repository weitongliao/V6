public class ClientTest2 {
    public static void main(String[] args) {
        Node node = new Node();

        int cpu = 2;
        int gpu = 3;
        int ram = 4;

        ClientTest.startService(node, cpu, gpu, ram);
    }
}
