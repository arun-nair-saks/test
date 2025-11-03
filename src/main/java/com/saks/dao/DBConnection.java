package com.saks.dao;

import com.saks.utils.Log4J;
import com.saks.utils.Constants;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private Connection con;
    private static final Logger logger = new Log4J(DBConnection.class).getLogger();

    // Constructor that initializes the DB connection using constants
    public DBConnection() throws SQLException {
        logger.debug("Attempting to connect to the database...");
        this.con = DriverManager.getConnection(Constants.url, Constants.user, Constants.pass);
        logger.debug("Database connection established.");
    }

    public DBConnection(String url, String user, String pass) throws SQLException {
        this.con = DriverManager.getConnection(url, user, pass);
        logger.debug("Database connection established.");
    }

    public Connection getConnection() {
        return con;
    }

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                logger.debug("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.error("Error closing the database connection: " + e.getMessage());
        }
    }
}
