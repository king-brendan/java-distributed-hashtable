package MessageTypes;

public class Neighbors implements Message {
    public String predecessor;
    public String successor;

    public Neighbors(String p, String s) {
        this.predecessor = p;
        this.successor = s;
    }

    public String type() {
        return "neighbors";
    }

}
