package com.gerson.projectpath_pro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gerson.projectpath_pro.auth.repository.Token;
import com.gerson.projectpath_pro.auth.repository.TokenRepository;
import com.gerson.projectpath_pro.auth.service.JwtService;
import com.gerson.projectpath_pro.user.User;
import com.gerson.projectpath_pro.user.UserRepository;

@Component
public class TestAuthUtil {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This method simulates the original workflow of the register endpoint.
     * It means that, in this method we create a user and save it in the database.
     * Then, we generate his jwtToken and then, we save it in the database too.
     * @return a {@code JWT Token} that can be used to put in the Authorization Header for tests.
     */
    public String generateTestJwtToken() {
        User savedTestUser = saveTestUser();

        String testJwtToken = jwtService.generateToken(savedTestUser);

        // Save in db
        saveTestToken(savedTestUser, testJwtToken);

        return testJwtToken;
    }

    private User saveTestUser() {
        User testUser = TestDataUtil.createTestUserA(passwordEncoder);

        return userRepository.save(testUser);
    }

    private void saveTestToken(User testUser, String testJwtToken) {
        var testToken = Token.builder()
                .user(testUser)
                .token(testJwtToken)
                .tokenType(Token.TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();

        tokenRepository.save(testToken);
    }
}
