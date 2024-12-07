package com.gerson.projectpath_pro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gerson.projectpath_pro.auth.controller.TokenResponse;
import com.gerson.projectpath_pro.auth.service.AuthService;

@Component
public class TestAuthUtil {

    @Autowired
    private AuthService authService;

    /**
     * This method uses the {@code AuthService} {@code register(RegisterRequest registerRequest)} method 
     * to create a user, save it in the database, generate its tokens and save them in the database too.
     * @return a {@code JWT Token} that can be used to put in the Authorization
     *         Header for tests.
     */
    public String generateTestJwtToken() {
        TokenResponse testTokenResponse = authService.register(TestDataUtil.createTestRegisterRequestA());

        String testJwtToken = testTokenResponse.accessToken();

        return testJwtToken;
    }

}
