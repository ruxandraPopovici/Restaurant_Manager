package bussiness;

import java.io.Serializable;

public abstract class MenuItem implements Serializable {
    public abstract float computePrice();

    public abstract String getName();
    public abstract void setName(String name);
}
