package com.cursos.api.springsecuritycourse.config.security;

import com.cursos.api.springsecuritycourse.config.security.filter.JwtAuthenticationFilter;
import com.cursos.api.springsecuritycourse.config.security.handler.CustomAccessDeniedHandler;
import com.cursos.api.springsecuritycourse.config.security.handler.CustomAuthenticationEntryPoint;
import com.cursos.api.springsecuritycourse.persistence.util.RoleEnum;
import com.cursos.api.springsecuritycourse.persistence.util.RolePermissionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true) // habilita la seguridad a nivel de metodos y me permite usar las anotaciones @PreAuthorize y @PostAuthorize
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private AuthorizationManager<RequestAuthorizationContext> authorizationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // .authorizeHttpRequests(HttpSecurityConfig::buildRequestMatchers) // Configuración basada en coincidencias de rutas HTTP, usaré otra forma
                .authorizeHttpRequests(authReqConfig -> {
                    authReqConfig.anyRequest().access(authorizationManager);
                })
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(customAuthenticationEntryPoint);
                    ex.accessDeniedHandler(customAccessDeniedHandler);
                })
                .build();
    }

    @Description("Configuración de autorizaciones por roles y authorities , basada en coincidencias de rutas HTTP")
    private static void buildRequestMatchers(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        /*Autorizacion Por Role (SE MANEJA PREFIJO _ROLE) de productos por solicitudes HTTP*/
        authReqConfig.requestMatchers(HttpMethod.GET, "/products")
                .hasAnyRole(
                        RoleEnum.ADMINISTRATOR.name(),
                        RoleEnum.ASSISTANT_ADMINISTRATOR.name());

        /*authReqConfig.requestMatchers(HttpMethod.GET, "/products/{productId}")
                .hasAnyRole(
                        Role.ADMINISTRATOR.name(),
                        Role.ASSISTANT_ADMINISTRATOR.name());*/

        /** coincidencias usando Expresiones regulares */
        authReqConfig.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/products/[0-9]*"))
                .hasAnyRole(
                        RoleEnum.ADMINISTRATOR.name(),
                        RoleEnum.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/products")
                .hasRole(RoleEnum.ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}")
                .hasAnyRole(
                        RoleEnum.ADMINISTRATOR.name(),
                        RoleEnum.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}/disable")
                .hasRole(RoleEnum.ADMINISTRATOR.name());

        /*Autorizacion por Authority de categorias por solicitudes HTTP*/

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories")
                .hasAuthority(RolePermissionEnum.READ_ALL_CATEGORIES.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories/{categoryId}")
                .hasAuthority(RolePermissionEnum.READ_ONE_CATEGORIES.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/categories")
                .hasAuthority(RolePermissionEnum.CREATE_ONE_CATEGORIES.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}")
                .hasAuthority(RolePermissionEnum.UPDATE_ONE_CATEGORIES.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disable")
                .hasAuthority(RolePermissionEnum.DISABLE_ONE_CATEGORIES.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/auth/profile")
                .hasAuthority(RolePermissionEnum.READ_MY_PROFILE.name());

        /*Autorizacion metodos publicos*/
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate").permitAll();

        authReqConfig.anyRequest().authenticated();
    }

    @Description("Configuración de autorizaciones por roles y authorities , basada en coincidencias de rutas HTTP")
    private static void buildRequestMatchersV2(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {

        /*Autorizacion metodos publicos*/
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate").permitAll();

        authReqConfig.anyRequest().authenticated();
    }
}