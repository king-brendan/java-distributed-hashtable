import java.io.IOException;
import java.net.InetAddress;

public class prj5peer {
    public static void main(String[] args) throws IOException, InterruptedException {
        MyPeerState state = new MyPeerState();
        state.myHostname = InetAddress.getLocalHost().getHostName();
        state.pre = null;
        state.succ = null;
        state.delay = 0;
        ArgParser.parse(state, args);

        Thread.sleep(state.delay * 1000);
        System.out.println("Starting peer server...");

        Sender sender = new Sender(state, "bootstrap");
        state.bootstrapSender = sender;
        sender.start();

        Receiver r = new Receiver(state);
        r.start();
    }
}
