# Solicitudes API

Aplicación didáctica para gestionar solicitudes internas. El backend usa Spring Boot y mantiene los datos en memoria; el frontend usa React, Axios, Context API, `useReducer`, `useEffect` y Bootstrap.

## Requisitos

- JDK 17 o superior
- Maven
- Node.js y npm

## Ejecutar el backend

```bash
cd solicitudes-api
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## Ejecutar el frontend

En otra terminal:

```bash
cd solicitudes-web
npm install
npm run dev
```

Vite muestra la URL del frontend, normalmente `http://localhost:5173`.

## Endpoints

| Método | Ruta | Respuesta |
| --- | --- | --- |
| GET | `/api/solicitudes` | `200` con la lista |
| POST | `/api/solicitudes` | `201` con la solicitud creada |
| PUT | `/api/solicitudes/{id}` | `200` con el estado actualizado |
| DELETE | `/api/solicitudes/{id}` | `204` sin contenido |

Para crear se envía `{ "descripcion": "..." }`. Para actualizar se envía `{ "estado": "RESUELTA" }` o `{ "estado": "PENDIENTE" }`.

## Preguntas de reflexión

1. El arreglo compartido pertenece al proveedor porque el proveedor coordina el estado de toda la aplicación. Así, el formulario y cada fila leen y actualizan la misma fuente de verdad.
2. `useState` es suficiente para cambios de estado simples. `useReducer` agrupa transiciones relacionadas en acciones explícitas, lo que ordena el estado de carga, error y operaciones CRUD.
3. `AbortController` cancela la petición GET cuando el proveedor se desmonta y evita actualizar el estado con una respuesta que ya no corresponde a un componente activo.
4. `useEffect` sincroniza un componente con sistemas externos y su cleanup se ejecuta al cambiar dependencias o desmontar. No es una equivalencia exacta de todos los métodos de ciclo de vida de clases.
5. La lista en memoria se pierde al reiniciar Spring Boot y no sirve como persistencia compartida entre instancias o reinicios.

## Verificación

Desde `solicitudes-web`, `npm run build` genera correctamente la versión de producción. Para probar el CRUD completo se debe ejecutar también Spring Boot y realizar las peticiones indicadas en la tabla.

## Evidencias de entrega

Las capturas solicitadas están en la carpeta `evidencias/`:

- `react-solicitudes-contador.jpg`: vista React con solicitudes y contador de pendientes.
- `bruno-get.png`: GET `/api/solicitudes` con `200 OK`.
- `bruno-post.png`: POST `/api/solicitudes` con `201 Created`.
- `bruno-put.png`: PUT `/api/solicitudes/1` con `200 OK`.
- `bruno-delete.png`: DELETE `/api/solicitudes/2` con `204 No Content`.
- `error-conexion.jpg`: vista React mostrando el error cuando la API no está disponible.
