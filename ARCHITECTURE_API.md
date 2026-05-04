# Arquitectura Backend - API Películas por Ver

## 1. Descripción del Proyecto

**Nombre:** Películas por Ver

**Descripción:** API REST desarrollada con Spring Boot que permite a los usuarios gestionar una colección personal de películas. Incluye funciones de autenticación segura mediante JWT, carga de portadas en Cloudinary, comentarios personales por película y un sistema de reacciones que permite expresar sentimientos sobre películas vistas.

**Stack Tecnológico:**
- Backend: Spring Boot 3.x
- Base de Datos: PostgreSQL (Aiven)
- Almacenamiento de Imágenes: Cloudinary
- Autenticación: JWT (JSON Web Tokens)
- ORM: Spring Data JPA / Hibernate
- Despliegue: Render

---

## 2. Patrones de Diseño Implementados

| Patrón | Archivo(s) | Clase Principal | Propósito | Por Qué Se Usó |
|--------|-----------|-----------------|----------|---|
| **MVC - Controller** | `controllers/AuthController.java`<br>`controllers/PeliculaController.java`<br>`controllers/PeliculaReaccionController.java` | `AuthController`<br>`PeliculaController`<br>`PeliculaReaccionController` | Recibe solicitudes HTTP del frontend y delega la lógica al servicio correspondiente. Mantiene la separación entre interfaz y lógica de negocio. | Estructura estándar en Spring. Facilita el mantenimiento y evita que el frontend acceda directamente a la BD. |
| **Repository Pattern** | `repository/PeliculaRepository.java`<br>`repository/PeliculaReaccionRepository.java`<br>`repository/UserRepository.java`<br>`repository/RoleRepository.java` | Interfaces que extienden `JpaRepository` | Abstrae el acceso a datos. Spring Data JPA genera automáticamente las consultas a partir del nombre de los métodos. | Reduce código boilerplate de SQL. Facilita testing y cambio de base de datos. |
| **Service Layer** | `service/CloudinaryService.java`<br>`service/PeliculaReaccionService.java`<br>`security/services/UserDetailsServiceImpl.java` | `CloudinaryService`<br>`PeliculaReaccionService`<br>`UserDetailsServiceImpl` | Centraliza la lógica de negocio y comunicación con servicios externos. Los controladores delegan al servicio. | Evita lógica redundante. Fácil de testear y mantener. Permite reutilización en diferentes endpoints. |
| **DTO - Data Transfer Object** | `payload/request/LoginRequest.java`<br>`payload/request/SignupRequest.java`<br>`payload/request/ComentarioPersonalRequest.java`<br>`payload/response/JwtResponse.java`<br>`payload/response/MessageResponse.java`<br>`payload/response/PeliculaReaccionResponse.java`<br>`payload/response/PeliculaEnReaccionResponse.java`<br>`payload/response/ReaccionResumenResponse.java` | Clases DTO (request/response) | Transportan datos entre frontend y API sin exponer directamente las entidades de BD. Validan datos de entrada. | Proporciona control sobre qué datos entran y salen del sistema. Mejora seguridad (no expone IDs internos ni campos sensibles). |
| **Factory Method** | `factory/ReaccionResumenFactory.java` | `ReaccionResumenFactory` | Centraliza la creación de objetos de respuesta relacionados con reacciones. | Evita repetir lógica de construcción de DTOs en servicios y controladores. Facilita cambios en la estructura de respuestas. |
| **Dependency Injection** | En todos los controladores, servicios y configuraciones | `@Autowired` / Constructores | Spring inyecta automáticamente las dependencias necesarias. | Reduce acoplamiento entre clases. Facilita testing con mocks. Simplifica la gestión de dependencias. |
| **Security Filter** | `security/jwt/AuthTokenFilter.java` | `AuthTokenFilter` | Intercepta solicitudes HTTP para validar tokens JWT antes de que accedan a endpoints protegidos. | Centraliza la lógica de autenticación. Protege recursos sin modificar cada controlador. |

---

## 3. Funcionalidades Principales

### A. Autenticación y Autorización

**Endpoints:**
- `POST /api/auth/signin` - Login de usuario
- `POST /api/auth/signup` - Registro de usuario

**Flujo:**
1. El usuario envía credenciales via `LoginRequest` (DTO).
2. `AuthController` delega a `AuthenticationManager` para validar.
3. Si es válido, `JwtUtils` genera un JWT y se devuelve en `JwtResponse`.
4. El token JWT se incluye en futuras solicitudes con el header `Authorization: Bearer <token>`.
5. `AuthTokenFilter` intercepta cada solicitud, valida el JWT y autentica al usuario en Spring Security.

**Componentes Involucrados:**
- Patrón MVC: `AuthController`
- Patrón DTO: `LoginRequest`, `SignupRequest`, `JwtResponse`
- Patrón Service Layer: `UserDetailsServiceImpl`
- Patrón Security Filter: `AuthTokenFilter`
- Patrón Repository: `UserRepository`, `RoleRepository`

---

### B. Gestión de Películas

**Endpoints:**
- `POST /api/peliculas` - Crear película con portada (solo admin)
- `GET /api/peliculas` - Listar películas
- `GET /api/peliculas/{id}` - Obtener película
- `PUT /api/peliculas/{id}` - Actualizar película (solo admin)
- `DELETE /api/peliculas/{id}` - Eliminar película (solo admin)

**Flujo:**
1. `PeliculaController` recibe solicitud HTTP.
2. Si incluye imagen, `CloudinaryService` la carga en Cloudinary y retorna la URL.
3. `PeliculaRepository` accede a la BD para persistir o recuperar datos.
4. La respuesta se envía al frontend (generalmente la entidad `Pelicula` directamente).

