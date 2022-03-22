package bussiness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompositeProduct extends MenuItem implements Serializable {
    private String name;
    private List<MenuItem> products;

    public CompositeProduct(String name) {
        this.name = name;
        products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getProducts() {
        return products;
    }

    public void addComponent(MenuItem item) {
        products.add(item);
    }

    @Override
    public float computePrice() {
        float price = 0;
        for(MenuItem menuItem : products){
            price += menuItem.computePrice();
        }
        return price;
    }
}
