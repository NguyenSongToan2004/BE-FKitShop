package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Tag;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.TagRepository;
import com.group4.FKitShop.Mapper.TagMapper;
import com.group4.FKitShop.Request.TagRequest;
import com.group4.FKitShop.Mapper.TagMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagService {

    TagRepository tagRepository;
    TagMapper tagMapper;

    public List<Tag> allTag(){
        return tagRepository.findAll();
    }

    public Tag getTagByID(int id){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOTFOUND));
        return tag;
    }

    public List<Tag> getTagByName(String name){
        return tagRepository.getTagByName("%"+name+"%");
    }

    // get tag by blogID
    public List<Tag> getTagByBlog(String id){
        return tagRepository.getTagList(id);
    }

    public Tag createTag(TagRequest request) {
        if (tagRepository.existsByTagName(request.getTagName()))
            throw new AppException(ErrorCode.TAG_NAME_DUPLICATED);
        Tag tag = tagMapper.toTag(request);
        tag.setStatus(1);
        return tagRepository.save(tag);
    }

    public Tag updateTag(int id, TagRequest request){
        Tag tag = tagRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.TAG_NOTFOUND));
        tag.setTagName(request.getTagName());
        tag.setDescription(request.getDescription());
        tag.setStatus(request.getStatus());
        return tagRepository.save(tag);
    }

    @Transactional
    public int deleteTag(int id) {
        if (!tagRepository.existsById(id))
            throw new AppException(ErrorCode.TAG_NOTFOUND);
        return tagRepository.deleteStatus(id);
    }


}
