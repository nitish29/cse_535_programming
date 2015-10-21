package ir_project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
* This is a Logger class, basically for logging data
* the method log handles the file write operations
* it opens a filewriter (the log file name is captured from the main function)
* it appends all the data written to it
* and once the data has been written to the log file, the open file writer connection is closed.
* */

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
