package bussiness;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Order implements Serializable {
    private int orderID;
    private int clientID;
    private Date orderDate;

    public int getOrderID() {
        return orderID;
    }
    public int getClientID() {
        return clientID;
    }
    public Date getOrderDate() {
        return orderDate;
    }

    public Order(int orderID, int clientID, Date orderDate) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.orderDate = orderDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientID, orderID, orderDate);
    }

    @Override
    public String toString() {
        return "Order no. " + orderID +
                ", client no." + clientID +
                ", date: " + orderDate +
                ".";
    }


}
