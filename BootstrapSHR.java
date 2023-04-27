
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import MessageTypes.Message;
import MessageTypes.Neighbors;
import MessageTypes.ObjStored;
import MessageTypes.Pre;
import MessageTypes.Request;
import MessageTypes.Succ;

public class BootstrapSHR extends SingleHostReceiver {
    MyBootstrapState state;
    Sender thisSender;

    public BootstrapSHR(MyBootstrapState state, Socket s, String hostname) {
        this.state = state;
        this.s = s;
        this.hostname = hostname;
    }

    void addMember() throws UnknownHostException, IOException, InterruptedException {
        Integer hostnum = ArgParser.hostnameToID(this.hostname);
        int index = 0;
        if (state.members.size() == 0) {
            state.members.add(this.hostname);
        } else {
            for (String s : state.members) {
                Integer thisnum = ArgParser.hostnameToID(s);
                if (hostnum > thisnum) {
                    index = index + 1;
                    continue;
                } else {
                    break;
                }
            }
            state.members.add(index, this.hostname);
        }

        System.out.println("Members: " + state.members);

        Sender sender = new Sender(state, this.hostname);
        state.senders.add(sender);
        this.thisSender = sender;
        sender.start();

        while (!sender.connectionSuccessful) {
            Thread.sleep(200);
        }
    }

    void alertNeighbors() {
        int myIndex = state.members.indexOf(this.hostname);
        if (state.members.size() > 1) {
            String pre = "";
            String succ = "";
            if (myIndex == 0) {
                succ = state.members.get(1);
                pre = state.members.get(state.members.size() - 1);
            } else if (myIndex == state.members.size() - 1) {
                succ = state.members.get(0);
                pre = state.members.get(myIndex - 1);
            } else {
                succ = state.members.get(myIndex + 1);
                pre = state.members.get(myIndex - 1);
            }

            this.thisSender.sendMessage(new Neighbors(pre, succ));
            for (Sender s : state.senders) {
                if (s.targetHostname.equals(pre)) {
                    s.sendMessage(new Succ(this.hostname));
                }
                if (s.targetHostname.equals(succ)) {
                    s.sendMessage(new Pre(this.hostname));
                }
            }
        }
    }

    void handleRequest(Request r) {
        for (Sender s : this.state.senders) {
            if (s.targetHostname.equals("client")) {
                continue;
            }
            if (ArgParser.hostnameToID(s.targetHostname) == 1) {
                s.sendMessage(r);
            }
        }
    }

    void handleObjResponse(Message m) {
        for (Sender s : this.state.senders) {
            if (s.targetHostname.equals("client")) {
                s.sendMessage(m);
            }
        }
    }

    public void listen() throws UnknownHostException, IOException, InterruptedException {
        System.out.println("New peer joined: " + this.hostname);
        if (this.hostname.equals("client")) {
            Sender sender = new Sender(state, this.hostname);
            state.senders.add(sender);
            sender.start();
        } else {
            addMember();
            alertNeighbors();
        }

        while (true) {
            try {
                Message m = (Message) din.readObject();
                switch (m.type()) {
                    case "request":
                        handleRequest((Request) m);
                        break;
                    case "objstored":
                        handleObjResponse(m);
                        break;
                    case "objretrieved":
                        handleObjResponse(m);
                        break;
                    case "notfound":
                        handleObjResponse(m);
                        break;
                    default:
                        System.err.println("Unsupported message type. You got a bug there, buster");
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                // silently cancel the thread if the TCP connection closes
                return;
            }
        }
    }
}
