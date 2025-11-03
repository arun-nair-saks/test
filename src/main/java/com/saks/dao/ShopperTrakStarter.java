package com.saks.dao;

import com.saks.utils.Log4J;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class ShopperTrakStarter {
    private static final Logger logger = new Log4J(ShopperTrakStarter.class).getLogger();
    public static void main(String[] args) throws SQLException, IOException {
        FetchAndProcess.fetchAndProcess();

        //fetchAndProcess(directoryPath, fileName,s);

        logger.debug("File created successfully");

    }
}