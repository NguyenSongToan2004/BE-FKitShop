package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.BlogTag;
import com.group4.FKitShop.Repository.BlogRepository;
import com.group4.FKitShop.Repository.BlogTagRepository;
import com.group4.FKitShop.Repository.TagRepository;
import com.group4.FKitShop.Request.BlogRequest;
import com.group4.FKitShop.Request.TagRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogTagService {

    BlogTagRepository blogTagRepository;
    BlogRepository blogRepository;
    private final TagRepository tagRepository;

    public List<BlogTag> getAllBlogTags() {
        return blogTagRepository.findAll();
    }

    public BlogTag getBlogTagById(int id) {
        return blogTagRepository.findById(id).orElse(null);
    }

    public List<BlogTag> getBlogTagsByBlogId(String id) {
        return blogTagRepository.findByBlogID(id);
    }

    public String generateBlogID() {
        int number = 1;
        String code;

        do {
            code = String.format("B%05d", number);
            number++;
        } while (blogRepository.existsById(code));
        String id = String.format("B%05d", number - 2);
        return id;
    }

    public boolean createBlogTag_Blog(BlogRequest request) {
        List<Integer> tagIDs = request.getTagID();

        String blogID = generateBlogID();

        // Loop through all tagIDs
        for (int tagID : tagIDs) {
            BlogTag blogtag = new BlogTag();
            blogtag.setBlogID(blogID);
            blogtag.setTagID(tagID);
            blogTagRepository.save(blogtag);
        }
        return true;
    }

    public boolean updateBlogTag_Blog(String blogID, BlogRequest request) {
        List<Integer> tagIDs = request.getTagID();

        // Loop through all tagIDs
        for (int tagID : tagIDs) {
            BlogTag blogtag = new BlogTag();
            blogtag.setBlogID(blogID);
            blogtag.setTagID(tagID);
            blogTagRepository.save(blogtag);
        }
        return true;
    }

    public void deleteBlogTag_Blog(String blogID) {
        List<BlogTag> blogTags = blogTagRepository.findByBlogID(blogID);
        for (BlogTag obj : blogTags) {
            blogTagRepository.deleteById(obj.getBlogTagID());
        }
    }

    public void createBlogTag_Tag(TagRequest request) {
        List<String> blogIDs = request.getBlogID();

        int tagID = 1;
        do {
            tagID++;
        } while (tagRepository.existsById(tagID));

        for (String blogID : blogIDs) {
            BlogTag blogtag = new BlogTag();
            blogtag.setBlogID(blogID);
            blogtag.setTagID(tagID);
            blogTagRepository.save(blogtag);
        }
    }

    public void updateBlogTag_Tag(int tagID, TagRequest request) {
        List<String> blogIDs = request.getBlogID();

        for (String blogID : blogIDs) {
            BlogTag blogtag = new BlogTag();
            blogtag.setBlogID(blogID);
            blogtag.setTagID(tagID);
            blogTagRepository.save(blogtag);
        }
    }

    public void deleteBlogTag_Tag(int tagID) {
        List<BlogTag> blogTags = blogTagRepository.findByTagID(tagID);
        for (BlogTag obj : blogTags) {
            blogTagRepository.deleteById(obj.getBlogTagID());
        }
    }
}
