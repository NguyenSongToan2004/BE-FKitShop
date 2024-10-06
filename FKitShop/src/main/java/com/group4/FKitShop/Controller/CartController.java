package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.CartRequest;
import com.group4.FKitShop.Service.CartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/carts")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @PostMapping("/create")
    public ResponseObject createCart(@RequestBody @Valid CartRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create Cart successfully")
                .data(cartService.createCart(request))
                .build();
    }

    @GetMapping("/view/{accountID}")
    public ResponseObject viewCart(@PathVariable String accountID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Cart List")
                .data(cartService.viewCartByAccountID(accountID))
                .build();
    }

    @DeleteMapping("/delete")
    public ResponseObject deleteCart(@RequestParam String accountID, @RequestParam String productID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Delete Cart successfully")
                .data(cartService.deleteCartByProductID(accountID, productID))
                .build();
    }

    @PutMapping()
    public ResponseObject updateCart(@RequestBody @Valid CartRequest request) {
        Map<String, Integer> cart = request.getProductQuantity();
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String productID = entry.getKey();
            Integer quantity = entry.getValue();
            cartService.updateAllQuantityCart(request.getAccountID(), request.getProductQuantity());
        }
        return ResponseObject.builder()
                .status(1000)
                .message("Update Cart successfully")
                .data(cart)
                .build();
    }

}
