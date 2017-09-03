package me.paraweb.orderbuilder.Orders;

import java.io.Serializable;

public class OrderComponents implements Serializable {

    private String componentName;
    private int hourse;

    public OrderComponents(String componentName, int hourse){
        this.componentName = componentName;
        this.hourse = hourse;
    }

    public String getComponentName() {
        return componentName;
    }

    public int getHourse() {
        return hourse;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void setHourse(int hourse) {
        this.hourse = hourse;
    }
}