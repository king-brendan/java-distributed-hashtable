import java.io.IOException;
import java.net.InetAddress;

public class prj5client {
    public static void main(String[] args) throws InterruptedException, IOException {
        MyClientState state = new MyClientState();
        state.myHostname = InetAddress.getLocalHost().getHostName();
        ArgParser.parse(state, args);

        Thread.sleep(state.delay * 1000);
        System.out.println("Starting client server...");

        Sender sender = new Sender(state, "bootstrap");
        sender.start();

        while (!sender.connectionSuccessful) {
            Thread.sleep(200);
        }

        System.out.println("Boutta start this test case!");
        return;

        // Receiver r = new Receiver(state);
        // r.start();
    }
}