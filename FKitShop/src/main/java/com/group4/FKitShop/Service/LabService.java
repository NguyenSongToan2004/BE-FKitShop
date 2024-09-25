package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Lab;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.LabRepository;
import com.group4.FKitShop.Request.LabRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LabService {
    @Autowired
    private LabRepository labRepository;

    public Lab addLabRequest(LabRequest request) {
        if(labRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.LAB_NAMEDUPLICATED);
        Lab lab = new Lab();
        lab.setLabID(generateID());
        lab.setName(request.getName());
        lab.setProductID(request.getProductID());
        lab.setDescription(request.getDescription());
        lab.setLevel(request.getLevel());
        // create current Date
        Date date = new Date();
        lab.setCreateDate(date);
        return labRepository.save(lab);
    }

    public Lab getLab(String id) {
        return labRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LAB_NOTFOUND));
    }

    public Lab updateLab(String id, LabRequest request) {
        if (!labRepository.existsByLabID(id)) {
            throw new AppException(ErrorCode.LAB_NOTFOUND);
        }
        Lab lab = labRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LAB_NOTFOUND));
        lab.setName(request.getName());
        lab.setLevel(request.getLevel());
        lab.setProductID(request.getProductID());
        lab.setDescription(request.getDescription());
        return labRepository.save(lab);
    }

    public String deleteLab(String id) {
        Lab lab = labRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LAB_NOTFOUND));
        return "";
    }

    public List<Lab> getAllLab() {
        return labRepository.findAll();
    }

    String generateID(){
        String num = labRepository.getNumberLab();
        int max = Integer.parseInt(num.substring(1,6))+1;
        num = String.format("C%05d", max);
        return num;
    }


}
