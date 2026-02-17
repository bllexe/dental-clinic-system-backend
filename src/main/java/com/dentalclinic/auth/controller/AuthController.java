package com.dentalclinic.auth.controller;

import com.dentalclinic.auth.dto.JwtResponse;
import com.dentalclinic.auth.dto.LoginRequest;
import com.dentalclinic.auth.dto.MessageResponse;
import com.dentalclinic.auth.dto.SignupRequest;
import com.dentalclinic.auth.dto.TokenRefreshRequest;
import com.dentalclinic.auth.dto.TokenRefreshResponse;
import com.dentalclinic.auth.entity.RefreshToken;
import com.dentalclinic.auth.exception.TokenRefreshException;
import com.dentalclinic.auth.service.RefreshTokenService;
import com.dentalclinic.security.jwt.JwtUtils;
import com.dentalclinic.security.services.UserDetailsImpl;
import com.dentalclinic.user.entity.User;
import com.dentalclinic.user.enums.Role;
import com.dentalclinic.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final JwtUtils jwtUtils;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);
    
    // Delete existing refresh tokens for user before creating a new one
    refreshTokenService.deleteByUserId(userDetails.getId());
    
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
                         refreshToken.getToken(),
                         userDetails.getId(),
                         userDetails.getUsername(),
                         userDetails.getEmail(),
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User();
    user.setFirstName(signUpRequest.getFirstName());
    user.setLastName(signUpRequest.getLastName());
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(encoder.encode(signUpRequest.getPassword()));
    user.setPhoneNumber(signUpRequest.getPhoneNumber());
    
    Role role = signUpRequest.getRole();
    if (role == null) {
        user.setRole(Role.PATIENT); // Default role
    } else {
        user.setRole(role);
    }

    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getEmail());
          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }
  
  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userDetails.getId();
    refreshTokenService.deleteByUserId(userId);
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  }
}
