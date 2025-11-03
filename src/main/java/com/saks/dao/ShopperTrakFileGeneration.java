package com.saks.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ShopperTrakFileGeneration {

    private static BufferedWriter writer;
    private static String filePath;
    private static OpRecord currentRecord = null;

    // Simplified OpRecord with only the six fields you want to write
    public static class OpRecord {
        public String strOpSalesDate;
        public String lngOpSalesTime;
        public int lngOpStore;
        public int lngOpRegister;
        public int lngOpTransNo;
        public String dbOpSalesPrice;  // Assuming this corresponds to 'dbOpSalesRetail' or similar
    }

    // Initialize file and open writer once before writing lines
    public static void initializeFile() throws IOException {
        String directoryPath = "C:\\FileDump";
        Path dirPath = Paths.get(directoryPath);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDateTime = now.format(formatter);
        String fileName = "SALES_Saks" + formattedDateTime + ".txt";
        filePath = Paths.get(directoryPath, fileName).toString();

        writer = new BufferedWriter(new FileWriter(filePath, false)); // overwrite mode
    }

    // Write a single line to the file
    public static void writeLine(String line) throws IOException {
        if (writer == null) {
            throw new IOException("Writer not initialized. Call initializeFile() first.");
        }
        writer.write(line);
        writer.newLine();
        writer.flush();
    }

    // Write the OpRecord as a single CSV line without labels
    public static void writeRecord(OpRecord record) throws IOException {
        if (writer == null) {
            throw new IOException("Writer not initialized. Call initializeFile() first.");
        }
        if (record.strOpSalesDate != null) {
            StringBuilder line = new StringBuilder();
            line.append(String.format("%04d", record.lngOpStore)).append(",");
            line.append(record.strOpSalesDate).append(",");
            line.append(record.lngOpSalesTime).append(",");
            line.append(record.dbOpSalesPrice).append(",");
            line.append(String.format("%03d", record.lngOpRegister));
            line.append(String.format("%05d", record.lngOpTransNo)).append(",,,");
            line.append("USD");

            writeLine(line.toString());
        }
    }

    // Close the writer when done
    public static void closeFile() throws IOException {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

    public static String getFilePath() {
        return filePath;
    }

    // Transform method that creates a new OpRecord based on the inputs you want to keep
    public static OpRecord transformRecord(String strTranDate, int intTranStore, int intTranTerm, int intTranNum, long lngTranTime, double dbOpSalesRetail, String strSale_present) {
        OpRecord record = new OpRecord();
        if (!"0".equals(strSale_present)) {
            record.lngOpStore = intTranStore;
            record.strOpSalesDate = strTranDate;

            // Format lngTranTime (e.g. 1244 means 00:12:44)
            String padded = String.format("%06d", lngTranTime);
            int hours = Integer.parseInt(padded.substring(2, 4));
            int minutes = Integer.parseInt(padded.substring(4, 6));
            /*int seconds = Integer.parseInt(padded.substring(4, 6));*/
            record.lngOpSalesTime = String.format("%02d%02d00", hours, minutes);
            record.lngOpRegister = intTranTerm;
            record.lngOpTransNo = intTranNum;
            DecimalFormat df = new DecimalFormat("0000000.00");
            String formatted;
            if (dbOpSalesRetail < 0) {
                formatted = "-" + df.format(Math.abs(dbOpSalesRetail));
            } else {
                formatted = "+" + df.format(Math.abs(dbOpSalesRetail));
            }
            record.dbOpSalesPrice = formatted;
        }
        return record;
    }
}