package com.group4.FKitShop.Repository;



import com.group4.FKitShop.Entity.OrderResultSet;
import com.group4.FKitShop.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findAllByaccountID(String strings);


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
}
