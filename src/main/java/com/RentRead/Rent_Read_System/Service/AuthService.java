package com.RentRead.Rent_Read_System.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.RentRead.Rent_Read_System.Repository.UserRepository;
import com.RentRead.Rent_Read_System.Exchange.RegisterRequest;
import com.RentRead.Rent_Read_System.DTO.Role;
import com.RentRead.Rent_Read_System.Exchange.AuthRequest;
import com.RentRead.Rent_Read_System.Exchange.AuthResponse;
import com.RentRead.Rent_Read_System.Model.User;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;    

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request){
        if(request.getRole() == null)request.setRole(Role.USER);
        String roleStr = request.getRole().toString().toUpperCase();
        try{
            Role role = Role.valueOf(roleStr);
            request.setRole(role);
        }catch(Exception e){
            request.setRole(Role.USER);
        }
        User user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();
        userRepository.save(user);
        return AuthResponse.builder().build();
    }

    public AuthResponse login(AuthRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return AuthResponse.builder().build();
    }
}
