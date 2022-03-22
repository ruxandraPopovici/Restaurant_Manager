package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import bussiness.BaseProduct;
import bussiness.DeliveryService;
import bussiness.MenuItem;

public class AdminGUI extends JFrame  implements Serializable {

    private DeliveryService deliveryService;

    private JTextField nameText = new JTextField(17);
    private JTextField newProductText = new JTextField(17);
    private JTextField ratingText = new JTextField(13);
    private JTextField caloriesText = new JTextField(13);
    private JTextField proteinsText = new JTextField(13);
    private JTextField fatsText = new JTextField(13);
    private JTextField sodiumText = new JTextField(13);
    private JTextField priceText = new JTextField(17);

    private JLabel nameLabel = new JLabel("Name of Product");
    private JLabel newProductLabel = new JLabel("Name of New Composite Product");
    private JLabel ratingLabel = new JLabel("Rating");
    private JLabel caloriesLabel = new JLabel("Calories");
    private JLabel proteinsLabel = new JLabel("Proteins");
    private JLabel fatsLabel = new JLabel("Fats");
    private JLabel sodiumLabel = new JLabel("Sodium");
    private JLabel priceLabel = new JLabel("Price");

    private JButton importProductButton = new JButton("IMPORT product");
    private JButton addProductButton = new JButton("ADD product");
    private JButton deleteProductButton = new JButton("DELETE product");
    private JButton editProductButton = new JButton("EDIT product");

    //for reports
    private JLabel timeLabel = new JLabel("Time Interval Of Required Orders");
    private JLabel noOrdersLabel = new JLabel("Number Of Orders");
    private JLabel valueLabel = new JLabel("Value of Order");
    private JLabel noOrdersClientsLabel = new JLabel(", ordered more than the specified amount of:");
    private JLabel dayLabel = new JLabel("Day of Orders");

    private JTextField minTimeText = new JTextField(10);
    private JTextField maxTimeText = new JTextField(10);
    private JTextField noOrdersClientText = new JTextField(10);
    private JTextField noOrdersText = new JTextField(10);
    private JTextField valueText = new JTextField(10);
    private JTextField dayText = new JTextField(13);

    private JButton generateReport = new JButton("Generate Report");

    private enum Criteria{
        RATING, CALORIES, PROTEIN, FAT, SODIUM, PRICE
    }

