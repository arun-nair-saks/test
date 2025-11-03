package com.saks.utils;

public class Constants {
    public static final String url = "jdbc:sqlserver://listener.scs-salesaudit-prod.c1q40wi0oz23.us-east-1.rds.amazonaws.com;databaseName=salesauditdb;encrypt=true;trustServerCertificate=true";
    public static final String user = "Anair";
    public static final String pass = "Welcome@123";
    public static final String SP_QUERY = "SELECT TRAN_Date, TRAN_STORE, TRAN_TERM, TRAN_NUM, TRAN_TIME, " +
            "       SUM(total_price)    AS Total_price, " +
            "       SUM(sale_present)   AS sale_present " +
            "  FROM ( " +
            "         SELECT " +
            "           FORMAT(d.TRAN_Date, 'yyyyMMdd') AS TRAN_Date, " +
            "           d.TRAN_STORE, " +
            "           d.TRAN_TERM, " +
            "           d.TRAN_NUM, " +
            "           h.TRAN_time, " +
            "           SUM(d.PRICE) as total_price, " +
            "           (case when (d.TRAN_CATEGORY ='SL') or (d.price =0 and d.TRAN_CATEGORY not in('SPA')) \n" +
            " or (d.TRAN_CATEGORY ='SPA' and SUBSTRING(d.DEPT_CLASS,1,1)=9 and d.PRICE>=0) then 1 else 0 END) as sale_present" +
            "         FROM salesauditdb.saks.SA_SALES_HEADER h " +
            "         JOIN salesauditdb.saks.SA_SALES_DETAIL d " +
            "           ON h.TRAN_Date        = d.TRAN_Date " +   // fixed join
            "          AND h.TRAN_STORE       = d.TRAN_STORE " +
            "          AND h.TRAN_TERM        = d.TRAN_TERM " +
            "          AND h.TRAN_NUM         = d.TRAN_NUM " +
            "          AND h.APTOS_ENTRY_NO   = d.APTOS_ENTRY_NO " +
            "        WHERE h.VOID_FLAG    <> 9 " +
            "          AND h.TRANS_TYPE   <> 'POA' " +
            "          AND d.UNIQUE_QUALIFIER NOT IN ('REV','MOD') " +
            "          AND d.SPECIAL_SERVICE_IND is NULL" +
            "          AND SUBSTRING(d.DEPT_CLASS,2,3) <> '961' " +
            "          AND CAST(d.EXTRACT_SA_MNF_DATE AS DATE) = CAST(GETDATE()-2 AS DATE) " +
            "          AND h.status = 'sent' " +
            "                  GROUP BY d.TRAN_Date, d.TRAN_STORE, d.TRAN_TERM, d.TRAN_NUM, " +
            "                 h.TRAN_TIME, d.TRAN_CATEGORY, d.PRICE,d.dept_class " +
            "       ) sub " +
            " GROUP BY TRAN_Date, TRAN_STORE, TRAN_TERM, TRAN_NUM, TRAN_TIME " +
            " ORDER BY TRAN_Date, TRAN_STORE, TRAN_TERM, TRAN_NUM;";


}
