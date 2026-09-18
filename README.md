# Gestión de Plantilla

Aplicación de escritorio en Java para administrar una plantilla de empleados. Permite trabajar con perfiles comerciales y técnicos mediante una interfaz gráfica Swing y conserva la información en una base de datos SQLite local.

## Vista de la aplicación

![Interfaz de Gestión de Plantilla](docs/gestion-plantilla.png)

*Datos ficticios de demostración.*

## Funcionalidades

- Alta, consulta, modificación y eliminación de empleados.
- Distinción entre comerciales (con comisión) y técnicos (con especialidad).
- Búsqueda de empleados por nombre.
- Cálculo del salario total y del salario medio de la plantilla.
- Validación de campos, sueldo, comisión y duplicidad del DNI.
- Persistencia local de los datos entre ejecuciones.

## Tecnologías

- Java 21
- Java Swing
- JDBC
- SQLite
- Maven

## Estructura

```text
src/gestionplantilla/
├── Empleado.java, Comercial.java, Tecnico.java  # Modelo
├── EmpleadoDAO.java, EmpleadoJdbcDAO.java       # Persistencia
├── ConexionBD.java                              # Conexión e inicialización
├── ControladorPlantilla.java                    # Lógica de aplicación
├── EmpleadoTableModel.java                      # Adaptador de tabla Swing
└── VentanaPrincipal.java                        # Interfaz gráfica y entrada
```

Las clases `PruebaFase1`, `PruebaConexionBD`, `PruebaFase2` y `PruebaControlador` son comprobaciones manuales incluidas en el desarrollo original; no constituyen una batería de pruebas automatizadas.

## Requisitos

- JDK 21
- Maven 3.9 o posterior

## Ejecución

Desde la raíz del proyecto:

```bash
mvn clean compile
mvn exec:java
```

La aplicación crea automáticamente `plantilla.db` en el directorio desde el que se ejecuta. Este archivo contiene los datos locales y no se versiona.

También puede importarse en Eclipse como proyecto Maven existente. Los archivos `.project`, `.classpath` y `.settings/` incluyen la configuración necesaria para trabajar con Java 21 y Maven en Eclipse.

## Documentación adicional

- [`MEMORIA.md`](MEMORIA.md): decisiones de diseño y alcance.
- [`GUIA_DEMOSTRACION.md`](GUIA_DEMOSTRACION.md): recorrido de demostración funcional.
