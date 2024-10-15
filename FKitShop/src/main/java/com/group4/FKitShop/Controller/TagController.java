package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Entity.Tag;
import com.group4.FKitShop.Request.TagRequest;
import com.group4.FKitShop.Service.TagService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:5173")
public class TagController {

    TagService tagService;

    // get all tag
    @GetMapping()
    public List<Tag> allTag(){
        return tagService.allTag();
    }

    // get tag by ID
    @GetMapping("/{tagID}")
    ResponseEntity<ResponseObject> getTagByID(@PathVariable() int tagID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", tagService.getTagByID(tagID))
        );
    }

    // get tag by blogID
    @GetMapping("/byBlogID/{blogID}")
    ResponseEntity<ResponseObject> getTagByBlogID(@PathVariable String blogID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", tagService.getTagByBlog(blogID))
        );
    }

    // get tag by blogID
    @GetMapping("/byName/{name}")
    ResponseEntity<ResponseObject> getTagByName(@PathVariable String name) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", tagService.getTagByName(name))
        );
    }

    // create tag
    @PostMapping()
    public ResponseObject createTag(@RequestBody @Valid TagRequest request ) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create tag successfully")
                .data(tagService.createTag(request))
                .build();
      }

    // update tag by ID
    @PutMapping("/{tagID}")
    public ResponseObject updateTag(@RequestBody @Valid TagRequest request, @PathVariable int tagID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Update tag successfully")
                .data(tagService.updateTag(tagID, request))
                .build();

    }

    // delete tag by ID
    @DeleteMapping("/{tagID}")
    ResponseEntity<ResponseObject> deleteTag(@PathVariable int tagID){
        return ResponseEntity.ok(
                new ResponseObject(1000, "Delete Successfully !!",  tagService.deleteTag(tagID) + " row affeted")
        );
    }
}
