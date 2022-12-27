import java.io.*;
import java.util.Objects;

public class CodeWriter {

    private FileWriter writer;
    private int currentLine;
    private int labelCount;
    private String fileName;

    public CodeWriter(File output) throws IOException {
        this.fileName = output.getName();
        this.writer = new FileWriter(output);
        setFileName(output.getName());
        this.currentLine = 0;
        this.labelCount = 0;
    }
    
    // informs that the translation of a new VM file has started
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public void writeInit() throws IOException{
        writeAndCount("@256");
        writeAndCount("D=A");
        writeAndCount("@SP");
        writeAndCount("M=D");
        
        writeCall("Sys.init", 0);
    }

    public void writeAndCount(String command) throws IOException{
        writer.write(command);
        writer.write('\n');
        this.currentLine++;
        
    }

    public void writeArithmetic(String command) throws IOException {

        if (command.equals("add")) {
            writeAndCount("@SP");
            writeAndCount("AM=M-1");
            writeAndCount("D=M");
            writeAndCount("A=A-1");
            writeAndCount("M=D+M");
        }
        if (Objects.equals(command, "sub")) {
            writeAndCount("@SP");
            writeAndCount("AM=M-1");
            writeAndCount("D=M");
            writeAndCount("A=A-1");
            writeAndCount("M=M-D");
        }
        if (Objects.equals(command, "and")) {
            writeAndCount("@SP");
            writeAndCount("AM=M-1");
            writeAndCount("D=M");
            writeAndCount("A=A-1");
            writeAndCount("M=D&M");
        }
        if (Objects.equals(command, "or")) {
            writeAndCount("@SP");
            writeAndCount("AM=M-1");
            writeAndCount("D=M");
            writeAndCount("A=A-1");
            writeAndCount("M=D|M");
        }
        if (Objects.equals(command, "eq")) {
            writeAndCount("@SP");
            writeAndCount("AM=M-1");
            writeAndCount("D=M");
            //writeAndCount("@SP"); //MABYE BUG
            writeAndCount("A=A-1");
            writeAndCount("D=M-D");
            writeAndCount("@" + (this.currentLine + 4)); // label True
            writeAndCount("D;JEQ");
            writeAndCount("D=0"); // False
            writeAndCount("@" + (this.currentLine + 3)); // label False
            writeAndCount("0;JMP");
            writeAndCount("D=-1");// True
            writeAndCount("@SP");
            writeAndCount("A=M-1");
            writeAndCount("M=D");

        }
        if (Objects.equals(command, "gt")) {
            writeAndCount("@SP");
            writeAndCount("AM=M-1");
            writeAndCount("D=M");
            writeAndCount("@SP"); //MABYE BUG
            writeAndCount("A=A-1");
            writeAndCount("D=M-D");
            writeAndCount("@" + (this.currentLine + 4)); // label True
            writeAndCount("D;JLT");
            writeAndCount("D=0"); // False
            writeAndCount("@" + (this.currentLine + 3)); // label False
            writeAndCount("0;JMP");
            writeAndCount("D=-1");// True
            writeAndCount("@SP");
            writeAndCount("A=M-1");
            writeAndCount("M=D");
        }
        if (Objects.equals(command, "lt")) {
            writeAndCount("@SP");
            writeAndCount("AM=M-1");
            writeAndCount("D=M");
            writeAndCount("@SP"); //MABYE BUG
            writeAndCount("A=A-1");
            writeAndCount("D=M-D");
            writeAndCount("@" + (this.currentLine + 4)); // label True
            writeAndCount("D;JGT");
            writeAndCount("D=0"); // False
            writeAndCount("@" + (this.currentLine + 3)); // label False
            writeAndCount("0;JMP");
            writeAndCount("D=-1");// True
            writeAndCount("@SP");
            writeAndCount("A=M-1");
            writeAndCount("M=D");
        }
        if (Objects.equals(command, "neg")) {
            writeAndCount("@SP"); 
            writeAndCount("A=M-1"); 
            writeAndCount("M=-M");
        }
        if (Objects.equals(command, "not")) {
            writeAndCount("@SP");
            writeAndCount("A=M-1");
            writeAndCount("M=!M");
        }
    }

