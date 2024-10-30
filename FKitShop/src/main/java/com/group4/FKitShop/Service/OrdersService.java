package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.*;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.LabMapper;
import com.group4.FKitShop.Mapper.OrdersMapper;
import com.group4.FKitShop.Repository.*;
import com.group4.FKitShop.Request.CheckoutRequest;
import com.group4.FKitShop.Request.DateRequest;
import com.group4.FKitShop.Request.OrderLab;
import com.group4.FKitShop.Request.OrdersRequest;
import com.group4.FKitShop.Response.CheckoutResponse;
import com.group4.FKitShop.Response.DailyRevenueResponse;
import com.group4.FKitShop.Response.GetLabResponse;
import com.group4.FKitShop.Response.RevenueResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Aspect
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
    OwnService ownService;
    ProductRepository productRepository;
    CartRepository cartRepository;
    AccountsRepository accountsRepository;
    LabRepository labRepository;

    public CheckoutResponse checkOut(CheckoutRequest request) {
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
            double totalPrice = updateStockAndTotalPrice(details, orders.getShippingPrice());
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
            orders.setStatus("pending");
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
        status = status.toLowerCase();
        String[] ostatus = {"pending", "in-progress", "delivering", "delivered"};
        List<String> statusSequence = Arrays.asList(ostatus);
        Orders orders = ordersRepository.findById(ordersID)
                .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOTFOUND));

        //check if the order is already canceled => cannot change status
        if (orders.getStatus().toLowerCase().equals("cancel"))
            throw new AppException(ErrorCode.CANCEL_ORDER_FAILED);

        //cancel order
        if (status.equals("cancel")) {
            return ordersRepository.save(cancelOrder(orders));
        }
        // Check if the new status is in the allowed sequence and follows the current status
        //Cannot downdate status
        int currentIndex = statusSequence.indexOf(orders.getStatus().toLowerCase());
        int newIndex = statusSequence.indexOf(status.toLowerCase());
        if (newIndex == -1 || newIndex <= currentIndex) {
            throw new AppException(ErrorCode.INVALID_UPDATE_STATUS_DOWNDATE);
        }
        //Update status by following flow: pending, in-progress, delivering, delivered
        if (newIndex - currentIndex > 1) {
            throw new AppException(ErrorCode.INVALID_UPDATE_STATUS_UPDATE);
        }

        if (status.equals("delivered")) {
            orders.setShipDate(new java.sql.Date(System.currentTimeMillis()));
            List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrdersID(ordersID);
            for (OrderDetails orderDetails : orderDetailsList) {
                Product product = productRepository.findById(orderDetails.getProductID()).orElseThrow(
                        () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
                );
                if (product.getType().equals("kit")) {
                    orderDetails.setIsActive(1);
                    //set warranty date up to 30 days
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(System.currentTimeMillis()));
                    calendar.add(Calendar.DAY_OF_MONTH, 30); // Add 30 days

                    orderDetails.setWarrantyDate(new Date(calendar.getTimeInMillis()));
                }
                orderDetailsRepository.save(orderDetails);
            }
            createOwn(orders.getAccountID(), orders.getOrdersID());
        }
        orders.setStatus(status.toLowerCase());
        // tao order status
        orderStatusService.createOrderStatus(orders.getOrdersID(), orders.getStatus());
        return ordersRepository.save(orders);
    }

    //cancel order
    //user can cancel order only if the order's status is "pending"
    public Orders cancelOrder(Orders orders) {
        String ordersID = orders.getOrdersID();
        String defaultOrderStatus = "pending";
        String currentOrderStatus = orders.getStatus().toLowerCase();
        if (!currentOrderStatus.equals(defaultOrderStatus)) {
            throw new AppException(ErrorCode.ORDER_CANCEL_FAILED);
        }
        //return product quantity to product's stock
        orderDetailsService.updatePQuantityWhenCancel(ordersID);
        //set the order status to cancel
        orders.setStatus("cancel");
        return orders;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fullAddress = orders.getAddress() + ", " + orders.getWard() + ", " + orders.getDistrict() + ", " + orders.getProvince();
        String body = "<p>Cảm ơn quý khách <strong>" + accounts.getFullName() + "</strong> đã đặt hàng tại FKShop.</p>" +
                "<p>Đơn hàng <strong>" + orders.getOrdersID() + "</strong> của quý khách đã được tiếp nhận, chúng tôi sẽ xử lý trong khoảng thời gian sớm nhất. Sau đây là thông tin đơn hàng.</p>" +
                "<h3>Thông tin đơn hàng " + orders.getOrdersID() + " vào ngày " + dateFormat.format(orders.getOrderDate()) + " " + "</h3>" +

                "<table border='1'>" +
                "<tr><th>Thông tin khách hàng</th><th>Địa chỉ giao hàng</th></tr>" +
                "<tr><td>" + accounts.getFullName() + "<br/>" + accounts.getEmail() + "<br/>SDT: " + accounts.getPhoneNumber() + "</td>" +
                "<td>" + orders.getName() + "<br/>" + accounts.getEmail() + "<br/>" + fullAddress + "<br/>SDT: " + orders.getPhoneNumber() + "</td></tr>" +
                "</table>" + //email, sdt,

                "<p><strong>Phương thức thanh toán:</strong> " + orders.getPayingMethod() + "</p>" +
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
                "<p><strong>Phí vận chuyển:</strong> " + numberFormat.format(orders.getShippingPrice()) + " đ</p>" +
                "<p><strong>Thành tiền:</strong> " + numberFormat.format(orders.getTotalPrice()) + " đ</p>" +
                "<p>Đơn hàng sẽ được giao đến địa chỉ <strong>" + fullAddress + "</strong> sau 3 - 5 ngày kể từ khi tiếp nhận đơn hàng, đối với vùng sâu vùng xa, thời gian giao hàng có thể kéo dài đến 7 ngày.</p>" +
                "<p>Nếu bạn có bất kỳ thắc mắc nào, vui lòng gọi đến số 0344017063, nhân viên tư vấn của chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>" +
                "<p>Một lần nữa, Website <a href = \"http://localhost:5173/\">FKShop</a> xin cảm ơn quý khách.</p>";

        helper.setTo(accounts.getEmail());
        helper.setSubject("[FKShop] Đơn Hàng #" + orders.getOrdersID() + " đã được đặt !!");
        helper.setText(body, true);

        mailSender.send(message);
    }

    private double updateStockAndTotalPrice(List<OrderDetails> details, double shippingPrice) {
        double totalPrice = 0;
        for (OrderDetails detail : details) {
            Product product = productRepository.findById(detail.getProductID()).orElseThrow(
                    () -> new RuntimeException("Product not found")
            );
            if (product.getQuantity() < detail.getQuantity()) {
                throw new AppException(ErrorCode.PRODUCT_UNAVAILABLE);
            }
            product.setQuantity(product.getQuantity() - detail.getQuantity());
            product.setUnitOnOrder(product.getUnitOnOrder() + detail.getQuantity());
            totalPrice += detail.getPrice();
            productRepository.save(product);
        }
        return totalPrice += shippingPrice;
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

    public Orders findOrderbyStatus(String status) {
        Orders orders = ordersRepository.findBystatus(status)
                .orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOTFOUND));
        return orders;
    }

    public byte[] getOrderReportFile(String time) throws IOException {
        LocalDate now = LocalDate.now();
        List<Orders> orders = getListOrderByTime(time);
        // List<Orders> orders = ordersRepository.findAll(); // Lấy danh sách đơn hàng từ database
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle rowStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        //======================
        Font titleFont = workbook.createFont();
        titleFont.setColor(IndexedColors.BLACK.getIndex());
        titleFont.setBold(true);  // In đậm
        titleFont.setFontHeightInPoints((short) 20);
        //========================
        font.setColor(IndexedColors.BROWN.getIndex());
        font.setBold(true);  // In đậm
        rowStyle.setFont(font);
        //======================
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa
        titleStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
        //======================
        Sheet sheet = workbook.createSheet("Order Report");
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(getTitle(time)); // Nội dung tiêu đề
        titleCell.setCellStyle(titleStyle);

        // Merging cells để tiêu đề nằm giữa các cột (giả sử có 5 cột dữ liệu)
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 11)); // Merge từ cột 0 đến cột 4
        // Tạo tiêu đề cột
        Row headerRow = sheet.createRow(3);

        String[] columnHeaders = {"Order ID", "AccountID", "Customer Name", "Address", "Paying Method",
                "Phone Number", "Ship Fee", "Total Price", "Order Date", "Ship Date", "Note", "Status"};
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            cell.setCellStyle(rowStyle);
        }

        // Thêm dữ liệu đơn hàng vào các dòng
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi-VN"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int rowIdx = 4;
        double totalPrice = 0;
        for (Orders order : orders) {
            String address = order.getAddress() + ", " + order.getWard() + ", " + order.getDistrict() + ", " + order.getProvince();
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(order.getOrdersID());
            row.createCell(1).setCellValue(order.getAccountID());
            row.createCell(2).setCellValue(order.getName());
            row.createCell(3).setCellValue(address);
            row.createCell(4).setCellValue(order.getPayingMethod());
            row.createCell(5).setCellValue(order.getPhoneNumber());
            row.createCell(6).setCellValue(numberFormat.format(order.getShippingPrice()));
            row.createCell(7).setCellValue(numberFormat.format(order.getTotalPrice()));
            row.createCell(8).setCellValue(dateFormat.format(order.getOrderDate()));
            row.createCell(9).setCellValue(order.getShipDate() != null ?
                    dateFormat.format(order.getShipDate()) : "unUpdated");
            row.createCell(10).setCellValue(order.getNote());
            row.createCell(11).setCellValue(order.getStatus());
            if (order.getStatus().equals("delivered"))
                totalPrice += order.getTotalPrice();
        }
        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 6, 8));
        Row row = sheet.createRow(rowIdx++);
        row.setHeightInPoints(17);
        Cell cell = row.createCell(6);
        cell.setCellStyle(rowStyle);
        cell.setCellValue("Total Price : " + numberFormat.format(totalPrice));
        //sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 7, 8)); // Merge từ cột 0 đến cột 4

        // Ghi workbook vào output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private List<Orders> getListOrderByTime(String time) {
        LocalDate now = LocalDate.now();
        List<Orders> orders = new ArrayList<>();
        switch (time) {
            case "week": {
                LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                try {
                    orders = ordersRepository.findAll().stream().filter(
                            order -> {
                                LocalDate orderDate = new java.sql.Date(order.getOrderDate().getTime())
                                        .toLocalDate();
                                return (orderDate.isEqual(startOfWeek) || orderDate.isEqual(endOfWeek) || (orderDate.isAfter(startOfWeek) && orderDate.isBefore(endOfWeek)));
                            }).toList();
                    System.out.println("bla bla");
                    System.out.println("size of list: " + orders.size());
                    return orders;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "month": {
                int currentMonth = now.getMonthValue(); // Lấy tháng hiện tại
                int currentYear = now.getYear(); // Lấy năm hiện tại
                try {
                    orders = ordersRepository.findAll().stream().filter(
                            order -> {
                                // Chuyển đổi từ java.util.Date (hoặc java.sql.Date) sang LocalDate
                                LocalDate orderDate = new java.sql.Date(order.getOrderDate().getTime())
                                        .toLocalDate();
                                // Kiểm tra tháng và năm có khớp với tháng và năm hiện tại không
                                return orderDate.getMonthValue() == currentMonth && orderDate.getYear() == currentYear;
                            }).toList();

                    // In ra số lượng đơn hàng đã lọc
                    System.out.println("Size of list: " + orders.size());
                } catch (Exception e) {
                    e.printStackTrace(); // Bắt và in ra lỗi nếu có
                }
                break;
            }
            case "quarter": {
                int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                System.out.println("current quarter: " + currentQuarter);
                int currentYear = now.getYear();
                orders = ordersRepository.findAll().stream().filter(
                        order -> {
                            LocalDate orderDate = new java.sql.Date(order.getOrderDate().getTime())
                                    .toLocalDate();
                            int orderQuarter = (orderDate.getMonthValue() - 1) / 3 + 1;
                            System.out.println("order quarter: " + orderQuarter);
                            return orderQuarter == currentQuarter && orderDate.getYear() == currentYear;
                        }).toList();
                break;
            }
            default:
                orders = ordersRepository.findAll();
                System.out.println("default");
                break;
        }
        return orders;
    }

    private String getTitle(String time) {
        String title = "SALES REPORT FOR " + time.toUpperCase();
        LocalDate now = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        switch (time) {
            case "week": {
                LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                title += "FROM " + format.format(startOfWeek) + " TO " + format.format(endOfWeek);
                break;
            }
            case "month": {
                int currentMonth = now.getMonthValue(); // Lấy tháng hiện tại
                int currentYear = now.getYear(); // Lấy năm hiện tại
                title += " IN " + currentMonth + "/" + currentYear;
                break;
            }
            case "quarter": {
                int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                title += "IN QUARTER " + currentQuarter;
                break;
            }
            default:
                break;
        }
        return title;
    }

    private void createOwn(String accountID, String orderID) {
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findActiveOrderDetails(orderID, 1);
        for (OrderDetails o : orderDetailsList) {
            List<Lab> listLab = labRepository.findByProductID(o.getProductID());
            for (Lab labTmp : listLab) {
                if (ownService.isExist(accountID, labTmp.getLabID()))
                    ownService.updateSupportTimes(accountID, labTmp.getLabID(), 5 * o.getQuantity());
                else
                    ownService.createOwn(accountID, labTmp.getLabID(), 5 * o.getQuantity());
            }
        }
    }

    public List<Object> getMonth(){
        return ordersRepository.getMonth();
    }

    public List<Orders> getOrderByMonth(DateRequest request){
        return ordersRepository.findOrdersByMonth(request.getDate1(), request.getDate2());
    }

    public List<RevenueResponse> getRevenue(){
        List<Object[]> objs = ordersRepository.getRevenue();
        List<RevenueResponse> responses = new ArrayList<>();
        for (Object[] row : objs) {
            RevenueResponse revenueResponse = RevenueResponse.builder()
                    .monthCode((String) row[0])
                    .totalProductPrice(((Number) row[1]).doubleValue())
                    .totalShippingPrice(((Number) row[2]).doubleValue())
                    .totalRevenue(((Number) row[3]).doubleValue())
                    .build();
            responses.add(revenueResponse);
        }
        List<RevenueResponse> finalResponses = new ArrayList<>();
        for(int i = 0; i < responses.size(); i++){
            RevenueResponse revenueResponse = new RevenueResponse();
            if(i == 0){
                revenueResponse = responses.get(i);
                revenueResponse.setDifferenceRevenue(null);
                revenueResponse.setDifferencePercent(null);
                revenueResponse.setStatus(0);
            }else{
                revenueResponse = responses.get(i);
                revenueResponse.setDifferenceRevenue(responses.get(i).getTotalRevenue() - responses.get(i-1).getTotalRevenue());
                revenueResponse.setDifferencePercent(responses.get(i).getTotalRevenue()/responses.get(i-1).getTotalRevenue());
                if(revenueResponse.getDifferenceRevenue() == 0){
                    revenueResponse.setStatus(0);
                }else if(revenueResponse.getDifferenceRevenue() > 0){
                    revenueResponse.setStatus(1);
                }else{
                    revenueResponse.setStatus(-1);
                }
            }
            finalResponses.add(revenueResponse);
        }
        return finalResponses;
    }

    public List<DailyRevenueResponse> getDailyRevenueForCurrentMonth() {
        List<Object[]> results = ordersRepository.findDailyRevenueForCurrentMonth();
        return results.stream()
                .map(result -> new DailyRevenueResponse(
                        (java.sql.Date) result[0],
                        ((BigDecimal) result[1]).doubleValue()  // Chuyển đổi BigDecimal thành Double
                ))
                .collect(Collectors.toList());
    }
}
