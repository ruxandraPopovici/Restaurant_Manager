package start;

import bussiness.DeliveryService;
import data.Serializer;
import presentation.UserGUI;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String [] args) throws IOException {
        DeliveryService deliveryService = new DeliveryService("hei");

        File test = new File("deliveryService.ser");
        if(test.exists()){
            deliveryService = Serializer.deserialize("deliveryService.ser");
        }

        UserGUI gui = new UserGUI(deliveryService);

        gui.setVisible(true);
    }
}
