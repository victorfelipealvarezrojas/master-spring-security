package com.cursos.api.springsecuritycourse.config.security;

import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.persistence.repository.UserRepository;
import com.cursos.api.springsecuritycourse.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @AuthenticationManager       :: responsable de procesar solicitudes de autenticación.
 *
 * @AuthenticationConfiguration :: proporciona un AuthenticationManager.
 *                                 y permite acceder a la configuración de autenticación.
 */
@Configuration
public class SecurityBeansInjector {

    @Deprecated(since = "la configuracion de UserDetailsService se implementa en la capa de servicio")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * @AuthenticationConfiguration :: proporciona un AuthenticationManager.
                                       y permite acceder a la configuración de autenticación.
     */
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;


    /**
     * @AuthenticationManager       :: responsable de procesar solicitudes de autenticación.
                                       y permite acceder a la configuración de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // obtiene el AuthenticationManager
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationStrategy = new DaoAuthenticationProvider();
        authenticationStrategy.setPasswordEncoder(passwordEncoder);
        authenticationStrategy.setUserDetailsService(userService);

        // impl desde esta misma clase modificando el nombre del metodo de loadUserByUsername a userDetailsService
        // authenticationStrategy.setUserDetailsService(userDetailsService());

        return authenticationStrategy;
    }

    /*******************************************************************************************
     * @UserDetailsService :: es una interface tiene un metodo llamado loadUserByUsername
     * @loadUserByUsername :: es un metodo que permite cargar un usuario basado en su nombre
     * *****************************************************************************************
     * la implementacion que sigue es una forma de implementar la interface UserDetailsService
     * el cual permite sobre escribir el metodo loadUserByUsername y ademas cambiar el nombre de
     * loadUserByUsername a userDetailsService o como quiera llamarle.
     * ...preferi implementar en la capa de servicio y extender de UserDetailsService
     * y sobre escribir el metodo loadUserByUsername en ella, y esto es una alternativa
     * que solo me permite tener mas control sobre el nombre que da por defecto spring security
     *****************************************************************************************
     * *****************************************************************************************
     */
    @Deprecated(since = "la configuracion de UserDetailsService se implementa en la capa de servicio")
    public UserDetailsService userDetailsServiceV2() {
        // utilizo una expresion lambda, si devolviera una instancia tendria que anotarlo como @beans
        return (username) -> {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new ObjectNotFoundException("User not found with username " + username));
        };
    }

    // otra opcion es impelmentarla en la capa de userServiceImpl y extender de UserDetailsService
    // y sobre escribir el metodo loadUserByUsername, pero esto me permite implementar mi propio
    // nombre de metodo
    @Deprecated(since = "la configuracion de UserDetailsService se implementa en la capa de servicio")
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // username se obtiene de la solicitud
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new ObjectNotFoundException("User not found with username " + username));
            }
        };
    }
}
