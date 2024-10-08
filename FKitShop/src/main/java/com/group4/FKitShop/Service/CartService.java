package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Cart;
import com.group4.FKitShop.Entity.Orders;
import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.CartRepository;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.CartRequest;
import com.group4.FKitShop.Response.CartResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartService {

    CartRepository cartRepository;
    ProductRepository productRepository;

    ProductService productService;

    public CartResponse createCart(CartRequest cartRequest) {
        try {
            Map<String, Integer> productQuantity = cartRequest.getProductQuantity();

            for (String productID : productQuantity.keySet()) {
                int requestQuantity = productQuantity.get(productID);
                // Check if the product is already in the cart
                Optional<Cart> carttmp = cartRepository.findByaccountIDAndproductQuantity(cartRequest.getAccountID(), productID);
                Product product = productRepository.findById(productID).orElseThrow(
                        () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
                );
                int pQuantity = product.getQuantity() - requestQuantity;
                if (carttmp.isEmpty()) {
                    Cart cart = new Cart();
                    cart.setAccountID(cartRequest.getAccountID());
                    cart.setProductID(productID);
                    cart.setQuantity(requestQuantity);
                    //update product requestQuantity
                    // add cart => - product quantity
                    cart.setStatus("available");
                    cartRepository.save(cart);
                    productService.updateQuantity(pQuantity, productID);
                    // If the product exists, update its quantity
                } else {
                    Cart cart = carttmp.get();
                    int cQuantity = carttmp.get().getQuantity();
                    cart.setQuantity(requestQuantity + carttmp.get().getQuantity());
                    cartRepository.save(cart);
                    productService.updateQuantity(pQuantity, productID);
                }
            }
            Map<String, Integer> tmp = new HashMap<>();
            List<Cart> cartlist = cartRepository.findByaccountID(cartRequest.getAccountID());
            for (Cart cart : cartlist) {
                tmp.put(cart.getProductID(), cart.getQuantity());
            }
            return CartResponse.builder()
                    .accountID(cartRequest.getAccountID())
                    .productQuantity(tmp)
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

    public CartResponse viewCartByAccountID(String accountID) {
        try {
            Map<String, Integer> tmp = new HashMap<>();
            List<Cart> cartlist = cartRepository.findByaccountID(accountID);
            for (Cart cart : cartlist) {
                tmp.put(cart.getProductID(), cart.getQuantity());
            }
            return CartResponse.builder()
                    .accountID(accountID)
                    .productQuantity(tmp)
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

    //update all cart quantity
    public CartResponse updateAllQuantityCart(String accountID, Map<String, Integer> productQuantity) {
        try {
            List<Cart> cartlist = cartRepository.findByaccountID(accountID);
            for (Cart cart : cartlist) {
                //product's quantity in cart
                int currentQuantity = cart.getQuantity();
                Product product = productRepository.findById(cart.getProductID()).orElseThrow(
                        () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
                );
                int availableProductQuantity = product.getQuantity();
                for (Map.Entry<String, Integer> entry : productQuantity.entrySet()) {
                    if (cart.getProductID().equals(entry.getKey())) {
                        //request quantity
                        int requestQuantity = entry.getValue();
                        //case
                        if (requestQuantity > availableProductQuantity) {
                            throw new AppException(ErrorCode.PRODUCT_UNAVAILABLE);
                        }
                        cart.setQuantity(requestQuantity);
                        cartRepository.save(cart);
                        if (requestQuantity > currentQuantity) {
                            availableProductQuantity -= (requestQuantity - currentQuantity);
                            productService.updateQuantity(availableProductQuantity, product.getProductID());
                        } else {
                            availableProductQuantity += (requestQuantity - currentQuantity);
                            productService.updateQuantity(availableProductQuantity, product.getProductID());
                        }
                    }
                }
            }
            return CartResponse.builder()
                    .accountID(accountID)
                    .productQuantity(productQuantity)
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

    public CartResponse deleteCartByProductID(String accountID, String productID) {
        try {
            Map<String, Integer> tmp = new HashMap<>();
            cartRepository.deletebyAccountIDAndProductID(accountID, productID);
            List<Cart> cartlist = cartRepository.findByaccountID(accountID);
            for (Cart cart : cartlist) {
                tmp.put(cart.getProductID(), cart.getQuantity());
            }
            return CartResponse.builder()
                    .accountID(accountID)
                    .productQuantity(tmp)
                    .build();

        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }




}
