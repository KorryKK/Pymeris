# Pymeris

**Aplicación de escritorio para la gestión de pequeñas empresas**, desarrollada como proyecto final del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).

Pymeris centraliza en una única aplicación la gestión de clientes, productos, usuarios y pedidos. Está desarrollada en Java, utiliza JavaFX para la interfaz gráfica y SQLite para almacenar la información localmente.

> **Estado del proyecto:** proyecto académico. El código publicado refleja el estado actual del desarrollo y no debe considerarse preparado para un despliegue en producción ni para almacenar datos reales.

## Funcionalidades

- **Inicio de sesión:** autenticación de usuarios y control de acceso según su estado y rol.
- **Gestión de usuarios:** alta, consulta, modificación y administración de usuarios.
- **Gestión de clientes:** registro, consulta, modificación y desactivación de clientes.
- **Gestión de productos:** administración de productos, categorías, precios, IVA y existencias.
- **Gestión de pedidos:** creación y consulta de pedidos con sus correspondientes líneas de detalle.
- **Control de stock:** actualización de existencias al añadir o retirar productos de un pedido.
- **Persistencia local:** almacenamiento de la información en una base de datos SQLite.

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje de programación |
| JavaFX | Interfaz gráfica de escritorio |
| FXML | Definición de las vistas |
| SQLite | Base de datos local |
| JDBC | Conexión y acceso a la base de datos |
| Maven | Gestión de dependencias y construcción del proyecto |
| JUnit 5 | Pruebas unitarias |
| Mockito | Simulación de dependencias en pruebas |
| jBCrypt | Hash de contraseñas |
| SLF4J | API de registro de eventos |

## Arquitectura

El proyecto organiza el código separando las vistas, la lógica de presentación, el acceso a datos y las entidades del dominio. En las pantallas se utiliza una estructura basada en **MVP (Model-View-Presenter)**.

```text
src/
├── main/
│   ├── java/angel/tfg/pymeris/
│   │   ├── Base/          # Clases e interfaces comunes
│   │   ├── Model/
│   │   │   ├── DAO/       # Acceso directo a SQLite
│   │   │   ├── Entities/  # Entidades del dominio
│   │   │   └── Repository/# Abstracción del acceso a datos
│   │   ├── Util/          # Base de datos, sesión y utilidades
│   │   ├── Views/         # Controladores, contratos y presentadores
│   │   ├── Launcher.java  # Punto de entrada
│   │   └── PymerisApp.java
│   └── resources/
│       ├── Imagenes/      # Recursos gráficos
│       └── Vistas/        # Interfaces FXML
└── test/
    └── java/              # Pruebas unitarias
```

## Requisitos

- JDK 21.
- IntelliJ IDEA u otro IDE compatible con proyectos Maven y JavaFX.
- Conexión a Internet la primera vez que Maven necesite descargar las dependencias.

El repositorio incluye Maven Wrapper, por lo que no es necesario tener Maven instalado globalmente.

## Obtener el proyecto

```bash
git clone https://github.com/KorryKK/Pymeris.git
cd Pymeris
```

Abre la carpeta del proyecto desde tu IDE y espera a que Maven importe y descargue las dependencias.

## Compilación y ejecución

Para generar el paquete Maven en Windows:

```bat
mvnw.cmd clean package
```

En Linux o macOS:

```bash
./mvnw clean package
```

Para iniciar la aplicación desde IntelliJ IDEA, ejecuta la clase `angel.tfg.pymeris.Launcher`.

> **Nota sobre la ejecución:** la configuración actual del objetivo `javafx:run` en `pom.xml` todavía referencia `HelloApplication`, clase que no forma parte del repositorio. Por eso, las instrucciones anteriores indican el inicio desde el IDE mediante `Launcher` y no dan por hecho que `mvn javafx:run` funcione.

## Base de datos

Pymeris utiliza SQLite. Al iniciar la aplicación, el código prepara la carpeta y las tablas de la base de datos en el directorio personal del usuario:

```text
<carpeta-del-usuario>/Pymeris/database_protecto.db
```

La estructura contempla tablas para usuarios, clientes, productos, pedidos y detalles de pedidos.

## Pruebas

El proyecto incluye pruebas unitarias con **JUnit 5** y **Mockito** dentro de `src/test/java`.

Se pueden intentar ejecutar desde la raíz del proyecto con Maven Wrapper:

```bat
mvnw.cmd test
```

En Linux o macOS:

```bash
./mvnw test
```

La presencia de las pruebas en el repositorio no implica que se haya verificado su ejecución satisfactoria en todos los entornos.

## Seguridad y uso

Este repositorio corresponde a un proyecto académico y debe utilizarse con datos de prueba. Antes de utilizar o distribuir la aplicación en un entorno real, es necesario revisar y reforzar la configuración de las credenciales iniciales, los procesos de restablecimiento de contraseñas y la gestión de errores y conexiones a la base de datos.

No introduzcas información personal, credenciales reales ni datos de clientes en una instalación de prueba.

## Autor

**Ángel Hormigo Gila**  
GitHub: [@KorryKK](https://github.com/KorryKK)
