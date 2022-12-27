import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class VMTranslator {
    public static void main(String[] args) throws IOException {

        // read the file
        String file = args[0];
        // creating the name of the output file
        String fileName = file.replace(".vm", ".asm");
        // creating the output
        File output = new File(fileName);

        // Constructs a Parser to handle the input file
        Parser p = new Parser(new File(file));

        // Constructs a CodeWriter to handle the output file
        CodeWriter cw = new CodeWriter(output);

        cw.writeInit();

        // marches through the input file, parsing each line and generating code from it
        while (p.hasMoreLines()) {
            p.advance();

            System.out.println(p.commandType());
            //System.out.println(Parser.CommandType.C_PUSH);
            //System.out.println(p.commandType() == Parser.C_PUSH);
            if((p.commandType() == Parser.C_PUSH) || (p.commandType() == Parser.C_POP)) {
                cw.writePushPop(p.commandType(), p.arg1(), p.arg2());
            }
            else if(p.commandType() == Parser.C_ARITHMETIC) {
                cw.writeArithmetic(p.arg1());
            }
            else if(p.commandType() == Parser.C_LABEL) {
                cw.writeLabel(p.arg1());
            }
            else if(p.commandType() == Parser.C_GOTO) {
                cw.writeGoto(p.arg1());
            }
            else if(p.commandType() == Parser.C_IF) {
                cw.writeIf(p.arg1());
            }
            else if(p.commandType() == Parser.C_FUNCTION) {
                cw.writeFunction(p.arg1(), p.arg2());
            }
            else if(p.commandType() == Parser.C_CALL) {
                cw.writeCall(p.arg1(), p.arg2());
            }
            else if(p.commandType() == Parser.C_RETURN) {
                cw.writeReturn();
            }

        }
        cw.endCommand();
        cw.close();
    }
}
