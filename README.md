# Prueba Practica Dev. Backend

API REST reactiva para la gestión de franquicias, sucursales y productos, desarrollada como prueba técnica para Accenture.

---

## Tecnologías

- **Java 21**
- **Spring Boot 4.0.6**
- **Spring WebFlux** 
- **Spring Data R2DBC** 
- **MySQL 8.0**
- **Docker & Docker Compose**
- **Gradle**
- **Lombok**
- **JUnit 5 + Mockito + Reactor Test**

---

## Arquitectura

El proyecto implementa **Clean Architecture** dividida en tres capas:

```
src/
└── main/java/com/example/pruebatecnicaaccenture/
    ├── domain/
    │   ├── model/          → Entidades del dominio
    │   ├── repository/     → Interfaces de repositorios
    │   └── exception/      → Excepciones del dominio
    ├── application/
    │   ├── usecase/        → Casos de uso
    │   └── dto/            → Objetos de transferencia de datos
    └── infrastructure/
        ├── persistence/
        │   ├── entity/     → Entidades R2DBC
        │   ├── mapper/     → Mappers entidad ↔ dominio
        │   └── repository/ → Repositorios Spring Data R2DBC
        ├── repositoryImpl/ → Implementaciones de repositorios
        └── web/
            ├── handler/    → Handlers funcionales
            ├── mapper/     → Mappers dominio ↔ DTO
            └── router/     → Definición de rutas
```

---

### Flujo de trabajo

1. Cada funcionalidad se desarrolló en su propia rama `feature/*` creada desde `develop`
2. Cada rama contiene commits por cada componente desarrollado
3. Al finalizar cada feature se creó un **Pull Request** de `feature/*` hacia `develop`
4. Una vez completadas todas las features se realizó un **Pull Request** final de `develop` hacia `main`

### Convención de commits

Todos los commits siguieron el prefijo `feat:` con descripción

---

## Requisitos 

- Java 21
- Docker Desktop
- MySQL 8.0 (solo para ejecución local)
- Git
- Postman 

---

## Ejecución con Docker

### 1. Clonar el repositorio

```bash
git clone https://github.com/DeibyBustos/Prueba_tecnica.git
cd PruebaTecnicaAccenture
```

### 2. Levantar los contenedores

```bash
docker-compose up --build
```

Esto levanta dos contenedores:
- `franchise_mysql` — MySQL 8.0 con la base de datos creada automáticamente
- `franchise_app` — API Spring Boot en el puerto `8080`

### 3. Verificar que está corriendo

```bash
docker ps
```

La API estará disponible en: `http://localhost:8080`

### 4. Detener los contenedores

```bash
docker-compose down
```

---

## Ejecución local 

### 1. Crear la base de datos

Ejecutar el script SQL en MySQL Workbench o cualquier cliente MySQL:

```sql
source db/schema.sql
```

### 2. Configurar `application.properties`

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=franchise_db
DB_USER=root
DB_PASSWORD=tu_password
```

### 3. Ejecutar la aplicación

```bash
./gradlew bootRun
```

La API estará disponible en: `http://localhost:8080`

---

## Endpoints

### Franquicias

| Método | URL | Descripción |
|--------|-----|-------------|
| `POST` | `/api/v1/franchises` | Crear franquicia |
| `GET` | `/api/v1/franchises` | Listar franquicias |
| `PATCH` | `/api/v1/franchises/{id}/name` | Actualizar nombre |

### Sucursales

| Método | URL | Descripción |
|--------|-----|-------------|
| `POST` | `/api/v1/franchises/{franchiseId}/branches` | Agregar sucursal |
| `GET` | `/api/v1/franchises/{franchiseId}/branches` | Listar sucursales |
| `PATCH` | `/api/v1/branches/{id}/name` | Actualizar nombre |

### Productos

| Método | URL | Descripción |
|--------|-----|-------------|
| `POST` | `/api/v1/branches/{branchId}/products` | Agregar producto |
| `DELETE` | `/api/v1/products/{id}` | Eliminar producto |
| `PATCH` | `/api/v1/products/{id}/stock` | Actualizar stock |
| `PATCH` | `/api/v1/products/{id}/name` | Actualizar nombre |
| `GET` | `/api/v1/franchises/{franchiseId}/top-products` | Producto con más stock por sucursal |

