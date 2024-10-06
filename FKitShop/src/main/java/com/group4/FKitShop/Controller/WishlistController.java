package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.Feedback;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Entity.Wishlist;
import com.group4.FKitShop.Request.FeedbackRequest;
import com.group4.FKitShop.Request.WishlistRequest;
import com.group4.FKitShop.Service.WishlistService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {

    WishlistService wishlistService;

    // get all wish
    @GetMapping()
    public List<Wishlist> allWishlists() {
        return wishlistService.allWishlists();
    }


    // get wish by ID
    @GetMapping("/{wishlistID}")
    ResponseEntity<ResponseObject> getWishlistByID(@PathVariable int wishlistID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", wishlistService.getWishlistByID(wishlistID))
        );
    }

    // get list wish by productID
    @GetMapping("/byProductID/{productID}")
    ResponseEntity<ResponseObject> getWishlistByProductID(@PathVariable String productID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", wishlistService.getWishlistByProductID(productID))
        );
    }

    // get list wish by accountID
    @GetMapping("/byAccountID/{accountID}")
    ResponseEntity<ResponseObject> getWishlistByAccountID(@PathVariable String accountID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", wishlistService.getWishlistByAccountID(accountID))
        );
    }

    // create feedback
    @PostMapping()
    public ResponseObject createWishlist(@RequestBody WishlistRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create wishlist successfully")
                .data(wishlistService.createWishlist(request))
                .build();
    }

    @PutMapping("/{wishlistID}")
    public ResponseObject updateWishlist(@RequestBody WishlistRequest request, @PathVariable int wishlistID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Update wishlist successfully")
                .data(wishlistService.updateWish(wishlistID, request))
                .build();
    }

    // delete feedback
    @DeleteMapping("/{wishlistID}")
    public ResponseObject deleteWishlist(@PathVariable int wishlistID) {
        wishlistService.deleteWishlist(wishlistID);
        return ResponseObject.builder()
                .status(1000)
                .message("Delete feedback successfully")
                .build();
    }
}
