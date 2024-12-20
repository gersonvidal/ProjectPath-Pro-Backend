package com.gerson.projectpath_pro.user.service;

import java.util.Optional;

import com.gerson.projectpath_pro.user.repository.User;

public interface UserService {

    Optional<User> findByEmail(String email);

}
