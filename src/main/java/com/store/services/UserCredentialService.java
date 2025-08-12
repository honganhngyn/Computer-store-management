package com.store.services;

import com.store.models.UserCredential;
import com.store.repositories.UserCredentialRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;

    public UserCredentialService(UserCredentialRepository userCredentialRepository) {
        this.userCredentialRepository = userCredentialRepository;
    }

    // Đăng nhập
    public Optional<UserCredential> login(String username, String password) {
        return userCredentialRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    // Đăng ký
    public void register(UserCredential userCredential) {
        if (userCredentialRepository.findByUsername(userCredential.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Tài khoản đã tồn tại!");
        }
        userCredentialRepository.save(userCredential);
    }
}