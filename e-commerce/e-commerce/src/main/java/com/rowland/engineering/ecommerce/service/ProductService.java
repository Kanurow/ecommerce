package com.rowland.engineering.ecommerce.service;

import com.rowland.engineering.ecommerce.dto.*;
import com.rowland.engineering.ecommerce.exception.BadRequestException;
import com.rowland.engineering.ecommerce.exception.InsufficientFundException;
import com.rowland.engineering.ecommerce.exception.ResourceNotFoundException;
import com.rowland.engineering.ecommerce.model.*;
import com.rowland.engineering.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartCheckoutRepository cartCheckoutRepository;
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

        User user = userRepository.getReferenceById(userId);
        Favourite existingFavourite = favouriteRepository.findByProductAndUser(product, user);
        if (existingFavourite != null) {
            logger.info("{} has already been marked", product.getProductName());
            throw new BadRequestException("Sorry! You have already marked this product");
        }

        Favourite favourite = new Favourite();
        favourite.setProduct(product);
        favourite.setUser(user);

        try {
            favourite = favouriteRepository.save(favourite);
        } catch (DataIntegrityViolationException ex) {
            logger.info("{} has already been marked product",  product.getProductName());
            throw new BadRequestException("Sorry! You have already marked this product");
        }
        return new ApiResponse(true, "Favourite Selected");
    }


    public ApiResponse addToCart(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        User user = userRepository.getReferenceById(userId);
        ShoppingCart existingCartEntry = shoppingCartRepository.findByProductAndUser(product, user);
        if (existingCartEntry != null) {
            logger.info("{} has already been added to your shopping list", product.getProductName());
            throw new BadRequestException("Sorry! You have already added this product");
        }
        ShoppingCart cart = new ShoppingCart();
        cart.setProduct(product);
        cart.setUser(user);

        shoppingCartRepository.save(cart);
        return new ApiResponse(true, "Product Added to cart");
    }


    public List<Product> viewProduct() {
        return productRepository.findAll();

    }

    public List<Favourite> viewMarked() {
        List<Favourite> fav = favouriteRepository.findAll();
        return fav;
    }

//    public ApiResponse deleteProduct(Long id, User currentUser) {
//        List<Favourite> productIds = favouriteRepository.findAllByProductId(id);
//        favouriteRepository.deleteAll(productIds);
//        shoppingCartRepository.deleteByProductId(id);
//        productRepository.deleteById(id);
//        return new ApiResponse(true, "Product with id "+id+ " Deleted by user with id "+ currentUser.getId());
//    }

    public ApiResponse deleteProduct(Long id, User currentUser) {
        List<Favourite> productIds = favouriteRepository.findAllByProductId(id);
        favouriteRepository.deleteAll(productIds);

        // Delete the product from the shopping cart for the current user
        ShoppingCart shoppingCart = shoppingCartRepository.findByProductIdAndUserId(id, currentUser.getId());
        if (shoppingCart != null) {
            shoppingCartRepository.delete(shoppingCart);
        }

        productRepository.deleteById(id);
        return new ApiResponse(true, "Product with id " + id + " deleted by user with id " + currentUser.getId());
    }


public ApiResponse unmark(Long id, User currentUser) throws Exception {
    try {
        favouriteRepository.deleteById(id);
        return new ApiResponse(true, "Favourite Unmarked by user with id " + currentUser.getName());
    } catch (Exception ex) {
        throw new Exception("Error occurred while unmarking the favourite: " + ex.getMessage(), ex);
    }
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

    public List<ShoppingCart> getUserCart(Long userId) {
        return shoppingCartRepository.findAllByUserId(userId);
    }

    public ApiResponse removeFromCart(Long id) {
        shoppingCartRepository.deleteById(id);
        return new ApiResponse(true, "Item removed from cart");
    }



    public ApiResponse checkoutCart(CartCheckoutRequest checkoutRequest, Long userId) {
        User user = userRepository.getReferenceById(userId);
        User newUser = new User(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getPassword(), user.getMobile(),user.getAccountNumber(), user.getVoucherBalance(), user.getRoles(), user.getAuthorities());

        if (checkoutRequest.getTotal() > user.getAccountBalance()) {
            throw new InsufficientFundException(checkoutRequest.getTotal() - user.getAccountBalance());
        } else {
            newUser.setAccountBalance(user.getAccountBalance() - checkoutRequest.getTotal());
        }



        CartCheckout cartCheckout = new CartCheckout();
        cartCheckout.setOrderAddress(checkoutRequest.getOrderAddress());
        cartCheckout.setPrice(checkoutRequest.getTotal());
        cartCheckout.setQuantity(checkoutRequest.getQuantity());
        cartCheckout.setUserId(userId);

        // Create CartItem entities and populate the cart list
        List<CartCheckout.CartItem> cartItems = checkoutRequest.getCart().stream()
                .map(item -> {
                    CartCheckout.CartItem cartItem = new CartCheckout.CartItem();
                    cartItem.setProductName(item.getProductName());
                    cartItem.setPrice(item.getPrice());
                    cartItem.setQuantity(item.getQuantity());
                    cartItem.setSubtotal(item.getSubtotal());
                    return cartItem;
                })
                .collect(Collectors.toList());
        cartCheckout.setCart(cartItems);

        // Save the CartCheckout entity to the database
        cartCheckoutRepository.save(cartCheckout);
        userRepository.save(newUser);

        return new ApiResponse(true, "Checked Out");
    }

    public List<CartCheckout> getCheckedOutCart(Long id) {
        return cartCheckoutRepository.findByUserId(id);
    }
}
