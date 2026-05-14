# Guía de Despliegue: Docker + Render.com

Esta guía detalla los pasos necesarios para desplegar la aplicación Spring Boot en Render.com utilizando Docker.

## 1. Archivos de Configuración

Se han agregado los siguientes archivos al proyecto:

- **`Dockerfile`**: Configura una construcción multi-etapa para generar una imagen ligera (JRE Alpine).
- **`.dockerignore`**: Optimiza la carga de archivos al demonio de Docker, ignorando carpetas innecesarias como `target` o `.git`.
- **`render.yaml`**: Define la infraestructura como código para Render, configurando automáticamente el servicio web y la base de datos PostgreSQL.

## 2. Requisitos Previos

1.  Tener instalado **Docker Desktop** localmente.
2.  Contar con una cuenta en [Render.com](https://render.com).
3.  Subir el código a un repositorio de **GitHub** o **GitLab**.

## 3. Prueba Local con Docker

Para verificar que la configuración de Docker es correcta, puedes construir y ejecutar la imagen localmente:

```bash
# Construir la imagen
docker build -t mi-app-spring .

# Ejecutar el contenedor (asegúrate de tener una DB o usar H2 para pruebas rápidas)
docker run -p 8080:8080 -e DB_URL=jdbc:postgresql://host:port/db mi-app-spring
```

## 4. Despliegue en Render.com

### Opción A: Usando el Blueprint (Recomendado)

Render detectará automáticamente el archivo `render.yaml`.

1.  Ve al Dashboard de Render -> **Blueprints**.
2.  Conecta tu repositorio de GitHub.
3.  Render creará automáticamente:
    - Una instancia de **PostgreSQL**.
    - Un **Web Service** configurado con Docker.
    - Las variables de entorno (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) se vincularán automáticamente.

### Opción B: Despliegue Manual del Servicio Web

Si prefieres no usar el Blueprint:

1.  **Crear Base de Datos**: Dashboard -> New -> **PostgreSQL**. Copia las credenciales.
2.  **Crear Servicio Web**: Dashboard -> New -> **Web Service**.
3.  Selecciona tu repositorio.
4.  **Runtime**: Selecciona **Docker**.
5.  **Environment Variables**: Configura manualmente:
    - `DB_URL`: La URL de conexión de tu base de datos Render.
    - `DB_USERNAME`: El usuario de la base de datos.
    - `DB_PASSWORD`: La contraseña de la base de datos.

## 5. Notas Importantes

- **Puerto**: Render asigna dinámicamente un puerto a través de la variable `$PORT`. El `Dockerfile` está configurado para que Spring Boot escuche en ese puerto.
- **Base de Datos**: La configuración de Render usa el nivel gratuito. Ten en cuenta que los servicios gratuitos de Render "entran en reposo" tras 15 minutos de inactividad.
- **Seguridad**: Nunca subas archivos `.env` al repositorio. Las variables sensibles se manejan directamente en el panel de Render.
