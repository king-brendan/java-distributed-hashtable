public class ArgParser {
    public static void parse(MyState state, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d")) {
                state.delay = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equals("-t")) {
                state.testcase = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equals("-o")) {
                state.objectsFile = args[i + 1];
            }
        }
    }

    public static Integer hostnameToID(String hostname) {
        return Integer.parseInt(hostname.substring(1));
    }
}