    private void parsePop(String segment, int index) throws IOException {
        switch (segment) {
            case "local": {
                writeAndCount("@LCL");
                writeAndCount("D=M");
                writeAndCount("@" + index);
                writeAndCount("D=D+A");
                break;
            }
            case "argument": {
                writeAndCount("@ARG");
                writeAndCount("D=M");
                writeAndCount("@" + index);
                writeAndCount("D=D+A");
                break;
            }
            case "this": {
                writeAndCount("@THIS");
                writeAndCount("D=M");
                writeAndCount("@" + index);
                writeAndCount("D=D+A");
                break;
            }
            case "that": {
                writeAndCount("@THAT");
                writeAndCount("D=M");
                writeAndCount("@" + index);
                writeAndCount("D=D+A");
                break;
            }
            case "pointer": {
                if (index == 0){
                    writeAndCount("@THIS");
                    writeAndCount("D=A");
                    break;
                } else{
                    writeAndCount("@THAT");
                    writeAndCount("D=A");
                    break;
                }
            }
            case "static": {
                writeAndCount("@" + fileName + "." + index); //Maybe bug
                writeAndCount("D=A");
                break;
            }
            case "temp": {
                writeAndCount("@R5");
                writeAndCount("D=A");
                writeAndCount("@" + index);
                writeAndCount("D=D+A");
                break;
            }
        }
        writeAndCount("@R13");
        writeAndCount("M=D");
        writeAndCount("@SP");
        writeAndCount("AM=M-1"); 
        writeAndCount("D=M");
        writeAndCount("@R13");
        writeAndCount("A=M");
        writeAndCount("M=D");
    }

    public void parsePush(String segment, int index) throws IOException {
        switch (segment) {
            case "local": {
                writeAndCount("@LCL");
                writeAndCount("D=M");
                writeAndCount("@" + index);
                writeAndCount("A=D+A");
                writeAndCount("D=M");
                break;
            }
            case "argument": {
                writeAndCount("@ARG");
                writeAndCount("D=M");
                writeAndCount("@" + index);
                writeAndCount("A=D+A");
                writeAndCount("D=M");
                break;
            }
            case "this": {
                writeAndCount("@THIS");
                writeAndCount("D=M");
                writeAndCount("@" + index);
                writeAndCount("A=D+A");
                writeAndCount("D=M");
                break;
            }
            case "that": {
                writeAndCount("@THAT");
                writeAndCount("D=M");
                writeAndCount("@" + index);
                writeAndCount("A=D+A");
                writeAndCount("D=M");
                break;
            }
            case "pointer": {
                if (index == 0) {
                    writeAndCount("@THIS");
                    writeAndCount("D=M");
                    break;
                } else {
                    writeAndCount("@THAT");
                    writeAndCount("D=M");
                    break;
                }
            }
            case "constant": {
                writeAndCount("@" + index);
                writeAndCount("D=A");
                break;
            }
            case "static": {
                writeAndCount("@" + fileName + "." + index);
                writeAndCount("D=M");
                break;
            }
            case "temp": {
                writeAndCount("@R5");
                writeAndCount("D=A");
                writeAndCount("@" + index);
                writeAndCount("A=D+A");
                writeAndCount("D=M");
                break;
            }

        }
        writeAndCount("@SP");
        writeAndCount("A=M");
        writeAndCount("M=D");
        writeAndCount("@SP");
        writeAndCount("M=M+1");
    }

    public void writePushPop(int command, String segment, int index) throws IOException {
        if (command == Parser.C_POP) {
            parsePop(segment, index);
        } else if (command == Parser.C_PUSH) {
            parsePush(segment, index);
        }
    }

    public void writeLabel(String label) throws IOException{
        writeAndCount("("+ label +")");
    }
    
    public void writeGoto(String label) throws IOException{
        writeAndCount("@" + label);
        writeAndCount("0;JMP");
    }

    public void writeIf(String label) throws IOException{
        writeAndCount("// if goto call " + label);
        writeAndCount("@SP");
        writeAndCount("AM=M-1");
        writeAndCount("D=M");
        writeAndCount("@" + label);
        writeAndCount("D;JNE");
    }

