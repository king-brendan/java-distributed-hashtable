
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class SingleHostReceiver extends Thread {
    ObjectInputStream din;
    Socket s;
    String hostname;

    public abstract void listen() throws UnknownHostException, IOException, InterruptedException;

    public void run() {
        try {
            this.din = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            System.err.println("Could not assign DataInputStream");
            return;
        }
        try {
            this.listen();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
