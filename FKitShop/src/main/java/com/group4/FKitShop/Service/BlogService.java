package com.group4.FKitShop.Service;



import com.group4.FKitShop.Entity.Blog;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.BlogMapper;
import com.group4.FKitShop.Repository.BlogRepository;
import com.group4.FKitShop.Request.BlogRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogService {

    BlogRepository blogRepository;
    BlogMapper blogMapper;

    public List<Blog> allBlog(){
        return blogRepository.findAll();
    }

    public Blog getBlogByID(String id){
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.Blog_NOTFOUND));

        return blog;
    }

    private static final String UPLOAD_DIRECTORY = "uploads" + File.separator + "blogs";

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
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    public String generateUniqueCode() {
        int number = 1;
        String code;

        do {
            code = String.format("B%05d", number);
            number++;
        } while (blogRepository.existsById(code));
        return code;
    }

    public Blog createBlog(BlogRequest request, MultipartFile image) {
        if (blogRepository.existsByBlogName(request.getBlogName()))
            throw new AppException(ErrorCode.Blog_DUPLICATED);

        Blog blog = blogMapper.toBlog(request);
        blog.setBlogID(generateUniqueCode());
        blog.setImage(uploadImage(image));
        blog.setCreateDate(new Date());
        return blogRepository.save(blog);
    }

    public Blog updateBlog(String id, BlogRequest request, MultipartFile image) {

        if (!blogRepository.existsById(id))
            throw new AppException(ErrorCode.Blog_NOTFOUND);

        Blog blogGetDate = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.Blog_NOTFOUND));

        Blog blog = blogMapper.toBlog(request);
        blog.setBlogID(id);
        blog.setCreateDate(blogGetDate.getCreateDate());
        String imageUrl = uploadImage(image);
        if(imageUrl != ""){
            blog.setImage(imageUrl);
        }
        return blogRepository.save(blog);
    }



    @Transactional
    public void deleteBlog(String id) {
        if (!blogRepository.existsById(id))
            throw new AppException(ErrorCode.Blog_NOTFOUND);
        blogRepository.deleteById(id);
    }
}