---

##  Prueba en Postman

> Para cada request en Postman:
> - Selecciona el **método** (POST, GET, PATCH, DELETE)
> - Ingresa la **URL**
> - En la pestaña **Headers** agrega: `Content-Type: application/json`
> - En la pestaña **Body** selecciona `raw` y `JSON`

---

### 1. Crear una franquicia
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/v1/franchises`
- **Body:**
```json
{
  "name": "McDonald's"
}
```
- **Respuesta esperada `201`:**
```json
{
  "id": 1,
  "name": "McDonald's"
}
```

---

### 2. Agregar sucursal a una franquicia
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/v1/franchises/1/branches`
- **Body:**
```json
{
  "name": "Bogotá Centro"
}
```
- **Respuesta esperada `201`:**
```json
{
  "id": 1,
  "name": "Bogotá Centro",
  "franchiseId": 1
}
```

---

### 3. Agregar producto a una sucursal
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/v1/branches/1/products`
- **Body:**
```json
{
  "name": "Big Mac",
  "stock": 150
}
```
- **Respuesta esperada `201`:**
```json
{
  "id": 1,
  "name": "Big Mac",
  "stock": 150,
  "branchId": 1
}
```

---

### 4. Eliminar un producto
- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/v1/products/1`
- **Respuesta esperada `204 No Content`**

---

### 5. Actualizar stock de un producto
- **Method:** `PATCH`
- **URL:** `http://localhost:8080/api/v1/products/1/stock`
- **Body:**
```json
{
  "stock": 999
}
```
- **Respuesta esperada `200`:**
```json
{
  "id": 1,
  "name": "Big Mac",
  "stock": 999,
  "branchId": 1
}
```

---

### 6. Producto con más stock por sucursal 
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/v1/franchises/1/top-products`
- **Respuesta esperada `200`:**
```json
[
  {
    "branchName": "Bogotá Centro",
    "productName": "Big Mac",
    "stock": 999
  },
  {
    "branchName": "Medellín El Poblado",
    "productName": "Quarter Pounder",
    "stock": 200
  }
]
```

---

### 7. Actualizar nombre de franquicia
- **Method:** `PATCH`
- **URL:** `http://localhost:8080/api/v1/franchises/1/name`
- **Body:**
```json
{
  "name": "McDonald's Colombia"
}
```
- **Respuesta esperada `200`:**
```json
{
  "id": 1,
  "name": "McDonald's Colombia"
}
```

---

### 8. Actualizar nombre de sucursal
- **Method:** `PATCH`
- **URL:** `http://localhost:8080/api/v1/branches/1/name`
- **Body:**
```json
{
  "name": "Bogotá Chapinero"
}
```
- **Respuesta esperada `200`:**
```json
{
  "id": 1,
  "name": "Bogotá Chapinero",
  "franchiseId": 1
}
```

---

### 9. Actualizar nombre de producto
- **Method:** `PATCH`
- **URL:** `http://localhost:8080/api/v1/products/1/name`
- **Body:**
```json
{
  "name": "Big Mac Doble"
}
```
- **Respuesta esperada `200`:**
```json
{
  "id": 1,
  "name": "Big Mac Doble",
  "stock": 999,
  "branchId": 1
}
```

---

## Pruebas unitarias

```bash
./gradlew test
```

Las pruebas cubren los casos de uso:
- `FranchiseUseCaseTest` — 5 pruebas
- `BranchUseCaseTest` — 5 pruebas
- `ProductUseCaseTest` — 7 pruebas

---

## Estructura del proyecto

```
PruebaTecnicaAccenture/
├── src/
│   ├── main/
│   │   ├── java/         → Código fuente
│   │   └── resources/
│   │       └── application.properties
│   └── test/             → Pruebas unitarias
├── db/
│   └── schema.sql        → Script de base de datos
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── build.gradle
└── README.md
```

---
