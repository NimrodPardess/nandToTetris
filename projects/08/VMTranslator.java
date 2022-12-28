import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class VMTranslator {
    
    public static void translateFiles(File currentFile, CodeWriter cw) throws IOException{
        // Constructs a Parser to handle the input file
        Parser p = new Parser(currentFile);
        cw.setFileName(currentFile.getName());
        while (p.hasMoreLines()) {
            p.advance();
            if ((p.commandType() == Parser.C_PUSH) || (p.commandType() == Parser.C_POP)) {
                cw.writePushPop(p.commandType(), p.arg1(), p.arg2());
            } else if (p.commandType() == Parser.C_ARITHMETIC) {
                cw.writeArithmetic(p.arg1());
            } else if (p.commandType() == Parser.C_LABEL) {
                cw.writeLabel(p.arg1(), p.getFunctionName());
            } else if (p.commandType() == Parser.C_GOTO) {
                cw.writeGoto(p.arg1(), p.getFunctionName());
            } else if (p.commandType() == Parser.C_IF) {
                cw.writeIf(p.arg1(), p.getFunctionName());
            } else if (p.commandType() == Parser.C_FUNCTION) {
                cw.writeFunction(p.arg1(), p.arg2());
            } else if (p.commandType() == Parser.C_CALL) {
                cw.writeCall(p.arg1(), p.arg2());
            } else if (p.commandType() == Parser.C_RETURN) {
                cw.writeReturn();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // read the file
        String file = args[0];
        System.out.println("the input = " + file);
        // creating the output
        File check = new File(file);
        System.out.println("the check = " + check.getAbsolutePath());

        // checks whether th file is a directory or a single file 
        if (check.isDirectory()){
            //get the directory path
            String dirPath = check.getAbsolutePath();
            // create output file name
            String dirName = check.getAbsolutePath() + "/" + check.getName()+ ".asm";
            // create output file 
            File output = new File(dirName);
            System.out.println("the dir file name = " + output);
            // construct codewriter with output
            CodeWriter cw = new CodeWriter(output);
            cw.writeInit();

            System.out.println("the dir path = " + dirPath);
            File[] directoryListing = check.listFiles();

            for (File f : directoryListing){
                System.out.println("File f name is: " + f.getName());
                if(f.getName().endsWith(".vm")){
                    System.out.println("good");
                    translateFiles(f, cw);
                }
            }
            cw.endCommand();
            cw.close();
        }
        // the case where the file is single 
        else{
            // creating the name of the output file
            String fileName = file.replace(".vm", ".asm");
            // creating the output
            File output = new File(fileName);
            // Constructs a CodeWriter to handle the output file
            CodeWriter cw = new CodeWriter(output);
            translateFiles(check, cw);
            cw.endCommand();
            cw.close();
        }

        //cw.writeInit();

        // marches through the input file, parsing each line and generating code from it
        // System.out.println(p.hasMoreLines());
        // while (p.hasMoreLines()) {
        //     System.out.println("2");
        //     p.advance();
        //     System.out.println("3");
        //     if ((p.commandType() == Parser.C_PUSH) || (p.commandType() == Parser.C_POP)) {
        //         cw.writePushPop(p.commandType(), p.arg1(), p.arg2());
        //     } else if (p.commandType() == Parser.C_ARITHMETIC) {
        //         cw.writeArithmetic(p.arg1());
        //     } else if (p.commandType() == Parser.C_LABEL) {
        //         cw.writeLabel(p.arg1(), p.getFunctionName());
        //     } else if (p.commandType() == Parser.C_GOTO) {
        //         cw.writeGoto(p.arg1(), p.getFunctionName());
        //     } else if (p.commandType() == Parser.C_IF) {
        //         cw.writeIf(p.arg1(), p.getFunctionName());
        //     } else if (p.commandType() == Parser.C_FUNCTION) {
        //         cw.writeFunction(p.arg1(), p.arg2());
        //     } else if (p.commandType() == Parser.C_CALL) {
        //         cw.writeCall(p.arg1(), p.arg2());
        //     } else if (p.commandType() == Parser.C_RETURN) {
        //         cw.writeReturn();
        //     }
        // }
        // cw.endCommand();
        // cw.close();
    }
}
