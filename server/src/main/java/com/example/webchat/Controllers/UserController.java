package com.example.webchat.Controllers;

import com.example.webchat.DTOs.UserDTO;
import com.example.webchat.Models.User;
import com.example.webchat.DTOs.UserDTO;
import com.example.webchat.Repositories.UserRepository;
import com.example.webchat.Services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private String token = null;
    private void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return token;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(@RequestBody UserDTO userRequest)
    {
        if(userRequest.getToken().equals(this.token)) {
            return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/findbyusername/{username}")
    public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable String username, @RequestBody UserDTO userRequest)
    {
        if(userRequest.getToken().equals(this.token)) {
            return new ResponseEntity<Optional<User>>(userService.getUserByUsername(username), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<Optional<User>> getUserByEmail(@PathVariable String email, @RequestBody UserDTO userRequest)
    {
        if(userRequest.getToken().equals(this.token)) {
            return new ResponseEntity<Optional<User>>(userService.getUserByEmail(email), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/search/{username}")
    public ResponseEntity<List<User>> searchUsers(@PathVariable String username, @RequestBody UserDTO userRequest) {
        if(userRequest.getToken().equals(this.token)) {
            List<User> matchingUsers = userService.searchUsersByUsername(username);
            return new ResponseEntity<>(matchingUsers, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
//    @CrossOrigin(origins = "http://localhost:3000")
//    @PostMapping("/create")
//    public ResponseEntity<User> createUser(@RequestBody UserDTO userRequest) {
//        try {
//            String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
//            return new ResponseEntity<>(userService.createUser(userRequest.getEmail(), userRequest.getUsername(), hashedPassword), HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userRequest) {
        try {
            String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
            return new ResponseEntity<>(userService.createUser(userRequest.getEmail(), userRequest.getUsername(), hashedPassword), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userRequest, HttpServletResponse response) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        int expirationTime = 3600000;
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String role = user.getRole();
                SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
                String token = Jwts.builder()
                        .claim("username", username)
                        .claim("role", role)
                        .signWith(SignatureAlgorithm.HS512, secretKey)
                        .compact();
//                Cookie cookie = new Cookie("session_data", token);
//                cookie.setPath("/");
//                cookie.setHttpOnly(true);
//                cookie.setMaxAge(expirationTime);
//                response.addCookie(cookie);
                setToken(token);
                return new ResponseEntity<>(token, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        setToken(null);
        return new ResponseEntity<>("User logged out", HttpStatus.OK);
    }
}
