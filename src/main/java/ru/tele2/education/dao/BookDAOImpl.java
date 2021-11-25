package ru.tele2.education.dao;

import ru.tele2.education.pojo.BookPOJO;

public class BookDAOImpl implements BookDAO {

    public static final String CREATE_TABLE
            = "DROP TABLE IF EXISTS salebook;\n"
            + "create table consoles\n"
            + "(\n"
            + "    id bigserial not null\n"
            + "        constraint consoles_pkey\n"
            + "            primary key,\n"
            + "    name varchar(100) not null, \n"
            + "    model varchar(100) not null,\n"
            + "    price integer not null,\n"
            + "    includeGames integer not null,\n"
            + "    company varchar(100) not null\n"
            + ");\n";


    @Override
    public boolean createBook() {
        return false;
    }

    @Override
    public boolean addBook(BookPOJO bookPOJO) {
        return false;
    }

    @Override
    public boolean deleteBook() {
        return false;
    }

    @Override
    public void dataExtraction() {

    }

    @Override
    public void dataChecking() {

    }

    @Override
    public void sumSale() {

    }

    @Override
    public void reportAvgTime() {

    }
}