**Componentes Involucrados:**
- Patrón MVC: `PeliculaController`
- Patrón Service Layer: `CloudinaryService`
- Patrón Repository: `PeliculaRepository`
- Dependency Injection: Inyección de dependencias en constructor

---

### C. Comentarios Personales en Películas

**Funcionalidad:**
Cada usuario puede agregar un comentario de texto personal para cada película.

**Endpoint:**
- `PATCH /api/peliculas/{id}/comentario` - Actualizar comentario personal

**Flujo:**
1. El usuario envía el comentario via `ComentarioPersonalRequest` (DTO).
2. `PeliculaReaccionController` delega a `PeliculaReaccionService`.
3. El servicio obtiene la película, valida que el usuario existe y le autorización.
4. Se actualiza el campo `comentarioPersonal` de la película en BD.
5. Se retorna la película actualizada al frontend.

**Componentes Involucrados:**
- Patrón MVC: `PeliculaReaccionController`
- Patrón DTO: `ComentarioPersonalRequest`, `PeliculaEnReaccionResponse`
- Patrón Service Layer: `PeliculaReaccionService`
- Patrón Repository: `PeliculaReaccionRepository`, `PeliculaRepository`, `UserRepository`
- Patrón Factory Method: `ReaccionResumenFactory`

---

### D. Sistema de Reacciones

**Qué son las reacciones:**
Las reacciones permiten a los usuarios expresar sentimientos sobre películas. Tipos disponibles:
- Me gusta
- No me gusta
- Me encanta
- Me aburrió
- Me hizo reír
- Me sorprendió

**Funcionalidades:**
1. **Crear/actualizar reacción:** El usuario selecciona una reacción para una película.
2. **Ver reacciones de una película:** Resumen estadístico de todas las reacciones de otros usuarios.
3. **Colección automática:** Los comentarios y películas reaccionadas se agrupan automáticamente en vistas especiales.

**Endpoints:**
- `GET /api/peliculas/{id}/reacciones` - Obtener resumen de reacciones
- `POST /api/reacciones` - Crear reacción (no documentado en patrón, pero existe)

**Flujo (Crear/Actualizar Reacción):**
1. Usuario selecciona una reacción en una película.
2. `PeliculaReaccionController` recibe solicitud.
3. `PeliculaReaccionService` verifica:
   - Que la película existe
   - Que el usuario existe y está autenticado
   - Si ya existe una reacción previa, la reemplaza
4. Se persiste en tabla `pelicula_reaccion`.
5. Se retorna `ReaccionResumenResponse` con estadísticas.

**Flujo (Ver Reacciones):**
1. `PeliculaReaccionController` recibe solicitud para obtener reacciones de película.
2. `PeliculaReaccionService` consulta todas las reacciones de esa película desde BD.
3. Agrupa por tipo de reacción y cuenta.
4. `ReaccionResumenFactory` construye los DTOs de respuesta.
5. Se retorna lista de `ReaccionResumenResponse`.

**Componentes Involucrados:**
- Patrón MVC: `PeliculaReaccionController`
- Patrón Service Layer: `PeliculaReaccionService`
- Patrón Repository: `PeliculaReaccionRepository`, `PeliculaRepository`, `UserRepository`
- Patrón DTO: `ReaccionResumenResponse`, `PeliculaReaccionResponse`, `PeliculaEnReaccionResponse`
- Patrón Factory Method: `ReaccionResumenFactory`
- Dependency Injection: Inyección en constructor

---

## 4. Decisiones de Arquitectura

### ¿Por Qué No Se Agregaron Patrones Innecesarios?

1. **No usamos Observer Pattern:** Las reacciones no requieren notificaciones en tiempo real a otros usuarios. Un sistema de eventos sería overhead innecesario.

2. **No usamos Mediator Pattern:** La interacción entre componentes es sencilla. Un mediador central complicaría más de lo que beneficiaría.

3. **No usamos Strategy Pattern:** La lógica de validación y procesamiento es consistente. No hay múltiples estrategias intercambiables.

4. **No usamos Decorator Pattern:** Los DTOs son suficientemente flexibles. Decoradores agregarían complejidad sin beneficio tangible.

5. **No usamos Builder Pattern:** Los DTOs son simples objetos de transferencia. Constructores estándar son suficientes.

**Principio Aplicado:** Solo se implementan patrones cuando resuelven un problema específico o mejoran significativamente la mantenibilidad, no por principio arquitectónico genérico.

---

## 5. Nota Final

Los patrones de diseño documentados en esta API se aplicaron en **lugares específicos donde aportan:**

✓ **Separación de responsabilidades:** Cada componente (Controller, Service, Repository) tiene un rol claro.

✓ **Mantenimiento facilicitado:** Cambios en reglas de negocio se hacen en un único lugar (Service/Repository).

✓ **Reutilización de código:** Servicios y Repositories se comparten entre múltiples controllers sin duplicación.

✓ **Testing mejorado:** Dependency Injection permite inyectar mocks fácilmente en tests unitarios.

✓ **Escalabilidad:** La estructura permite agregar nuevas entidades, servicios y endpoints sin refactoring masivo.

✓ **Seguridad:** DTOs y Security Filter proporcionan capas de validación y protección.

La arquitectura es **pragmática y enfocada en resolver los requisitos del negocio** sin sobre-ingeniería innecesaria.

---

**Última Actualización:** 3 de mayo de 2026
