package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Cart;
import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.CartMapper;
import com.group4.FKitShop.Repository.CartRepository;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.CartRequest;
import com.group4.FKitShop.Response.CartResponse;
import com.group4.FKitShop.Response.ProductCartResponse;
import jakarta.transaction.Transactional;
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
    CartMapper cartMapper;

    public List<ProductCartResponse> responsesByAccountID(String accountID) {
        List<Cart> cartlist = cartRepository.findByaccountID(accountID);
        List<ProductCartResponse> cartResponses = new ArrayList<>();
        for (Cart cart : cartlist) {
            Product p = productRepository.findById(cart.getProductID())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
            ProductCartResponse productCartResponse = cartMapper.toProductCartResponse(cart);
            productCartResponse.setImage(p.getImages().get(0).getUrl());
            productCartResponse.setName(p.getName());
            productCartResponse.setPrice(p.getPrice());
            cartResponses.add(productCartResponse);
        }
        return cartResponses;
    }

    public CartResponse createCart(CartRequest cartRequest) {
        try {
            // Get the current cart by accountID
            List<Cart> currentCart = cartRepository.findByaccountID(cartRequest.getAccountID());


            Cart newcart = null;
            // Check if the cart is not empty
            if (!currentCart.isEmpty()) {
                for (Cart existingcart : currentCart) {
                    Product product = productRepository.findById(existingcart.getProductID())
                            .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOTFOUND));
                    if (cartRequest.getQuantity() > product.getQuantity())
                        throw new AppException(ErrorCode.PRODUCT_UNAVAILABLE);
                    // Check if the product is already in the existingcart
                    if (cartRequest.getProductID().equals(existingcart.getProductID())) {
                        // If the product exists, update its quantity
                        newcart = existingcart;
                        newcart.setQuantity(newcart.getQuantity() + cartRequest.getQuantity());
                        break;
                    }
                }
            }
            if (newcart == null) {
                newcart = Cart.builder()
                        .accountID(cartRequest.getAccountID())
                        .productID(cartRequest.getProductID())
                        .quantity(cartRequest.getQuantity())
                        .status("available")
                        .build();

            }
            cartRepository.save(newcart);
            List<ProductCartResponse> cartResponses = responsesByAccountID(cartRequest.getAccountID());
            return CartResponse.builder()
                    .accountID(cartRequest.getAccountID())
                    .products(cartResponses)
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

    public CartResponse viewCartByAccountID(String accountID) {
        try {
            List<ProductCartResponse> cartResponses = responsesByAccountID(accountID);
            return CartResponse.builder()
                    .accountID(accountID)
                    .products(cartResponses)
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

    //update cart quantity
    public CartResponse updateQuantityCart(String accountID, String productID, int requestQuantity) {
        try {
//            for (Cart cart : cartlist) {
//                int currentQuantity = cart.getQuantity();
//                Product product = productRepository.findById(cart.getProductID()).orElseThrow(
//                        () -> new AppException(ErrorCode.PRODUCT_NOTFOUND)
//                );
//                int availableProductQuantity = product.getQuantity();
//                if (requestQuantity > availableProductQuantity) {
//                    throw new AppException(ErrorCode.PRODUCT_UNAVAILABLE);
//                }
//                cart.setQuantity(requestQuantity);
//                cartRepository.save(cart);
//            }
            Optional<Cart> carttmp = cartRepository.findByaccountIDAndproductID(accountID, productID);
            if (carttmp.isEmpty())
                throw new AppException(ErrorCode.CART_NOTFOUND);

            Product product = productRepository.findById(carttmp.get().getProductID())
                    .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOTFOUND));

            if (requestQuantity > product.getQuantity())
                throw new AppException(ErrorCode.PRODUCT_UNAVAILABLE);

            carttmp.get().setQuantity(requestQuantity);
            cartRepository.save(carttmp.get());

            List<ProductCartResponse> cartResponses = responsesByAccountID(accountID);
            return CartResponse.builder()
                    .accountID(accountID)
                    .products(cartResponses)
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }

    @Transactional
    public CartResponse deleteCartByProductID(String accountID, String productID) {
        try {
            cartRepository.deletebyAccountIDAndProductID(accountID, productID);
            List<ProductCartResponse> cartlist = responsesByAccountID(accountID);
            return CartResponse.builder()
                    .accountID(accountID)
                    .products(cartlist)
                    .build();
        } catch (DataIntegrityViolationException e) {
            // Catch DataIntegrityViolationException and rethrow as AppException
            //e.getMostSpecificCause().getMessage()
            throw new AppException(ErrorCode.EXECUTED_FAILED);
        }
    }
}
