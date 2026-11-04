# Pymeris

### Management Solution for Small Businesses

**Pymeris** es una aplicación de escritorio desarrollada como proyecto final del ciclo formativo de Desarrollo de Aplicaciones Multiplataforma (DAM).

Su objetivo es facilitar la gestión interna de pequeñas empresas mediante una aplicación que centraliza la administración de clientes, productos, pedidos y usuarios en un único entorno.

Está desarrollada en Java, utiliza JavaFX para la interfaz gráfica y SQLite como sistema de gestión de bases de datos.

---

## Características principales

### Gestión de clientes
- Registro de nuevos clientes.
- Consulta y modificación de información.
- Búsqueda por nombre y apellido.
- Gestión del estado de los clientes.
- Eliminación de registros.

### Gestión de productos
- Alta y modificación de productos.
- Administración de categorías, precios e IVA.
- Control de existencias.
- Registro de entradas y salidas de stock.
- Activación y desactivación de productos.
- Consulta de productos inactivos.
- Búsqueda de productos.

### Gestión de pedidos
- Creación de nuevos pedidos.
- Selección de clientes.
- Incorporación de productos y cantidades.
- Cálculo de subtotales, IVA e importes finales.
- Actualización de existencias durante la creación de pedidos.
- Consulta del detalle de cada pedido.
- Finalización y eliminación de pedidos.
- Filtrado por cliente y rango de fechas.

### Gestión de usuarios
- Creación y modificación de usuarios.
- Generación automática de nombres de usuario.
- Asignación de roles: Administrador y Empleado.
- Activación y desactivación de cuentas.
- Restablecimiento de contraseñas.
- Control de acceso según el rol del usuario.

### Autenticación y seguridad
- Sistema de inicio y cierre de sesión.
- Control de permisos según el rol.
- Almacenamiento de contraseñas mediante hashes BCrypt.
- Cambio de contraseña.
- Gestión de sesiones de usuario.

---

## Tecnologías utilizadas

| Tecnología | Aplicación |
|---|---|
| Java 21 | Lenguaje principal |
| JavaFX 21 | Desarrollo de la interfaz gráfica |
| FXML | Definición de las vistas |
| SQLite | Sistema de gestión de bases de datos |
| JDBC | Conexión y operaciones con la base de datos |
| Maven | Gestión de dependencias y construcción |
| JUnit 5 | Pruebas unitarias |
| Mockito | Simulación de dependencias en pruebas |
| jBCrypt | Hash de contraseñas |
| ControlsFX | Componentes adicionales para JavaFX |

---

## Arquitectura del proyecto

Pymeris utiliza una arquitectura organizada en diferentes capas para separar las responsabilidades de la aplicación.

En la capa de presentación se utiliza el patrón **MVP (Model-View-Presenter)**, mientras que para el acceso a los datos se emplean los patrones **DAO (Data Access Object)** y **Repository**.

### Estructura de carpetas

