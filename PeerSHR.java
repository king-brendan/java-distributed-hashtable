
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import MessageTypes.Message;
import MessageTypes.Neighbors;
import MessageTypes.Pre;
import MessageTypes.Request;
import MessageTypes.Succ;

public class PeerSHR extends SingleHostReceiver {
    MyPeerState state;

    public PeerSHR(MyPeerState state, Socket s, String hostname) {
        this.state = state;
        this.s = s;
        this.hostname = hostname;
    }

    public void listen() throws UnknownHostException, IOException, InterruptedException {
        while (true) {
            try {
                Message m = null;
                try {
                    m = (Message) din.readObject();
                } catch (Error e) {
                    System.out.println(e);
                }
                switch (m.type()) {
                    case "neighbors":
                        Neighbors n = (Neighbors) m;
                        System.out.println("Predecessor is: " + n.predecessor + ", Successor is: " + n.successor);
                        this.state.pre = n.predecessor;
                        this.state.succ = n.successor;
                        break;
                    case "pre":
                        Pre p = (Pre) m;
                        System.out.println("Predecessor is: " + p.pre);
                        this.state.pre = p.pre;
                        break;
                    case "succ":
                        Succ s = (Succ) m;
                        System.out.println("Successor is: " + s.succ);
                        this.state.succ = s.succ;
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                // silently cancel the thread if the TCP connection closes
                return;
            }
        }
    }
}
