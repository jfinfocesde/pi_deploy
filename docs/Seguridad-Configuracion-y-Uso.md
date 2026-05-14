# Guía Completa de Configuración y Uso de Spring Security

Esta guía detalla la implementación de seguridad en el proyecto, incluyendo el código fuente de cada componente y su explicación técnica.

---

## 1. Configuración de Dependencias (`pom.xml`)
Para habilitar la seguridad, se incluyó el starter oficial de Spring Boot.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

---

## 2. Modelo de Persistencia: Entidad `Usuario`
Creamos una entidad para almacenar los usuarios en la base de datos real.

**Ubicación:** `src/main/java/com/example/demo_basic/model/entity/Usuario.java`

```java
@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Se almacena encriptada con BCrypt

    @Column(nullable = false)
    private String role; // Ejemplo: ROLE_USER, ROLE_ADMIN
}
```

---

## 3. Servicio de Autenticación: `JpaUserDetailsService`
Esta clase es la encargada de conectar Spring Security con nuestra base de datos.

**Ubicación:** `src/main/java/com/example/demo_basic/service/JpaUserDetailsService.java`

```java
@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole()))
        );
    }
}
```

### Resumen de Permisos por Rol
Se ha implementado una jerarquía de permisos para proteger la integridad de los datos:

| Ruta / Método | Rol `USER` | Rol `ADMIN` |
| :--- | :---: | :---: |
| `GET /api/**` (Ver datos) | ✅ Permitido | ✅ Permitido |
| `POST /api/**` (Crear) | ❌ Denegado | ✅ Permitido |
| `PUT /api/**` (Editar) | ❌ Denegado | ✅ Permitido |
| `DELETE /api/**` (Borrar) | ❌ Denegado | ✅ Permitido |

---

## 5. Configuración Central: `SecurityConfig`
Aquí se definen las reglas de acceso, el cifrado de contraseñas y la configuración de CORS.

**Ubicación:** `src/main/java/com/example/demo_basic/config/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitado para APIs REST (stateless)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/scalar/**", "/").permitAll()
                // Configuración de autorización por métodos HTTP
                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                .anyRequest().permitAll()
            )
            .cors(Customizer.withDefaults()) // Configuración de CORS
            .httpBasic(Customizer.withDefaults()); // Habilita Basic Auth

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Permite cualquier origen (Frontend)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Algoritmo de encriptación seguro
    }
}
```

---

## 5. Inicialización de Datos: `DataLoader`
Para asegurar que el sistema no inicie vacío, creamos usuarios por defecto en la base de datos.

```java
if (usuarioRepository.count() == 0) {
    Usuario admin = Usuario.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .role("ROLE_ADMIN")
            .build();
    usuarioRepository.save(admin);
}
```

---

## 6. Cómo realizar peticiones
Para consumir la API, debes enviar las credenciales en la cabecera `Authorization`.

**Ejemplo cURL:**
```bash
curl -u admin:admin123 http://localhost:8080/api/adoptantes
```

**Ejemplo Postman:**
1. Selecciona la pestaña **Authorization**.
2. Tipo: **Basic Auth**.
3. Ingresa `admin` y `admin123`.

---
*Configuración implementada por Antigravity AI.*
