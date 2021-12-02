package ru.tele2.education.dao;

import ru.tele2.education.connection.ConnectionManager;
import ru.tele2.education.pojo.BookPOJO;

import javax.ejb.EJB;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@EJB
public class BookDAOImpl implements BookDAO {

    private ConnectionManager connectionManager;
    boolean state = false;
    int count;

    public static final String CREATE_TABLE
            = "DROP TABLE IF EXISTS salebook;\n"
            + "CREATE TABLE salebook\n"
            + "SET DATEFORMAT mdy\n"
            + "(\n"
            + "     id bigserial not null\n"
            + "         constraint salebook_pkey\n"
            + "             primary key, \n"
            + "     region varchar(100) not null, \n"
            + "     country varchar(100) not null, \n"
            + "     itemType varchar(100) not null, \n"
            + "     salesChannel varchar(100) not null, \n"
            + "     orderPriority varchar(100) not null, \n"
            + "     orderDate date not null, \n"
            + "     orderId long not null, \n"
            + "     shipDate date not null, \n"
            + "     unitsSold integer not null, \n"
            + "     unitPrice double not null, \n"
            + "     unitCost double not null, \n"
            + "     totalRevenue double not null, \n"
            + "     totalCost double not null, \n"
            + "     totalProfit double not null, \n"
            + ");\n";

    public static final String INSERT_DATA = "INSERT INTO salebook (region, country, itemType, salesChannel, " +
            "orderPriority, orderDate, orderId, shipDate, unitsSold, unitPrice, unitCost, totalRevenue, totalCost, totalProfit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String DELETE_FROM_TABLE = "DELETE FROM salebook WHERE ?=?";

    public static final String SELECT_DATA = "CREATE TABLE handbook\n" + "SELECT region, country, itemType, salesChannel, orderPriority INTO handbook " +
            "FROM salebook";
    public static final String DATA_CHECK = "SELECT ? FROM handbook WHERE ? = ?";
    public static final String SUM = "SELECT totalProfit INTO summa FROM salebook WHERE ?=?\n"
            + "SELECT SUM(totalProfit) FROM summa";
    public static final String REPORT_AVG = "CREATE VIEW report AS \n"
            + "SELECT orderDate, shipDate INTO tempbook FROM ?=?\n"
            + "INSERT INTO tempbook (dateDiff) VALUE (EXTRACT(DAY FROM TO_TIMESTAMP(shipDate)-TO_TIMESTAMP(orderDate)) AS different"
            + "SELECT AVG(different) FROM report";

