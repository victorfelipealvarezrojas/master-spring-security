# Spring Security Configuration Diagram

```mermaid
---
title: Configuración de Spring Security
---
graph TD
    subgraph "HttpSecurityConfig"
        HC["HttpSecurityConfig<br/>@Configuration<br/>@EnableWebSecurity"]
        SFC["SecurityFilterChain<br/>(Bean)"]
        
        subgraph "Security Configurations"
            CSRF["CSRF Disabled"]
            SESS["Session Management<br/>STATELESS"]
            AUTH["Authentication Provider"]
            JWT["JWT Authentication Filter"]
        end
        
        subgraph "Request Authorization"
            PUB["Public Endpoints<br/>POST /customers<br/>POST /auth/authenticate<br/>GET /auth/validate"]
            PRIV["Private Endpoints<br/>All other routes"]
        end
    end
    
    %% Relaciones
    HC --> SFC
    SFC --> CSRF
    SFC --> SESS
    SFC --> AUTH
    SFC --> JWT
    SFC --> PUB
    SFC --> PRIV
    
    %% Componentes Externos
    DAP[("DaoAuthenticationProvider<br/>(Autowired)")]
    JAF[("JwtAuthenticationFilter<br/>(Autowired)")]
    
    %% Conexiones
    AUTH --> DAP
    JWT --> JAF
    
    style HC fill:#f9f,stroke:#333,stroke-width:2px
    style SFC fill:#bbf,stroke:#333,stroke-width:2px
    style DAP fill:#fcf,stroke:#333,stroke-width:2px
    style JAF fill:#fcf,stroke:#333,stroke-width:2px
```

## Descripción del Diagrama

El diagrama muestra la configuración principal de Spring Security con los siguientes componentes:

### Componentes Principales
1. **HttpSecurityConfig**: Clase de configuración principal
2. **SecurityFilterChain**: Bean que configura la cadena de filtros

### Configuraciones de Seguridad
- CSRF deshabilitado
- Manejo de sesión STATELESS
- Proveedor de autenticación
- Filtro JWT

### Autorización de Peticiones
- **Endpoints Públicos**:
    - POST /customers
    - POST /auth/authenticate
    - GET /auth/validate
- **Endpoints Privados**: Todas las demás rutas

### Componentes Externos
- DaoAuthenticationProvider (Autowired)
- JwtAuthenticationFilter (Autowired)