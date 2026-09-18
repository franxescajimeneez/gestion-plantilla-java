package gestionplantilla;

import java.util.List;

public class PruebaFase2 {

    public static void main(String[] args) {
        try {
            ConexionBD.inicializar();

            EmpleadoDAO dao = new EmpleadoJdbcDAO();

            Empleado comercial = new Comercial(
                    "11111111A",
                    "Empleado Comercial",
                    1400,
                    250);

            Empleado tecnico = new Tecnico(
                    "22222222A",
                    "Empleado Técnico",
                    1600,
                    "Sistemas");

            guardarSiNoExiste(dao, comercial);
            guardarSiNoExiste(dao, tecnico);

            probarEmpleadoTemporal(dao);
            mostrarPlantilla(dao.listarTodos());

        } catch (Exception e) {
            System.err.println(
                    "Error durante la prueba de persistencia: "
                    + e.getMessage());

            e.printStackTrace();
        }
    }

    private static void guardarSiNoExiste(
            EmpleadoDAO dao,
            Empleado empleado) throws Exception {

        if (dao.buscarPorDni(empleado.getDni()) == null) {
            dao.guardar(empleado);

            System.out.println(
                    "Empleado guardado: "
                    + empleado.getNombre());
        } else {
            System.out.println(
                    "El empleado ya estaba registrado: "
                    + empleado.getNombre());
        }
    }

    private static void mostrarPlantilla(
            List<Empleado> empleados) {

        System.out.println();
        System.out.println("EMPLEADOS GUARDADOS");
        System.out.println("-------------------");

        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        for (Empleado empleado : empleados) {
            System.out.println(empleado);
        }
    }
    private static void probarEmpleadoTemporal(
            EmpleadoDAO dao) throws Exception {

        String dniTemporal = "55555555A";

        // Limpieza preventiva para que la prueba pueda repetirse.
        if (dao.buscarPorDni(dniTemporal) != null) {
            dao.eliminar(dniTemporal);
        }

        Tecnico temporal = new Tecnico(
                dniTemporal,
                "Empleado temporal",
                1200,
                "Soporte");

        dao.guardar(temporal);
        System.out.println();
        System.out.println(
                "Empleado temporal guardado: "
                + dao.buscarPorDni(dniTemporal));

        temporal.setNombre("Empleado temporal modificado");
        temporal.setSueldo(1350);
        temporal.setEspecialidad("Administración de sistemas");

        dao.actualizar(temporal);

        System.out.println(
                "Empleado temporal actualizado: "
                + dao.buscarPorDni(dniTemporal));

        dao.eliminar(dniTemporal);

        if (dao.buscarPorDni(dniTemporal) == null) {
            System.out.println(
                    "Empleado temporal eliminado correctamente.");
        }
    }
}
