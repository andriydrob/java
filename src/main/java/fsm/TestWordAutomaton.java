package fsm;

public class TestWordAutomaton {

    public static String run(String input) {
        String state = "S";
        for (char c : input.toCharArray()) {
            state = next(state, c);
        }
        return state;
    }

    static String next(String state, char c) {
        return switch (state) {
            case "S" -> c == 'T' ? "1" : "S";
            case "1" -> c == 'E' ? "2" : fail(c);
            case "2" -> c == 'S' ? "3" : fail(c);
            case "3" -> c == 'T' ? "F" : fail(c);
            case "F" -> "F";
            default -> throw new IllegalStateException(state);
        };
    }

    private static String fail(char c) {
        return c == 'T' ? "1" : "S";
    }

    public static void main(String[] args) {
        for (String s : args) {
            System.out.println("\"" + s + "\" -> " + run(s));
        }
    }
}
