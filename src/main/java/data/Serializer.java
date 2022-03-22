package data;

import bussiness.DeliveryService;

import java.io.*;

public class Serializer {

    public static void serialize(Serializable object){
        try {
            FileOutputStream file = new FileOutputStream("deliveryService.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(object);
            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DeliveryService deserialize(String path) {
        DeliveryService deliveryService;
        try {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);
            deliveryService = (DeliveryService)in.readObject();
            in.close();
            file.close();
            return deliveryService;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
