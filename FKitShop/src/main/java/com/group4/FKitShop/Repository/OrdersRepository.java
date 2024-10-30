package com.group4.FKitShop.Repository;


import com.group4.FKitShop.Entity.OrderResultSet;
import com.group4.FKitShop.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query(value = "WITH months AS (\n" +
            "    SELECT \n" +
            "        DATE_FORMAT(DATE_ADD('2024-01-01', INTERVAL (n - 1) MONTH), '%Y-%m') AS month_code,\n" +
            "        DATE_ADD('2024-01-01', INTERVAL (n - 1) MONTH) AS month_start_date,\n" +
            "        LAST_DAY(DATE_ADD('2024-01-01', INTERVAL (n - 1) MONTH)) AS month_end_date\n" +
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
            "        DATE_ADD('2024-01-01', INTERVAL (n - 1) MONTH) <= '2024-12-31'\n" +
            ")\n" +
            "SELECT \n" +
            "    month_code,\n" +
            "    month_start_date,\n" +
            "    month_end_date\n" +
            "FROM \n" +
            "    months\n" +
            "ORDER BY \n" +
            "    month_start_date;", nativeQuery = true)
    List<Object> getMonth();

    @Query(value = "SELECT *\n" +
            "FROM Orders\n" +
            "WHERE orderDate BETWEEN :dateStart AND :dateEnd", nativeQuery = true)
    List<Orders> findOrdersByMonth(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd);

    @Query(value = "SELECT \n" +
            "    DATE_FORMAT(orderDate, '%Y-%m') AS month_code,\n" +
            "    SUM(totalPrice) AS total_productPrice,\n" +
            "    SUM(shippingPrice) AS total_ship,\n" +
            "    SUM(totalPrice)  + SUM(shippingPrice) AS total_revenue\n" +
            "FROM \n" +
            "    Orders\n" +
            "WHERE \n" +
            "    orderDate >= '2024-01-01' AND orderDate <= '2024-12-31'\n" +
            "GROUP BY \n" +
            "    month_code\n" +
            "ORDER BY \n" +
            "    month_code\n", nativeQuery = true)
    List<Object[]> getRevenue();
}
