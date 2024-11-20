package com.gerson.projectpath_pro.auth.controller;

public record LoginRequest(
    String email, 
    String password
) {

}
