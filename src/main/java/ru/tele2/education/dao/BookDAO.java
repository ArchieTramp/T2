package ru.tele2.education.dao;

import ru.tele2.education.pojo.BookPOJO;

public interface BookDAO {
    boolean createBook();
    boolean addBook(BookPOJO bookPOJO);
    boolean deleteBook();
    void dataExtraction();
    void dataChecking();
    void sumSale();
    void reportAvgTime();
}
