package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.Component;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.ComponentRequest;
import com.group4.FKitShop.Response.ComponentResponse;
import com.group4.FKitShop.Service.ComponentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/components")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ComponentController {

    ComponentService componentService;

    @GetMapping()
    public List<ComponentResponse> getComponents() {
        return componentService.getComponents();
    }

    @GetMapping("/byProduct/{id}")
    public List<Component> getComponentByProduct(@PathVariable String id) {
        return componentService.getComponentByProduct(id);
    }

    @PostMapping()
    public ResponseObject createComponent(@RequestBody @Valid ComponentRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create component successfully")
                .data(componentService.createComponent(request))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseObject createComponent(@RequestBody @Valid ComponentRequest request, @PathVariable String id) {
        return ResponseObject.builder()
                .status(1000)
                .message("Update component successfully")
                .data(componentService.updateComponent(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteTag(@PathVariable int id){
        return ResponseEntity.ok(
                new ResponseObject(1000, "Delete Successfully !!",  componentService.deleteComponent(id))
        );
    }
}