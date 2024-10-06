package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Tag;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.TagRepository;
import com.group4.FKitShop.Mapper.TagMapper;
import com.group4.FKitShop.Request.TagRequest;
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
                .orElseThrow(() -> new AppException(ErrorCode.Tag_NOTFOUND));

        return tag;
    }

    public Tag createTag(TagRequest request) {
        if (tagRepository.existsByTagName(request.getTagName()))
            throw new AppException(ErrorCode.TagName_DUPLICATED);

//        Tag tag = new Tag();
//        tag.setTagName(tagName);
//        tag.setDescription(description);

        Tag tag = tagMapper.toTag(request);
        return tagRepository.save(tag);
    }

    public Tag updateTag(int id, TagRequest request){
        Tag tag = tagRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.Tag_NOTFOUND));

//        if(request.getTagName() == null){
//            tag.setTagName(getTagByID(id).getTagName());
//        }else{
//            tag.setTagName(request.getTagName());
//        }
//
//        if(request.getDescription() == null){
//            tag.setDescription(getTagByID(id).getDescription());
//        }else{
//            tag.setDescription(request.getDescription());
//        }

        tag.setTagName(request.getTagName());
        tag.setDescription(request.getDescription());
        return tagRepository.save(tag);
    }


    @Transactional
    public void deleteTag(int id) {
        if (!tagRepository.existsById(id))
            throw new AppException(ErrorCode.Tag_NOTFOUND);
        tagRepository.deleteById(id);
    }

    

}
