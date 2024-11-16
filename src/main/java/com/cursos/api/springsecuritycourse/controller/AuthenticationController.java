package com.cursos.api.springsecuritycourse.controller;

import com.cursos.api.springsecuritycourse.dto.auth.AuthRequest;
import com.cursos.api.springsecuritycourse.dto.auth.AuthResponse;
import com.cursos.api.springsecuritycourse.persistence.entity.User;
import com.cursos.api.springsecuritycourse.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PreAuthorize("permitAll()")
    @GetMapping("validate")
    public ResponseEntity<Boolean> validate(@RequestParam String jwt){
        boolean isTokenValid = this.authenticationService.validateToken(jwt);
        return ResponseEntity.ok(isTokenValid);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest){
        AuthResponse response =  this.authenticationService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ASSISTANT_ADMINISTRATOR')")
    // Accede al securityContextHolder
    @GetMapping("profile")
    public ResponseEntity<User> findMyProfile(){
        User user = authenticationService.findLoggedInUser();
        return ResponseEntity.ok(user);
    }
}