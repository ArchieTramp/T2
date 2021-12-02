package ru.tele2.education;

import ru.tele2.education.dao.BookDAOImpl;
import ru.tele2.education.pojo.BookPOJO;

public class Book {
    public static void main(String[] args) {
        BookPOJO bookPOJO = new BookPOJO();
        bookPOJO.setRegion("Europe");
        bookPOJO.setCountry("Latvia");
        bookPOJO.setItemType("Vegetables");
        bookPOJO.setSalesChannel("Online");
        bookPOJO.setOrderPriority("C");
        bookPOJO.setOrderDate("5/11/2017");
        bookPOJO.setOrderID(564251221);
        bookPOJO.setShipDate("6/12/2017");
        bookPOJO.setUnitsSold(6618);
        bookPOJO.setUnitPrice(668.27);
        bookPOJO.setUnitCost(552.21);
        bookPOJO.setTotalRevenue(67287.78);
        bookPOJO.setTotalCost(42971.04);
        bookPOJO.setTotalProfit(24316.74);

        BookDAOImpl bookDAO = new BookDAOImpl();
        bookDAO.createBook();
        bookDAO.insertCSV();
        bookDAO.addIntoBook(bookPOJO);
        bookDAO.sumSale("region", "Europe");
        bookDAO.reportAvgTime("salesChanel", "Online");

    }
}
