package com.group4.FKitShop.Service;



import com.group4.FKitShop.Entity.Blog;
import com.group4.FKitShop.Entity.BlogTag;
import com.group4.FKitShop.Entity.Tag;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.BlogMapper;
import com.group4.FKitShop.Repository.BlogRepository;
import com.group4.FKitShop.Repository.TagRepository;
import com.group4.FKitShop.Request.BlogRequest;
import com.group4.FKitShop.Response.BlogResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogService {


    BlogRepository blogRepository;

    BlogMapper blogMapper;

    BlogTagService blogTagService;

    TagRepository tagRepository;

    AmazonClient amazonClient;


    public List<BlogResponse> allBlog(){
        List<Blog> blogs = blogRepository.findAll();
        List<BlogResponse> blogResponses = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setBlog(blog);
            List<BlogTag> blogTags = blogTagService.getBlogTagsByBlogId(blog.getBlogID());
            List<Tag> tags = new ArrayList<>();
            for (BlogTag blogTag : blogTags) {
                tags.add(tagRepository.findById(blogTag.getTagID()).get());
            }
            blogResponse.setTags(tags);
            blogResponses.add(blogResponse);
        }
        return blogResponses;
    }

    public BlogResponse getBlogByID(String id){
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOTFOUND));
        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setBlog(blog);
        List<BlogTag> blogTags = blogTagService.getBlogTagsByBlogId(blog.getBlogID());
        List<Tag> tags = new ArrayList<>();
        for (BlogTag blogTag : blogTags) {
            tags.add(tagRepository.findById(blogTag.getTagID()).get());
        }
        blogResponse.setTags(tags);
        return blogResponse;
    }

    // get blog by tagID
    public List<BlogResponse> getBlogByTag(int id){
        List<Blog> blogs = blogRepository.getBlogList(id);
        List<BlogResponse> blogResponses = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setBlog(blog);
            List<BlogTag> blogTags = blogTagService.getBlogTagsByBlogId(blog.getBlogID());
            List<Tag> tags = new ArrayList<>();
            for (BlogTag blogTag : blogTags) {
                tags.add(tagRepository.findById(blogTag.getTagID()).get());
            }
            blogResponse.setTags(tags);
            blogResponses.add(blogResponse);
        }
        return blogResponses;
    }

    // get blog active
    public List<BlogResponse> getBlogActive(){
        List<Blog> blogs = blogRepository.getBlogActive();
        List<BlogResponse> blogResponses = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setBlog(blog);
            List<BlogTag> blogTags = blogTagService.getBlogTagsByBlogId(blog.getBlogID());
            List<Tag> tags = new ArrayList<>();
            for (BlogTag blogTag : blogTags) {
                tags.add(tagRepository.findById(blogTag.getTagID()).get());
            }
            blogResponse.setTags(tags);
            blogResponses.add(blogResponse);
        }
        return blogResponses;
    }

    // filter by date
    public List<BlogResponse> getBlogDateDesc(){
        List<Blog> blogs = blogRepository.getBlogListDesc();
        List<BlogResponse> blogResponses = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setBlog(blog);
            List<BlogTag> blogTags = blogTagService.getBlogTagsByBlogId(blog.getBlogID());
            List<Tag> tags = new ArrayList<>();
            for (BlogTag blogTag : blogTags) {
                tags.add(tagRepository.findById(blogTag.getTagID()).get());
            }
            blogResponse.setTags(tags);
            blogResponses.add(blogResponse);
        }
        return blogResponses;
    }
    public List<BlogResponse> getBlogDateAsc(){
        List<Blog> blogs = blogRepository.getBlogListAsc();
        List<BlogResponse> blogResponses = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setBlog(blog);
            List<BlogTag> blogTags = blogTagService.getBlogTagsByBlogId(blog.getBlogID());
            List<Tag> tags = new ArrayList<>();
            for (BlogTag blogTag : blogTags) {
                tags.add(tagRepository.findById(blogTag.getTagID()).get());
            }
            blogResponse.setTags(tags);
            blogResponses.add(blogResponse);
        }
        return blogResponses;
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

    public Blog createBlog(BlogRequest request) {
        if (blogRepository.existsByBlogName(request.getBlogName()))
            throw new AppException(ErrorCode.BLOG_DUPLICATED);


        Blog blog = blogMapper.toBlog(request);
        blog.setBlogID(generateUniqueCode());
        blog.setCreateDate(new Date());
        blog.setToDelete(1);
        return blogRepository.save(blog);
    }

    public Blog updateBlog(String id, BlogRequest request) {

        if (!blogRepository.existsById(id))
            throw new AppException(ErrorCode.BLOG_NOTFOUND);

        Blog blogGetDate = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOTFOUND));

        Blog blog = blogMapper.toBlog(request);
        blog.setBlogID(id);
        blog.setCreateDate(blogGetDate.getCreateDate());
        return blogRepository.save(blog);
    }

    @Transactional
    public int deleteBlog(String id) {
        if (!blogRepository.existsById(id))
            throw new AppException(ErrorCode.BLOG_NOTFOUND);
        return blogRepository.deleteStatus(id);
    }


}
