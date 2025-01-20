package com.RentRead.Rent_Read_System.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.RentRead.Rent_Read_System.Exchange.AuthResponse;
import com.RentRead.Rent_Read_System.Exception.ResourceNotFoundException;
import com.RentRead.Rent_Read_System.Exchange.AdminUserResponse;
import com.RentRead.Rent_Read_System.Exchange.AuthRequest;
import com.RentRead.Rent_Read_System.Exchange.RegisterRequest;
import com.RentRead.Rent_Read_System.Model.User;
import com.RentRead.Rent_Read_System.Repository.UserRepository;
import com.RentRead.Rent_Read_System.Service.AuthService;
import com.RentRead.Rent_Read_System.Service.BookService;
import com.RentRead.Rent_Read_System.Service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    // @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> randomMethod() {
        return ResponseEntity.ok().body("Hello world!");
    }

    @GetMapping()
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok().body("Welcome new user! Kindly register and then you are set to begin reading!");
    }

    @GetMapping("/admin/user/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserResponse> seeUsersBooks(@PathVariable String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            logger.error("No user found!");
            throw new ResourceNotFoundException("User doesn't exist!");
        }
        AdminUserResponse books = bookService.findUsersBooks(user);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/admin/book/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserResponse> seeBooksUsers(@PathVariable String title){
        AdminUserResponse books = bookService.findBooksUsers(title);
        return ResponseEntity.ok().body(books);
    }
}