```text
Pymeris/
│
├── .mvn/
│   └── wrapper/                  # Configuración de Maven Wrapper
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── angel/tfg/pymeris/
│   │   │       │
│   │   │       ├── Base/         # Clases e interfaces comunes
│   │   │       │
│   │   │       ├── Model/
│   │   │       │   ├── DAO/        # Acceso directo a la base de datos
│   │   │       │   ├── Entities/   # Entidades del sistema
│   │   │       │   └── Repository/ # Capa de repositorios
│   │   │       │
│   │   │       ├── Util/         # Utilidades generales
│   │   │       │   ├── Enums/
│   │   │       │   └── Navegation/
│   │   │       │
│   │   │       ├── Views/        # Vistas y lógica de presentación
│   │   │       │   ├── Login/
│   │   │       │   ├── ChangePassword/
│   │   │       │   ├── ManageClients/
│   │   │       │   ├── ManageOrders/
│   │   │       │   ├── ManageProducts/
│   │   │       │   ├── ManageUsers/
│   │   │       │   └── SelectOptionMain/
│   │   │       │
│   │   │       ├── Launcher.java
│   │   │       ├── PymerisApp.java
│   │   │       └── module-info.java
│   │   │
│   │   └── resources/
│   │       ├── Imagenes/         # Recursos gráficos
│   │       └── Vistas/           # Interfaces FXML
│   │
│   └── test/
│       └── java/                 # Pruebas unitarias
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

### Organización de las capas

**View**

Contiene los controladores JavaFX y los archivos FXML encargados de representar la interfaz gráfica y gestionar las interacciones del usuario.

**Presenter**

Contiene la lógica de presentación y coordina las operaciones entre las vistas y los repositorios.

**Model**

Contiene las entidades del sistema, los objetos DAO responsables de las consultas SQL y los repositorios que abstraen el acceso a los datos.

**Util**

Agrupa las herramientas comunes de la aplicación, como la gestión de la base de datos, las sesiones, la navegación y las utilidades de seguridad.

---

## Modelo de datos

La aplicación utiliza SQLite para almacenar la información de forma local.

La base de datos está compuesta por cinco tablas principales:

| Tabla | Descripción |
|---|---|
| Usuarios | Información de los empleados y administradores |
| Clientes | Datos de los clientes registrados |
| Productos | Catálogo de productos y existencias |
| Pedidos | Información general de los pedidos |
| DetallePedidos | Productos, cantidades y precios asociados a cada pedido |

### Relaciones principales

- Cada pedido está asociado a un empleado.
- Cada pedido pertenece a un cliente.
- Cada pedido puede contener varios productos.
- Los detalles de los pedidos están relacionados con los productos y con su pedido correspondiente.

Se utilizan claves primarias, claves foráneas, restricciones `CHECK` y un trigger para registrar modificaciones de usuarios.

### Ubicación de la base de datos

La aplicación genera la base de datos en el directorio personal del usuario:

```text
<USER_HOME>/
└── Pymeris/
    └── database_protecto.db
```

---

## Requisitos previos

Para trabajar con el proyecto necesitas:

- JDK 21.
- IntelliJ IDEA o un IDE compatible con Java y Maven.
- Conexión a Internet para descargar las dependencias la primera vez.

El proyecto incorpora **Maven Wrapper**, por lo que no es necesario instalar Maven globalmente.

---

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/KorryKK/Pymeris.git
```

### 2. Acceder al directorio

```bash
cd Pymeris
```

### 3. Abrir el proyecto

Abre el directorio desde IntelliJ IDEA y espera a que Maven descargue e importe las dependencias.

### 4. Ejecutar la aplicación

En Windows:

```bat
mvnw.cmd clean javafx:run
```

En Linux o macOS:

```bash
chmod +x mvnw
./mvnw clean javafx:run
```

También puedes ejecutar directamente la clase principal:

```text
angel.tfg.pymeris.Launcher
```

---

## Construcción del proyecto

Para compilar y generar el paquete del proyecto:

En Windows:

```bat
mvnw.cmd clean package
```

En Linux o macOS:

```bash
./mvnw clean package
```

Los archivos generados se almacenan en el directorio:

```text
target/
```

---

## Pruebas unitarias

El proyecto incluye pruebas unitarias desarrolladas con JUnit 5 y Mockito.

Las pruebas se encuentran en:

```text
src/test/java/
```

Se han desarrollado pruebas para diferentes componentes de la aplicación, entre ellos:

- Entidades del modelo.
- Autenticación.
- Gestión de contraseñas.
- Sesiones de usuario.
- Presentadores de clientes.
- Presentadores de productos.
- Presentadores de pedidos.
- Presentadores de usuarios.

Para ejecutar las pruebas:

En Windows:

```bat
mvnw.cmd test
```

En Linux o macOS:

```bash
./mvnw test
```

La ejecución de la suite completa debe verificarse en el entorno de desarrollo.

---

## Estado del proyecto

Pymeris es un proyecto académico desarrollado como trabajo final del ciclo de Desarrollo de Aplicaciones Multiplataforma.

El repositorio contiene la implementación de los principales módulos de gestión y una suite de pruebas unitarias.

Actualmente existen aspectos pendientes de revisión y mejora, especialmente en la gestión de errores, determinados procesos de actualización del stock y la configuración de seguridad de las cuentas iniciales.

Por este motivo, el proyecto no debe utilizarse como sistema de gestión en producción ni con información real de clientes o usuarios.

---

## Autor

**KorryKK**

GitHub: [https://github.com/KorryKK](https://github.com/KorryKK)

Repositorio: [https://github.com/KorryKK/Pymeris](https://github.com/KorryKK/Pymeris)

---

*Pymeris — Management Solution for Small Businesses.*