import java.net.InetAddress;
import java.net.UnknownHostException;

public class prj5client {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("Starting client server...");
        MyClientState state = new MyClientState();
        state.myHostname = InetAddress.getLocalHost().getHostName();

        Receiver r = new Receiver(state);
        r.start();
    }
}