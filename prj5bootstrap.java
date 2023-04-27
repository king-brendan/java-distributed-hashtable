import java.net.InetAddress;
import java.io.IOException;

import java.util.ArrayList;

public class prj5bootstrap {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting bootstrap server...");
        MyBootstrapState state = new MyBootstrapState();
        state.myHostname = InetAddress.getLocalHost().getHostName();
        state.members = new ArrayList<String>();
        state.senders = new ArrayList<Sender>();

        Receiver r = new Receiver(state);
        r.start();
    }
}
