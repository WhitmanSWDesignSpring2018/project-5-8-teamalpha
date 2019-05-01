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
    
//    public String getFileName(){
//        
//    }
    
    public void newFile(String textfile){
        File file = new File("/home/taylorkm/Documents/" + "testfile"+ ".txt"); 
        try{
            file.createNewFile(); 
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile()); 
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(textfile); 
            bw.close();
            
            System.out.println("something");
                    
        }
        catch (IOException e){
            
        }
    }
    
}
