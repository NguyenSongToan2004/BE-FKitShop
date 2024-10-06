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

    @GetMapping()
    public List<Tag> allTag(){
        return tagService.allTag();
    }


    @GetMapping("/{tagID}")
    ResponseEntity<ResponseObject> getTagByID(@PathVariable() int tagID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", tagService.getTagByID(tagID))
        );
    }

    @PostMapping()
    public ResponseObject createTag(@RequestBody @Valid TagRequest request ) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create tag successfully")
                .data(tagService.createTag(request))
                .build();
      }


    @PutMapping("/{tagID}")
    public ResponseObject updateTag(@RequestBody @Valid TagRequest request, @PathVariable int tagID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Update tag successfully")
                .data(tagService.updateTag(tagID, request))
                .build();

    }

    @DeleteMapping("/{tagID}")
    public ResponseObject deleteTag(@PathVariable int tagID){
        tagService.deleteTag(tagID);
        return ResponseObject.builder()
                .status(1000)
                .message("Delete tag successfully")
                .build();

    }
}
