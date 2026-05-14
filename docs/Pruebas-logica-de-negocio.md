# Pruebas paso a paso (Lógica de Negocio) — Sistema de Adopción de Mascotas

Este documento explica cómo probar, de forma manual y paso a paso (para principiantes), toda la lógica de negocio implementada en el servicio de **Solicitudes**.

## 1) Requisitos antes de empezar

### 1.1 Tener la API corriendo

El proyecto está configurado en `src/main/resources/application.properties` para conectarse a PostgreSQL usando variables de entorno:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Ejemplo de valores típicos:

- `DB_URL=jdbc:postgresql://localhost:5432/demo_basic`
- `DB_USERNAME=postgres`
- `DB_PASSWORD=postgres`

Luego inicia la aplicación (desde la raíz del proyecto):

```bash
.\mvnw.cmd spring-boot:run
```

Cuando esté arriba, abre Swagger:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/api-docs`

### 1.2 Endpoints que vas a usar

Adoptantes:
- `POST /api/adoptantes`
- `GET /api/adoptantes/{id}`

Mascotas:
- `POST /api/mascotas`
- `GET /api/mascotas/{id}`
- `PUT /api/mascotas/{id}`

Solicitudes:
- `POST /api/solicitudes`
- `GET /api/solicitudes/{id}`
- `PATCH /api/solicitudes/{id}/estado/{estado}`

Notas importantes:
- Esta API usa entidades directamente (sin DTOs).
- Para crear una solicitud, lo más simple es enviar los IDs dentro de `mascota` y `adoptante` (no necesitas enviar el objeto completo):
  ```json
  {
    "mascota": { "id": 1 },
    "adoptante": { "id": 2 }
  }
  ```
- En los errores, si ves respuesta 500, es porque no hay un manejador de errores global; aun así, el mensaje suele incluir el motivo (por ejemplo: “La mascota debe estar DISPONIBLE…”).

## 2) Datos base (crear adoptantes y mascotas)

La idea es crear algunos registros “de prueba” para poder comprobar todas las reglas.

### 2.1 Crear un adoptante mayor de 18 SIN patio

Endpoint:
- `POST /api/adoptantes`

Body:
```json
{
  "nombre": "Ana",
  "identificacion": "CC-1001",
  "edad": 25,
  "tienePatio": false
}
```

Guarda el `id` que devuelve la respuesta. En este documento lo llamaremos:
- `ADOPTANTE_SIN_PATIO_ID`

### 2.2 Crear un adoptante mayor de 18 CON patio

Endpoint:
- `POST /api/adoptantes`

Body:
```json
{
  "nombre": "Carlos",
  "identificacion": "CC-1002",
  "edad": 30,
  "tienePatio": true
}
```

Guarda el `id`:
- `ADOPTANTE_CON_PATIO_ID`

### 2.3 Crear un adoptante de 18 o menor (para forzar error)

Endpoint:
- `POST /api/adoptantes`

Body (ejemplo con 18):
```json
{
  "nombre": "Luis",
  "identificacion": "CC-1003",
  "edad": 18,
  "tienePatio": true
}
```

Guarda el `id`:
- `ADOPTANTE_MENOR_O_18_ID`

### 2.4 Crear 3 mascotas DISPONIBLES (2 medianas/pequeñas y 1 grande)

Mascota 1 (pequeña, disponible):
- `POST /api/mascotas`

Body:
```json
{
  "nombre": "Luna",
  "especie": "Perro",
  "edad": 2,
  "tamano": "PEQUENO",
  "estado": "DISPONIBLE"
}
```

Guarda el `id`:
- `MASCOTA_1_ID`

Mascota 2 (mediana, disponible):
- `POST /api/mascotas`

Body:
```json
{
  "nombre": "Milo",
  "especie": "Gato",
  "edad": 3,
  "tamano": "MEDIANO",
  "estado": "DISPONIBLE"
}
```

Guarda el `id`:
- `MASCOTA_2_ID`

Mascota 3 (grande, disponible):
- `POST /api/mascotas`

Body:
```json
{
  "nombre": "Thor",
  "especie": "Perro",
  "edad": 4,
  "tamano": "GRANDE",
  "estado": "DISPONIBLE"
}
```

Guarda el `id`:
- `MASCOTA_GRANDE_ID`

## 3) Regla 1 — Validar que la mascota esté “DISPONIBLE” antes de crear la solicitud

Regla:
- Si la mascota NO está `DISPONIBLE`, no se puede crear la solicitud.

### 3.1 Caso OK (mascota DISPONIBLE)

Endpoint:
- `POST /api/solicitudes`

Body:
```json
{
  "mascota": { "id": MASCOTA_1_ID },
  "adoptante": { "id": ADOPTANTE_CON_PATIO_ID }
}
```

Qué verificar:
1) La respuesta devuelve una solicitud con `estado` en `PENDIENTE`.
2) El `id` de la solicitud existe (guárdalo como `SOLICITUD_OK_1_ID`).

### 3.2 Caso ERROR (mascota NO DISPONIBLE)

Primero fuerza a que una mascota quede `EN_PROCESO` para simular que ya no está disponible.

Paso A: cambiar estado de la Mascota 2 a `EN_PROCESO`:
- `PUT /api/mascotas/{id}`

Usa `{id} = MASCOTA_2_ID` y body:
```json
{
  "nombre": "Milo",
  "especie": "Gato",
  "edad": 3,
  "tamano": "MEDIANO",
  "estado": "EN_PROCESO"
}
```

Paso B: intentar crear solicitud con esa mascota:
- `POST /api/solicitudes`

Body:
```json
{
  "mascota": { "id": MASCOTA_2_ID },
  "adoptante": { "id": ADOPTANTE_CON_PATIO_ID }
}
```

Resultado esperado:
- Debe fallar.
- Mensaje esperado (o similar): `La mascota debe estar DISPONIBLE para crear la solicitud.`

## 4) Regla 2 — Verificar que el adoptante sea mayor de 18 años

Regla:
- El adoptante debe tener `edad > 18`.

### 4.1 Caso ERROR (edad 18 o menor)

Endpoint:
- `POST /api/solicitudes`

Body (usa una mascota DISPONIBLE):
```json
{
  "mascota": { "id": MASCOTA_1_ID },
  "adoptante": { "id": ADOPTANTE_MENOR_O_18_ID }
}
```

Resultado esperado:
- Debe fallar.
- Mensaje esperado: `El adoptante debe ser mayor de 18 años.`

## 5) Regla 3 — Impedir que un adoptante tenga más de 2 solicitudes activas

Definición práctica en esta API:
- “Solicitudes activas” = solicitudes con `estado = PENDIENTE` para el mismo adoptante.
- Máximo permitido: 2.

### 5.1 Crear 2 solicitudes PENDIENTES (debe permitirlo)

Solicitud #1:
- `POST /api/solicitudes`

Body:
```json
{
  "mascota": { "id": MASCOTA_1_ID },
  "adoptante": { "id": ADOPTANTE_CON_PATIO_ID }
}
```

Guarda `SOLICITUD_1_ID`.

Solicitud #2:
- `POST /api/solicitudes`

Body:
```json
{
  "mascota": { "id": MASCOTA_GRANDE_ID },
  "adoptante": { "id": ADOPTANTE_CON_PATIO_ID }
}
```

Guarda `SOLICITUD_2_ID`.

Si la segunda falla porque la mascota grande exige patio, asegúrate de estar usando `ADOPTANTE_CON_PATIO_ID` (con patio = true).

### 5.2 Intentar crear la solicitud #3 (debe fallar)

Primero crea una tercera mascota DISPONIBLE para esta prueba (Mascota 4).
- `POST /api/mascotas`

Body:
```json
{
  "nombre": "Nala",
  "especie": "Perro",
  "edad": 1,
  "tamano": "MEDIANO",
  "estado": "DISPONIBLE"
}
```

Guarda `MASCOTA_4_ID`.

Ahora intenta crear la tercera solicitud:
- `POST /api/solicitudes`

Body:
```json
{
  "mascota": { "id": MASCOTA_4_ID },
  "adoptante": { "id": ADOPTANTE_CON_PATIO_ID }
}
```

Resultado esperado:
- Debe fallar.
- Mensaje esperado: `El adoptante ya tiene 2 solicitudes activas.`

### 5.3 “Liberar cupo” cambiando el estado de una solicitud

Si cambias una solicitud de `PENDIENTE` a `RECHAZADA`, ya no cuenta como activa.

Paso A: rechazar `SOLICITUD_1_ID`:
- `PATCH /api/solicitudes/{id}/estado/{estado}`

Usa `{id} = SOLICITUD_1_ID` y `{estado} = RECHAZADA`.

Paso B: intenta de nuevo crear la solicitud #3 (con `MASCOTA_4_ID`).

Resultado esperado:
- Ahora sí debe permitirla.

## 6) Regla 4 — Cambiar automáticamente el estado de la mascota a “EN_PROCESO” al recibir una solicitud

Regla:
- Cuando se crea una solicitud, la mascota pasa automáticamente de `DISPONIBLE` a `EN_PROCESO`.

### 6.1 Probar el cambio automático

Paso A: toma una mascota que esté `DISPONIBLE` (por ejemplo `MASCOTA_4_ID` recién creada).

Paso B: crea una solicitud:
- `POST /api/solicitudes`

Body:
```json
{
  "mascota": { "id": MASCOTA_4_ID },
  "adoptante": { "id": ADOPTANTE_SIN_PATIO_ID }
}
```

Paso C: consulta la mascota:
- `GET /api/mascotas/{id}`

Usa `{id} = MASCOTA_4_ID`.

Resultado esperado:
- Debe aparecer `estado: "EN_PROCESO"`.

## 7) Regla 5 — Si la mascota es “GRANDE”, validar que el adoptante tenga patio

Regla:
- Si `mascota.tamano == GRANDE`, entonces `adoptante.tienePatio` debe ser `true`.

### 7.1 Caso ERROR (mascota grande + adoptante sin patio)

Asegúrate de que `MASCOTA_GRANDE_ID` esté `DISPONIBLE`. Si no lo está, puedes crear otra mascota grande DISPONIBLE o actualizarla.

Endpoint:
- `POST /api/solicitudes`

Body:
```json
{
  "mascota": { "id": MASCOTA_GRANDE_ID },
  "adoptante": { "id": ADOPTANTE_SIN_PATIO_ID }
}
```

Resultado esperado:
- Debe fallar.
- Mensaje esperado: `Para una mascota GRANDE el adoptante debe tener patio.`

### 7.2 Caso OK (mascota grande + adoptante con patio)

Endpoint:
- `POST /api/solicitudes`

Body:
```json
{
  "mascota": { "id": MASCOTA_GRANDE_ID },
  "adoptante": { "id": ADOPTANTE_CON_PATIO_ID }
}
```

Resultado esperado:
- Debe crear la solicitud.
- La mascota debe quedar en `EN_PROCESO` automáticamente (verifícalo con `GET /api/mascotas/{id}`).

## 8) Resumen rápido de “qué debe pasar”

- Mascota no DISPONIBLE → no crea solicitud.
- Adoptante edad <= 18 → no crea solicitud.
- Adoptante con 2 solicitudes PENDIENTES → no crea una tercera.
- Crear solicitud → mascota cambia a EN_PROCESO.
- Mascota GRANDE + adoptante sin patio → no crea solicitud.
