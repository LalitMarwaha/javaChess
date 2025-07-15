package mypack;

import java.util.*;
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MoveLogger {
    private String logFilename;

    public MoveLogger(String filename) {
        this.logFilename = filename;
    }

    public void logMove(/*String moveNotation*/) {

        try (PrintStream out = new PrintStream(new FileOutputStream(logFilename,true))
        /*PrintWriter writer = new PrintWriter(new FileWriter(logFilename, true))*/) { // `true` for append mode
            if(GamePanel.Moves[GamePanel.moveCount-1]!=null){
                out.println(GamePanel.Moves[GamePanel.moveCount-1]);
            }
            //writer.println(GamePanel.Moves[GamePanel.moveCount]);
        } catch (IOException e) {
            System.err.println("Error logging move: " + e.getMessage());
        }
    }

    //to remove last line after Undo
    

    // Optional: Clear log file
    public void clearLog() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilename, false))) { // `false` to overwrite
            writer.print(""); // Clear content
        } catch (IOException e) {
            System.err.println("Error clearing log: " + e.getMessage());
        }
    }
}