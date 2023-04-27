import java.net.ServerSocket;
import java.net.Socket;

public class Receiver extends Thread {
    MyState state;
    String type;

    Receiver(MyState state) {
        this.state = state;
        this.type = state.getType();
    }

    public void run() {
        try {
            try (ServerSocket ss = new ServerSocket(8000)) {
                // listen for new connections
                while (true) {
                    Socket s = ss.accept();
                    String hostname = s.getInetAddress().getHostName().split("-")[1];
                    SingleHostReceiver shr = null;
                    if (this.type.equals("bootstrap")) {
                        shr = new BootstrapSHR((MyBootstrapState) state, s, hostname);
                    } else if (this.type.equals("peer")) {
                        shr = new PeerSHR((MyPeerState) state, s, hostname);
                    } else {
                        // TODO
                    }
                    shr.start();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}