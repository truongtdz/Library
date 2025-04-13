package com.build.core_restful.controller;

import com.build.core_restful.domain.User;
import com.build.core_restful.domain.request.LoginRequest;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.util.JwtUtil;
import com.build.core_restful.domain.response.LoginResponse;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.exception.NewException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Value("${core.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil createToken;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          JwtUtil createToken, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.createToken = createToken;
        this.userService = userService;
    }

    @PostMapping("/login")
    @AddMessage("api login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest login){

        // Nạp username and password
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

        // Xác thực người dùng
        Authentication auth = authenticationManagerBuilder.getObject().authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(auth);

        User userDB = this.userService.getUserByEmail(login.getEmail());
        LoginResponse loginResponse = LoginResponse.builder()
                .user(LoginResponse.UserLoginResponse.builder()
                        .id(userDB.getId())
                        .name(userDB.getFullName())
                        .email(userDB.getEmail())
                        .role(userDB.getRole())
                        .build())
                .build();

        String accessToken = createToken.createAccessToken(auth.getName(), loginResponse.getUser());
        loginResponse.setAccessToken(accessToken);

        String refreshToken = createToken.createRefreshToken(userDB.getEmail(), loginResponse.getUser());
        userService.updateUserByEmail(userDB.getEmail(), refreshToken);

        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }

    @GetMapping("/account")
    public ResponseEntity<Object> account(){
        String email = JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "";

        User userDB = this.userService.getUserByEmail(email);
        LoginResponse.UserLoginResponse userLoginResponse = LoginResponse.UserLoginResponse.builder()
                        .id(userDB.getId())
                        .name(userDB.getFullName())
                        .email(userDB.getEmail())
                        .build();

        return ResponseEntity.ok()
                .body(userLoginResponse);
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> getUserByRefreshToken(@CookieValue(name = "refresh_token", defaultValue = "not") String refreshToken){
        // Check Valid
        Jwt decodeToken = this.createToken.checkValidRefreshToken(refreshToken);

        String email = decodeToken.getSubject();

        // Create access token
        LoginResponse loginResponse = new LoginResponse();

        User userDB = this.userService.getUserByEmail(email);
        LoginResponse.UserLoginResponse userLoginResponse = LoginResponse.UserLoginResponse.builder()
                .id(userDB.getId())
                .name(userDB.getFullName())
                .email(userDB.getEmail())
                .build();

        loginResponse.setAccessToken(createToken.createAccessToken(email, userLoginResponse));
        loginResponse.setUser(userLoginResponse);

        // Create refresh token
        String newRefreshToken = this.createToken.createRefreshToken(email, userLoginResponse);

        // Update refresh token
        this.userService.updateUserByEmail(email, refreshToken);

        // Set Cookies
        ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        String email = JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "";

        this.userService.updateUserByEmail(email, null);

        // Set Cookies
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(null);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest registerUser){
        if(userService.existUserByEmail(registerUser.getEmail())){
            throw new NewException("Người dùng email : " + registerUser.getEmail() + " đã tồn tại ");
        }
        UserResponse user = this.userService.createUser(registerUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
