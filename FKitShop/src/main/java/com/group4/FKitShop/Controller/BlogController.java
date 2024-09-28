package com.group4.FKitShop.Controller;



import com.group4.FKitShop.Entity.Blog;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.BlogRequest;
import com.group4.FKitShop.Service.BlogService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    @GetMapping()
    public List<Blog> allBlog(){
        return blogService.allBlog();
    }


    @GetMapping("/{blogID}")
    ResponseEntity<ResponseObject> getTagByID(@PathVariable String blogID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", blogService.getBlogByID(blogID))
        );
    }

    @PostMapping()
    public ResponseObject createTag(@RequestBody @Valid BlogRequest request,
                                    @RequestParam("image") MultipartFile image ) {
        return ResponseObject.builder()
                .code(1000)
                .message("Create blog successfully")
                .o(blogService.createBlog(request, image))
                .build();
    }


    @PutMapping("/{blogID}")
    public ResponseObject updateTag(@RequestBody @Valid BlogRequest request,
                                    @PathVariable String blogID,
                                    @RequestParam("image") MultipartFile image) {
        return ResponseObject.builder()
                .code(1000)
                .message("Update blog successfully")
                .o(blogService.updateBlog(blogID, request, image))
                .build();

    }

    @DeleteMapping("/{blogID}")
    public ResponseObject deleteTag(@PathVariable String blogID){
        blogService.deleteBlog(blogID);
        return ResponseObject.builder()
                .code(1000)
                .message("Delete blog successfully")
                .build();

    }
}
