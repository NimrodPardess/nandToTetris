import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    private final Scanner scanner;
    private String currentLine;
    private String nextLine;
    public String instruct;
    private String functionName;

    // create enum for handling constants
//    public enum CommandType {
//        C_ARITHMETIC,
//        C_PUSH,
//        C_POP,
//        C_LABEL,
//        C_GOTO,
//        C_IF,
//        C_FUNCTION,
//        C_RETURN,
//        C_CALL;
    //}
    public static final int C_ARITHMETIC = 1;
    public static final int C_PUSH = 2;
    public static final int C_POP = 3;
    public static final int C_LABEL = 4;
    public static final int C_GOTO = 5;
    public static final int C_IF = 6;
    public static final int C_FUNCTION = 7;
    public static final int C_CALL = 8;
    public static final int C_RETURN = 9;

    // Implement constructor
    public Parser(File file) throws IOException {
        System.out.println("trying to create new file = " + file.getName());
        this.scanner = new Scanner(file);
        this.currentLine = "";
        this.functionName = "";
    }

    // checks if there are more line in the file
    public boolean hasMoreLines() {
        System.out.println("yes00");
        return (this.scanner.hasNextLine());
    }

    // keep reading until there are good line, ignore white spaces and comments
    public void advance() throws IOException {
        // keep reading until there are good line
        System.out.println("yes0");
        if (hasMoreLines()) {
            String line = scanner.nextLine();
            System.out.println("yes1");
            // checks if there is a comment
            if (!line.startsWith("//") && !line.isEmpty()) {
                System.out.println("yes2");
                this.currentLine = line.split("//")[0].trim();
            } else {
                System.out.println("yes3");
                advance();
            }
        }
    }

    // Returns a constant representing the type of the current command
    public int commandType() {

        ArrayList<String> arithmetics = new ArrayList<>();
        arithmetics.add("add"); 
        arithmetics.add("sub");
        arithmetics.add("neg");
        arithmetics.add("eq");
        arithmetics.add("gt");
        arithmetics.add("lt");
        arithmetics.add("and");
        arithmetics.add("or");
        arithmetics.add("not");

        String command = this.currentLine.split(" ")[0].trim();
        //System.out.println(command);

        // checks C_ARITHMETIC command
        if (arithmetics.contains(command)) {
            return C_ARITHMETIC;
        }
        // checks C_PUSH command
        if (command.equals("push")) {
            return C_PUSH;
        }
        // checks C_POP command
        if (command.equals("pop")) {
            return C_POP;
        }
        // checks C_LABEL command
        if (command.equals("label")) {
            return C_LABEL;
        }
        // checks C_GOTO command
        if (command.equals("goto")) {
            return C_GOTO;
        }
        // checks C_IF command
        if (command.equals("if-goto")) {
            return C_IF;
        }
        // checks C_FUNCTION command
        if (command.equals("function")) {
            this.functionName = this.currentLine.split(" ")[1];
            return C_FUNCTION;
        }
        // checks C_RETURN command
        if (command.equals("return")) {
            return C_RETURN;
        }
        // checks C_CALL command
        if (command.equals("call")) {
            return C_CALL;
        }
        return 0;
    }

    // Return the first argument of the current command
    public String arg1() {
        if (this.commandType() == C_ARITHMETIC) {
            return this.currentLine.split(" ")[0];
        } else {
            return this.currentLine.split(" ")[1];
        }
    }

    // Returns the second argument of the current command
    public int arg2() throws NumberFormatException {
        try {
            return Integer.parseInt(currentLine.split(" ")[2]);
        } catch (NumberFormatException nte) {
        }
        return 0;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(String currentLine) {
        this.currentLine = currentLine;
    }
}







