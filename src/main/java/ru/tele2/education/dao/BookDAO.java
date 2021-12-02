package ru.tele2.education.dao;

import ru.tele2.education.pojo.BookPOJO;

public interface BookDAO {
    boolean createBook();
    boolean addIntoBook(BookPOJO bookPOJO);
    boolean insertCSV();
    boolean deleteBook(String parameter, String value);
    boolean dataExtraction();
    boolean dataChecking(BookPOJO bookPOJO);
    void sumSale(String parameter, String value);
    boolean reportAvgTime(String parameter, String value);
}
