package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.Blog;
import com.group4.FKitShop.Entity.BlogTag;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.BlogRequest;
import com.group4.FKitShop.Response.BlogResponse;
import com.group4.FKitShop.Service.BlogService;
import com.group4.FKitShop.Service.BlogTagService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {

    BlogService blogService;
    BlogTagService blogTagService;

    @GetMapping()
    public List<Blog> allBlog() {
        return blogService.allBlog();
    }


    @GetMapping("/{blogID}")
    ResponseEntity<ResponseObject> getBlogByID(@PathVariable String blogID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", blogService.getBlogByID(blogID))
        );
    }

    // get blog by tagID
    @GetMapping("/byTagID/{tagID}")
    ResponseEntity<ResponseObject> getBlogByTagID(@PathVariable int tagID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", blogService.getBlogByTag(tagID))
        );
    }

    // filter by date
    @GetMapping("/dateDesc")
    ResponseEntity<ResponseObject> getBlogDateDesc() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", blogService.getBlogDateDesc())
        );
    }
    @GetMapping("/dateAsc")
    ResponseEntity<ResponseObject> getBlogDateAsc() {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", blogService.getBlogDateAsc())
        );
    }


    @PostMapping()
    public ResponseObject createBlog(
            @RequestParam("blogName") String blogName,
            @RequestParam("content") String content,
            @RequestParam("status") String status,
            @RequestParam("accountID") String accountID,
            @RequestParam("image") MultipartFile image,
            @RequestParam("tagID") List<Integer> tagID
    ) {
        BlogRequest request = BlogRequest.builder()
                .blogName(blogName)
                .content(content)
                .status(status)
                .accountID(accountID)
                .tagID(tagID)
                .build();
        Blog blog = blogService.createBlog(request, image);
        blogTagService.createBlogTag_Blog(request);
        List<BlogTag> bt = blogTagService.getBlogTagsByBlogId(blog.getBlogID());
//        return ResponseEntity.status(HttpStatus.CREATED).body(
//                new ResponseObject(1000, "Create new blog sucessfully !!", blog));

        return ResponseObject.builder()
                .status(1000)
                .message("Create blog successfully")
                .data(new BlogResponse(blog, bt) )
                .build();

    }

    @PutMapping("/{blogID}")
    public ResponseObject updateBlog(@PathVariable String blogID,
                                              @RequestParam("blogName") String blogName,
                                              @RequestParam("content") String content,
                                              @RequestParam("status") String status,
                                              @RequestParam("accountID") String accountID,
                                              @RequestParam("image") MultipartFile image,
                                              @RequestParam("toDelete") int toDelete,
                                              @RequestParam("tagID") List<Integer> tagID
    ) {
        BlogRequest request = BlogRequest.builder()
                .blogName(blogName)
                .content(content)
                .status(status)
                .accountID(accountID)
                .toDelete(toDelete)
                .tagID(tagID)
                .build();
        Blog blog = blogService.updateBlog(blogID, request, image);
        blogTagService.deleteBlogTag_Blog(blogID);
        blogTagService.updateBlogTag_Blog(blogID, request);
        List<BlogTag> bt = blogTagService.getBlogTagsByBlogId(blog.getBlogID());

        return ResponseObject.builder()
                .status(1000)
                .message("Update blog successfully")
                .data(new BlogResponse(blog, bt) )
                .build();
    }

    @DeleteMapping("/{blogID}")
    public ResponseObject deleteBlog(@PathVariable String blogID) {
       // blogTagService.deleteBlogTag_Blog(blogID);
        blogService.deleteBlog(blogID);
        return ResponseObject.builder()
                .status(1000)
                .message("Delete blog successfully")
                .build();
    }


}
