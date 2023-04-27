package MessageTypes;

public class Pre implements Message {
    public String pre;

    public Pre(String p) {
        this.pre = p;
    }

    public String type() {
        return "pre";
    }

}
