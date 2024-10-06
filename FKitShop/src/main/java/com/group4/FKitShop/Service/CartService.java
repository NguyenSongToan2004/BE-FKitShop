package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Cart;
import com.group4.FKitShop.Entity.Orders;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.CartRepository;
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

    public CartResponse createCart(CartRequest cartRequest) {
        try {
            Map<String, Integer> productQuantity = cartRequest.getProductQuantity();

            for (String productID : productQuantity.keySet()) {
                int quantity = productQuantity.get(productID);
                // Check if the product is already in the cart
                Optional<Cart> carttmp = cartRepository.findByaccountIDAndproductQuantity(cartRequest.getAccountID(), productID);
                if (carttmp.isEmpty()) {
                    Cart cart = new Cart();
                    cart.setAccountID(cartRequest.getAccountID());
                    cart.setProductID(productID);
                    cart.setQuantity(quantity);
                    cartRepository.save(cart);
                    // If the product exists, update its quantity
                } else {
                    Cart cart = carttmp.get();
                    cart.setQuantity(quantity + carttmp.get().getQuantity());
                    cartRepository.save(cart);
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
            Map<String, Integer> tmp = new HashMap<>();
            List<Cart> cartlist = cartRepository.findByaccountID(accountID);
            for (Cart cart : cartlist) {
                tmp.put(cart.getProductID(), cart.getQuantity());
            }
            for (Cart cart : cartlist) {
                for (Map.Entry<String, Integer> entry : productQuantity.entrySet()) {
                    if (cart.getProductID().equals(entry.getKey())) {
                        cart.setQuantity(entry.getValue());
                        cartRepository.save(cart);
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

    //update 1 product quantity
//    public Cart updateCartByProductID(CartRequest request) {
//        try {
//            List<Cart> cartlist = cartRepository.findByaccountID(request.getAccountID());
//            Cart cart = cartRepository.findByproductID(String.valueOf(request.getProductQuantity().keySet());
//
//            cart.setQuantity(request.getProductQuantity());
//            cartRepository.save(cart);
//            return cart;
//        } catch (DataIntegrityViolationException e) {
//            // Catch DataIntegrityViolationException and rethrow as AppException
//            //e.getMostSpecificCause().getMessage()
//            throw new AppException(ErrorCode.EXECUTED_FAILED);
//        }
//    }

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
