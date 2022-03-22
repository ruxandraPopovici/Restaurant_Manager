package presentation;

import bussiness.DeliveryService;
import bussiness.Order;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class EmployeeGUI extends JFrame implements Serializable {
    DeliveryService deliveryService;

    private JLabel employeeLabel = new JLabel("Hello Employee!");
    private JLabel orderLabel = new JLabel("Last placed Order:");
    private JTextField orderText = new JTextField();

    private Order currentOrder = null;

    public EmployeeGUI(DeliveryService deliveryService){
        this.deliveryService = deliveryService;

        JPanel content = new JPanel();
        JPanel[] contents = setPanels(3);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        orderText.setPreferredSize( new Dimension( 450, 180 ) );
        employeeLabel.setFont(new Font(employeeLabel.getFont().getName(), Font.BOLD, 16));

        contents[0].add(employeeLabel);
        contents[1].add(orderLabel);
        contents[2].add(orderText);

        for(int i = 0; i < 3; i++){
            content.add(contents[i]);
        }

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(500, 300);
        this.setTitle("Employee");
        this.setLayout(new BorderLayout());

        this.setContentPane(content);
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }
    private void setOrder(Order order){
        this.currentOrder = order;
    }
    private JPanel[] setPanels(int noPanels){
        JPanel [] contents = new JPanel[noPanels];
        for(int i = 0; i < noPanels; i++){
            contents[i] = new JPanel();
            contents[i].setLayout(new FlowLayout(FlowLayout.CENTER));
        }
        return contents;
    }

    public void notifyObserver(Order order){
        setOrder(order);
        String textToAdd = "Order with ID " + currentOrder.getOrderID() + ", of client no. " +
                currentOrder.getClientID() + "\n is currently being processed.\n";
        textToAdd += "Date: " + currentOrder.getOrderDate().toString();

        orderText.setText(textToAdd);
    }
}
