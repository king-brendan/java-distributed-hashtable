import MessageTypes.Request;

public class ClientTestCases {
    public static void initialize(MyClientState state) {
        System.out.println("Starting testcase " + state.testcase);
        switch (state.testcase) {
            case 3:
                state.bootstrapSender.sendMessage(new Request(1, "STORE", 88, 3));
                break;
            default:
                System.err.println("You messed up your testcase input! NMP");
                break;
        }
    }
}
