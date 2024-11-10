package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Component;
import com.group4.FKitShop.Entity.Product;
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
import java.util.concurrent.atomic.AtomicReference;

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
            componentResponse.setComponentName(productRepository.findById(component.getComponentID())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND)).getName());
            componentResponses.add(componentResponse);
        }
        return componentResponses;
    }

    public List<ComponentResponse> getComponentByProduct(String id) {
        List<Component> components = componentRepository.findByProductID(id);
        List<ComponentResponse> componentResponses = new ArrayList<>();
        for (Component component : components) {
            ComponentResponse componentResponse = new ComponentResponse();
            componentResponse.setComponentIndex(component.getComponentIndex());
            componentResponse.setComponentID(component.getComponentID());
            componentResponse.setQuantity(component.getQuantity());
            componentResponse.setComponentName(productRepository.findById(component.getComponentID())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND)).getName());
            componentResponses.add(componentResponse);
        }
        return componentResponses;
    }


    // create component
//    public String generateProductID() {
//        int number = 1;
//        String code;
//        do {
//            code = String.format("P%05d", number);
//            number++;
//        } while (productRepository.existsById(code));
//        String id = String.format("P%05d", number - 2);
//        return id;
//    }

//    public String generateComponentID() {
//        int number = 1;
//        String code;
//        do {
//            code = String.format("CP%05d", number);
//            number++;
//        } while (productRepository.existsById(code));
//        String id = String.format("P%05d", number - 2);
//        return id;
//    }

//    public List<Component> createComponent(ComponentRequest request, String productID, double totalPrice) {
//        AtomicReference<Double> totalComPrice = new AtomicReference<>((double) 0);
//        List<Component> components = new ArrayList<>();
//        request.getComponents().forEach((componentID, quantity) -> {
//            Product product = productRepository.findById(productID).orElseThrow(
//                    () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
//            totalComPrice.set(totalPrice + product.getPrice() * quantity);
//            Component component = new Component();
//            component.setComponentID(componentID);
//            component.setQuantity(quantity);
//            component.setProductID(productID);
//            components.add(component);
//        });
//        System.out.println("totalComPrice : " + totalComPrice.get());
//        System.out.println("totalPrice : " + totalPrice);
//        if (totalComPrice.get() > totalPrice)
//            throw new AppException(ErrorCode.PRODUCT_KIT_INVALID_PRICE);
//        return componentRepository.saveAll(components);
//    }

    public List<Component> createComponent(ComponentRequest request, String productID, double totalPrice) {
        AtomicReference<Double> totalComPrice = new AtomicReference<>((double) 0);
        List<Component> components = new ArrayList<>();
        request.getComponents().forEach((componentID, quantity) -> {
            Product product = productRepository.findById(componentID).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
            totalComPrice.set(totalComPrice.get() + product.getPrice() * quantity);
            Component component = new Component();
            component.setComponentID(componentID);
            component.setQuantity(quantity);
            component.setProductID(productID);
            components.add(component);
        });
        if (totalComPrice.get() > totalPrice)
            throw new AppException(ErrorCode.PRODUCT_KIT_INVALID_PRICE);
        return componentRepository.saveAll(components);
    }

    public List<Component> updateComponent(ComponentRequest request, String productID) {
        List<Component> oldComponents = componentRepository.findByProductID(productID);
//        request.getComponents().forEach((componentID, quantity) -> {
//            for (Component component : oldComponents) {
//                if (component.getComponentID().equals(componentID)) {
//                    componentRepository.delete(component);
//                }
//            }
//        });
        componentRepository.deleteAll(oldComponents);
        Product product = productRepository.findById(productID).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
        );
        return createComponent(request, productID, product.getPrice());
    }

    public boolean deleteComponent(int id) {
        componentRepository.deleteById(id);
        return true;
    }

    public void updateQuantityCompo(String productID, int detailQuantity, String type) {
        List<Component> compoList = componentRepository.findByProductID(productID);
        compoList.forEach(compo ->
                productRepository.findById(compo.getComponentID())
                        .ifPresent(p -> {
                            if (type.equals("cancel")) {
                                p.setQuantity(p.getQuantity() + compo.getQuantity() * detailQuantity);
                                productRepository.save(p);
                            } else {
                                p.setQuantity(p.getQuantity() - compo.getQuantity() * detailQuantity);
                                productRepository.save(p);
                            }
                        })
        );
    }

}