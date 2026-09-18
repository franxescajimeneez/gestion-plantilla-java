package gestionplantilla;

import java.util.ArrayList;
import java.util.List;

public class PruebaFase1 {

	public static void main(String[] args) throws SalarioInvalidoException {

		List<Empleado> plantilla = new ArrayList<>();

		plantilla.add(new Comercial("11111111A", "Empleado Comercial", 1400, 250));

		plantilla.add(new Tecnico("22222222A", "Empleado Técnico", 1600, "Sistemas"));

		System.out.println("PLANTILLA DE EMPLEADOS");
		System.out.println("----------------------");

		for (Empleado empleado : plantilla) {
			mostrarEmpleado(empleado);
		}
		probarSueldoInvalido();
	}

	private static void mostrarEmpleado(Empleado empleado) {
		System.out.println("DNI: " + empleado.getDni());
		System.out.println("Nombre: " + empleado.getNombre());
		System.out.println("Rol: " + empleado.rolPrincipal());

		System.out.printf("Sueldo base: %.2f €%n", empleado.getSueldo());

		System.out.printf("Salario total: %.2f €%n", empleado.calcularSalarioTotal());

		System.out.println("CSV: " + empleado.toCSV());
		System.out.println();

	}

	private static void probarSueldoInvalido() {

		try {
			new Tecnico("33333333A", "Empleado inválido", -500, "Pruebas");

		} catch (SalarioInvalidoException e) {
			System.out.println("Error controlado: " + e.getMessage());
		}
	}
}
