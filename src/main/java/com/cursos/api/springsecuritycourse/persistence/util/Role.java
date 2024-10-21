package com.cursos.api.springsecuritycourse.persistence.util;

import java.util.List;

public enum Role {
    ROLE_ADMINISTRATOR(List.of(
            // permisos sobre productos
            RolePermission.READ_ALL_PRODUCTS,
            RolePermission.READ_ONE_PRODUCTS,
            RolePermission.CREATE_ONE_PRODUCTS,
            RolePermission.UPDATE_ONE_PRODUCTS,
            RolePermission.DISABLE_ONE_PRODUCTS,
            // permisos sobre categorias
            RolePermission.READ_ALL_CATEGORIES,
            RolePermission.READ_ONE_CATEGORIES,
            RolePermission.CREATE_ONE_CATEGORIES,
            RolePermission.UPDATE_ONE_CATEGORIES,
            RolePermission.DISABLE_ONE_CATEGORIES,
            // permisos sobre mi perfil
            RolePermission.READ_MY_PROFILE
    )),
    ROLE_ASSISTANT_ADMINISTRATOR(List.of(
            // permisos sobre productos
            RolePermission.READ_ALL_PRODUCTS,
            RolePermission.READ_ONE_PRODUCTS,
            RolePermission.UPDATE_ONE_PRODUCTS,
            // permisos sobre categorias
            RolePermission.READ_ALL_CATEGORIES,
            RolePermission.READ_ONE_CATEGORIES,
            RolePermission.UPDATE_ONE_CATEGORIES,
            // permisos sobre mi perfil
            RolePermission.READ_MY_PROFILE
    )),
    ROLE_CUSTOMER(List.of(
            // permisos sobre mi perfil
            RolePermission.READ_MY_PROFILE
    ));

    private  List<RolePermission> permissions;

    Role(List<RolePermission> permissions) {
        this.permissions = permissions;
    }

    public List<RolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RolePermission> permissions) {
        this.permissions = permissions;
    }
}
