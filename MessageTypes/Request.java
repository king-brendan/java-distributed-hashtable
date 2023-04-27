package MessageTypes;

public class Request implements Message {
    public Integer reqID;
    public String operationType;
    public Integer objectID;
    public Integer clientID;

    public Request(Integer r, String op, Integer ob, Integer c) {
        this.reqID = r;
        this.operationType = op;
        this.objectID = ob;
        this.clientID = c;
    }

    public String type() {
        return "request";
    }

}