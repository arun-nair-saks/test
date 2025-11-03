package com.saks.dao;

import com.saks.utils.Constants;

import java.sql.*;

public class FetchAndProcess {

    public static void fetchAndProcess() {
        DBConnection dbConnection = null;

        try {
            // Open DB connection
            dbConnection = new DBConnection();
            Connection conn = dbConnection.getConnection();
            String query = Constants.SP_QUERY;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Initialize file once before writing lines
            ShopperTrakFileGeneration.initializeFile();

            while (rs.next()) {
                // Fetch data from ResultSet
                String strTranDate = rs.getString("TRAN_Date");
                int lngTranStore = rs.getInt("TRAN_STORE");  // as per transformRecord signature
                int lngTranTerm = rs.getInt("TRAN_TERM");
                int lngTranNum = rs.getInt("TRAN_NUM");
                long lngTranTime = rs.getLong("TRAN_time");
                double dbOpSalesRetail = rs.getDouble("Total_price");
                String strSale_present = rs.getString("sale_present"); // not used in transformRecord, ignore here

                // Transform into OpRecord
                ShopperTrakFileGeneration.OpRecord record = ShopperTrakFileGeneration.transformRecord(
                        strTranDate, lngTranStore, lngTranTerm, lngTranNum, lngTranTime, dbOpSalesRetail,strSale_present);

                // Write the record directly
                if (record != null) {
                    ShopperTrakFileGeneration.writeRecord(record);
                }
            }

            // Close resources
            rs.close();
            stmt.close();

            // Close the file writer
            ShopperTrakFileGeneration.closeFile();

        } catch (Exception e) {
            e.printStackTrace(); // ideally use logging framework
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }
    }
}
