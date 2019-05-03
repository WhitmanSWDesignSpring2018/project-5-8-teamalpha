/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author taylorkm
 */
public class FileSaver {
    
    Scanner scanner = new Scanner(System.in); 
    
    /**
     * Creates and writes to a new file.
     * @param textfile, the String to be written to the file
     * @param filename, the name of the new file
     */
    public void newFile(String textfile, String filename){
        File file = new File("/home/taylorkm/Documents/" + filename + ".txt"); 
        try{
            file.createNewFile(); 
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile()); 
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(textfile); 
            bw.close();
        }
        catch (IOException e){
            
        }
    }
    
}
