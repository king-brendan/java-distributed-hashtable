package MessageTypes;

public class Succ implements Message {
    public String succ;

    public Succ(String s) {
        this.succ = s;
    }

    public String type() {
        return "succ";
    }

}
