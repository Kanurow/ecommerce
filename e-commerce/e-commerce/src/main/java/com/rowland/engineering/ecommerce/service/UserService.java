package com.rowland.engineering.ecommerce.service;

import com.rowland.engineering.ecommerce.dto.DepositRequest;
import com.rowland.engineering.ecommerce.dto.RegisterRequest;
import com.rowland.engineering.ecommerce.dto.UpdateUserRequest;
import com.rowland.engineering.ecommerce.exception.BadRequestException;
import com.rowland.engineering.ecommerce.model.Favourite;
import com.rowland.engineering.ecommerce.model.Product;
import com.rowland.engineering.ecommerce.model.User;
import com.rowland.engineering.ecommerce.repository.FavouriteRepository;
import com.rowland.engineering.ecommerce.repository.ProductRepository;
import com.rowland.engineering.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final FavouriteRepository favouriteRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUserById(UpdateUserRequest update, Long userId) {
        return userRepository.findById(userId).map(
                user -> {
                    user.setUsername(update.getUsername());
                    user.setName(update.getName());
                    user.setEmail(update.getEmail());
                    user.setMobile(update.getMobile());
                    user.setVoucherBalance(user.getVoucherBalance());
                    user.setAccountBalance(user.getAccountBalance());
                    user.setPassword(user.getPassword());
                    return userRepository.save(user);
                }
        );
    }

    public void deleteUserById(Long id) {
        List<Favourite> userFavouriteIds = favouriteRepository.findAllByUserId(id);
        favouriteRepository.deleteAll(userFavouriteIds);

        Optional<User> userId = userRepository.findById(id);
        userRepository.deleteById(id);
    }

    public void makeDeposit(DepositRequest depositRequest,Long userId) {

        Optional<User> userAccount = userRepository.findById(userId);
        System.out.println(userAccount);
        if (depositRequest.getDepositAmount() < 1) {
            throw new BadRequestException("Sorry! You cannot deposit a negative amount or zero");
        }
        userAccount.map(
                user -> {
                    user.setUsername(user.getUsername());
                    user.setName(user.getName());
                    user.setEmail(user.getEmail());
                    user.setMobile(user.getMobile());
                    user.setVoucherBalance(user.getVoucherBalance());
                    user.setAccountBalance(user.getAccountBalance() + depositRequest.getDepositAmount());
                    user.setPassword(user.getPassword());
                    return userRepository.save(user);
                }
        );

    }
}
