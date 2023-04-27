import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import MessageTypes.Message;

public class Sender extends Thread {
    Socket s;
    ObjectOutputStream dout;
    String targetHostname;
    public boolean connectionSuccessful;
    MyState state;

    public Sender(MyState state, String hostname) throws UnknownHostException, IOException, InterruptedException {
        this.connectionSuccessful = false;
        this.state = state;
        this.targetHostname = hostname;
    }

    public void run() {
        boolean ready = false;
        while (!ready) {
            // add delay
            try {
                this.s = new Socket(this.targetHostname, 8000);
                ready = true;
            } catch (IOException e) {
                continue;
            }
        }

        try {
            this.dout = new ObjectOutputStream(this.s.getOutputStream());
            this.connectionSuccessful = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message m) {
        if (!connectionSuccessful) {
            return;
        }
        try {
            // System.err.println("Sending message of type " + m.type() + " to " +
            // this.targetHostname);
            this.dout.writeObject(m);
        } catch (IOException e) {
            System.err.println("Error sending message to " + this.targetHostname);
        }
    }
}
