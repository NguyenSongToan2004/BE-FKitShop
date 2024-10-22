package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.*;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.OrdersMapper;
import com.group4.FKitShop.Repository.*;
import com.group4.FKitShop.Request.CheckoutRequest;
import com.group4.FKitShop.Request.OrdersRequest;
import com.group4.FKitShop.Response.CheckoutResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrdersService {

    OrdersRepository ordersRepository;
    OrdersMapper ordersMapper;
    JavaMailSender mailSender;
    OrderDetailsRepository orderDetailsRepository;
    OrderStatusService orderStatusService;
    OrderDetailsService orderDetailsService;
    ProductRepository productRepository;
    CartRepository cartRepository;
    AccountsRepository accountsRepository;

    public CheckoutResponse checkOut(CheckoutRequest request){
        try {
            //create order
            //ensure order save and immediately flushes the changes to the database
            Orders orders = createOrder(request.getOrdersRequest());
            if (orders == null) {
                throw new AppException(ErrorCode.ORDER_CREATION_FAILED);
            }
            // create order status
            orderStatusService.createOrderStatus(orders.getOrdersID(), orders.getStatus());

            //create order details by orderid
            String ordersID = orders.getOrdersID();
            List<OrderDetails> details = orderDetailsService.createOrderDetails(request.getOrderDetailsRequest(), ordersID);
            //update totalPrice in order
            //including shipping price
            double totalPrice = orders.getShippingPrice();

            for (OrderDetails detail : details) {
                totalPrice += detail.getPrice();
            }
            //update totalprice
            orders = updateTotalPrice(totalPrice, orders.getOrdersID());
            sendOrderEmail(orders, details);
            return CheckoutResponse.builder()
                    .orders(orders)
                    .orderDetails(details)
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.ORDER_FAILED);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private Orders createOrder(OrdersRequest request) {
        try {
            Orders orders = ordersMapper.toOrders(request);
            orders.setOrdersID(generateUniqueCode());
            //total price auto tinh, gio test truoc
            orders.setAccountID(request.getAccountID());
            orders.setName(request.getName());
            orders.setTotalPrice(1000000.0);
            orders.setShippingPrice(request.getShippingPrice());
            orders.setStatus("Pending");
            orders.setOrderDate(new Date());
            orders.setNote(request.getNote());

            // Xóa những thành phần trong cart
            cartRepository.clearCartByAccountID(request.getAccountID());
            //ensure order save and immediately flushes the changes to the database
            return ordersRepository.saveAndFlush(orders);
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

//    @PreAuthorize("hasRole('manager' || 'admin')")
    public List<Orders> getOrders() {
        return ordersRepository.findAll();
    }

    public List<Orders> findByAccountID(String accountid) {
         return ordersRepository.findAllByaccountID(accountid);
    }

    public Orders updateOrder(String ordersID, OrdersRequest request) {
        Orders orders = ordersRepository.findById(ordersID)
                .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOTFOUND));
        orders.setAddress(request.getAddress());
        orders.setPayingMethod(request.getPayingMethod());
        orders.setPhoneNumber(request.getPhoneNumber());
        orders.setShippingPrice(request.getShippingPrice());
        orders.setName(request.getName());
        orders.setAddress(request.getAddress());
        return ordersRepository.save(orders);
    }

    public Orders updateOrderStatus(String ordersID, String status) {
        Orders orders = ordersRepository.findById(ordersID)
                .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOTFOUND));
        if(status.equals("delivered")) {
            List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrdersID(ordersID);
            for (OrderDetails orderDetails : orderDetailsList) {
                Product product = productRepository.findById(orderDetails.getProductID()).orElseThrow(
                        () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
                );
                if(product.getType().equals("kit"))
                    orderDetails.setIsActive(1);
                orderDetailsRepository.save(orderDetails);
            }
        }

        orders.setStatus(status);
        // tao order status
        orderStatusService.createOrderStatus(orders.getOrdersID(), orders.getStatus());
        return ordersRepository.save(orders);
    }

    private Orders updateTotalPrice(Double totalPrice, String ordersID) {
        Orders orders = ordersRepository.findById(ordersID)
                .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOTFOUND));
        orders.setTotalPrice(totalPrice);
        return ordersRepository.save(orders);
    }

    private void sendOrderEmail(Orders orders, List<OrderDetails> orderDetails) throws MessagingException, UnsupportedEncodingException, SQLException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi-VN"));
        helper.setFrom(new InternetAddress("blackpro2k4@gmail.com", "FKShop"));

        Accounts accounts = accountsRepository.findById(orders.getAccountID()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );

        String scriptTable = "";
        int count = 1;
        for (OrderDetails o : orderDetails) {
            Product product = productRepository.findById(o.getProductID()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
            );
            scriptTable += "<tr>";
            scriptTable += String.format("<td>%d</td> <td>%s</td> <td>%s</td> <td>%d</td> <td>%s</td>",
                    count, product.getName(), numberFormat.format(product.getPrice()), o.getQuantity(), numberFormat.format(o.getPrice())
                    );
            scriptTable += "</tr>";
            count++;
        }

        String fullAddress = orders.getAddress() + " " +  orders.getWard()+ " " + orders.getDistrict() + " " + orders.getProvince();
        String body = "<p>Cảm ơn quý khách <strong>"+accounts.getFullName()+"</strong> đã đặt hàng tại FKShop.</p>" +
                "<p>Đơn hàng <strong>"+orders.getOrdersID()+"</strong> của quý khách đã được tiếp nhận, chúng tôi sẽ xử lý trong khoảng thời gian sớm nhất. Sau đây là thông tin đơn hàng.</p>" +
                "<h3>Thông tin đơn hàng "+orders.getOrdersID()+" vào ngày "+ orders.getOrderDate()+" "+ "</h3>" +

                "<table border='1'>" +
                "<tr><th>Thông tin khách hàng</th><th>Địa chỉ giao hàng</th></tr>" +
                "<tr><td>"+accounts.getFullName()+"<br/>"+accounts.getEmail()+"<br/>SDT: "+accounts.getPhoneNumber()+"</td>" +
                "<td>"+orders.getName()+"<br/>"+accounts.getEmail()+"<br/>"+fullAddress+"<br/>SDT: "+orders.getPhoneNumber()+"</td></tr>" +
                "</table>" + //email, sdt,

                "<p><strong>Phương thức thanh toán:</strong> "+orders.getPayingMethod()+"</p>" +
