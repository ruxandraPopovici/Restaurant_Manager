package data;

import bussiness.MenuItem;
import bussiness.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {
    private File file;
    private String fileName;

    public ReportGenerator(String name){
        this.fileName = name + ".txt";

        try{
            file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        }catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void write(String textToAdd){
        try{
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("Your Report\n");

            myWriter.write(textToAdd);
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