    public AdminGUI(DeliveryService deliveryService){
        this.deliveryService = deliveryService;
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        nameText.setBackground(Color.LIGHT_GRAY);
        priceText.setBackground(Color.LIGHT_GRAY);
        newProductText.setBackground(Color.PINK);

        JPanel[] flowContents = setPanels(13, true);
        JPanel[] boxContents = setPanels(5, false);

        JLabel helloMsg = new JLabel("Hello Administrator!");
        helloMsg.setFont(new Font(helloMsg.getFont().getName(), Font.BOLD, 16));
        flowContents[0].add(helloMsg);
        flowContents[1].add(nameLabel);
        flowContents[1].add(nameText);
        flowContents[2].add(newProductLabel);
        flowContents[2].add(newProductText);

        boxContents[0].add(ratingLabel);
        boxContents[0].add(ratingText);
        boxContents[1].add(caloriesLabel);
        boxContents[1].add(caloriesText);
        boxContents[2].add(proteinsLabel);
        boxContents[2].add(proteinsText);
        boxContents[3].add(fatsLabel);
        boxContents[3].add(fatsText);
        boxContents[4].add(sodiumLabel);
        boxContents[4].add(sodiumText);

        flowContents[3].add(importProductButton);
        flowContents[3].add(addProductButton);
        flowContents[3].add(deleteProductButton);
        flowContents[3].add(editProductButton);

        flowContents[4].add(boxContents[0]);
        flowContents[4].add(boxContents[1]);
        flowContents[4].add(boxContents[2]);

        flowContents[5].add(boxContents[3]);
        flowContents[5].add(boxContents[4]);

        flowContents[6].add(priceLabel);
        flowContents[6].add(priceText);

        JLabel reportMsg = new JLabel("Generate reports");
        reportMsg.setFont(new Font(reportMsg.getFont().getName(), Font.BOLD, 14));
        flowContents[7].add(reportMsg);

        flowContents[8].add(timeLabel);
        flowContents[8].add(minTimeText);
        flowContents[8].add(maxTimeText);

        flowContents[9].add(noOrdersLabel);
        flowContents[9].add(noOrdersText);

        flowContents[10].add(valueLabel);
        flowContents[10].add(valueText);
        flowContents[10].add(noOrdersClientsLabel);
        flowContents[10].add(noOrdersClientText);

        flowContents[11].add(dayLabel);
        flowContents[11].add(dayText);

        flowContents[12].add(generateReport);

        for(int i = 0; i < 13; i++){
            content.add(flowContents[i]);
        }

        Map<String, MenuItem> menuItems = deliveryService.getMenuItems();

        //buttons
        importProductButton.addActionListener(e -> {
            deliveryService.importProducts("products.csv");
            for(int i = 0; i < 7; i++){
                System.out.println(deliveryService.getInitialProducts().get(i).getName());
            }
        });

        addProductButton.addActionListener(e -> {
            String initialProductName = nameText.getText();
            String compositeProductName = newProductText.getText();

            List<BaseProduct> initialProducts = deliveryService.getInitialProducts();

            if(!initialProductName.isEmpty() && compositeProductName.isEmpty()){
                BaseProduct searchFor = initialProducts.stream().filter(baseProduct ->
                        (initialProductName + " ").equals(baseProduct.getName()))
                        .findFirst()
                        .orElse(null);

                if(searchFor != null){
                    float[] values = new float[6];
                    values[0] = searchFor.getRating();
                    values[1] = searchFor.getCalories();
                    values[2] = searchFor.getProteins();
                    values[3] = searchFor.getFats();
                    values[4] = searchFor.getSodium();
                    values[5] = searchFor.computePrice();

                    deliveryService.addProduct(searchFor.getName(), values);
                    showError("Item successfully added!");
                }
                else{
                    try{
                        float [] values = getItemValues();
                        deliveryService.addProduct(initialProductName, values);
                        showError("New item successfully added!");
                    }
                    catch(NumberFormatException ex){
                        showError("Product doesn't exist! If you want to add a new product, please provide" +
                                " with valid data!");
                    }
                }

            }
            else if(initialProductName.isEmpty() && !compositeProductName.isEmpty()){
                deliveryService.addProduct(compositeProductName);
                showError("A new Composite Product has been created. Please add components!");

            }
            else if(!initialProductName.isEmpty() && !compositeProductName.isEmpty()){

                if(!initialProductName.equals(compositeProductName + " "))
                {
                    try{
                        deliveryService.addItemComposite(compositeProductName, initialProductName);
                        showError("Item successfully added to menu.");
                    }catch(AssertionError assertionError){
                        showError("Did not find required items!");
                    }
                }
                else
                    {
                    showError("Invalid operation - cannot add composite product into composite" +
                            "product with the same name!");
                }
            }
            else{
                showError("Provide with products to add!");
            }
        });
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productName = nameText.getText();
                MenuItem search = menuItems.get(productName);
                if(search == null){
                    showError("Product doesn't exist.");
                }
                else{
                    deliveryService.deleteMenuItem(productName);
                    showError("Product successfully removed.");
                }
            }
        });
        editProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String productName = nameText.getText();
                if(productName.isEmpty()){
                    showError("Please provide with the name of the product you want to edit!");
                    return;
                }

                float[] newValues = new float[6];

                if (!ratingText.getText().isEmpty()) {
                    newValues[0] = Float.parseFloat(ratingText.getText());
                }
                if (!caloriesText.getText().isEmpty()) {
                    newValues[1] = Float.parseFloat(caloriesText.getText());
                }
                if (!proteinsText.getText().isEmpty()) {
                    newValues[2] = Float.parseFloat(proteinsText.getText());
                }
                if (!fatsText.getText().isEmpty()) {
                    newValues[3] = Float.parseFloat(fatsText.getText());
                }
                if (!sodiumText.getText().isEmpty()) {
                    newValues[4] = Float.parseFloat(sodiumText.getText());
                }
                if (!priceText.getText().isEmpty()) {
                    newValues[5] = Float.parseFloat(priceText.getText());
                }
                try{
                    for (int i = 0; i < 6; i++) {
                        if (newValues[i] != 0) {
                            deliveryService.editMenuItem(productName, Criteria.values()[i].toString(), newValues[i]);
                            showError("Item successfully edited.");
                        }
                    }
                }catch(AssertionError error){
                    showError("Invalid operation - product either does not exist, either cannot edit its " +
                            "components!");
                }

            }
        });
        generateReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    switch (whatReport()){
                        case 1:
                            int min = Integer.parseInt(minTimeText.getText());
                            int max = Integer.parseInt(maxTimeText.getText());
                            deliveryService.intervalReport(min, max);
                            break;
                        case 2:
                            int noOrders = Integer.parseInt(noOrdersText.getText());
                            deliveryService.noOrderReport(noOrders);
                            break;
                        case 3:
                            int valueOfOrder = Integer.parseInt(valueText.getText());
                            int noOrdersClient = Integer.parseInt(noOrdersClientText.getText());
                            deliveryService.valueReport(noOrdersClient, valueOfOrder);
                            break;
                        case 4:
                            int dayOfOrder = Integer.parseInt(dayText.getText());
                            deliveryService.dayReport(dayOfOrder);
                            break;
                        default:

                    }
                }
                catch(NumberFormatException ex){
                    showError("Enter valid data!");
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(700, 500);
        this.setTitle("Administrator");
        this.setLayout(new BorderLayout());

        this.setContentPane(content);
        this.setVisible(true);
    }

    private JPanel[] setPanels(int noPanels, boolean flow){
        JPanel [] contents = new JPanel[noPanels];
        for(int i = 0; i < noPanels; i++){
            contents[i] = new JPanel();
            if(flow){
                contents[i].setLayout(new FlowLayout(FlowLayout.CENTER));
            }
            else{
                contents[i].setLayout(new BoxLayout(contents[i], BoxLayout.Y_AXIS));
            }
        }
        return contents;
    }
    private void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }
    private int whatReport(){
        if(!minTimeText.getText().isEmpty() && !minTimeText.getText().isEmpty()) return 1;
        else if(!noOrdersText.getText().isEmpty()) return 2;
        else if(!valueText.getText().isEmpty()) return 3;
        else if(!dayText.getText().isEmpty()) return 4;
        else{
            showError("Please provide with data to generate desired report!");
        }
        return 0;
    }
    private float [] getItemValues(){
        float[] values = new float[6];
        values[0] = Float.parseFloat(ratingText.getText());
        values[1] = Float.parseFloat(caloriesText.getText());
        values[2] = Float.parseFloat(proteinsText.getText());
        values[3] = Float.parseFloat(fatsText.getText());
        values[4] = Float.parseFloat(sodiumText.getText());
        values[5] = Float.parseFloat(priceText.getText());
        return values;
    }
}
