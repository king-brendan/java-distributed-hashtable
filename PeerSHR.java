
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import MessageTypes.Message;
import MessageTypes.Neighbors;
import MessageTypes.NotFound;
import MessageTypes.ObjRetrieved;
import MessageTypes.ObjStored;
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

    void handleRequest(Request r) throws IOException {
        if (r.objectID >= ArgParser.hostnameToID(state.myHostname)) {
            this.state.succSender.sendMessage(r);
        } else {
            File objectsFile = new File("objects/" + state.objectsFile);
            switch (r.operationType) {
                case "STORE":
                    handleStore(r, objectsFile);
                    break;
                case "RETRIEVE":
                    handleRetrieve(r, objectsFile);
                    break;
            }

        }
    }

    void handleStore(Request r, File objectsFile) throws IOException {
        FileWriter fr = new FileWriter(objectsFile, true);
        fr.write(r.clientID + "::" + r.objectID);
        fr.close();
        printObjectsFile(objectsFile);
        this.state.bootstrapSender
                .sendMessage(new ObjStored(r.objectID, r.clientID, ArgParser.hostnameToID(state.myHostname)));
    }

    void printObjectsFile(File f) throws FileNotFoundException {
        Scanner scanner = new Scanner(f);
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if (data == null) {
                break;
            }
            System.out.println(data);
        }
        scanner.close();
    }

    void handleRetrieve(Request r, File objectsFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(objectsFile);
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if (data == null) {
                continue;
            }
            String[] parts = data.split("::");
            Integer clientID = Integer.parseInt(parts[0]);
            Integer objectID = Integer.parseInt(parts[1]);
            if (clientID.equals(r.clientID) && objectID.equals(r.objectID)) {
                this.state.bootstrapSender.sendMessage(new ObjRetrieved(r.objectID));
                scanner.close();
                return;
            }
        }
        this.state.bootstrapSender.sendMessage(new NotFound(r.objectID));
        scanner.close();
    }

    void updatePreSender() throws UnknownHostException, IOException, InterruptedException {
        Sender s = new Sender(state, this.state.pre);
        this.state.preSender = s;
        s.start();
    }

    void updateSuccSender() throws UnknownHostException, IOException, InterruptedException {
        Sender s = new Sender(state, this.state.succ);
        this.state.succSender = s;
        s.start();
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
                        updatePreSender();
                        updateSuccSender();
                        break;
                    case "pre":
                        Pre p = (Pre) m;
                        System.out.println("Predecessor is: " + p.pre);
                        this.state.pre = p.pre;
                        updatePreSender();
                        break;
                    case "succ":
                        Succ s = (Succ) m;
                        System.out.println("Successor is: " + s.succ);
                        this.state.succ = s.succ;
                        updateSuccSender();
                        break;
                    case "request":
                        handleRequest((Request) m);
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                // silently cancel the thread if the TCP connection closes
                return;
            }
        }
    }
}
