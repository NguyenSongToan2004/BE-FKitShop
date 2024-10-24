package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Entity.Question;
import com.group4.FKitShop.Entity.Wishlist;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Repository.WishlistRepository;
import com.group4.FKitShop.Request.WishlistRequest;
import com.group4.FKitShop.Response.QuestionResponse;
import com.group4.FKitShop.Response.WishlistResponse;
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
public class WishlistService {

    WishlistRepository wishlistRepository;
    AccountsService accountsService;
    ProductRepository productRepository;


    public List<WishlistResponse> allWishlists(){
        List<WishlistResponse> responses = new ArrayList<>();
        List<Wishlist> wishlists = wishlistRepository.findAll();
        for (Wishlist wishlist : wishlists) {
            WishlistResponse wishlistResponse = new WishlistResponse();
            String customerName = (accountsService.getAccountByID(wishlist.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            Product product = productRepository.findById(wishlist.getProductID()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
            wishlistResponse.setWishlist(wishlist);
            wishlistResponse.setCustomerName(customerName);
            wishlistResponse.setProducts(product);
            responses.add(wishlistResponse);
        }
        return responses;
    }

    public WishlistResponse getWishlistByID(int id){
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WISHLIST_NOTFOUND));
        WishlistResponse wishlistResponse = new WishlistResponse();
        String customerName = (accountsService.getAccountByID(wishlist.getAccountID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
        Product product = productRepository.findById(wishlist.getProductID()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        wishlistResponse.setWishlist(wishlist);
        wishlistResponse.setCustomerName(customerName);
        wishlistResponse.setProducts(product);
        return wishlistResponse;
    }

    public List<WishlistResponse> getWishlistByAccountID(String id){
        List<WishlistResponse> responses = new ArrayList<>();
        List<Wishlist> wishlists = wishlistRepository.getWishlistByAccountID(id);
        for (Wishlist wishlist : wishlists) {
            WishlistResponse wishlistResponse = new WishlistResponse();
            String customerName = (accountsService.getAccountByID(wishlist.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            Product product = productRepository.findById(wishlist.getProductID()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
            wishlistResponse.setWishlist(wishlist);
            wishlistResponse.setCustomerName(customerName);
            wishlistResponse.setProducts(product);
            responses.add(wishlistResponse);
        }
        return responses;
    }

    public List<WishlistResponse> getWishlistByProductID(String id){
        List<WishlistResponse> responses = new ArrayList<>();
        List<Wishlist> wishlists = wishlistRepository.getWishlistByProductID(id);
        for (Wishlist wishlist : wishlists) {
            WishlistResponse wishlistResponse = new WishlistResponse();
            String customerName = (accountsService.getAccountByID(wishlist.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            Product product = productRepository.findById(wishlist.getProductID()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
            wishlistResponse.setWishlist(wishlist);
            wishlistResponse.setCustomerName(customerName);
            wishlistResponse.setProducts(product);
            responses.add(wishlistResponse);
        }
        return responses;
    }

    public WishlistResponse createWishlist(WishlistRequest request) {
       if(wishlistRepository.checkWishlistByAccountIDAndProductID(request.getAccountID(), request.getProductID()) != null){
           throw new AppException(ErrorCode.WISHLIST_EXIST);
       }
        Wishlist wishlist = new Wishlist();
        wishlist.setAccountID(request.getAccountID());
        wishlist.setProductID(request.getProductID());
        wishlistRepository.save(wishlist);

        WishlistResponse wishlistResponse = new WishlistResponse();
        String customerName = (accountsService.getAccountByID(wishlist.getAccountID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
        Product product = productRepository.findById(wishlist.getProductID()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        wishlistResponse.setWishlist(wishlist);
        wishlistResponse.setCustomerName(customerName);
        wishlistResponse.setProducts(product);
        return wishlistResponse;
    }

    public WishlistResponse updateWish(int id, WishlistRequest request){
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.WISHLIST_NOTFOUND));
        wishlist.setAccountID(request.getAccountID());
        wishlist.setProductID(request.getProductID());
        wishlistRepository.save(wishlist);

        WishlistResponse wishlistResponse = new WishlistResponse();
        String customerName = (accountsService.getAccountByID(wishlist.getAccountID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
        Product product = productRepository.findById(wishlist.getProductID()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        wishlistResponse.setWishlist(wishlist);
        wishlistResponse.setCustomerName(customerName);
        wishlistResponse.setProducts(product);
        return wishlistResponse;
    }

    @Transactional
    public void deleteWishlist(int id) {
        if (!wishlistRepository.existsById(id))
            throw new AppException(ErrorCode.WISHLIST_NOTFOUND);
        wishlistRepository.deleteById(id);
    }


}
