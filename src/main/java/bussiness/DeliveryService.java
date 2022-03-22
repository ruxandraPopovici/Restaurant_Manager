package bussiness;


import data.BillGenerator;
import data.ReportGenerator;
import data.User;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


public class DeliveryService implements IDeliveryServiceProcessing, Serializable {

    private String name;
    private List<BaseProduct> initialProducts;
    private Map<String, MenuItem> menuItems;
    private Map<Order, List<MenuItem>> orderListMap;
    private List<Order> orders;
    private int orderID;
    private BillGenerator billGenerator;
    private int index;
    private List<User> users;

    public DeliveryService(String name) {
        this.name = name;
        this.orderID = 0;
        this.index = 0;
        this.initialProducts = new ArrayList<>();
        this.menuItems = new HashMap<>();
        this.orderListMap = new HashMap<>();
        this.orders = new ArrayList<>();
        this.users = new ArrayList<>();
        assert this.name != null;
    }

    @Override
    public void importProducts(String fileName) {
        Path pathToFile = Paths.get(fileName);

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("products.csv"),"utf-8"))){
            String line = bufferedReader.readLine();

            line = bufferedReader.readLine();
            while(line != null){
                String[] attributes = line.split(",");
                BaseProduct product = createBaseProduct(attributes);
                initialProducts.add(product);

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private BaseProduct createBaseProduct(String[] metadata){
        String name = metadata[0];
        float[] values = new float[6];
        for(int i = 0; i < 6; i++){
            values[i] = Float.parseFloat(metadata[i + 1]);
        }
        return new BaseProduct(name, values);
    }

    @Override
    public void addProduct(String name, float[] values) {
        assert name != null;
        for (float value : values) {
            assert value >= 0;
        }
        menuItems.put(name, new BaseProduct(name, values));
    }

    @Override
    public void addProduct(String name) {
        assert name != null;
        menuItems.put(name, new CompositeProduct(name));
    }

    public void createMenuItem(MenuItem item) {
        assert item != null;
        menuItems.put(item.getName(), item);
    }

    @Override
    public void addItemComposite(String compositeProductItem, String baseProductName) {
        assert compositeProductItem != null && baseProductName != null;

        MenuItem compositeProduct = menuItems.get(compositeProductItem);
        MenuItem baseSearch = initialProducts.stream().filter(baseProduct ->
                (baseProductName + " ").equals(baseProduct.getName()))
                .findAny()
                .orElse(null);
        //nu e baseProduct, e un alt composite product
        if(baseSearch == null){
            baseSearch = menuItems.get(baseProductName);
        }
        assert (compositeProduct != null && baseSearch != null);
        ((CompositeProduct)compositeProduct).addComponent(baseSearch);
    }

    @Override
    public void editMenuItem(String productName, String criteria, float numberValue) {
        assert productName != null && criteria != null && numberValue >= 0;

        MenuItem menuItem = menuItems.get(productName);
        assert menuItem instanceof BaseProduct;

        menuItems.remove(productName);

        switch(criteria){
            case "RATING":
                ((BaseProduct)menuItem).setRating(numberValue);
                break;
            case "CALORIES":
                ((BaseProduct)menuItem).setCalories(numberValue);
                break;
            case "PROTEINS":
                ((BaseProduct)menuItem).setProteins(numberValue);
                break;
            case "FATS":
                ((BaseProduct)menuItem).setFats(numberValue);
                break;
            case "SODIUM":
                ((BaseProduct)menuItem).setSodium(numberValue);
                break;
            default:
                ((BaseProduct)menuItem).setPrice(numberValue);

        }
        createMenuItem(menuItem);
    }

    @Override
    public void deleteMenuItem(String productName) {
        assert productName != null;
        MenuItem menuItem = menuItems.remove(productName);
        MenuItem currentMenuItem;

        Iterator<Map.Entry<String, MenuItem>> compositeIterator = menuItems.entrySet().iterator();
        while(compositeIterator.hasNext()){
            Map.Entry<String, MenuItem> currentEntry = compositeIterator.next();
            currentMenuItem = currentEntry.getValue();

            if(currentMenuItem instanceof CompositeProduct)
            {
                if(((CompositeProduct)currentMenuItem).getProducts().contains(menuItem)) {
                    compositeIterator.remove();
                }
            }
        }
    }

    @Override
    public void createOrder(int clientID, Date date){
        assert clientID > 0 && date != null;
        Order order = new Order(orderID, clientID, date);
        orders.add(order);
        orderListMap.put(order, new ArrayList<>());
    }

    @Override
    public void addOrderItem(int orderID, String productName) {
        assert orderID >= 0 && orderID <= orders.size();
        MenuItem itemForOrder = menuItems.get(productName);
        assert itemForOrder != null;

        for(Order order : orders){
            if(order.getOrderID() == orderID){
                orderListMap.get(order).add(itemForOrder);
                return;
            }
        }
    }

    @Override
    public float computeOrderPrice(int orderID) {
        assert orderID >= 0 && orderID < orders.size();

        float price = 0;
        for(Order order : orders){
            if(order.getOrderID() == orderID){
                for(MenuItem menuItem : orderListMap.get(order)){
                    price += menuItem.computePrice();
                }
                return price;
            }
        }
        return 0;
    }

    @Override
    public List<BaseProduct> getInitialProducts() {
        return initialProducts;
    }

    @Override
    public Map<String, MenuItem> getMenuItems() {
        return menuItems;
    }

    @Override
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public List<MenuItem> searchMenuItems(String criteria, String searchValue) {
        assert criteria != null && searchValue != null;
        float searchFloat = 0;

        if(criteria.compareTo("Name") != 0){
            searchFloat = Float.parseFloat(searchValue);
        }

        MenuItem currentMenuItem;
        List<MenuItem> returnMenuItems = new ArrayList<>();

        Iterator<Map.Entry<String, MenuItem>> iterator = menuItems.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, MenuItem> currentEntry = iterator.next();
            currentMenuItem = currentEntry.getValue();

            switch (criteria){
                case "Name" :
                    if(currentMenuItem.getName().compareTo(searchValue) == 0){
                        returnMenuItems.add(currentMenuItem);
                    }
                    break;
                case "Rating" :
                    if((currentMenuItem instanceof BaseProduct) && ((BaseProduct) currentMenuItem).getRating() == searchFloat){
                        returnMenuItems.add(currentMenuItem);
                    }
                    break;
                case "Calories" :
                    if((currentMenuItem instanceof BaseProduct) && ((BaseProduct) currentMenuItem).getCalories() == searchFloat){
                        returnMenuItems.add(currentMenuItem);
                    }
                    break;
                case "Proteins" :
                    if((currentMenuItem instanceof BaseProduct) && ((BaseProduct) currentMenuItem).getProteins() == searchFloat){
                        returnMenuItems.add(currentMenuItem);
                    }
                    break;
                case "Fats" :
                    if((currentMenuItem instanceof BaseProduct) && ((BaseProduct) currentMenuItem).getFats() == searchFloat){
                        returnMenuItems.add(currentMenuItem);
                    }
                    break;
                case "Sodium" :
                    if((currentMenuItem instanceof BaseProduct) && ((BaseProduct) currentMenuItem).getSodium() == searchFloat){
                        returnMenuItems.add(currentMenuItem);
                    }
                    break;
                default:
                    if(currentMenuItem.computePrice() == searchFloat){
                        returnMenuItems.add(currentMenuItem);
                    }
            }
        }
        return returnMenuItems;
    }

    public List<MenuItem> getOrderByID(int orderID){
        assert orderID >= 0 && orderID < orders.size();

        for(Order order : orders){
            if(order.getOrderID() == orderID){
                return orderListMap.get(order);
            }
        }
        return null;
    }

    @Override
    public void generateBill(int orderID) throws IOException {
        this.billGenerator = new BillGenerator(orderID);
        Order order = orders.get(orderID);
        List<MenuItem> currentOrder= getOrderByID(orderID);
        billGenerator.generateBill(order, currentOrder, computeOrderPrice(orderID));
    }
    @Override
    public int getCurrentOrderID() {
        return orderID;
    }
    @Override
    public void setCurrentOrderID(int orderID) {
        this.orderID = orderID;
    }

    @Override
    public void intervalReport(int min, int max) {
        assert min < max;
        Calendar calendar = GregorianCalendar.getInstance();

        List <Order> returnOrders = new ArrayList<>();

        for (Order o : orders){
            calendar.setTime(o.getOrderDate());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(hour >= min && hour <= max){
                returnOrders.add(o);
            }
        }

        ReportGenerator report = new ReportGenerator("intervalReport" + index);
        String textToAdd = "Orders performed between " + min + " and " + max + ":\n";

        for(Order o : returnOrders){
            textToAdd += "Order with ID " + o.getOrderID() + ", ordered at: " + o.getOrderDate() +".\n";
        }
        report.write(textToAdd);
        index++;
    }
    @Override
    public void noOrderReport(int no) {
        assert no > 0;

        List <MenuItem> returnProducts = new ArrayList<>();
        List <MenuItem> processedProducts = new ArrayList<>();
        int [] noTimesOrdered = new int[initialProducts.size()];
        List <MenuItem> currentOrder;

        for (Map.Entry<Order, List<MenuItem>> currentEntry : orderListMap.entrySet()) {
            currentOrder = currentEntry.getValue();

            for (MenuItem m : currentOrder) {
                if (processedProducts.contains(m)) {
                    noTimesOrdered[processedProducts.indexOf(m)]++;

                } else {
                    processedProducts.add(m);
                    noTimesOrdered[processedProducts.indexOf(m)] = 1;
                }
            }
        }
        for(int i = 0; i < noTimesOrdered.length; i++){
            if(noTimesOrdered[i] >= no){
                returnProducts.add(processedProducts.get(i));
            }
        }
        ReportGenerator report = new ReportGenerator("noOrdersReport" + index);
        String textToAdd = "";

        for(MenuItem m : returnProducts){
            textToAdd += "Product " + m.getName() + " has been ordered " + noTimesOrdered[processedProducts.indexOf(m)]
                    + " times.\n";

        }
        report.write(textToAdd);
        index++;
    }
    @Override
    public void valueReport(int no, float value) {
        assert no > 0 && value >= 0;

        int [] noOrders = new int [users.size() + 1];
        List <Order> returnOrders = new ArrayList<>();

        for(Order o : orders){
            noOrders[o.getClientID()]++;
        }
        for(int i = 0; i < noOrders.length; i++){

            if(noOrders[i] >=  no){

                for(Order o : orders){
                    if(i == o.getClientID()){
                        int orderID = o.getOrderID();
                        List<MenuItem> currentOrder  = getOrderByID(orderID);

                        float price = 0;

                        for(MenuItem m : currentOrder){
                            price += m.computePrice();
                        }
                        if(price >= value){
                            returnOrders.add(o);
                            break;
                        }
                    }
                }
            }
        }
        ReportGenerator report = new ReportGenerator("valueReport" + index);
        String textToAdd = "";

        for(Order o : returnOrders){
            textToAdd += "Client with ID " + o.getClientID() + " has ordered " + noOrders[o.getClientID()]
                        + " times.\n";

        }
        report.write(textToAdd);
        index++;
    }
    @Override
    public void dayReport(int day) {
        assert day >= 1 && day <= 7;

        ReportGenerator report = new ReportGenerator("dayReport" + index);

        List <MenuItem> processedProducts = new ArrayList<>();
        int [] noTimesOrdered = new int[orderListMap.size()];

        for(Order order : orders){
            Calendar c = Calendar.getInstance();
            c.setTime(order.getOrderDate());
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            if(dayOfWeek == day){
                List<MenuItem> currentOrder = getOrderByID(order.getOrderID());
                for(MenuItem m : currentOrder){
                    if(processedProducts.contains(m)){
                        noTimesOrdered[processedProducts.indexOf(m)]++;
                    }
                    else{
                        processedProducts.add(m);
                        noTimesOrdered[processedProducts.indexOf(m)] = 1;
                    }
                }
            }
        }

        String textToAdd = "";

        for(int i = 0; i < noTimesOrdered.length; i++){
            if(noTimesOrdered[i] != 0){
                textToAdd += "Product is: " + processedProducts.get(i).getName() + ", ordered "
                        + noTimesOrdered[i] +" times.\n";
            }
        }
        report.write(textToAdd);
        index++;
    }

}
