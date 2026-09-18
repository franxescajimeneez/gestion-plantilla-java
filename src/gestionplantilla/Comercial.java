package gestionplantilla;

public class Comercial extends Empleado {

	private double comision;

	public Comercial(String dni, String nombre, double sueldo, double comision) throws SalarioInvalidoException {
		super(dni, nombre, sueldo);
		this.comision = comision;
	}
	public double getComision() {
		return comision;
	}
	public void setComision(double comision) {
		this.comision = comision;
	}
	@Override
	public double calcularSalarioTotal() {
		return getSueldo() + comision;
	}
	@Override
	public String rolPrincipal() {
		return "Comercial";
	}
	@Override
	public String toCSV() {
		return "COMERCIAL;"
				+ getDni() + ";"
				+ getNombre() + ";"
				+ getSueldo() + ";"
				+ comision;
	}
	@Override
	public String toString() {
	    return "Comercial -> "
	            + super.toString()
	            + String.format(
	                    " - Comisión: %.2f €",
	                    comision);
	}
}
