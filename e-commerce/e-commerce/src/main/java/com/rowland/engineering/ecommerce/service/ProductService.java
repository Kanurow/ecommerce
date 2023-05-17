package com.rowland.engineering.ecommerce.service;

import com.rowland.engineering.ecommerce.dto.*;
import com.rowland.engineering.ecommerce.exception.BadRequestException;
import com.rowland.engineering.ecommerce.exception.ResourceNotFoundException;
import com.rowland.engineering.ecommerce.model.Favourite;
import com.rowland.engineering.ecommerce.model.Product;
import com.rowland.engineering.ecommerce.model.PromoCode;
import com.rowland.engineering.ecommerce.model.User;
import com.rowland.engineering.ecommerce.repository.FavouriteRepository;
import com.rowland.engineering.ecommerce.repository.ProductRepository;
import com.rowland.engineering.ecommerce.repository.PromoCodeRepository;
import com.rowland.engineering.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;
    private final PromoCodeRepository promoCodeRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);


    public Product createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        return productRepository.save(product);
    }



    public ApiResponse markProductAsFavourite(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        System.out.println(product);

        User user = userRepository.getReferenceById(userId);

        System.out.println(user);
        Favourite favourite = new Favourite();
        favourite.setProduct(product);
        favourite.setUser(user);

        System.out.println(favourite + "FAVOURITE");
        try {
            favourite = favouriteRepository.save(favourite);
        } catch (DataIntegrityViolationException ex) {
            logger.info("{} has already been marked product",  product.getProductName());
            throw new BadRequestException("Sorry! You have already marked this product");
        }
        return new ApiResponse(true, "Favourite Selected");
    }


    public List<Product> viewProduct() {
        return productRepository.findAll();

    }

    public List<Favourite> viewMarked() {
        List<Favourite> fav = favouriteRepository.findAll();
        return fav;
    }

    public ApiResponse deleteProduct(Long id, User currentUser) {
        productRepository.deleteById(id);
        return new ApiResponse(true, "Product with id "+id+ " Deleted by user with id "+ currentUser.getId());
    }

    public ApiResponse unmark(Long id, User currentUser) {
        favouriteRepository.deleteById(id);
        return new ApiResponse(true, "Favourite Unmarked");
    }

    public PromoCode createPromo(PromoCodeRequest promoCodeRequest) {
        PromoCode promo = new PromoCode();
        promo.setCode(promoCodeRequest.getCode());
        promo.setPromoAmount(promoCodeRequest.getPromoAmount());
        return promoCodeRepository.save(promo);
    }

    public List<PromoCode> getAllPromo() {
        return promoCodeRepository.findAll();
    }

    public List<Favourite> getUserFavourites(Long userId) {
        return favouriteRepository.findAllFavouriteByUserId(userId);
    }
}
