package com.gerson.projectpath_pro.auth.controller;

import java.time.LocalDate;

public record RegisterRequest(
    String fullName, 
    String username, 
    String email, 
    LocalDate dateOfBirth, 
    String password
) {

}
