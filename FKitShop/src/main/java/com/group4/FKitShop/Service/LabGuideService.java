package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Lab;
import com.group4.FKitShop.Entity.LabGuide;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.LabGuideRepository;
import com.group4.FKitShop.Repository.LabRepository;
import com.group4.FKitShop.Request.LabGuideRequest;
import com.group4.FKitShop.Response.GetLabGuideResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class LabGuideService {
    @Autowired
    private LabGuideRepository repository;
    @Autowired
    private LabRepository labRepository;

//    public LabGuide addLabGuide(LabGuideRequest request, MultipartFile image) {
//
//        LabGuide labGuide = new LabGuide();
//        labGuide.setLabID(request.getLabID());
//        labGuide.setStepDescription(request.getStepDescription());
//        labGuide.setStepNumber(repository.getStepNumber(request.getLabID()).orElseGet(() -> 0) + 1);
//        labGuide.setStepImage(uploadImage(image));
//
//        return repository.save(labGuide);
//    }

    public LabGuide addLabGuide(LabGuideRequest request) {
        Lab lab = labRepository.findById(request.getLabID()).orElseThrow(
                    () -> new AppException(ErrorCode.LAB_NOTFOUND)
        );
          LabGuide guide = new LabGuide();
          guide.setLab(lab);
          guide.setDescription(request.getDescription());
          guide.setStep(repository.getStepNumber(request.getLabID()).orElseGet(() -> 0) + 1);
          guide.setContent(request.getContent());
          guide.setIsUsed(0);
          return repository.save(guide);
    }
    public LabGuide getGuideByID(int guideID) {
        return repository.findById(guideID).orElseThrow(
                () -> new AppException(ErrorCode.LAB_GUIDE_NOT_FOUND)
        );
    }

    public LabGuide updateInfoLabGuide(int guideID, LabGuideRequest request) {
        LabGuide guide = repository.findById(guideID).map(updateLab -> {
                    updateLab.setDescription(request.getDescription());
                    updateLab.setContent(request.getContent());
                    return updateLab;
                }
                ).orElseThrow(
                () -> new AppException(ErrorCode.LAB_GUIDE_NOT_FOUND)
        );
        return repository.save(guide);
    }

    public String deleteLabGuide(int guideID) {
        LabGuide guideDelete = repository.findById(guideID).orElseThrow(
                () -> new AppException(ErrorCode.LAB_GUIDE_NOT_FOUND)
        );
        List<LabGuide> labGuideList = repository.findByLabID(guideDelete.getLab().getLabID(),
                guideDelete.getStep());
        for (LabGuide l : labGuideList) {
            if (l.getStep() == guideDelete.getStep()) break;
            l.setStep(l.getStep() - 1);
            repository.save(l);
        }
        repository.deleteById(guideID);
        return "";
    }

//    public List<LabGuide> getAllGuides(String id) {
//        return  repository.findByLabID(id);
//    }

//    public LabGuide getLabGuide(int id) {
//        return repository.findById(id).orElseThrow(() -> new RuntimeException("NOT FOUND"));
//    }

    public GetLabGuideResponse getLabGuidesByLabID(String labID) {
        Lab lab = labRepository.findById(labID).orElseThrow(
                () -> new AppException(ErrorCode.LAB_NOTFOUND)
        );
        GetLabGuideResponse response = GetLabGuideResponse.builder()
                .labID(lab.getLabID())
                .labGuides(lab.getLabGuides())
                .build();
        return response;
    }

    public List<LabGuide> getAll() {
        return repository.findAll();
    }

    private static final String UPLOAD_DIRECTORY = "uploads" + File.separator + "lab_guides";


    String uploadImage(MultipartFile file) {
        // Kiểm tra xem file có rỗng không
        if (file.isEmpty()) {
            return "";
        }
        try {
            // Lấy đường dẫn tương đối đến thư mục uploads (có thể thay đổi tùy môi trường)
            String uploadDir = System.getProperty("user.dir") + File.separator + UPLOAD_DIRECTORY;
            // System.getProperty("user.dir") : lấy ra đường dẫn đến thư mục hiện tại
            // Tạo thư mục nếu chưa tồn tại
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            // Lưu file vào thư mục
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return UPLOAD_DIRECTORY + File.separator + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Loi hinh anh !!");
        }
    }


//    public String deleteLabGuide(int id) {
//        LabGuide labDelete = repository.findById(id).orElseThrow(() -> new RuntimeException("NOT FOUND"));
//        List<LabGuide> list = repository.findByLabID(labDelete.getLabID(), labDelete.getStepNumber());
//        for (LabGuide lab : list) {
//            if (lab.getStepNumber() == labDelete.getStepNumber()) break;
//            lab.setStepNumber(lab.getStepNumber() - 1);
//            repository.save(lab);
//        }
//        repository.deleteById(id);
//        return "";
//    }

//    public LabGuide updateLabGuide(LabGuideUpdateRequest request, MultipartFile image, int id) {
//        LabGuide labGuide = repository.findById(id).orElseThrow(() -> new RuntimeException("NOT FOUND"));
//
//        labGuide.setLabID(request.getLabID());
//        labGuide.setStepDescription(request.getStepDescription());
//        labGuide.setStepNumber(request.getStepNumber());
//        String imageUrl = uploadImage(image);
//        if (imageUrl != "")
//            labGuide.setStepImage(imageUrl);
//        return repository.save(labGuide);
//    }

}
