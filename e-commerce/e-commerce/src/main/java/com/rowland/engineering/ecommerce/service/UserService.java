package com.rowland.engineering.ecommerce.service;

import com.rowland.engineering.ecommerce.dto.DepositRequest;
import com.rowland.engineering.ecommerce.dto.TransferRequest;
import com.rowland.engineering.ecommerce.dto.UpdateUserRequest;
import com.rowland.engineering.ecommerce.exception.BadRequestException;
import com.rowland.engineering.ecommerce.model.Favourite;

import com.rowland.engineering.ecommerce.model.User;
import com.rowland.engineering.ecommerce.repository.FavouriteRepository;
import com.rowland.engineering.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
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

    @Transactional
    public void makeTransfer(TransferRequest transferRequest, Long senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UsernameNotFoundException("Sender user not found"));

        User receiver = userRepository.findByEmailOrAccountNumber(transferRequest.getEmailOrAccountNumber(), transferRequest.getEmailOrAccountNumber());
        if (receiver.equals(null)) {
            throw new UsernameNotFoundException("Receiver user not found");
        }

        if (transferRequest.getTransferAmount() > sender.getAccountBalance()) {
            throw new BadRequestException("Insufficient balance to complete transfer");
        }

        double transferAmount = transferRequest.getTransferAmount();

        double senderBalance = sender.getAccountBalance();
        sender.setAccountBalance(senderBalance - transferAmount);

        double receiverBalance = receiver.getAccountBalance();
        receiver.setAccountBalance(receiverBalance + transferAmount);

        userRepository.save(sender);
        userRepository.save(receiver);
    }


//    @Transactional
//    public void makeTransfer(TransferRequest transferRequest, Long senderId) {
//        User userAccount = userRepository.getReferenceById(senderId);
//        User senderInstance = new User(userAccount.getId(), userAccount.getUsername(), userAccount.getName(), userAccount.getEmail(), userAccount.getPassword(), userAccount.getMobile(), userAccount.getVoucherBalance(), userAccount.getRoles(), userAccount.getAuthorities());
//
//        User receiverAccount = userRepository.findByEmailOrAccountNumber(transferRequest.getEmailOrAccountNumber(), transferRequest.getEmailOrAccountNumber());
//        User receiverInstance = new User(receiverAccount.getId(), receiverAccount.getUsername(), receiverAccount.getName(), receiverAccount.getEmail(), receiverAccount.getPassword(), receiverAccount.getMobile(), receiverAccount.getVoucherBalance(), receiverAccount.getRoles(), receiverAccount.getAuthorities());
//
//        if (transferRequest.getTransferAmount() > senderInstance.getAccountBalance() ) {
//            throw new BadRequestException("Insufficient Balance to complete transfer!");
//        }
//        senderInstance.setAccountBalance(senderInstance.getAccountBalance() - transferRequest.getTransferAmount());
//        receiverInstance.setAccountBalance(receiverAccount.getAccountBalance() + transferRequest.getTransferAmount());
//
//        userRepository.save(sender);
//        userRepository.save(receiver);
//
//    }
}
