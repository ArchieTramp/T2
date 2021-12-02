package ru.tele2.education.connection;

import java.sql.Connection;

public interface ConnectionManager {
    Connection getConnection();
}
