public class MyPeerState extends MyState {
    public String pre;
    public String succ;
    public Sender preSender;
    public Sender succSender;
    public Sender bootstrapSender;

    public String getType() {
        return "peer";
    }
}
