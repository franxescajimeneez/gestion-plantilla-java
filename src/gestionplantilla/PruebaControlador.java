package gestionplantilla;

import java.util.List;

public class PruebaControlador {

    public static void main(String[] args) {
        try {
            EmpleadoDAO dao = new EmpleadoJdbcDAO();
            ControladorPlantilla controlador =
                    new ControladorPlantilla(dao);

            controlador.inicializar();

            mostrarPlantilla(controlador);
            probarFiltro(controlador);
            mostrarRolesPresentes(controlador);
            mostrarSalarioMedio(controlador);
            probarDniDuplicado(controlador);
            probarValidacionNombres(controlador);
            probarValidacionEspecialidades(controlador);

        } catch (Exception e) {
            System.err.println(
                    "Error inesperado durante la prueba: "
                    + e.getMessage());

            e.printStackTrace();
        }
    }

    private static void mostrarPlantilla(
            ControladorPlantilla controlador)
            throws Exception {

        System.out.println("PLANTILLA DESDE EL CONTROLADOR");
        System.out.println("-----------------------------");

        List<Empleado> empleados =
                controlador.listarTodos();

        for (Empleado empleado : empleados) {
            System.out.println(empleado);
        }
    }

    private static void probarFiltro(
            ControladorPlantilla controlador)
            throws Exception {

        System.out.println();
        System.out.println("FILTRO POR NOMBRE: empleado");
        System.out.println("---------------------------");

        List<Empleado> coincidencias =
                controlador.filtrarPorNombre("empleado");

        for (Empleado empleado : coincidencias) {
            System.out.println(empleado);
        }
    }
    private static void mostrarRolesPresentes(
            ControladorPlantilla controlador)
            throws Exception {

        System.out.println();
        System.out.println(
                "Tipos presentes: "
                + controlador.obtenerRolesPresentes());
    }
    private static void mostrarSalarioMedio(
            ControladorPlantilla controlador)
            throws Exception {

        double salarioMedio =
                controlador.calcularSalarioMedio();

        System.out.println();
        System.out.printf(
                "Salario medio de la plantilla: %.2f €%n",
                salarioMedio);
    }

    private static void probarDniDuplicado(
            ControladorPlantilla controlador) {

        try {
            controlador.altaComercial(
                    "11111111A",
                    "Empleado duplicado",
                    1300,
                    100);

        } catch (IllegalArgumentException e) {
            System.out.println();
            System.out.println(
                    "Validación correcta: "
                    + e.getMessage());

        } catch (Exception e) {
            System.err.println(
                    "Error de persistencia: "
                    + e.getMessage());
        }
    }

    private static void probarValidacionNombres(
            ControladorPlantilla controlador) throws Exception {

        System.out.println();
        System.out.println("VALIDACIÓN DE NOMBRES");
        System.out.println("---------------------");

        probarNombreValido(controlador, "60000001A", "María");
        probarNombreValido(controlador, "60000002A", "María del Mar");
        probarNombreValido(controlador, "60000003A", "Anne-Marie O’Connor");
        probarNombreValido(controlador, "60000004A", "Maria1");
        probarNombreValido(controlador, "60000005A", "Maria2");

        probarNombreInvalidoEnAlta(controlador, "60000006A", "123456");
        probarNombreInvalidoEnAlta(controlador, "60000007A", "0000");
        probarNombreInvalidoEnAlta(controlador, "60000008A", "   ");
        probarNombresEnModificacion(controlador);
    }

    private static void probarNombreValido(
            ControladorPlantilla controlador,
            String dni,
            String nombre) throws Exception {

        eliminarSiExiste(controlador, dni);

        try {
            controlador.altaTecnico(dni, nombre, 1500, "Pruebas");
            System.out.println("Nombre válido aceptado: " + nombre);
        } finally {
            eliminarSiExiste(controlador, dni);
        }
    }

