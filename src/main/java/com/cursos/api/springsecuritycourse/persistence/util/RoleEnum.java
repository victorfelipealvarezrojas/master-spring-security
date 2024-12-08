package com.cursos.api.springsecuritycourse.persistence.util;

import java.util.List;

public enum RoleEnum {
    ADMINISTRATOR(List.of(
            // permisos sobre productos
            RolePermissionEnum.READ_ALL_PRODUCTS,
            RolePermissionEnum.READ_ONE_PRODUCTS,
            RolePermissionEnum.CREATE_ONE_PRODUCTS,
            RolePermissionEnum.UPDATE_ONE_PRODUCTS,
            RolePermissionEnum.DISABLE_ONE_PRODUCTS,
            // permisos sobre categorias
            RolePermissionEnum.READ_ALL_CATEGORIES,
            RolePermissionEnum.READ_ONE_CATEGORIES,
            RolePermissionEnum.CREATE_ONE_CATEGORIES,
            RolePermissionEnum.UPDATE_ONE_CATEGORIES,
            RolePermissionEnum.DISABLE_ONE_CATEGORIES,
            // permisos sobre mi perfil
            RolePermissionEnum.READ_MY_PROFILE
    )),
    ASSISTANT_ADMINISTRATOR(List.of(
            // permisos sobre productos
            RolePermissionEnum.READ_ALL_PRODUCTS,
            RolePermissionEnum.READ_ONE_PRODUCTS,
            RolePermissionEnum.UPDATE_ONE_PRODUCTS,
            // permisos sobre categorias
            RolePermissionEnum.READ_ALL_CATEGORIES,
            RolePermissionEnum.READ_ONE_CATEGORIES,
            RolePermissionEnum.UPDATE_ONE_CATEGORIES,
            // permisos sobre mi perfil
            RolePermissionEnum.READ_MY_PROFILE
    )),
    CUSTOMER(List.of(
            // permisos sobre mi perfil
            RolePermissionEnum.READ_MY_PROFILE
    ));

    private  List<RolePermissionEnum> permissions;

    RoleEnum(List<RolePermissionEnum> permissions) {
        this.permissions = permissions;
    }

    public List<RolePermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RolePermissionEnum> permissions) {
        this.permissions = permissions;
    }
}
