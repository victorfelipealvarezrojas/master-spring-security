package com.cursos.api.springsecuritycourse.config.security.filter;

import com.cursos.api.springsecuritycourse.persistence.entity.User;
import com.cursos.api.springsecuritycourse.service.UserService;
import com.cursos.api.springsecuritycourse.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// OncePerRequestFilter implementa GenericFilterBean
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    /**
     * Este metodo se ejecuta por cada request que llega al servidor
     * y s eencarga de validar el token y setear el objeto Authentication
     * en el SecurityContextHolder y es parte del proceso de autenticacion
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1. Obtener Headers http Authorization
        String authorizationHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        //2. Obtener Token desde el Header
        String jwt = authorizationHeader.split(" ")[1];

        //3. Obtener el subject/username desde el token, validar token
        String username = jwtService.extractUsername(jwt);

        //4. setea obj Authentication dentro del security context holder
        User userDetails = userService.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username, null, userDetails.getAuthorities()
        );

        // WebAuthenticationDetails me permite setear valores inmportantes del request
        authentication.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //5. Ejecutar resto de filtros
        filterChain.doFilter(request, response);
    }
}
