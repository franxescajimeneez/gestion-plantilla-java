# Decisiones técnicas


## Persistencia

La aplicación utiliza JDBC con una base de datos SQLite. JDBC permite trabajar con una base de datos relacional mediante las API estándar de Java, mientras que SQLite no requiere instalar ni configurar un servidor independiente. La información se almacena localmente en el archivo `plantilla.db`.

La persistencia se encapsula mediante la interfaz `EmpleadoDAO` y su implementación `EmpleadoJdbcDAO`. Esta clase contiene las operaciones necesarias para guardar, consultar, listar, modificar y eliminar empleados. Las instrucciones SQL utilizan `PreparedStatement`, evitando la concatenación directa de datos.


## Modelo de empleados

El modelo parte de la clase abstracta `Empleado`, que contiene los atributos comunes: DNI, nombre y sueldo. Esta clase define los métodos abstractos `calcularSalarioTotal()`, `rolPrincipal()` y `toCSV()`.

Las clases `Comercial` y `Tecnico` heredan de `Empleado`. `Comercial` incorpora una comisión y calcula su salario total sumándola al sueldo base. `Tecnico` incorpora una especialidad y mantiene el sueldo base como salario total. Ambas clases implementan los comportamientos definidos por el modelo, lo que permite trabajar con una colección `List<Empleado>` y obtener resultados diferentes según el tipo de cada objeto.


## Validación del sueldo

`SalarioInvalidoException` es una excepción comprobada que representa una regla del dominio: el sueldo debe ser un número finito mayor que cero. La validación está centralizada en `Empleado`, por lo que se aplica tanto a comerciales como a técnicos al crear un empleado o modificar su sueldo.


## Posibles mejoras

El proyecto incluye comprobaciones manuales, pero no una batería de pruebas automatizadas. Una posible evolución sería incorporar pruebas del modelo, el controlador y el DAO. Otras mejoras posibles son ampliar la validación del DNI o añadir filtros adicionales, manteniendo el alcance actual de la aplicación.
