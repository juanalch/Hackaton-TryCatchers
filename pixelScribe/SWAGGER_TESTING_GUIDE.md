# Guía de Pruebas con Swagger

## Acceso a Swagger UI

Una vez que inicies la aplicación, puedes acceder a Swagger UI en:
```
http://localhost:8080/swagger-ui/index.html
```

## Pasos para Probar la API

### 1. Registrar un Usuario
1. Ve a la sección **Autenticación**
2. Expande el endpoint `POST /api/auth/register`
3. Haz clic en **Try it out**
4. Completa el JSON de ejemplo:
```json
{
  "email": "test@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```
5. Haz clic en **Execute**
6. Copia el **token** de la respuesta

### 2. Configurar Autenticación JWT
1. Haz clic en el botón **Authorize** (candado) en la parte superior derecha
2. En el campo de texto, ingresa: `Bearer TU_TOKEN_AQUI`
   - Reemplaza `TU_TOKEN_AQUI` con el token que copiaste
   - Ejemplo: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
3. Haz clic en **Authorize**
4. Cierra el modal

### 3. Iniciar Sesión (Alternativa)
Si prefieres usar login en lugar de register:
1. Expande `POST /api/auth/login`
2. Haz clic en **Try it out**
3. Ingresa las credenciales:
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```
4. Copia el token de la respuesta
5. Configura la autenticación como se explicó arriba

### 4. Probar Endpoints Protegidos
Una vez autorizado, puedes probar los endpoints de imágenes:
- `POST /api/images/upload` - Subir imagen
- `GET /api/images` - Obtener imágenes del usuario

## Notas Importantes

### Token JWT
- El token expira en 24 horas (86400000 ms)
- Si el token expira, deberás hacer login nuevamente
- El token debe incluir el prefijo "Bearer " (con espacio)

### Configuración de MongoDB
Asegúrate de tener MongoDB ejecutándose:
```bash
# Windows
mongod

# O usando Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### Variables de Entorno
Revisa el archivo `application.properties`:
- `jwt.secret`: Cambia esto en producción
- `spring.data.mongodb.database`: Base de datos (pixelscribe)
- `server.port`: Puerto del servidor (8080)

## Ejemplos de Respuestas

### Registro/Login Exitoso
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "test@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

### Error de Validación
```json
{
  "message": "Email already registered"
}
```

### Error de Autenticación
```json
{
  "message": "Invalid email or password"
}
```

## Solución de Problemas

### 401 Unauthorized
- Verifica que copiaste el token completo
- Asegúrate de incluir "Bearer " antes del token
- Verifica que el token no haya expirado

### 403 Forbidden
- El token es válido pero no tienes permisos
- Verifica que estés usando el usuario correcto

### 500 Internal Server Error
- Verifica que MongoDB esté ejecutándose
- Revisa los logs de la aplicación en la consola

## Ejecutar la Aplicación

### Con Maven Wrapper (Recomendado)
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Con Maven instalado
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui/index.html`
