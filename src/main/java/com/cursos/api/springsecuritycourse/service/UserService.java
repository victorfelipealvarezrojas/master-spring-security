package com.cursos.api.springsecuritycourse.service;

import com.cursos.api.springsecuritycourse.dto.UserDto;
import com.cursos.api.springsecuritycourse.persistence.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerCustomer(UserDto newUser);

    Optional<User> findByUserName(String username);
}
