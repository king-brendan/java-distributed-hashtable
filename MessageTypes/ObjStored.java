package MessageTypes;

public class ObjStored implements Message {
    public Integer objectID;
    public Integer clientID;
    public Integer peerID;

    public ObjStored(Integer o, Integer c, Integer p) {
        this.objectID = o;
        this.clientID = c;
        this.peerID = p;
    }

    public String type() {
        return "objstored";
    }

}
