# 🎨 PixelScribe - Analizador de Imágenes con IA

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-brightgreen)
![Gemini AI](https://img.shields.io/badge/AI-Gemini-blue)
![License](https://img.shields.io/badge/license-MIT-blue)

**PixelScribe** es una plataforma web donde usuarios autenticados pueden subir imágenes para analizarlas usando IA multimodal (Gemini). El sistema procesa las imágenes, genera descripciones detalladas y las almacena en una base de datos para consultas posteriores.

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [API Endpoints](#-api-endpoints)
- [Testing con Swagger](#-testing-con-swagger)
- [Despliegue](#-despliegue)
- [Equipo](#-equipo)

---

## ✨ Características

### Funcionalidades Implementadas

✅ **Autenticación y Seguridad**
- Sistema completo de registro y login
- Autenticación JWT con tokens seguros
- Protección de rutas con Spring Security
- Validación de credenciales

✅ **Gestión de Imágenes**
- Subida de imágenes (JPEG, PNG, WEBP)
- Validación de tamaño (máx. 5MB) y tipo de archivo
- Almacenamiento en Base64
- Galería personal de imágenes por usuario

✅ **Análisis con IA**
- Integración con Google Gemini AI
- Análisis detallado de imágenes
- Descripción automática de contenido
- Detección de objetos, colores y contexto
- Extracción de texto visible (OCR)

✅ **Dashboard y Consultas**
- Obtener todas las imágenes del usuario
- Consultar imágenes específicas
- Historial de análisis
- Estado de procesamiento en tiempo real

✅ **Documentación**
- API REST completamente documentada con Swagger/OpenAPI
- Guía de pruebas incluida
- Ejemplos de request/response

---

## 🛠 Tecnologías

### Backend
- **Java 17** - Lenguaje de programación
- **Spring Boot 3.5.7** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data MongoDB** - Persistencia de datos
- **JWT (JSON Web Tokens)** - Tokens de autenticación
- **Lombok** - Reducción de boilerplate
- **Maven** - Gestión de dependencias

### Base de Datos
- **MongoDB Atlas** - Base de datos NoSQL en la nube

### Inteligencia Artificial
- **Google Gemini AI (1.5 Flash)** - Análisis de imágenes multimodal

### Documentación
- **SpringDoc OpenAPI 3** - Documentación automática de la API
- **Swagger UI** - Interfaz interactiva para testing

---

## 🏗 Arquitectura

```
pixelScribe/
├── src/main/java/com/hackaton/tryCatchers/pixelScribe/
│   ├── config/              # Configuraciones
│   │   ├── SecurityConfig.java
│   │   └── SwaggerConfig.java
│   ├── controller/          # Endpoints REST
│   │   ├── AuthController.java
│   │   └── ImageController.java
│   ├── dto/                 # Data Transfer Objects
│   │   ├── AuthResponse.java
│   │   ├── ImageAnalysisDTO.java
│   │   ├── ImageUploadResponse.java
│   │   ├── LoginRequest.java
│   │   └── RegisterRequest.java
│   ├── model/               # Entidades de dominio
│   │   ├── ImageAnalysis.java
│   │   └── User.java
│   ├── repository/          # Acceso a datos
│   │   ├── ImageAnalysisRepository.java
│   │   ├── ImageRepository.java
│   │   └── UserRepository.java
│   ├── security/            # Seguridad JWT
│   │   ├── JwtAuthenticationFilter.java
│   │   └── SecurityUtils.java
│   ├── service/             # Lógica de negocio
│   │   ├── AIService.java
│   │   ├── CustomUserDetailsService.java
│   │   └── ImageService.java
│   └── util/                # Utilidades
│       └── JwtUtil.java
└── src/main/resources/
    └── application.properties
```

### Flujo de Datos

```
Usuario → Controller → Service → Repository → MongoDB
                    ↓
                 AIService → Gemini API
```

---

## 📦 Requisitos Previos

- **Java 17** o superior
- **Maven 3.8+** (o usar el wrapper incluido)
- **MongoDB** (o conexión a MongoDB Atlas)
- **API Key de Google Gemini** ([Obtener aquí](https://makersuite.google.com/app/apikey))

---

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/juanalch/Hackaton-TryCatchers.git
cd Hackaton-TryCatchers/pixelScribe
```

### 2. Configurar Variables de Entorno

Edita el archivo `src/main/resources/application.properties`:

```properties
# MongoDB (Usa tu propia conexión o la incluida)
spring.data.mongodb.uri=mongodb+srv://tu-usuario:tu-password@cluster.mongodb.net/pixelscribe

# JWT Secret (¡CAMBIA ESTO EN PRODUCCIÓN!)
jwt.secret=tu-secret-key-muy-larga-y-segura-para-produccion

# Gemini API Key
gemini.api.key=TU_API_KEY_DE_GEMINI_AQUI
```

### 3. Instalar Dependencias

```bash
# Windows
.\mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

---

## ⚙️ Configuración

### Configuración de MongoDB

#### Opción 1: MongoDB Atlas (Cloud) - Recomendado
1. Crear cuenta en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Crear un cluster gratuito
3. Obtener la cadena de conexión
4. Actualizar `spring.data.mongodb.uri` en `application.properties`

#### Opción 2: MongoDB Local
```bash
# Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest

# Actualizar en application.properties
spring.data.mongodb.uri=mongodb://localhost:27017/pixelscribe
```

### Configuración de Gemini AI

1. Visitar [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Crear/obtener API Key
3. Agregar la key en `application.properties`:
```properties
gemini.api.key=TU_API_KEY_AQUI
```

---

## 💻 Uso

### Iniciar la Aplicación

```bash
# Con Maven Wrapper (Recomendado)
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run

# Con Maven instalado
mvn spring-boot:run
```

La aplicación estará disponible en:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **API Docs**: http://localhost:8080/v3/api-docs

---

## 🔌 API Endpoints

### Autenticación

#### Registro de Usuario
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "usuario@ejemplo.com",
  "password": "password123",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "usuario@ejemplo.com",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@ejemplo.com",
  "password": "password123"
}
```

### Gestión de Imágenes (Requiere JWT)

#### Subir Imagen
```http
POST /api/images/upload
Authorization: Bearer {token}
Content-Type: multipart/form-data

file: [imagen.jpg]
```

**Respuesta:**
```json
{
  "id": "6543210abcdef",
  "fileName": "imagen.jpg",
  "status": "COMPLETED",
  "message": "Imagen subida y analizada correctamente"
}
```

#### Obtener Todas las Imágenes del Usuario
```http
GET /api/images
Authorization: Bearer {token}
```

**Respuesta:**
```json
[
  {
    "id": "6543210abcdef",
    "fileName": "imagen.jpg",
    "imageBase64": "data:image/jpeg;base64,...",
    "description": "Un gato naranja durmiendo sobre un portátil...",
    "status": "COMPLETED",
    "uploadedAt": "2025-11-04T10:30:00",
    "analyzedAt": "2025-11-04T10:30:05"
  }
]
```

#### Obtener Imagen Específica
```http
GET /api/images/{id}
Authorization: Bearer {token}
```

---

## 🧪 Testing con Swagger

### Acceso a Swagger UI

Navega a: http://localhost:8080/swagger-ui/index.html

### Pasos para Probar la API

1. **Registrarse o Hacer Login**
   - Expandir `POST /api/auth/register` o `POST /api/auth/login`
   - Click en "Try it out"
   - Completar el JSON con tus datos
   - Click en "Execute"
   - **Copiar el token** de la respuesta

2. **Autorizar las Peticiones**
   - Click en el botón **🔒 Authorize** (esquina superior derecha)
   - Ingresar: `Bearer {tu-token-aquí}`
   - Click en "Authorize" y cerrar el modal

3. **Probar Endpoints Protegidos**
   - Ahora puedes usar todos los endpoints de `/api/images`
   - Los requests incluirán automáticamente el token JWT

### Ejemplos de Prueba

#### 1. Registrar Usuario
```json
{
  "email": "test@pixelscribe.com",
  "password": "Test123!",
  "firstName": "Test",
  "lastName": "User"
}
```

#### 2. Subir Imagen
- Seleccionar archivo (JPG, PNG, WEBP < 5MB)
- Click en "Execute"
- Esperar el análisis (puede tomar 5-10 segundos)

Ver la guía completa en: [SWAGGER_TESTING_GUIDE.md](SWAGGER_TESTING_GUIDE.md)

---

## 🚀 Despliegue

### Backend (Railway / Render)

#### Railway
```bash
# Instalar Railway CLI
npm i -g @railway/cli

# Login
railway login

# Crear proyecto
railway init

# Configurar variables de entorno
railway variables set MONGODB_URI=<tu-uri>
railway variables set JWT_SECRET=<tu-secret>
railway variables set GEMINI_API_KEY=<tu-key>

# Desplegar
railway up
```

#### Render
1. Conectar repositorio en [Render.com](https://render.com)
2. Crear nuevo "Web Service"
3. Configurar:
   - **Build Command**: `./mvnw clean install`
   - **Start Command**: `java -jar target/pixelScribe-0.0.1-SNAPSHOT.jar`
4. Agregar variables de entorno
5. Deploy

### Variables de Entorno para Producción

```env
MONGODB_URI=mongodb+srv://...
JWT_SECRET=clave-secreta-muy-larga-y-segura
GEMINI_API_KEY=tu-api-key
CORS_ALLOWED_ORIGINS=https://tu-frontend.vercel.app
```

---

## 📊 Estado del Proyecto

### Completado ✅
- [x] Autenticación JWT completa
- [x] Registro y login de usuarios
- [x] Subida de imágenes con validaciones
- [x] Integración con Gemini AI
- [x] Análisis de imágenes
- [x] Galería de imágenes por usuario
- [x] Documentación con Swagger
- [x] Seguridad con Spring Security
- [x] Base de datos MongoDB


---

## 👥 Equipo

**TryCatchers** - Equipo Hackatón

- Desarrollador Backend
- Integración IA
- Arquitectura y Seguridad

---

<div align="center">

Desarrollado con ❤️ por **TryCatchers**

</div>