    @Override
    public boolean createBook() {

        Connection connection = connectionManager.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE);
            preparedStatement.execute();
            state = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(state);
        return state;
    }

    @Override
    public boolean addIntoBook(BookPOJO bookPOJO) {
        boolean permit = dataChecking(bookPOJO);
        if (permit) {
            Connection connection = connectionManager.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DATA);
                preparedStatement.setString(1, bookPOJO.getRegion());
                preparedStatement.setString(2, bookPOJO.getCountry());
                preparedStatement.setString(3, bookPOJO.getItemType());
                preparedStatement.setString(4, bookPOJO.getSalesChannel());
                preparedStatement.setString(5, bookPOJO.getOrderPriority());
                preparedStatement.setDate(6, bookPOJO.getOrderDate());
                preparedStatement.setLong(7, bookPOJO.getOrderID());
                preparedStatement.setDate(8, bookPOJO.getShipDate());
                preparedStatement.setInt(9, bookPOJO.getUnitsSold());
                preparedStatement.setDouble(10, bookPOJO.getUnitPrice());
                preparedStatement.setDouble(11, bookPOJO.getUnitCost());
                preparedStatement.setDouble(12, bookPOJO.getTotalRevenue());
                preparedStatement.setDouble(13, bookPOJO.getTotalCost());
                preparedStatement.setDouble(14, bookPOJO.getTotalProfit());
                count = preparedStatement.executeUpdate();
                state = true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Operation state = " + state + ". Added " + count + " row(s).");
        }
        return state;
    }

    @Override
    public boolean insertCSV() {
        Connection connection = connectionManager.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DATA);
            String path = "10000 Sales Records.csv";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;

            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                String region = data[0];
                String country = data[1];
                String itemType = data[2];
                String salesChannel = data[3];
                String orderPriority = data[4];
                String orderDate = data[5];
                String orderId = data[6];
                String shipDate = data[7];
                String unitsSold = data[8];
                String unitPrice = data[9];
                String unitCost = data[10];
                String totalRevenue = data[11];
                String totalCost = data[12];
                String totalProfit = data[13];

                preparedStatement.setString(1, region);
                preparedStatement.setString(2, country);
                preparedStatement.setString(3, itemType);
                preparedStatement.setString(4, salesChannel);
                preparedStatement.setString(5, orderPriority);
                preparedStatement.setDate(6, Date.valueOf(orderDate));

                long orderIdLong = Long.parseLong(orderId);
                preparedStatement.setLong(7, orderIdLong);

                preparedStatement.setDate(8, Date.valueOf(shipDate));

                int unitsSoldInt = Integer.parseInt(unitsSold);
                preparedStatement.setInt(9, unitsSoldInt);

                double unitPriceDouble = Double.parseDouble(unitPrice);
                preparedStatement.setDouble(10, unitPriceDouble);
                double unitCostDouble = Double.parseDouble(unitCost);
                preparedStatement.setDouble(11, unitCostDouble);
                double totalRevenueDouble = Double.parseDouble(totalRevenue);
                preparedStatement.setDouble(12, totalRevenueDouble);
                double totalCostDouble = Double.parseDouble(totalCost);
                preparedStatement.setDouble(13, totalCostDouble);
                double totalProfitDouble = Double.parseDouble(totalProfit);
                preparedStatement.setDouble(14, totalProfitDouble);

                preparedStatement.addBatch();
                preparedStatement.executeBatch();
            }

            bufferedReader.close();
            preparedStatement.executeBatch();
            connection.commit();
            connection.close();
            state = true;


        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Operation state = " + state);
        return state;
    }

    @Override
    public boolean deleteBook(String parameter, String value) {
        Connection connection = connectionManager.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_TABLE);
            preparedStatement.setString(1, parameter);
            preparedStatement.setString(2, value);
            count = preparedStatement.executeUpdate();
            state = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Operation state = " + state + ". Deleted " + count + " row(s).");
        return state;
    }

    @Override
    public boolean dataExtraction() {
        Connection connection = connectionManager.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DATA);
            count = preparedStatement.executeUpdate();
            state = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Operation state = " + state + ". Copy " + count + " row(s).");
        return state;
    }

    @Override
    public boolean dataChecking(BookPOJO bookPOJO) {
        dataExtraction();
        Connection connection = connectionManager.getConnection();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("region", bookPOJO.getRegion());
        paramMap.put("country", bookPOJO.getCountry());
        paramMap.put("itemType", bookPOJO.getItemType());
        paramMap.put("salesChannel", bookPOJO.getSalesChannel());
        paramMap.put("orderPriority", bookPOJO.getOrderPriority());
        int calculate = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DATA_CHECK);
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                preparedStatement.setString(1, entry.getKey());
                preparedStatement.setString(2, entry.getKey());
                preparedStatement.setString(3, entry.getValue());
                count = preparedStatement.executeUpdate();
                if (count > 0) {
                    calculate++;
                }
                else {
                    System.out.println("Don't find element " + entry.getKey() + " equals " + entry.getValue() + " in catalog");
                    break;
                }
            }
            if (calculate > 5) {state = true;}

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return state;
    }

    @Override
    public void sumSale(String parameter, String value) {
        Connection connection = connectionManager.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SUM);
            switch (parameter) {
                case ("region"), ("country"), ("itemType") -> {
                    preparedStatement.setString(1, parameter);
                    preparedStatement.setString(2, value);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        double tp = resultSet.getDouble(1);
                        System.out.println("Total profit with " + parameter + " = " + tp);
                    }
                }
                default -> System.out.println("Don't find parameter");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public boolean reportAvgTime(String parameter, String value) {
        Connection connection = connectionManager.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REPORT_AVG);
            switch (parameter) {
                case ("salesChannel"), ("orderPriority") -> {
                    preparedStatement.setString(1, parameter);
                    preparedStatement.setString(2, value);
                    preparedStatement.execute();
                    state = true;
                }
                default -> System.out.println("Don't find parameter");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return state;
    }
}
