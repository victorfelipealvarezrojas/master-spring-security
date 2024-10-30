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


# Diagrama de Flujo del Filtro JWT

```mermaid
graph TD
    subgraph "JWT Authentication Filter Flow"
        JAF2["JwtAuthenticationFilter<br/>@Component"]
        
        subgraph "Filter Process"
            REQ[/"Incoming HTTP Request"/]
            HEAD["1. Extract Bearer Token<br/>from Authorization Header"]
            TOKEN["2. Parse JWT Token"]
            USER["3. Extract Username<br/>from Token"]
            VALID["4. Find & Validate User"]
            AUTH2["5. Create Authentication Object"]
            CTX["6. Set Security Context"]
            NEXT["7. Continue Filter Chain"]
        end
    end
    
    REQ --> HEAD
    HEAD --> TOKEN
    TOKEN --> USER
    USER --> VALID
    VALID --> AUTH2
    AUTH2 --> CTX
    CTX --> NEXT
    
    JS["JwtService"]
    US["UserService"]
    
    USER --> JS
    VALID --> US

    style JAF2 fill:#bbf,stroke:#333,stroke-width:2px
    style JS fill:#fcf,stroke:#333,stroke-width:2px
    style US fill:#fcf,stroke:#333,stroke-width:2px
```

## Descripción del Flujo

### Componente Principal
- **JwtAuthenticationFilter**: Filtro que procesa y valida los tokens JWT

### Proceso de Filtrado
1. **Recepción de Request**: Intercepta la petición HTTP entrante
2. **Extracción de Token**: Obtiene el token Bearer del header Authorization
3. **Parsing de JWT**: Procesa el token JWT
4. **Extracción de Username**: Obtiene el username del token
5. **Validación de Usuario**: Busca y valida el usuario en el sistema
6. **Creación de Autenticación**: Genera el objeto de autenticación
7. **Configuración de Contexto**: Establece la autenticación en el SecurityContext

### Servicios Relacionados
- **JwtService**: Manejo y procesamiento de tokens JWT
- **UserService**: Gestión y validación de usuarios

### Notas Técnicas
- El filtro se ejecuta una vez por cada request
- Hereda de OncePerRequestFilter
- Integrado en la cadena de filtros de Spring Security