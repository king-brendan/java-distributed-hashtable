package MessageTypes;

public class ObjRetrieved implements Message {
    public Integer objectID;

    public ObjRetrieved(Integer o) {
        this.objectID = o;
    }

    public String type() {
        return "objretrieved";
    }

}
