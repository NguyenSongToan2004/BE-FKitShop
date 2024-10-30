package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Component;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.ComponentRepository;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.ComponentRequest;
import com.group4.FKitShop.Response.ComponentResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ComponentService {


    ComponentRepository componentRepository;
    ProductRepository productRepository;

    public List<ComponentResponse> getComponents() {
        List<Component> components = componentRepository.findAll();
        List<ComponentResponse> componentResponses = new ArrayList<>();
        for (Component component : components) {
            ComponentResponse componentResponse = new ComponentResponse();
            componentResponse.setComponentIndex(component.getComponentIndex());
            componentResponse.setComponentID(component.getComponentID());
            componentResponse.setQuantity(component.getQuantity());
            componentResponse.setProduct(productRepository.findById(component.getProductID())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND)));
            componentResponse.setComponentName(productRepository.findById(component.getComponentID())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND)).getName());
            componentResponses.add(componentResponse);
        }
        return componentResponses;
    }

    public List<Component> getComponentByProduct(String id) {
        return componentRepository.getComponentByProductID(id);
    }


    // create component
    public String generateProductID() {
        int number = 1;
        String code;
        do {
            code = String.format("P%05d", number);
            number++;
        } while (productRepository.existsById(code));
        String id = String.format("P%05d", number - 2);
        return id;
    }

    public List<Component> createComponent(ComponentRequest request) {
        String id = generateProductID();

        List<Component> components = new ArrayList<>();
        request.getComponents().forEach((componentID, quantity) -> {
            Component component = new Component();
            component.setComponentID(componentID);
            component.setQuantity(quantity);
            component.setProductID(id);
            components.add(component);
        });
        return componentRepository.saveAll(components);
    }

    public List<Component> updateComponent(ComponentRequest request, String productID) {
        List<Component> oldComponents = componentRepository.getComponentByProductID(productID);
        request.getComponents().forEach((componentID, quantity) -> {
            for (Component component : oldComponents) {
                if (component.getComponentID().equals(componentID)) {
                    componentRepository.delete(component);
                }
            }
        });
        List<Component> components = new ArrayList<>();
        request.getComponents().forEach((componentID, quantity) -> {
            Component component = new Component();
            component.setComponentID(componentID);
            component.setQuantity(quantity);
            component.setProductID(productID);
            components.add(component);
        });
        return componentRepository.saveAll(components);
    }

    public boolean deleteComponent(int id) {
        componentRepository.deleteById(id);
        return true;
    }

}