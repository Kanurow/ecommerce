package com.rowland.engineering.ecommerce.controller;


import com.rowland.engineering.ecommerce.dto.*;
import com.rowland.engineering.ecommerce.model.Favourite;
import com.rowland.engineering.ecommerce.model.Product;
import com.rowland.engineering.ecommerce.model.PromoCode;
import com.rowland.engineering.ecommerce.model.User;
import com.rowland.engineering.ecommerce.security.CurrentUser;
import com.rowland.engineering.ecommerce.service.ProductService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping("/create")   // @RolesAllowed()    //  @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        Product product = productService.createProduct(productRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{productId}")
                .buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Product Created Successfully"));
    }


    @PostMapping("/mark/{productId}/{userId}")
    public ResponseEntity<ApiResponse> markProductAsFavourite(
            @PathVariable(value = "productId") Long productId,
//            @RequestBody FavouriteRequest favouriteRequest,
            @PathVariable(value = "userId") Long userId) {
        return new ResponseEntity<ApiResponse>(productService.markProductAsFavourite(productId,userId),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/unmark/{id}")
    public ResponseEntity<ApiResponse> unmarkProductAsFavourite(
            @PathVariable(value = "id") Long id,
            @CurrentUser User currentUser) {
        return new ResponseEntity<ApiResponse>(productService.unmark(id, currentUser),HttpStatus.ACCEPTED);
    }

    @GetMapping("/view")
    public List<Product> viewProducts() {
        return productService.viewProduct();
    }


    @GetMapping("/favourites")
    public List<Favourite> viewAllMarked() {
        return productService.viewMarked();
    }

    @GetMapping("/favourites/{userId}")
    public List<Favourite> getUserMarkedFavourites(
            @PathVariable(value = "userId") Long userId
    ) {
        return productService.getUserFavourites(userId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(
            @PathVariable(value = "id") Long id,
            @CurrentUser User currentUser) {
        return new ResponseEntity<ApiResponse>(productService.deleteProduct(id, currentUser),HttpStatus.OK);
    }


    @PostMapping("/addPromoCode")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createPromo(@Valid @RequestBody PromoCodeRequest promoCodeRequest) {
        PromoCode promo = productService.createPromo(promoCodeRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{promoId}")
                .buildAndExpand(promo.getId()).toUri();
        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Promo Created Successfully"));
    }

    @GetMapping("/getPromoCodes")   // @RolesAllowed()    //  @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PromoCode> getAllPromo() {
        return productService.getAllPromo();
    }


}
