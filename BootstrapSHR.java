
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import MessageTypes.Message;
import MessageTypes.Neighbors;
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
        Integer hostnum = Integer.parseInt(this.hostname.substring(1));
        int index = 0;
        if (state.members.size() == 0) {
            state.members.add(this.hostname);
        } else {
            for (String s : state.members) {
                Integer thisnum = Integer.parseInt(s.substring(1));
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

    public void listen() throws UnknownHostException, IOException, InterruptedException {
        System.out.println("New peer joined: " + this.hostname);
        if (!this.hostname.equals("client")) {
            addMember();
            alertNeighbors();
        }

        while (true) {
            try {
                Message m = (Message) din.readObject();
                System.out.println("Message received from " + this.hostname + " " + m.type());
                Request r = (Request) m;
                System.out.println(r.operationType);
            } catch (IOException | ClassNotFoundException e) {
                // silently cancel the thread if the TCP connection closes
                return;
            }
        }
    }
}
