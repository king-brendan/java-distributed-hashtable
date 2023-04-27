package MessageTypes;

public class NotFound implements Message {
    public Integer objectID;

    public NotFound(Integer o) {
        this.objectID = o;
    }

    public String type() {
        return "notfound";
    }

}
