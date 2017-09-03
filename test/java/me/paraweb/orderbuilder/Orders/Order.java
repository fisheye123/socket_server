package me.paraweb.orderbuilder.Orders;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {

    public String projectName; //Что делаем
    public String client; //Кому делаем

    //Списки того, что надо(та таблица короче говоря)
    public  ArrayList<OrderComponents> design;
    public  ArrayList<OrderComponents> programming;
    public  ArrayList<OrderComponents> pageProofs;

    //Параметры заказа
    private int designRate;
    private int programmingRate;
    private int pageProofsRate;

    private int totalPrice;
    private int designPrice;
    private int programmingPrice;
    private int pageProofsPrice;

    private int executionTime;
    private int designTime;
    private int programmingTime;
    private int pageProofsTime;

    private double discount;

    public Order(String projectName, String client) {
        this.projectName = projectName;
        this.client = client;

        this.designRate = 1800;
        this.programmingRate = 1200;
        this.pageProofsRate = 900;

        this.totalPrice = 0;
        this.designPrice = 0;
        this.programmingPrice = 0;
        this.pageProofsPrice = 0;
        this.executionTime = 0;
        this.programmingTime = 0;
        this.designTime = 0;
        this.pageProofsTime = 0;
        this.discount = 0;
        this.design = new ArrayList<OrderComponents>();
        this.pageProofs = new ArrayList<OrderComponents>();
        this.programming = new ArrayList<OrderComponents>();
    }

    //region Get

    public int getDesignRate() {
        return designRate;
    }

    public int getProgrammingRate() {
        return programmingRate;
    }

    public int getPageProofsRate() {
        return pageProofsRate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getProgrammingPrice() {
        return programmingPrice;
    }

    public int getDesignPrice() {
        return designPrice;
    }

    public int getPageProofsPrice() {
        return pageProofsPrice;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public String getClient() { return client; }

    public String getProjectName() {return projectName;}

    public double getDiscount() {
        return discount;
    }

    //endregion

    //region Set

    public void setDesignRate(int designRate) {
        this.designRate = designRate;
    }

    public void setProgrammingRate(int programmingRate) {
        this.programmingRate = programmingRate;
    }

    public void setPageProofsRate(int pageProofsRate) {
        this.pageProofsRate = pageProofsRate;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setClient(String client) {this.client = client;}

    public void setProjectName(String projectName) {this.projectName = projectName;}
//endregion

    public void AddComponent (String storage, OrderComponents componet) {

        switch (storage) {
            case "Дизайн":
                this.design.add(componet);
                break;
            case "Программирование":
                this.programming.add(componet);
                break;
            case "Вёрстка":
                this.pageProofs.add(componet);
                break;
        }
        TotalCalc();
    }

    //region Calculate


    private void TotalCalc() {
        this.executionTime = this.designTime + this.programmingTime + this.pageProofsTime;
        this.totalPrice = this.programmingPrice + this.designPrice + this.pageProofsPrice;
    }

    public void setTotalPrise(String storage, int sum) {
        switch (storage) {
            case "Дизайн":
                this.designPrice = sum;
                break;
            case "Вёрстка":
                this.pageProofsPrice = sum;
                break;
            case "Программирование":
                this.programmingPrice = sum;
            default:
                break;
        }
        TotalCalc();

    }

    //endregion

    public void Debug(){

        System.out.println("Название проекта " + projectName);
        System.out.println("Имя клиента " + client);
        System.out.println("Рейт дизайна " + designRate);
        System.out.println("Рейт программирования " + programmingRate);
        System.out.println("Рейт верстки " + pageProofsRate);
        System.out.println("Общая стоимость " + totalPrice);
        System.out.println("Стоимость дизайна" + designPrice);
        System.out.println("Стоимость программирования" + programmingPrice);
        System.out.println("Стоимость верстки" + pageProofsPrice);
        System.out.println("Время выполнения(общее)" + executionTime);
        System.out.println("Часов на дизайн" + designTime);
        System.out.println("Часов на программиролвание" + programmingTime);
        System.out.println("Чаасов на верстку" + pageProofsTime);

        for (OrderComponents orderComponents : design) {
            System.out.println(orderComponents.getComponentName() + " " + orderComponents.getHourse());
        }

        for (OrderComponents orderComponents : programming) {
            System.out.println(orderComponents.getComponentName() + " " + orderComponents.getHourse());
        }

        for (OrderComponents orderComponents : pageProofs) {
            System.out.println(orderComponents.getComponentName() + " " + orderComponents.getHourse());
        }


    }
}