    private static void probarNombreInvalidoEnAlta(
            ControladorPlantilla controlador,
            String dni,
            String nombre) throws Exception {

        eliminarSiExiste(controlador, dni);

        try {
            controlador.altaTecnico(dni, nombre, 1500, "Pruebas");
            throw new AssertionError("Se aceptó un nombre inválido: " + nombre);
        } catch (IllegalArgumentException e) {
            System.out.println("Nombre inválido rechazado: " + representar(nombre));
        } finally {
            eliminarSiExiste(controlador, dni);
        }
    }

    private static void probarNombresEnModificacion(
            ControladorPlantilla controlador) throws Exception {

        String dni = "60000009A";
        eliminarSiExiste(controlador, dni);

        try {
            controlador.altaTecnico(dni, "Nombre Válido", 1500, "Pruebas");
            Empleado empleado = controlador.buscarPorDni(dni);
            empleado.setNombre("Maria2");
            controlador.actualizar(empleado);
            System.out.println("Nombre alfanumérico aceptado al modificar: Maria2");

            empleado.setNombre("123456");

            try {
                controlador.actualizar(empleado);
                throw new AssertionError("La modificación aceptó un nombre sin letras.");
            } catch (IllegalArgumentException e) {
                System.out.println("Nombre inválido rechazado al modificar: 123456");
            }
        } finally {
            eliminarSiExiste(controlador, dni);
        }
    }

    private static void eliminarSiExiste(
            ControladorPlantilla controlador,
            String dni) throws Exception {

        try {
            controlador.eliminar(dni);
        } catch (IllegalArgumentException e) {
            // El empleado no existe; no es necesario limpiar nada.
        }
    }

    private static String representar(String nombre) {
        return nombre.trim().isEmpty() ? "<vacío>" : nombre;
    }

    private static void probarValidacionEspecialidades(
            ControladorPlantilla controlador) throws Exception {

        System.out.println();
        System.out.println("VALIDACIÓN DE ESPECIALIDADES");
        System.out.println("----------------------------");

        probarEspecialidadValida(controlador, "70000001A", "Java 21");
        probarEspecialidadValida(controlador, "70000002A", "Soporte L2");
        probarEspecialidadInvalidaEnAlta(controlador, "70000003A", "200");
        probarEspecialidadInvalidaEnAlta(controlador, "70000004A", "   ");
        probarEspecialidadesEnModificacion(controlador);
    }

    private static void probarEspecialidadValida(
            ControladorPlantilla controlador,
            String dni,
            String especialidad) throws Exception {

        eliminarSiExiste(controlador, dni);

        try {
            controlador.altaTecnico(dni, "Técnico Temporal", 1500, especialidad);
            System.out.println("Especialidad válida aceptada: " + especialidad);
        } finally {
            eliminarSiExiste(controlador, dni);
        }
    }

    private static void probarEspecialidadInvalidaEnAlta(
            ControladorPlantilla controlador,
            String dni,
            String especialidad) throws Exception {

        eliminarSiExiste(controlador, dni);

        try {
            controlador.altaTecnico(dni, "Técnico Temporal", 1500, especialidad);
            throw new AssertionError("Se aceptó una especialidad inválida: " + especialidad);
        } catch (IllegalArgumentException e) {
            System.out.println("Especialidad inválida rechazada: " + representar(especialidad));
        } finally {
            eliminarSiExiste(controlador, dni);
        }
    }

    private static void probarEspecialidadesEnModificacion(
            ControladorPlantilla controlador) throws Exception {

        String dni = "70000005A";
        eliminarSiExiste(controlador, dni);

        try {
            controlador.altaTecnico(dni, "Técnico Temporal", 1500, "Sistemas");
            Tecnico tecnico = (Tecnico) controlador.buscarPorDni(dni);
            tecnico.setEspecialidad("Soporte L2");
            controlador.actualizar(tecnico);
            System.out.println("Especialidad alfanumérica aceptada al modificar: Soporte L2");

            tecnico.setEspecialidad("200");

            try {
                controlador.actualizar(tecnico);
                throw new AssertionError("La modificación aceptó una especialidad sin letras.");
            } catch (IllegalArgumentException e) {
                System.out.println("Especialidad inválida rechazada al modificar: 200");
            }
        } finally {
            eliminarSiExiste(controlador, dni);
        }
    }
}
