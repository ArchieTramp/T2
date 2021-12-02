package ru.tele2.education.pojo;

import lombok.Data;

import java.sql.Date;

@Data
public class BookPOJO {
    private String region;
    private String country;
    private String itemType;
    private String salesChannel;
    private String orderPriority;
    private String orderDate;
    private int orderID;
    private String shipDate;
    private int unitsSold;
    private double unitPrice;
    private double unitCost;
    private double totalRevenue;
    private double totalCost;
    private double totalProfit;

}

