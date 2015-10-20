package ir_project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {



        private File logFile;

        public Log() {

        }

        public Log(String fileName) {
            logFile = new File(fileName);
        }

        public Log(File f) {
            logFile = f;

        }

        public void log(String s) {

            try {
                FileWriter fw = new FileWriter(this.logFile,true);
                fw.write(s);
                fw.write(System.lineSeparator());
                fw.close();
            } catch (IOException ex) {
                System.err.println("Error Logging Data: "+s);
            }

        }



}