    public void writeFunction(String functionName, int nVars) throws IOException{
        // we declare the function
        writeLabel(functionName);

        // set all vars to zero
        for(int i = 0 ; i < nVars ; i++){
            writePushPop(Parser.C_PUSH, "constant", 0);
        }
    }

    public void writeCall(String functionName, int nArgs) throws IOException{
//        StringBuilder closePushCommands = new StringBuilder();
//        // save calling function
//        writeAndCount("// call " + functionName + " " + nArgs + '\n');
//        writer.write("// saving calling function");
//        writer.write('\n');
//        writeAndCount("@return-address" + labelCount);
//        writeAndCount("D=A");
        String c = Integer.toString(currentLine);
        // building the close push commands 
        writeAndCount("@SP");
        writeAndCount("D=M");
        writeAndCount("@R13");
        writeAndCount("M=D");
        writeAndCount("@RET." + c);
        writeAndCount("D=A");
        writeAndCount("@SP");
        writeAndCount("A=M");
        writeAndCount("M=D");
        writeAndCount("@SP");
        writeAndCount("M=M+1");
        writeAndCount("@LCL");
        writeAndCount("D=M");
        writeAndCount("@SP");
        writeAndCount("A=M");
        writeAndCount("M=D");
        writeAndCount("@SP");
        writeAndCount("M=M+1");
        writeAndCount("@ARG");
        writeAndCount("D=M");
        writeAndCount("@SP");
        writeAndCount("A=M");
        writeAndCount("M=D");
        writeAndCount("@SP");
        writeAndCount("M=M+1");
        writeAndCount("@THIS");
        writeAndCount("D=M");
        writeAndCount("@SP");
        writeAndCount("A=M");
        writeAndCount("M=D");
        writeAndCount("@SP");
        writeAndCount("M=M+1");
        writeAndCount("@THAT");
        writeAndCount("D=M");
        writeAndCount("@SP");
        writeAndCount("A=M");
        writeAndCount("M=D");
        writeAndCount("@SP");
        writeAndCount("M=M+1");
        writeAndCount("@R13");
        writeAndCount("D=M");
        writeAndCount("@" + nArgs);
        writeAndCount("D=D-A");
        writeAndCount("@ARG");
        writeAndCount("M=D");
        writeAndCount("@SP");
        writeAndCount("D=M");
        writeAndCount("@LCL");
        writeAndCount("M=D");
        writeAndCount("@" + functionName);
        writeAndCount("0;JMP");
        writeAndCount("(RET." + c + ")");
    }

    public void writeReturn() throws IOException{
        writeAndCount("@LCL");
        writeAndCount("D=M");
        writeAndCount("@5");
        writeAndCount("A=D-A");
        writeAndCount("D=M");
        writeAndCount("@13");
        writeAndCount("M=D");

        // set ARG = pop()
        writeAndCount("@SP");
        writeAndCount("A=M-1");
        writeAndCount("D=M");
        writeAndCount("@ARG");
        writeAndCount("A=M");
        writeAndCount("M=D");

        // restore SP of the caller
        writeAndCount("D=A+1");
        writeAndCount("@SP");
        writeAndCount("M=D");

        // restore THAT of the caller
        writeAndCount("@LCL");
        writeAndCount("AM=M-1");
        writeAndCount("D=M");
        writeAndCount("@THAT");
        writeAndCount("M=D");

        // restore THIS of the caller
        writeAndCount("@LCL");
        writeAndCount("AM=M-1");
        writeAndCount("D=M");
        writeAndCount("@THIS");
        writeAndCount("M=D");

        // restore ARG of the caller
        writeAndCount("@LCL");
        writeAndCount("AM=M-1");
        writeAndCount("D=M");
        writeAndCount("@ARG");
        writeAndCount("M=D");

        // restore LCL of the caller
        writeAndCount("@LCL");
        writeAndCount("A=M-1");
        writeAndCount("D=M");
        writeAndCount("@LCL");
        writeAndCount("M=D");

        // goto RET
        writeAndCount("@13");
        writeAndCount("A=M");
        writeAndCount("0;JMP");
    }

    public void endCommand() throws IOException {  
        writeAndCount("(END)");
        writeAndCount("@END");
        writeAndCount("0;JMP");
    }

    // Close the input file
    public void close() throws IOException {
        this.writer.close();
    }
}

