package presentation;

import bussiness.*;
import bussiness.MenuItem;
import data.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class ClientGUI extends JFrame  implements Serializable {

    private DeliveryService deliveryService;
    private EmployeeGUI employee;

    private JLabel viewLabel = new JLabel("View All Products");
    private JButton viewButton = new JButton("Products");
    private JFrame productsFrame = new JFrame();

    private JComboBox criteria;
    private JLabel searchLabel = new JLabel("Search based on given criteria: ");
    private JTextField searchText = new JTextField(20);
    private JButton searchButton = new JButton("Search");
    private JComboBox foundProducts;

    private JLabel priceLabel = new JLabel("Total price");
    private JTextField priceText = new JTextField(15);

    Object [][] ordersData;
    private JLabel orderedLabel = new JLabel("Your current ordered products:");
    private JScrollPane ordersPane;
    private JButton addProductButton = new JButton("Add Product to your Order");
    private JButton placeOrderButton = new JButton("Place Order");

    JPanel[] contents = setPanels(8);
    final JPanel content = new JPanel();

    public ClientGUI(EmployeeGUI employeeGUI, int clientID){

        this.deliveryService = employeeGUI.getDeliveryService();
        employee = employeeGUI;

        deliveryService.createOrder(clientID, new Date());

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        contents[0].add(viewLabel);
        contents[0].add(viewButton);
        contents[1].add(searchLabel);

        String [] criteriaString = {"Name",
                "Rating",
                "Calories",
                "Proteins",
                "Fats",
                "Sodium",
                "Price"};
        criteria = new JComboBox(criteriaString);
        contents[1].add(criteria);
        contents[1].add(searchText);

        contents[2].add(searchButton);

        foundProducts = new JComboBox();
        contents[3].add(foundProducts);
        contents[3].add(addProductButton);

        contents[4].add(orderedLabel);
        ordersPane = new JScrollPane();
        contents[5].add(ordersPane);
        contents[6].add(placeOrderButton);

        contents[7].add(priceLabel);
        contents[7].add(priceText);

        for(int i = 0; i < 8; i++){
            content.add(contents[i]);
        }

        viewButton.addActionListener(e -> {
            Map<String, MenuItem> menuItemMap = deliveryService.getMenuItems();
            List<MenuItem> menuItems = new ArrayList<>(menuItemMap.values());

            setProductsFrame(menuItems);
        });
        searchButton.addActionListener(e -> {
            String searchCriteria = String.valueOf(criteria.getSelectedItem());
            String searchValue = searchText.getText();

            List<MenuItem> menuItems = deliveryService.searchMenuItems(searchCriteria, searchValue);
            System.out.println("ai apasat fmm");
            for (MenuItem m : menuItems){
                System.out.println(m.getName());
            }

            setFoundProducts(menuItems);
            contents[3].remove(0);
            contents[3].remove(0);
            contents[3].add(foundProducts);
            contents[3].add(addProductButton);

            content.revalidate();
            content.repaint();
        });
        addProductButton.addActionListener(e -> {
            String productName = String.valueOf(foundProducts.getSelectedItem());
            deliveryService.addOrderItem(deliveryService.getCurrentOrderID(), productName);

            List<MenuItem> orderedProducts = deliveryService.getOrderByID(deliveryService.getCurrentOrderID());

            ordersPane = setOrdersTable(orderedProducts);
            contents[5].remove(0);
            contents[5].add(ordersPane);

            content.revalidate();
            content.repaint();
        });
        placeOrderButton.addActionListener(e -> {
            priceText.setText(String.valueOf(deliveryService.computeOrderPrice(deliveryService.getCurrentOrderID())));
            try {
                deliveryService.generateBill(deliveryService.getCurrentOrderID());
                employee.notifyObserver(deliveryService.getOrders().get(deliveryService.getOrders().size() - 1));
                deliveryService.setCurrentOrderID(deliveryService.getCurrentOrderID() + 1);
            } catch (IOException ioException) {
                showError("Could not print bill.");
            }
        });

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 500);
        this.setTitle("Client Frame");
        this.setLayout(new BorderLayout());

        this.setContentPane(content);
        this.setVisible(true);
    }

    private JScrollPane setOrdersTable(List <MenuItem> menuItems){
        JTable ordersTable;
        String [] columnNames = {"Name",
                    "Price"};

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        ordersData = new Object[menuItems.size()][2];
        int i = 0;
        for(MenuItem m : menuItems){
            ordersData[i][0] = m.getName();
            ordersData[i][1] = m.computePrice();
            i++;
        }

        ordersTable = new JTable(ordersData, columnNames);

        return new JScrollPane(ordersTable);
    }
    private JPanel[] setPanels(int noPanels){
        JPanel [] contents = new JPanel[noPanels];
        for(int i = 0; i < noPanels; i++){
            contents[i] = new JPanel();
            contents[i].setLayout(new FlowLayout(FlowLayout.CENTER));
        }
        return contents;
    }
    private void setProductsFrame(List<MenuItem> menuItems){
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JTable productsTable;

        Object[][] productsData = new Object[menuItems.size()][7];

        String [] columnNames = {"Name",
                "Rating",
                "Calories",
                "Proteins",
                "Fats",
                "Sodium",
                "Price"};
        int i = 0;
        for(MenuItem m : menuItems){
            productsData[i][0] = m.getName();
            productsData[i][6] = m.computePrice();
            for(int j = 1; j < 6; j++){
                productsData[i][j] = 0;
            }
            if(m instanceof BaseProduct){
                productsData[i][1] = ((BaseProduct)m).getRating();
                productsData[i][2] = ((BaseProduct)m).getCalories();
                productsData[i][3] = ((BaseProduct)m).getProteins();
                productsData[i][4] = ((BaseProduct)m).getFats();
                productsData[i][5] = ((BaseProduct)m).getSodium();
            }
            i++;

        }
        productsTable = new JTable(productsData, columnNames);

        JScrollPane sp = new JScrollPane(productsTable);
        content.add(sp);

        productsFrame.setTitle("Products Table");
        productsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productsFrame.setSize(500, 300);
        productsFrame.setLayout(new BorderLayout());

        productsFrame.setContentPane(content);
        productsFrame.setVisible(true);
    }
    private void setFoundProducts(List <MenuItem> menuItems){
        String [] products = new String[menuItems.size()];
        int i = 0;
        for(MenuItem m : menuItems){
            products[i] = m.getName();
            i++;
        }
        foundProducts = new JComboBox(products);
    }
    private void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }

}
