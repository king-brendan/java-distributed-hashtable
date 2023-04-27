import java.io.IOException;
import java.net.Socket;

import MessageTypes.Message;
import MessageTypes.NotFound;
import MessageTypes.ObjRetrieved;
import MessageTypes.ObjStored;

public class ClientSHR extends SingleHostReceiver {
    MyClientState state;

    public ClientSHR(MyClientState state, Socket s, String hostname) {
        this.state = state;
        this.s = s;
        this.hostname = hostname;
    }

    public void listen() {
        while (true) {
            try {
                Message m = null;
                try {
                    m = (Message) din.readObject();
                } catch (Error e) {
                    System.out.println(e);
                }
                switch (m.type()) {
                    case "objstored":
                        ObjStored o = (ObjStored) m;
                        System.out.println("STORED: " + "<" + o.objectID + ">");
                        break;
                    case "objretrieved":
                        ObjRetrieved r = (ObjRetrieved) m;
                        System.out.println("RETRIEVED: " + "<" + r.objectID + ">");
                        break;
                    case "notfound":
                        NotFound n = (NotFound) m;
                        System.out.println("NOT FOUND: " + "<" + n.objectID + ">");
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                // silently cancel the thread if the TCP connection closes
                return;
            }
        }
    }
}
