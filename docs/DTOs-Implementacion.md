# Implementación de DTOs (Data Transfer Objects)

Este documento describe la integración y el uso del patrón **DTO** en el sistema. El objetivo es separar la capa de persistencia (Base de Datos) de la capa de presentación (API), mejorando la seguridad, el rendimiento y la mantenibilidad del código.

---

## 1. Concepto y Objetivos

El uso de **DTOs** permite definir estructuras de datos específicas para la comunicación externa, evitando la exposición directa de las **Entidades** de JPA.

### Objetivos Clave:
1.  **Seguridad**: Evitar la exposición de campos sensibles o internos (contraseñas, versiones de auditoría, IDs de sistema).
2.  **Optimización**: Reducir el tamaño del JSON enviado al cliente, incluyendo únicamente los campos requeridos por la interfaz.
3.  **Integridad**: Prevenir la modificación accidental de campos de solo lectura o internos mediante el uso de objetos de creación específicos.
4.  **Estabilidad**: El contrato de la API (JSON) se mantiene estable aunque la estructura de la base de datos cambie internamente.

---

## 2. Estrategias de Conversión Aplicadas

Se han implementado dos metodologías de mapeo para demostrar flexibilidad y control:

### A. Mapeo Manual
Se realiza mediante lógica explícita en la capa de servicio. Utilizado en `MascotaService` y `AdoptanteService`.
*   **Control**: Permite una personalización total campo por campo.
*   **Uso**: Recomendado para estructuras de datos simples o con lógica de transformación específica.

### B. Mapeo Automático (MapStruct)
Se utiliza la librería MapStruct para generar el código de conversión durante la compilación. Utilizado en `SolicitudService` a través de la interfaz `SolicitudMapper`.
*   **Productividad**: Automatiza el mapeo de objetos anidados y colecciones.
*   **Mantenibilidad**: Reduce el código repetitivo (*boilerplate*) en servicios con múltiples relaciones.

---

## 3. Comparativa: Entidades vs DTOs

Para validar los beneficios, se puede comparar la estructura de respuesta en el modelo de **Solicitud de Adopción**:

### Modelo Basado en Entidades (Sin DTO)
La respuesta incluye metadatos de auditoría interna y estructuras redundantes.
```json
{
  "id": 1,
  "fechaCreacion": "2024-03-20T10:00:00",
  "fechaModificacion": "2024-03-20T10:00:00",
  "estado": "PENDIENTE",
  "mascota": {
    "id": 5,
    "nombre": "Firulais",
    "especie": "Perro",
    "fechaCreacion": "2024-01-10T08:00:00"
  },
  "adoptante": {
    "id": 10,
    "nombre": "Juan Perez",
    "identificacion": "123456789",
    "fechaModificacion": "2024-02-15T09:00:00"
  }
}
```

### Modelo Basado en DTOs (Optimizado)
JSON limpio y estructurado para el consumo directo por parte del cliente.
```json
{
  "id": 1,
  "estado": "PENDIENTE",
  "mascota": {
    "id": 5,
    "nombre": "Firulais",
    "especie": "Perro"
  },
  "adoptante": {
    "id": 10,
    "nombre": "Juan Perez"
  }
}
```

---

## 4. Guía de Pruebas (Laboratorio)

Se han habilitado endpoints específicos para comparar ambas implementaciones en tiempo real:

1.  **Mascotas (Entidad)**: `GET /api/demo/mascotas-entidad`
2.  **Mascotas (DTO)**: `GET /api/demo/mascotas-dto`
3.  **Solicitudes (Entidad)**: `GET /api/demo/solicitudes-entidad`
4.  **Solicitudes (DTO)**: `GET /api/demo/solicitudes-dto`

---

## 5. Protocolo de Uso de la API

Con la integración de DTOs, las operaciones de creación y actualización se han simplificado:

### Registro de Mascota (`POST /api/mascotas`)
Solo se requieren los campos de negocio básicos.
```json
{
  "nombre": "Luna",
  "especie": "Gato",
  "edad": 2,
  "tamano": "PEQUENO",
  "estado": "DISPONIBLE"
}
```

### Creación de Solicitud de Adopción (`POST /api/solicitudes`)
Se utilizan IDs directos para establecer las relaciones, eliminando la necesidad de enviar objetos completos.
```json
{
  "mascotaId": 1,
  "adoptanteId": 1
}
```

---

## 6. Datos Iniciales (Seed)

La aplicación cuenta con un componente `DataLoader` que inserta automáticamente **5 mascotas** y **4 adoptantes** si la base de datos se encuentra vacía al iniciar. Esto permite realizar pruebas inmediatas de los endpoints comparativos sin configuración manual previa.

---

## Conclusión Técnica
*   **Capa JPA**: Exclusiva para persistencia y reglas de base de datos.
*   **Capa DTO**: Exclusiva para el contrato del API y transferencia de información.
*   **Conversión**: Gestionada en el Service mediante métodos manuales o mappers automáticos.
