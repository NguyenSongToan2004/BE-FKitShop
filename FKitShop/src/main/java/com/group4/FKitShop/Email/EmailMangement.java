package com.group4.FKitShop.Email;

import com.group4.FKitShop.Entity.Orders;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class EmailMangement {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailHtml(String to, String subject, Orders object) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi-VN"));
        helper.setFrom(new InternetAddress("blackpro2k4@gmail.com", "FKShop"));
        String scriptTable = "";
        String name = "PC 100 củ";
        String price = numberFormat.format(100000000);
        String discountPrice = numberFormat.format(99999000);
        int quantity = 1;
        String tmpPrice = numberFormat.format(1000);
        for (int i = 1 ; i == 1 ; i++){
            scriptTable += "<tr>";
            scriptTable += String.format("<td>%d</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%d</td> <td>%s</td>", i, name, price, discountPrice, quantity, tmpPrice);
            scriptTable += "</tr>";
        }
        String body = "<p>Cảm ơn quý khách <strong>Hello</strong> đã đặt hàng tại Website.</p>" +
                "<p>Đơn hàng <strong>#DATHANG4426</strong> của quý khách đã được tiếp nhận, chúng tôi sẽ xử lý trong khoảng thời gian sớm nhất. Sau đây là thông tin đơn hàng.</p>" +
                "<h3>Thông tin đơn hàng #DATHANG4426 (12-09-2024 02:48:42)</h3>" +

                "<table border='1'>" +
                "<tr><th>Thông tin khách hàng</th><th>Địa chỉ giao hàng</th></tr>" +
                "<tr><td>Exercise 1<br/>toannsse183104@fpt.edu.vn<br/>dia chi<br/>SDT: sdt</td>" +
                "<td>Exercise 1<br/>toannsse183104@fpt.edu.vn<br/>di chi<br/>SDT: 0123456789</td></tr>" +
                "</table>" +

                "<p><strong>Phương thức thanh toán:</strong> Chuyển khoản qua ngân hàng</p>" +
                "<p><strong>Do tình trạng BOOM hàng xảy ra nhiều, chúng tôi tạm dừng thu hộ CoD</strong></p>" +
                "<p>Vui lòng chuyển khoản qua các tài khoản dưới đây:</p>" +
                "<ul>" +
                "<li>Ngân hàng Vietcom Bank - Số tài khoản: hehe - Người nhận: ai z  - Chi nhánh Sài Thành</li>" +
                "<li>Momo - Số tài khoản: 0000000000 - Người nhận: Blackpro</li>" +
                "</ul>" +
                "<h3>Chi tiết đơn hàng</h3>" +
                "<table border='1'>" +
                "<tr><th>No</th><th>Sản phẩm</th><th>Đơn giá</th><th>Giảm giá</th><th>Số lượng</th><th>Tổng tạm</th></tr>" +
                scriptTable +
                "</table>" +
                "<p><strong>Tạm tính:</strong> 1000 đ</p>" +
                "<p><strong>Thành tiền:</strong> 1000 đ</p>" +
                "<p>Đây là thông báo tự động, nếu sau khi đối soát với ngân hàng không thành công, chúng tôi sẽ liên lạc với bạn để điều chỉnh.</p>" +
                "<p>Đơn hàng sẽ được giao đến địa chỉ <strong> 4/13 ấp Hưng Hòa, Thị xã Gò Công, Tiền Giang </strong> sau 3 - 5 ngày kể từ khi tiếp nhận đơn hàng, đối với vùng sâu vùng xa, thời gian giao hàng có thể kéo dài đến 7 ngày.</p>" +
                "<p>Nếu bạn có bất kỳ thắc mắc nào, vui lòng gọi đến số 0908.110586, nhân viên tư vấn của chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>" +
                "<p>Một lần nữa, Website https://fkshop.com xin cảm ơn quý khách.</p>";

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

}
