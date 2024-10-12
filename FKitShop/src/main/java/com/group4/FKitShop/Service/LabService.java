package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.*;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.LabMapper;
import com.group4.FKitShop.Repository.*;
import com.group4.FKitShop.Request.DownloadLabRequest;
import com.group4.FKitShop.Request.LabRequest;
import com.group4.FKitShop.Request.OrderLab;
import com.group4.FKitShop.Response.GetLabByAccountIDResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LabService {

    LabRepository labRepository;
    AccountsRepository accountsRepository;
    OrdersRepository ordersRepository;
    OrderDetailsRepository orderDetailsRepository;
    ProductRepository productRepository;

    public Lab addLabRequest(LabRequest request, MultipartFile file) {
        if(labRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.LAB_NAMEDUPLICATED);
        Lab lab = LabMapper.INSTANCE.toLab(request);
        lab.setLabID(generateID());
        // create current Date
        lab.setCreateDate(new Date());
        lab.setFileNamePDF(saveLabPDF(file));
        return labRepository.save(lab);
    }

    public Lab getLab(String id) {
        return labRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LAB_NOTFOUND));
    }

    public Lab updateLab(String id, LabRequest request, MultipartFile file) {
        if (!labRepository.existsByLabID(id)) {
            throw new AppException(ErrorCode.LAB_NOTFOUND);
        }
        Lab lab = labRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LAB_NOTFOUND));
        LabMapper.INSTANCE.updateLab(request,lab);
        String fileNamePDF = saveLabPDF(file);
        if (fileNamePDF != null){
            lab.setFileNamePDF(fileNamePDF);
        }
        return labRepository.save(lab);
    }

    public String deleteLab(String id) {
        Lab lab = labRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LAB_NOTFOUND));
        return "";
    }

    public List<Lab> getAllLab() {
        return labRepository.findAll();
    }

    private static final String STORAGE_DIRECTORY = "uploads" + File.separator + "lab";

    public String saveLabPDF(MultipartFile fileToSave) {

        if (fileToSave.isEmpty()) {
            return null;
        }

        var targetFile = new File(System.getProperty("user.dir") + File.separator + STORAGE_DIRECTORY + File.separator + fileToSave.getOriginalFilename());
        // bảo mật tránh hacker lỏ vào file cha
        if (!Objects.equals(targetFile.getParentFile().toString(), System.getProperty("user.dir")+ File.separator + STORAGE_DIRECTORY))
            throw new AppException(ErrorCode.LAB_UNSUPPORTED_FILENAME);
        try {
            Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e){
            e.printStackTrace();
            throw new AppException(ErrorCode.LAB_UPLOAD_FAILED);
        }

        return fileToSave.getOriginalFilename();
    }

    public String saveLabPDF(MultipartFile fileToSave, String id) {

        if (fileToSave.isEmpty()) {
            return null;
        }

        Lab lab = labRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LAB_NOTFOUND));

        var targetFile = new File(System.getProperty("user.dir") + File.separator + STORAGE_DIRECTORY + File.separator + fileToSave.getOriginalFilename());
        System.out.println(targetFile.getParentFile());

        // bảo mật tránh hacker lỏ vào file cha
        if (!Objects.equals(targetFile.getParentFile().toString(),System.getProperty("user.dir")+ File.separator + STORAGE_DIRECTORY))
            throw new AppException(ErrorCode.LAB_UNSUPPORTED_FILENAME);
        try {
            Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e){
            e.printStackTrace();
            throw new AppException(ErrorCode.LAB_UPLOAD_FAILED);
        }

        lab.setFileNamePDF(fileToSave.getOriginalFilename());
        labRepository.save(lab);
        return fileToSave.getOriginalFilename();
    }

    public File downloadFilePDF(DownloadLabRequest request){
        var fileToDownload = new File(System.getProperty("user.dir") + File.separator +
                STORAGE_DIRECTORY + File.separator + request.getFileName());
        if(request.getFileName().isEmpty())
            throw new NullPointerException("File Named Null!!");

        if(!fileToDownload.exists())
            throw new NullPointerException("File Does Not Exist!!");
        if(!Objects.equals(fileToDownload.getParentFile().toString(),System.getProperty("user.dir") + File.separator + STORAGE_DIRECTORY))
            throw new SecurityException("Unsupported Filename !!");
        return writeInfoToFile(fileToDownload, request.getAccountID(), request.getOrderID(), request.getLabID(), request.getProductName());
    }

    public GetLabByAccountIDResponse getLabByAccountID(String accountID){
        Accounts accounts = accountsRepository.findById(accountID).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        Set<OrderLab> setOrderLab = new HashSet<>();
        List<Orders> ordersList = ordersRepository.findOrdersByAccountID(accountID);
        for(Orders orders : ordersList){
            List<OrderDetails> orderDetailsList = orderDetailsRepository.findActiveOrderDetails(orders.getOrdersID(), 1);
            for (OrderDetails orderDetails : orderDetailsList) {
                List<Lab> listLabTmp = labRepository.findByProductID(orderDetails.getProductID());
                for (Lab labTmp : listLabTmp) {
                    OrderLab orderLab = new OrderLab(orders.getOrdersID(), labTmp);
                    setOrderLab.add(orderLab);
                }
            }
        }
        return GetLabByAccountIDResponse.builder()
                .accountID(accountID)
                .orderLabs(setOrderLab)
                .build();
    }

    public List<Lab> getLabByProductID(String productID) {
        return labRepository.findByProductID(productID);
    }

    private File writeInfoToFile(File file, String accountID, String orderID, String labID, String productName) {
        Orders orders = ordersRepository.findById(orderID).orElseThrow(
                () ->  new AppException(ErrorCode.ORDERS_NOTFOUND)
        );
        Accounts accounts = accountsRepository.findById(orders.getAccountID()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        Lab lab = labRepository.findById(labID).orElseThrow(
                () -> new AppException(ErrorCode.LAB_NOTFOUND)
        );

        String pdfPath = STORAGE_DIRECTORY + File.separator + file.getName();
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDDocument documentReplace = document;
            PDType0Font font = PDType0Font.load(documentReplace, new File("Arial Unicode MS Bold.ttf"));
            // Lấy trang đầu tiên của tài liệu PDF
            PDPage page = documentReplace.getPage(0);

            // Tạo ContentStream để thêm nội dung vào trang
            PDPageContentStream contentStream = new PDPageContentStream(documentReplace, page, PDPageContentStream.AppendMode.PREPEND, false);

            // Bắt đầu ghi văn bản vào trang
            contentStream.beginText();
            contentStream.setFont(font, 12); // Đặt font và cỡ chữ

            // Đặt vị trí để thêm văn bản (ví dụ: tọa độ x = 50, y = 750)
            contentStream.newLineAtOffset(50, 750);  // Tọa độ y cao để ghi ở đầu trang
            // Nội dung văn bản cần thêm vào
            contentStream.setLeading(20);
            contentStream.showText("OrderID: "+orders.getOrdersID()+"                    ShipDate: " + orders.getShipDate());
            contentStream.newLine();
            contentStream.showText("Customer Name: "+orders.getOrdersID());
            contentStream.newLine();
            contentStream.showText("StemName: "+productName+"                      Lab Name: " + lab.getName() );
            contentStream.newLine();
            contentStream.showText("Level : " + lab.getLevel());
            contentStream.newLine();

            // Kết thúc việc ghi văn bản
            contentStream.endText();
            contentStream.close();
            File pdfDownload =  new File("download");
            // Lưu file PDF đã cập nhật
            documentReplace.save(pdfDownload);
            return pdfDownload;
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.LAB_DOWNLOAD_FAILED);
        }
    }

    String generateID(){
        String num = labRepository.getNumberLab();
        if(num == null)
           return num = String.format("L%05d", 1);

        int max = Integer.parseInt(num.substring(1,6))+1;
        num = String.format("L%05d", max);
        return num;
    }
}
