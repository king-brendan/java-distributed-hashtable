import MessageTypes.Request;

public class ClientTestCases {
    public static void initialize(MyClientState state) {
        System.out.println("Starting testcase " + state.testcase);
        switch (state.testcase) {
            case 3:
                state.bootstrapSender.sendMessage(new Request(1, "STORE", 88, 3));
                break;
            case 4:
                state.bootstrapSender.sendMessage(new Request(1, "RETRIEVE", 67, 2));
                break;
            case 5:
                state.bootstrapSender.sendMessage(new Request(1, "RETRIEVE", 69, 2));
                break;
            default:
                System.err.println("You messed up your testcase input! NMP");
                break;
        }
    }
}
