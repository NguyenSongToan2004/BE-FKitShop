package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.Tag;
import com.group4.FKitShop.Request.CreateTagRequest;
import com.group4.FKitShop.Request.UpdateTagRequest;
import com.group4.FKitShop.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping()
    public List<Tag> allTag(){
        return tagService.allTag();
    }

    @GetMapping("/{tagID}")
    Tag getTagByID(@PathVariable() int tagID){

        return tagService.getTagByID(tagID);
    }

    @PostMapping()
    Tag createTag(@RequestBody CreateTagRequest request){

        return tagService.createTag(request);
    }

    @PutMapping("/{tagID}")
    Tag updateTag(@PathVariable int tagID, @RequestBody UpdateTagRequest request){
        return tagService.updateTag(tagID, request);
    }

    @DeleteMapping("/{tagID}")
    String deleteTag(@PathVariable() int tagID){
        tagService.deleteTag(tagID);
        return "Tag has been deleted";
    }
}