//                "<p><strong>Do tình trạng BOOM hàng xảy ra nhiều, chúng tôi tạm dừng thu hộ CoD</strong></p>" +
                "<p>Vui lòng chuyển khoản qua các tài khoản dưới đây:</p>" +
                "<ul>" +
                "<li>Ngân hàng Vietcom Bank - Số tài khoản: 0987123452 - Người nhận: Blackpro  - Chi nhánh Sài Thành</li>" +
                "<li>Momo - Số tài khoản: 0987654321 - Người nhận: FKShop</li>" +
                "</ul>" +
                "<h3>Chi tiết đơn hàng</h3>" +
                "<table border='1'>" +
                "<tr><th>No</th><th>Sản phẩm</th><th>Đơn giá</th><th>Số lượng</th><th>Tổng tạm</th></tr>" +
                scriptTable +
                "</table>" +
                "<p><strong>Phí vận chuyển:</strong> "+numberFormat.format(orders.getShippingPrice())+" đ</p>" +
                "<p><strong>Thành tiền:</strong> "+numberFormat.format(orders.getTotalPrice())+" đ</p>" +
                "<p>Đơn hàng sẽ được giao đến địa chỉ <strong>"+fullAddress+"</strong> sau 3 - 5 ngày kể từ khi tiếp nhận đơn hàng, đối với vùng sâu vùng xa, thời gian giao hàng có thể kéo dài đến 7 ngày.</p>" +
                "<p>Nếu bạn có bất kỳ thắc mắc nào, vui lòng gọi đến số 0344017063, nhân viên tư vấn của chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>" +
                "<p>Một lần nữa, Website <a href = \"http://localhost:5173/\">FKShop</a> xin cảm ơn quý khách.</p>";

        helper.setTo(accounts.getEmail());
        helper.setSubject("[FKShop] Đơn Hàng #"+orders.getOrdersID()+" đã được đặt !!");
        helper.setText(body, true);

        mailSender.send(message);
    }

    private String generateUniqueCode() {
        int number = 1;
        String code;
        do {
            code = String.format("O%05d", number);
            number++;
        } while (ordersRepository.existsById(code));
        return code;
    }

}
