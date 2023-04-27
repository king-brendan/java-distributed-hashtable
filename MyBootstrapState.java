
import java.util.ArrayList;

public class MyBootstrapState extends MyState {
    public ArrayList<String> members;
    public ArrayList<Sender> senders;

    public String getType() {
        return "bootstrap";
    }
}
