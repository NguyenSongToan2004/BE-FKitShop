package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Entity.Tag;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.TagRepository;
import com.group4.FKitShop.Mapper.TagMapper;
import com.group4.FKitShop.Request.TagRequest;
import com.group4.FKitShop.Response.TagResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagService {

    TagRepository tagRepository;
    TagMapper tagMapper;
    CategoryService categoryService;

    public List<TagResponse> allTag(){
        List<Tag> tags = tagRepository.findAll();
        List<TagResponse> tagResponses = new ArrayList<>();
        for (Tag tag : tags) {
            TagResponse tagResponse = new TagResponse();
            List<Category> cates = categoryService.getCategoryByTag(tag.getTagID());
            tagResponse.setTag(tag);
            tagResponse.setCates(cates);
            tagResponses.add(tagResponse);
        }
        return tagResponses;
    }

    public TagResponse getTagByID(int id){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOTFOUND));
        List<Category> cates = categoryService.getCategoryByTag(tag.getTagID());
        TagResponse tagResponse = new TagResponse();
        tagResponse.setTag(tag);
        tagResponse.setCates(cates);
        return tagResponse;
    }

    public List<TagResponse> getTagByName(String name){
        List<Tag> tags = tagRepository.getTagByName("%"+name+"%");
        List<TagResponse> tagResponses = new ArrayList<>();
        for (Tag tag : tags) {
            TagResponse tagResponse = new TagResponse();
            List<Category> cates = categoryService.getCategoryByTag(tag.getTagID());
            tagResponse.setTag(tag);
            tagResponse.setCates(cates);
            tagResponses.add(tagResponse);
        }
        return tagResponses;
    }

    // get tag by blogID
    public List<TagResponse> getTagByBlog(String id){
        List<Tag> tags = tagRepository.getTagList(id);
        List<TagResponse> tagResponses = new ArrayList<>();
        for (Tag tag : tags) {
            TagResponse tagResponse = new TagResponse();
            List<Category> cates = categoryService.getCategoryByTag(tag.getTagID());
            tagResponse.setTag(tag);
            tagResponse.setCates(cates);
            tagResponses.add(tagResponse);
        }
        return tagResponses;
    }

    //get tag active
    public List<TagResponse> getTagActive(){
        List<Tag> tags = tagRepository.getTagActive();
        List<TagResponse> tagResponses = new ArrayList<>();
        for (Tag tag : tags) {
            TagResponse tagResponse = new TagResponse();
            List<Category> cates = categoryService.getCategoryByTag(tag.getTagID());
            tagResponse.setTag(tag);
            tagResponse.setCates(cates);
            tagResponses.add(tagResponse);
        }
        return tagResponses;
    }

    public TagResponse createTag(TagRequest request) {
        if (tagRepository.existsByTagName(request.getTagName()))
            throw new AppException(ErrorCode.TAG_NAME_DUPLICATED);
        Tag tag = tagMapper.toTag(request);
        tag.setStatus(1);
        tagRepository.save(tag);
        List<Category> cates = categoryService.getCategoryByTag(tag.getTagID());
        TagResponse tagResponse = new TagResponse();
        tagResponse.setTag(tag);
        tagResponse.setCates(cates);
        return tagResponse;
    }

    public TagResponse updateTag(int id, TagRequest request){
        Tag tag = tagRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.TAG_NOTFOUND));
        tag.setTagName(request.getTagName());
        tag.setDescription(request.getDescription());
        tag.setStatus(request.getStatus());
        tagRepository.save(tag);
        List<Category> cates = categoryService.getCategoryByTag(tag.getTagID());
        TagResponse tagResponse = new TagResponse();
        tagResponse.setTag(tag);
        tagResponse.setCates(cates);
        return tagResponse;
    }

    @Transactional
    public int deleteTag(int id) {
        if (!tagRepository.existsById(id))
            throw new AppException(ErrorCode.TAG_NOTFOUND);
        return tagRepository.deleteStatus(id);
    }


}
