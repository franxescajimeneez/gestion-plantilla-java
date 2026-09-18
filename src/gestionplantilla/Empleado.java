package gestionplantilla;

public abstract class Empleado {

	private String dni;
	private String nombre;
	private double sueldo;

	public Empleado(String dni, String nombre, double sueldo) throws SalarioInvalidoException {

		validarSueldo(sueldo);

		this.dni = dni;
		this.nombre = nombre;
		this.sueldo = sueldo;
	}
	public String getDni() {
		return dni;
	}
	public String getNombre() {
		return nombre;
	}
	public double getSueldo() {
		return sueldo;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setSueldo(double sueldo) throws SalarioInvalidoException {
		validarSueldo(sueldo);

		this.sueldo = sueldo;
	}

	private void validarSueldo(double sueldo) throws SalarioInvalidoException {
		if (!Double.isFinite(sueldo) || sueldo <= 0) {
			throw new SalarioInvalidoException(sueldo);
		}
	}
	public abstract double calcularSalarioTotal();

	public abstract String rolPrincipal();

	public abstract String toCSV();

	@Override
	public String toString() {
	    return String.format(
	            "%s - %s - %.2f €",
	            dni,
	            nombre,
	            calcularSalarioTotal());
	}
}
