package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Tag;
import com.group4.FKitShop.Repository.TagRepository;
import com.group4.FKitShop.Request.CreateTagRequest;
import com.group4.FKitShop.Request.UpdateTagRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> allTag(){
        return tagRepository.findAll();
    }

    public Tag getTagByID(int id){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        Tag tagOut = new Tag(tag.getTagID(), tag.getTagName(), tag.getDescription());
        return tagOut;
    }

    public Tag createTag(CreateTagRequest request){
        Tag tag = new Tag();
        tag.setTagName(request.getTagName());
        tag.setDescription(request.getDescription());

        return tagRepository.save(tag);
    }

    public Tag updateTag(int id, UpdateTagRequest request){
        Tag tag = getTagByID(id);
        if(request.getTagName() == null){
            tag.setTagName(getTagByID(id).getTagName());
        }else{
            tag.setTagName(request.getTagName());
        }

        if(request.getDescription() == null){
            tag.setDescription(getTagByID(id).getDescription());
        }else{
            tag.setDescription(request.getDescription());
        }
        return tagRepository.save(tag);
    }

    public void deleteTag(int id){
        tagRepository.deleteById(id);
    }

}
