package com.cursos.api.springsecuritycourse.service.auth;

import com.cursos.api.springsecuritycourse.dto.RegisteredUser;
import com.cursos.api.springsecuritycourse.dto.SaveUser;
import com.cursos.api.springsecuritycourse.dto.auth.AuthRequest;
import com.cursos.api.springsecuritycourse.dto.auth.AuthResponse;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.persistence.entity.User;
import com.cursos.api.springsecuritycourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RegisteredUser registerCustomer(SaveUser newUser) {
        User user = userService.registerCustomer(newUser);
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setId(user.getId());
        registeredUser.setName(user.getName());
        registeredUser.setUsername(user.getUsername());
        registeredUser.setRole(user.getRole().name());

        String jwt = this.jwtService.generateToken(user, generateExtraClaims(user));
        registeredUser.setJwt(jwt);

        return registeredUser;
    }

    public AuthResponse login(AuthRequest authRequest) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()
        );

        // busca un proveedor que resulva UsernamePasswordAuthenticationToken y
        // DaoAuthenticationProvider que configure en SecurityBeansInjector lo resuelve., que ademas se configuro
        // en httpSecurityConfig y busca en la base de datos el usuario por metodo del metodo loadUserByUsername
        // en si este es el proceso de autenticacion
        this.authenticationManager.authenticate(authentication); // si no se encuentra el usuario lanza una excepcion

        // busco detalles, la auth la realice en el paso anterior
        UserDetails user = this.userService.findByUserName(authRequest.getUsername()).get();
        String jwt = this.jwtService.generateToken(user, generateExtraClaims((User) user));

        AuthResponse authResponse =  new AuthResponse();
        authResponse.setJwt(jwt);

        return authResponse;
    }

    public boolean validateToken(String jwt) {
        try {
            this.jwtService.extractUsername(jwt);
            return true;
        }catch (Exception e){
            System.out.println("error::" + e);
            return false;
        }
    }

    public User findLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // se que siempre sera un UsernamePasswordAuthenticationToken a no ser que maneje varios tipos de autenticacion
        if(auth instanceof UsernamePasswordAuthenticationToken authToken ){
             String username = (String) authToken.getPrincipal();
             return userService.findByUserName(username)
                     .orElseThrow(() -> new ObjectNotFoundException("User not found"));
        }
        return null;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name",user.getName());
        extraClaims.put("role",user.getRole());
        extraClaims.put("authorities",user.getAuthorities());
        return extraClaims;
    }

}
