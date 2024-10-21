package com.cursos.api.springsecuritycourse.service.impl;

import com.cursos.api.springsecuritycourse.dto.SaveUser;
import com.cursos.api.springsecuritycourse.exception.InvalidPasswordException;
import com.cursos.api.springsecuritycourse.persistence.entity.User;
import com.cursos.api.springsecuritycourse.persistence.repository.UserRepository;
import com.cursos.api.springsecuritycourse.persistence.util.Role;
import com.cursos.api.springsecuritycourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


/**
 * Implementación de las interfaces UserService y UserDetailsService.
 * Este servicio maneja operaciones relacionadas con usuarios, como registro,
 * búsqueda de usuarios y autenticación para Spring Security.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario cliente en el sistema.
     *
     * @param newUser El DTO que contiene la información del nuevo usuario.
     * @return La entidad User creada.
     */
    @Override
    public User registerCustomer(SaveUser newUser) {
        validatePassword(newUser);
        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setName(newUser.getName());
        user.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        user.setRole(Role.ROLE_CUSTOMER);
        return this.userRepository.save(user);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario a buscar.
     * @return Un Optional conteniendo el User si se encuentra, o vacío si no se encuentra.
     */
    @Override
    public Optional<User> findByUserName(String username) {
        return this.userRepository.findByUsername(username);
    }

    /**
     * Valida la contraseña proporcionada durante el registro del usuario.
     *
     * @param newUser El DTO que contiene la información de registro del usuario.
     * @throws InvalidPasswordException Si la contraseña está vacía o no coincide con la contraseña repetida.
     */
    private void validatePassword(SaveUser newUser) {
        if (!StringUtils.hasText(newUser.getPassword())
                || !StringUtils.hasText(newUser.getRepeatedPassword())) {
            throw new InvalidPasswordException("La contraseña no puede estar vacía");
        }

        if (!newUser.getPassword().equals(newUser.getRepeatedPassword())) {
            throw new InvalidPasswordException("Las contraseñas no coinciden");
        }
    }

    /**
     * Carga los detalles del usuario para propósitos de autenticación.
     * Este método es utilizado por Spring Security para la autenticación de usuarios.
     *
     * @param username El nombre de usuario del usuario a cargar.
     * @return Objeto UserDetails conteniendo la información de autenticación del usuario.
     * @throws UsernameNotFoundException Si no se encuentra ningún usuario con el nombre de usuario dado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre de usuario: " + username));
    }
}