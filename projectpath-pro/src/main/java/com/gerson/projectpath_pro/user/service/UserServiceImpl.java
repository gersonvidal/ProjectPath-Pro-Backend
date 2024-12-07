package com.gerson.projectpath_pro.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerson.projectpath_pro.user.repository.User;
import com.gerson.projectpath_pro.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    public UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
