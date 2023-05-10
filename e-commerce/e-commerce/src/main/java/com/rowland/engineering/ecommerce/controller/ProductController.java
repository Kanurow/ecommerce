package com.rowland.engineering.ecommerce.controller;


import com.rowland.engineering.ecommerce.dto.ApiResponse;
import com.rowland.engineering.ecommerce.dto.FavouriteRequest;
import com.rowland.engineering.ecommerce.dto.ProductRequest;
import com.rowland.engineering.ecommerce.dto.ProductResponse;
import com.rowland.engineering.ecommerce.model.Favourite;
import com.rowland.engineering.ecommerce.model.Product;
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


    @PostMapping("/mark")
    public ResponseEntity<ApiResponse> markProductAsFavourite(
            @RequestBody FavouriteRequest favouriteRequest,
            @CurrentUser User currentUser) {
        return new ResponseEntity<ApiResponse>(productService.markProductAsFavourite(favouriteRequest, currentUser),HttpStatus.ACCEPTED);
    }

    @GetMapping("/view")
    public List<Product> viewProducts() {
        return productService.viewProduct();
    }

    @GetMapping("/favourites")
    public List<Favourite> viewAllMarked(@CurrentUser User currentUser) {
        return productService.viewMarked(currentUser);
    }
    @GetMapping("/favourites1")
    public List<Favourite> viewAllMarked1(@CurrentUser User currentUser) {
        return productService.viewMarked1(currentUser);
    }
    @GetMapping("/favourites2")
    public List<Favourite> viewAllMarked2(@CurrentUser User currentUser) {
        return productService.viewMarked2(currentUser);
    }
    @GetMapping("/favourites3")
    public List<Favourite> viewAllMarked3(@CurrentUser User currentUser) {
        return productService.viewMarked4(currentUser);
    }


}
