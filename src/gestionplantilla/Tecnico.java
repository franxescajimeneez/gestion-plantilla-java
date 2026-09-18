package gestionplantilla;

public class Tecnico extends Empleado {

	private String especialidad;

	public Tecnico(
			String dni,
			String nombre,
			double sueldo,
			String especialidad) throws SalarioInvalidoException {

		super(dni, nombre, sueldo);
		this.especialidad = especialidad;
	}
	public String getEspecialidad() {
		return especialidad;
	}
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
	@Override
	public double calcularSalarioTotal() {
		return getSueldo();
	}
	@Override
	public String rolPrincipal() {
		return "Técnico";
	}
	@Override
	public String toCSV() {
		return "TECNICO;"
				+ getDni() + ";"
				+ getNombre() + ";"
				+ getSueldo() + ";"
				+ especialidad;
	}
	@Override
	public String toString() {
	    return "Técnico -> "
	            + super.toString()
	            + " - Especialidad: "
	            + especialidad;
	}
}
