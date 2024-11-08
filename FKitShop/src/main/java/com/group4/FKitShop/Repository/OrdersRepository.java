package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.OrderResultSet;
import com.group4.FKitShop.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findAllByaccountID(String strings);


//    @Query(value = "select account.fullName, account.email, account.phoneNumber, product.name as productName,\n" +
//            "\t\t\t\torders.ordersID, product.price, product.discount ,orderDetails.quantity, \n" +
//            "\t\t\t\tproduct.price*(1-product.discount/100) as discountPrice,\n" +
//            "                product.price*(1-product.discount/100)* orderDetails.quantity as tmpPrice\n" +
//            "from Orders orders join OrderDetails orderDetails on orders.ordersID = orderDetails.ordersID\n" +
//            "\t\t\t\t   join Accounts account on account.accountID = orders.accountID\n" +
//            "                   join StemProduct product on product.productID = orderDetails.productID\n" +
//            "where orders.ordersID = :orderID", nativeQuery = true)
//    List<OrderResultSet> getOrdersInfo(@Param("orderID") String orderID);
//
//    // Lấy ra những order có status : Delivered
//    @Query(value = "select *\n" +
//            "from Orders \n" +
//            "where status = 'Delivered' and accountID = :accountID", nativeQuery = true)
//    List<Orders> findOrdersByAccountID(@Param("accountID") String accountID);
//
//    Optional<Orders> findBystatus(String status);

    @Query(value = "select account.fullName, account.email, account.phoneNumber, product.name as productName,\n" +
            "\t\t\t\torders.ordersID, product.price, product.discount ,orderDetails.quantity, \n" +
            "\t\t\t\tproduct.price*(1-product.discount/100) as discountPrice,\n" +
            "                product.price*(1-product.discount/100)* orderDetails.quantity as tmpPrice\n" +
            "from Orders orders join OrderDetails orderDetails on orders.ordersID = orderDetails.ordersID\n" +
            "\t\t\t\t   join Accounts account on account.accountID = orders.accountID\n" +
            "                   join StemProduct product on product.productID = orderDetails.productID\n" +
            "where orders.ordersID = :orderID", nativeQuery = true)
    List<OrderResultSet> getOrdersInfo(@Param("orderID") String orderID);

    // Lấy ra những order có status : Delivered
    @Query(value = "select *\n" +
            "from Orders \n" +
            "where status = 'Delivered' and accountID = :accountID", nativeQuery = true)
    List<Orders> findOrdersByAccountID(@Param("accountID") String accountID);

    Optional<Orders> findBystatus(String status);

    @Query(value = "SELECT DATE(o.orderDate) AS orderDate, SUM(od.price * od.quantity) AS dailyRevenue \n" +
            "           FROM Orders o JOIN OrderDetails od ON o.ordersID = od.ordersID\n" +
            "           WHERE MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) AND o.status = 'delivered'\n" +
            "           GROUP BY DATE(o.orderDate)", nativeQuery = true)
    List<Object[]> findDailyRevenueForCurrentMonth();

    @Query(value = "WITH months AS (\n" +
            "    SELECT \n" +
            "        DATE_FORMAT(DATE_ADD(CONCAT(:year, '-01-01'), INTERVAL (n - 1) MONTH), '%Y-%m') AS month_code,\n" +
            "        DATE_ADD(CONCAT(:year, '-01-01'), INTERVAL (n - 1) MONTH) AS month_start_date,\n" +
            "        LAST_DAY(DATE_ADD(CONCAT(:year, '-01-01'), INTERVAL (n - 1) MONTH)) AS month_end_date\n" +
            "    FROM \n" +
            "        (SELECT @row := @row + 1 AS n FROM \n" +
            "            (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 \n" +
            "             UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 \n" +
            "             UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS d1,\n" +
            "            (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 \n" +
            "             UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 \n" +
            "             UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS d2,\n" +
            "            (SELECT @row := 0) AS r\n" +
            "        ) AS numbers\n" +
            "    WHERE \n" +
            "        DATE_ADD(CONCAT(:year, '-01-01'), INTERVAL (n - 1) MONTH) <= CONCAT(:year, '-12-31')\n" +
            ")\n" +
            "SELECT \n" +
            "    months.month_code,\n" +
            "    COALESCE(SUM(Orders.totalPrice), 0) AS total_productPrice,\n" +
            "    COALESCE(SUM(Orders.shippingPrice), 0) AS total_ship,\n" +
            "    COALESCE(SUM(Orders.totalPrice) + SUM(Orders.shippingPrice), 0) AS total_revenue\n" +
            "FROM \n" +
            "    months\n" +
            "LEFT JOIN \n" +
            "    Orders ON Orders.orderDate BETWEEN months.month_start_date AND months.month_end_date\n" +
            "AND \n" +
            "    Orders.status != 'canceled'\n" +
            "GROUP BY \n" +
            "    months.month_code\n" +
            "ORDER BY \n" +
            "    months.month_code;", nativeQuery = true)
    List<Object[]> getRevenueByYear(@Param("year") String year);


    @Query(value = "  WITH all_dates AS (\n" +
            "                SELECT \n" +
            "                    DATE_ADD(DATE(CONCAT(:year, '-', :month, '-01')), INTERVAL daynum DAY) AS date\n" +
            "                FROM (\n" +
            "                    SELECT 0 AS daynum UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 \n" +
            "                    UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 \n" +
            "                    UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 \n" +
            "                    UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 \n" +
            "                    UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 \n" +
            "                    UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 \n" +
            "                    UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 \n" +
            "                    UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30 UNION ALL SELECT 31\n" +
            "                ) AS days\n" +
            "                WHERE DATE_ADD(DATE(CONCAT(:year, '-', :month, '-01')), INTERVAL daynum DAY) <= LAST_DAY(CONCAT(:year, '-', :month, '-01'))\n" +
            "            )\n" +
            "            SELECT \n" +
            "                DATE_FORMAT(all_dates.date, '%Y-%m-%d') AS day_code,\n" +
            "                IFNULL(SUM(o.totalPrice), 0) AS total_productPrice,\n" +
            "                IFNULL(SUM(o.shippingPrice), 0) AS total_ship,\n" +
            "                IFNULL(SUM(o.totalPrice), 0) + IFNULL(SUM(o.shippingPrice), 0) AS total_revenue\n" +
            "            FROM \n" +
            "                all_dates\n" +
            "            LEFT JOIN \n" +
            "                Orders o ON all_dates.date = o.orderDate\n" +
            "            AND \n" +
            "                o.status != 'canceled'\n" +
            "            GROUP BY \n" +
            "                day_code\n" +
            "            ORDER BY \n" +
            "                day_code", nativeQuery = true)
    List<Object[]> getDailyRevenueByMonth(@Param("year") String year, @Param("month") String month);

}
