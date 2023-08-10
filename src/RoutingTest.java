public class RoutingTest {

    public static void main(String[] args) throws InterruptedException {
        MyCounter count = new MyCounter();
        // 创建一个共享的对象作为锁
        Object lock = new Object();

        // 创建 CommunicationThread 和 ReplyThread
        CommunicationThread communicationThread = new CommunicationThread("localhost", 9876, lock, count);
        ReplyThread replyThread = new ReplyThread(9876, lock, count);

        // 创建线程并启动
        Thread thread1 = new Thread(communicationThread);
        Thread thread2 = new Thread(replyThread);
        thread1.start();
        thread2.start();
//        System.out.println(count.getCount());
//        thread1.join();
//        thread2.join();
        while (true){
            System.out.println(count.getCount());
        }

    }

    static class CommunicationThread implements Runnable {
        private String serverAddress;
        private int serverPort;
        private Object lock;
        private MyCounter count;

        public CommunicationThread(String serverAddress, int serverPort, Object lock, MyCounter count) {
            this.serverAddress = serverAddress;
            this.serverPort = serverPort;
            this.lock = lock;
            this.count = count;
        }

        @Override
        public void run() {

            // 在同步块中访问共享对象
            while (true){
                count.increment();
            }
//            for (int i = 0; i < 5; i++) {
//
//            }

        }
    }

    static class ReplyThread implements Runnable {
        private int listenPort;
        private Object lock;
        private MyCounter count;

        public ReplyThread(int listenPort, Object lock, MyCounter count) {
            this.listenPort = listenPort;
            this.lock = lock;
            this.count = count;
        }

        @Override
        public void run() {

//            for (int i = 0; i < 5; i++) {
//                count.increment();
//            }
            while (true){
                count.increment();
            }
        }
    }
}

class MyCounter {
    private int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
