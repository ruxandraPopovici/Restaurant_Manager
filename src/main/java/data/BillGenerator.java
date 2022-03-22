package data;

import bussiness.MenuItem;
import bussiness.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class BillGenerator implements Serializable {

    private File file;
    private String fileName;

    public BillGenerator(int orderID){
        this.fileName = "billOfOrder" + orderID + ".txt";

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
    public void generateBill(Order order, List<MenuItem> orderItems, float orderPrice){
        try{
            FileWriter myWriter = new FileWriter(fileName);
            String textToAdd = "";
            textToAdd += order.toString();
            textToAdd += "\n";
            textToAdd += "Menu Items: \n";

            for(MenuItem menuItem : orderItems){
                textToAdd += menuItem.getName() + ", $" + menuItem.computePrice();
                textToAdd += "\n";
            }
            textToAdd += "Final Order Price: $" + orderPrice;

            myWriter.write(textToAdd);
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
