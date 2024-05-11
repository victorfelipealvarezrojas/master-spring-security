package com.cursos.api.springsecuritycourse.config.security;

import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // me permite usar los beans
public class SecurityBeansInjector {

    @Autowired
    private UserRepository userRepository; // podria haber creado un servicio pero no

    // me permite construir el AuthenticationManager y es proveida e inyectada por spring security
    // Autowired
    //  AuthenticationConfiguration _authenticationConfiguration

    // tipo de implementacion del AuthenticationManager basica,
    // es el encargado de realizar la autenticacion
    // authenticationManager( AuthenticationConfiguration authenticationConfiguration ) otra forma y elimino @Autowired
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //devulevo implementacion predeterminada
        //solo tiene un metodo authenticate (solo tiene el "Principal y password" al momento de intentar autenticarse),
        // y les aplicara una estrategia
        return authenticationConfiguration.getAuthenticationManager(); // implementacion mas comun
    }

    // tipo de autenticacion
    @Bean
    public AuthenticationProvider authenticationProvider() {
        System.out.println("authenticationProvider::::call");
        // estrategia de autenticacion
        // DaoAuthenticationProvider se extiende de AuthenticationProvider(estrategia)
        DaoAuthenticationProvider authenticationStrategy = new DaoAuthenticationProvider();
        authenticationStrategy.setPasswordEncoder(passwordEncoder());
        authenticationStrategy.setUserDetailsService(userDetailsService());
        // aqui la estrategia tiene todos sus mecanismos para el proceso
        return authenticationStrategy;
    }

    // esta interface permite codificar, comparar password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // esta relacionado con UserServiceImpl que implementa esta misma interface
    public UserDetailsService userDetailsServiceV2() {
        // utilizo una expresion lambda, si devolviera una instancia tendria que anotarlo como @beans
        return (username) -> {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new ObjectNotFoundException("User not found with username " + username));
        };
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // username se obtiene de la solicitud
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new ObjectNotFoundException("User not found with username " + username));
            };
        };
    }
}
