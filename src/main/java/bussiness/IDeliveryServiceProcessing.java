package bussiness;

import data.User;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IDeliveryServiceProcessing {

    /**
     * Imports the initial BaseProducts from the cvs file
     * @pre none
     * @post none
     * @param fileName name of the file from which we will import the products
     */
    void importProducts(String fileName);

    //pentru BaseProduct
    /**
     * Creates and adds a new MenuItem, instance of BaseProduct (as the method takes a String and an array of floats
     * as parameters)
     * @param name name of the new MenuItem to be created and inserted
     * @param values array of Floats representing the corresponding BaseProduct attributes
     * @pre name != null, values >= 0
     */
    void addProduct(String name, float[]values);

    //pentru CompositeProduct
    /**
     * Creates and adds a new MenuItem, instance of CompositeProduct (as the method takes only a String)
     * @param name name of the new MenuItem to be created and inserted
     * @pre name != null
     */
    void addProduct(String name);

    /**
     * Adds a new MenuItem into the composition of an existing CompositeProduct
     * @param compositeProductItem name of the existing CompositeProduct into which the new component will be added
     * @param baseProductName name of the existing MenuItem (Base or Composite) to be added
     * @pre compositeProductItem != null, baseProductName != null
     * @post compositeProduct != null && baseSearch != null
     */
    void addItemComposite(String compositeProductItem, String baseProductName);

    /**
     * Edits the provided field of a BaseProduct with the given value.
     * @param productName name of the MenuItem to be edited
     * @param criteria name of the BaseProduct's field to be edited
     * @param numberValue new value to be assigned to the provided field
     * @pre productName != null, criteria != null, numberValue >= 0
     * @pre menuItem instanceof BaseProduct
     */
    void editMenuItem(String productName, String criteria, float numberValue);

    /**
     * Deletes the MenuItem of given name from the list and the CompositeProducts that contain it.
     * @param name name of the MenuItem to be deleted
     * @pre productName != null
     */
    void deleteMenuItem(String name);

    /**
     * Creates a new Order for the given clientID and adds it to the list. A new Map Entry will also be created for
     * this new Order.
     * @param clientID ID of client for whom we want to create the new Order
     * @param date the date of order's placement
     * @pre assert clientID > 0, date != null
     */
    void createOrder(int clientID, Date date);

    /**
     * Adds a MenuItem to the list of MenuItems provided by the orderID.
     * @param orderID the ID of the Order to which we want to add the new Product
     * @param productName the name of the Product to be searched for and then added to the Order's list
     * @pre orderID >= 0, orderID <= orders.size()
     * @pre itemForOrder != null
     */
    void addOrderItem(int orderID, String productName);

    /**
     * Calculates the price of the Order provided by its given ID.
     * @param orderID the ID of the Order for which we want to compute the price
     * @return the value of the price that was generated as the sum of all MenuItems provided by the orderID
     * @pre orderID >= 0, orderID < orders.size()
     */
    float computeOrderPrice(int orderID);

    List<BaseProduct> getInitialProducts();
    Map<String, MenuItem> getMenuItems();
    List<Order> getOrders();
    List<User> getUsers();

    /**
     * Returns a list with the MenuItems that respect the given parameters
     * @param criteria the name of the field to be tested for equality
     * @param searchValue the value with which the fields of the MenuItems will be tested
     * @return the list of MenuItems that respect the equality between the filed to be tested and the provided value.
     * @pre criteria != null, searchValue != null
     */
    List<MenuItem> searchMenuItems(String criteria, String searchValue);

    /**
     * Generates the Bill of the newly placed Order that is provided by the orderID
     * @param orderID ID of order for which we want the bill to be generated
     * @throws IOException if the File could not be created / something else went wrong
     */
    void generateBill(int orderID) throws IOException;

    int getCurrentOrderID();
    void setCurrentOrderID(int orderID);

    /**
     * Generates report with the orders performed between a given start hour and a given end hour regardless the date.
     * @param min represents the start hour
     * @param max represents the max hour
     * @pre min < max
     */
    void intervalReport(int min, int max);

    /**
     * Generates report with the products ordered more than a specified number of times so far.
     * @param no number of times the products have been ordered
     * @pre no > 0
     */
    void noOrderReport(int no);

    /**
     * Generates report with the clients that have ordered more than a specified number of times and the value
     * of the order was higher than a specified amount.
     * @param no number of times the clients have ordered
     * @param value value of max order
     * @pre no > 0, value >= 0
     */
    void valueReport(int no, float value);

    /**
     * Generates report with the products ordered within a specified day with the number of times they have been ordered.
     * @param day day when the products have been ordered, given as an Integer number representing the number
     *            corresponding to the day of the week.
     * @pre day >= 1 && day <= 7
     */
    void dayReport(int day);

}
