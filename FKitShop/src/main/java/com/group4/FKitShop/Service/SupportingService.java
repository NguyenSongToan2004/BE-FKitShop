package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Entity.Lab;
import com.group4.FKitShop.Entity.Own;
import com.group4.FKitShop.Entity.Supporting;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.SupportingMapper;
import com.group4.FKitShop.Repository.AccountsRepository;
import com.group4.FKitShop.Repository.LabRepository;
import com.group4.FKitShop.Repository.SupportingRepository;
import com.group4.FKitShop.Request.*;
import com.group4.FKitShop.Response.LabSupportResponse;
import com.group4.FKitShop.Response.SubResponse.AllLabSupport;
import com.group4.FKitShop.Response.SubResponse.LabSupport;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupportingService {

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    SupportingRepository supportingRepository;

    @Autowired
    LabRepository labRepository;

    @Autowired
    SupportingMapper supportingMapper;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    OwnService ownService;

    public Supporting createSupporting(SupportingRequest request) {
        Supporting supporting = supportingRepository.findSupporting(request.getAccountID(), request.getLabID());
        Accounts accounts = accountsRepository.findById(request.getAccountID()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        Lab lab = labRepository.findById(request.getLabID()).orElseThrow(
                () -> new AppException(ErrorCode.LAB_NOTFOUND)
        );
        Own own = ownService.getOwn(request.getAccountID(), request.getLabID());
        if (supporting != null) {
            if (supporting.getStatus() != 2 && supporting.getStatus() != 3)
                throw new AppException(ErrorCode.SUPPORTING_LAB_EXISTING);
            if (supporting.getCountSupport() == own.getSupportTimes())
                throw new AppException(ErrorCode.SUPPORTING_LIMITED);
            else {
                Supporting newSupporting = supportingMapper.toSupporting(supporting);
                newSupporting.setStatus(0);
                newSupporting.setDescription(request.getDescription());
                newSupporting.setPostDate(new Date(System.currentTimeMillis()));
                newSupporting.setCountSupport(supporting.getCountSupport() + 1);
                return supportingRepository.save(newSupporting);
            }
        }
        // nếu đã có 1 request về 1 product (chưa done) thì ko được gửi tiếp
        Supporting newSupporting = new Supporting();
        newSupporting.setAccount(accounts);
        newSupporting.setLab(lab);
        newSupporting.setDescription(request.getDescription());
        newSupporting.setStatus(0);
        newSupporting.setPostDate(new Date(System.currentTimeMillis()));
        newSupporting.setCountSupport(1);
        return supportingRepository.save(newSupporting);
    }

    @Value("${spring.support.expected-date}")
    private int expectedDate;

    public Supporting updateStatus(SupportStatusUpdateRequest request) throws MessagingException, UnsupportedEncodingException {
        Supporting supporting = supportingRepository.findById(request.getSupportingID()).orElseThrow(
                () -> new AppException(ErrorCode.SUPPORTING_NOT_FOUND)
        );
        switch (request.getStatus()) {
            case 0:
                throw new AppException(ErrorCode.SUPPORTING_INVALID_STATUS);
            case 1: {
                if (supporting.getStatus() != 0)
                    throw new AppException(ErrorCode.SUPPORTING_INVALID_STATUS);
                else {
                    // gửi gmail báo đã approved, cho lịch hẹn qua điện thoại
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(System.currentTimeMillis()));
                    calendar.add(Calendar.DAY_OF_MONTH, expectedDate);

                    Date expectDate = new Date(calendar.getTimeInMillis());
                    sendGmail(supporting, expectDate);
                    supporting.setExpectedSpDate(expectDate);
                }
                break;
            }
            case 2: {
                if (supporting.getStatus() != 1)
                    throw new AppException(ErrorCode.SUPPORTING_INVALID_STATUS);
                else {
                    if (supporting.getSupportDate() == null)
                        throw new AppException(ErrorCode.SUPPORTING_DATE_NULL);
                }
                break;
            }
            case 3: {
                if (supporting.getStatus() < 2) {
                    supporting.setStatus(request.getStatus());
                    supporting.setCountSupport(supporting.getCountSupport() - 1);
                } else
                    throw new AppException(ErrorCode.SUPPORTING_LAB_DONE);
                break;
            }
        }

        supporting.setStatus(request.getStatus());
        return supportingRepository.save(supporting);
    }

    public Supporting updateExpectedDate(UpdateExpectedDateRequest request) {
        Supporting supporting = supportingRepository.findById(request.getSupportingID()).orElseThrow(
                () -> new AppException(ErrorCode.SUPPORTING_NOT_FOUND)
        );
        Date currentDate = new Date(System.currentTimeMillis());
        if (supporting.getStatus() == 2) {
            throw new AppException(ErrorCode.SUPPORTING_LAB_DONE);
        }
        if (currentDate.after(request.getExpectedDate()) && !currentDate.toLocalDate().equals(request.getExpectedDate().toLocalDate())) {
            throw new AppException(ErrorCode.SUPPORTING_INVALID_SUPPORT_DATE);
        }
        supporting.setExpectedSpDate(request.getExpectedDate());
        return supportingRepository.save(supporting);
    }

    public List<AllLabSupport> getAllSupport() {
        List<AllLabSupport> supportingResponses = new ArrayList<>();
        List<Supporting> supportings = supportingRepository.findAll();
        for (Supporting supporting : supportings) {
            Own own = ownService.getOwn(supporting.getAccount().getAccountID(), supporting.getLab().getLabID());
            AllLabSupport a = AllLabSupport.builder()
                    .accountID(supporting.getAccount().getAccountID())
                    .customerName(supporting.getAccount().getFullName())
                    .labID(supporting.getLab().getLabID())
                    .labName(supporting.getLab().getName())
                    .maxSupTimes(own.getSupportTimes())
                    .supporting(supporting)
                    .build();
            supportingResponses.add(a);
        }
        supportingResponses.sort((s1, s2) -> s2.getSupporting().getSupportingID().compareTo(s1.getSupporting().getSupportingID()));
        return supportingResponses;
    }

    public LabSupportResponse getSupportByAccount(String accountID) {
        Accounts accounts = accountsRepository.findById(accountID).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        List<LabSupport> labSupports = new ArrayList<>();
        for (Supporting supporting : accounts.getSupportings()) {
            Own own = ownService.getOwn(supporting.getAccount().getAccountID(), supporting.getLab().getLabID());
            LabSupport l = LabSupport.builder()
                    .labID(supporting.getLab().getLabID())
                    .labName(supporting.getLab().getName())
                    .maxSupTimes(own.getSupportTimes())
                    .supporting(supporting)
                    .build();
            labSupports.add(l);
        }
        labSupports.sort((l1, l2) -> l2.getSupporting().getSupportingID().compareTo(l1.getSupporting().getSupportingID()));
        return new LabSupportResponse(accounts.getAccountID(), accounts.getFullName(), labSupports);
    }

    public List<AllLabSupport> getSupportByStatus(int status) {
        if (status != 0 && status != 1 && status != 2 && status != 3)
            throw new AppException(ErrorCode.SUPPORTING_UNSUPPORTED_STATUS_CODE);
        List<AllLabSupport> supportingResponses = new ArrayList<>();
        for (Supporting supporting : supportingRepository.findAll()) {
            if (supporting.getStatus() == status) {
                AllLabSupport a = AllLabSupport.builder()
                        .accountID(supporting.getAccount().getAccountID())
                        .customerName(supporting.getAccount().getFullName())
                        .labID(supporting.getLab().getLabID())
                        .labName(supporting.getLab().getName())
                        .supporting(supporting)
                        .build();
                supportingResponses.add(a);
            }
        }
        supportingResponses.sort((s1, s2) -> s2.getSupporting().getSupportingID().compareTo(s1.getSupporting().getSupportingID()));
        return supportingResponses;
    }

    public LabSupportResponse getSupportByAccount(String accountID, int status) {
        if (status != 0 && status != 1 && status != 2)
            throw new AppException(ErrorCode.SUPPORTING_UNSUPPORTED_STATUS_CODE);
        if (!accountsRepository.existsById(accountID))
            throw new AppException(ErrorCode.USER_NOT_EXIST);
        Accounts accounts = accountsRepository.findById(accountID).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST)
        );
        List<LabSupport> labSupports = new ArrayList<>();
        for (Supporting supporting : accounts.getSupportings()) {
            if (supporting.getStatus() == status) {
                Own own = ownService.getOwn(supporting.getAccount().getAccountID(), supporting.getLab().getLabID());
                LabSupport l = LabSupport.builder()
                        .labID(supporting.getLab().getLabID())
                        .labName(supporting.getLab().getName())
                        .maxSupTimes(own.getSupportTimes())
                        .supporting(supporting)
                        .build();
                labSupports.add(l);
            }
        }
        return new LabSupportResponse(accountID, accounts.getFullName(), labSupports);
    }

    public Supporting updateSupportDate(UpdateSupportDate request) {
        Supporting supporting = supportingRepository.findById(request.getSupportingID()).orElseThrow(
                () -> new AppException(ErrorCode.SUPPORTING_NOT_FOUND)
        );
        Date currentDate = new Date(System.currentTimeMillis());
        if (supporting.getExpectedSpDate().after(request.getDate()) &&
                !supporting.getExpectedSpDate().toLocalDate().equals(request.getDate().toLocalDate()))
            throw new AppException(ErrorCode.SUPPORTING_INVALID_SUPPORT_DATE);
        supporting.setSupportDate(request.getDate());
        return supportingRepository.save(supporting);
    }

    private void sendGmail(Supporting supporting, Date expectDate) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(new InternetAddress("blackpro2k4@gmail.com", "FKShop"));
        SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy");
        String body = "<p>Xin chào " + supporting.getAccount().getFullName() + " ,</p>\n" +
                "\n" +
                "    <p>Your supporting request #" + supporting.getSupportingID() + " is approved</p>\n" +
                "\n" +
                "<p><strong>Lab name</strong> : " + supporting.getLab().getName() + "</p>" +
                "<p><strong>Support Information</strong></p>" +
                "<table border='1'>\n" +
                "        <tr>\n" +
                "            <th>SupportID</th>\n" +
                "            <th>Description</th>\n" +
                "            <th>Request Date</th>\n" +
                "            <th>Expected Date</th>\n" +
                "            <th>Current Support Turns</th>\n" +
                "            <th>Status</th>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>" + supporting.getSupportingID() + "</td>\n" +
                "            <td>" + supporting.getDescription() + "</td>\n" +
                "            <td>" + formater.format(supporting.getPostDate()) + "</td>\n" +
                "            <td>" + formater.format(expectDate) + "</td>\n" +
                "            <td>" + supporting.getCountSupport() + "</td>\n" +
                "            <td>approved</td>\n" +
                "        </tr>\n" +
                "    </table>" +
                "\n" +
                "    <p>FKShop's staff will contact your phone number <strong>" + supporting.getAccount().getPhoneNumber() + "</strong> to make a decision about the support date.</p>\n" +
                "\n" +
                "    <p>The expected date was updated on our website. <a href=\"http://localhost:5173/\">Click here</a> to view your supporting request.</p>";
        helper.setTo(supporting.getAccount().getEmail());
        helper.setSubject("[FKShop] Your Supporting Request #" + supporting.getSupportingID() + " Is Approved");
        helper.setText(body, true);

        mailSender.send(message);
    }
}
