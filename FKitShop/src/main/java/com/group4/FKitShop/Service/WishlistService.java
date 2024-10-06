package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Feedback;
import com.group4.FKitShop.Entity.Wishlist;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Repository.WishlistRepository;
import com.group4.FKitShop.Request.FeedbackRequest;
import com.group4.FKitShop.Request.WishlistRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistService {

    WishlistRepository wishlistRepository;

    public List<Wishlist> allWishlists(){
        return wishlistRepository.findAll();
    }

    public Wishlist getWishlistByID(int id){
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.Wishlist_NOTFOUND));
        return wishlist;
    }

    public Wishlist createWishlist(WishlistRequest request) {
       if(wishlistRepository.checkWishlistByAccountIDAndProductID(request.getAccountID(), request.getProductID()) != null){
           throw new AppException(ErrorCode.Wishlist_EXIST);
       }
        Wishlist wishlist = new Wishlist();
        wishlist.setAccountID(request.getAccountID());
        wishlist.setProductID(request.getProductID());
        return wishlistRepository.save(wishlist);
    }

    public Wishlist updateWish(int id, WishlistRequest request){
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.Wishlist_NOTFOUND));
        wishlist.setAccountID(request.getAccountID());
        wishlist.setProductID(request.getProductID());
        return wishlistRepository.save(wishlist);
    }

    @Transactional
    public void deleteWishlist(int id) {
        if (!wishlistRepository.existsById(id))
            throw new AppException(ErrorCode.Wishlist_NOTFOUND);
        wishlistRepository.deleteById(id);
    }

    public List<Wishlist> getWishlistByAccountID(String id){
        return wishlistRepository.getWishlistByAccountID(id);
    }

    public List<Wishlist> getWishlistByProductID(String id){
        return wishlistRepository.getWishlistByProductID(id);
    }
}
