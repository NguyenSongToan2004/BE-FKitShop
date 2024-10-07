package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.OrderDetails;
import com.group4.FKitShop.Entity.Orders;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.OrdersMapper;
import com.group4.FKitShop.Repository.OrderDetailsRepository;
import com.group4.FKitShop.Repository.OrdersRepository;
import com.group4.FKitShop.Request.OrdersRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
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

    public String generateUniqueCode() {
        int number = 1;
        String code;
        do {
            code = String.format("O%05d", number);
            number++;
        } while (ordersRepository.existsById(code));
        return code;
    }

    public Orders createOrder(OrdersRequest request) {
        try {
            Orders orders = ordersMapper.toOrders(request);
            orders.setOrdersID(generateUniqueCode());
            //total price auto tinh, gio test truoc
           // orders.setTotalPrice(1000000.0);
            orders.setShippingPrice(request.getShippingPrice());
            orders.setStatus("Pending");
            orders.setOrderDate(new Date());
            return ordersRepository.save(orders);
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }

    }

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
        return ordersRepository.save(orders);
    }

    public Orders updateOrderStatus(String ordersID, String status) {
        Orders orders = ordersRepository.findById(ordersID)
                .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOTFOUND));
        orders.setStatus(status);
        return ordersRepository.save(orders);
    }

    public Orders updateTotalPrice(Double totalPrice, String ordersID) {
        Orders orders = ordersRepository.findById(ordersID)
                .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOTFOUND));
        orders.setTotalPrice(totalPrice);
        return ordersRepository.save(orders);
    }

    private void sendOrderEmail(String to, String subject, Orders orders) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi-VN"));
        helper.setFrom(new InternetAddress("blackpro2k4@gmail.com", "FKShop"));
        String scriptTable = "";
        String body = "<p>Cảm ơn quý khách <strong>"+orders.getAccountID()+"</strong> đã đặt hàng tại FKShop.</p>" +
                "<p>Đơn hàng <strong>"+orders.getOrdersID()+"</strong> của quý khách đã được tiếp nhận, chúng tôi sẽ xử lý trong khoảng thời gian sớm nhất. Sau đây là thông tin đơn hàng.</p>" +
                "<h3>Thông tin đơn hàng "+orders.getOrdersID()+" "+ orders.getOrderDate()+" "+ "</h3>" +

                "<table border='1'>" +
                "<tr><th>Thông tin khách hàng</th><th>Địa chỉ giao hàng</th></tr>" +
                "<tr><td>Exercise 1<br/>toannsse183104@fpt.edu.vn<br/>dia chi<br/>SDT: sdt</td>" +
                "<td>Exercise 1<br/>toannsse183104@fpt.edu.vn<br/>di chi<br/>SDT: 0123456789</td></tr>" +
                "</table>" + //email, sdt,

                "<p><strong>Phương thức thanh toán:</strong> Chuyển khoản qua ngân hàng</p>" +
                "<p><strong>Do tình trạng BOOM hàng xảy ra nhiều, chúng tôi tạm dừng thu hộ CoD</strong></p>" +
                "<p>Vui lòng chuyển khoản qua các tài khoản dưới đây:</p>" +
                "<ul>" +
                "<li>Ngân hàng Vietcom Bank - Số tài khoản: 0987123452 - Người nhận: Blackpro  - Chi nhánh Sài Thành</li>" +
                "<li>Momo - Số tài khoản: 0987654321 - Người nhận: FKShop</li>" +
                "</ul>" +
                "<h3>Chi tiết đơn hàng</h3>" +
                "<table border='1'>" +
                "<tr><th>No</th><th>Sản phẩm</th><th>Đơn giá</th><th>Giảm giá</th><th>Số lượng</th><th>Tổng tạm</th></tr>" +
                scriptTable +
                "</table>" +
                "<p><strong>Tạm tính:</strong> 1000 đ</p>" +
                "<p><strong>Thành tiền:</strong> 1000 đ</p>" +
                "<p>Đây là thông báo tự động, nếu sau khi đối soát với ngân hàng không thành công, chúng tôi sẽ liên lạc với bạn để điều chỉnh.</p>" +
                "<p>Đơn hàng sẽ được giao đến địa chỉ <strong>"+orders.getAddress()+"</strong> sau 3 - 5 ngày kể từ khi tiếp nhận đơn hàng, đối với vùng sâu vùng xa, thời gian giao hàng có thể kéo dài đến 7 ngày.</p>" +
                "<p>Nếu bạn có bất kỳ thắc mắc nào, vui lòng gọi đến số 0908.110586, nhân viên tư vấn của chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>" +
                "<p>Một lần nữa, Website https://fkshop.com xin cảm ơn quý khách.</p>";

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }


}
