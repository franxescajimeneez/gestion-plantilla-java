package gestionplantilla;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.LinkedHashSet;
import java.util.Set;

public class ControladorPlantilla {

	private final EmpleadoDAO dao;

	public ControladorPlantilla(EmpleadoDAO dao) {
		this.dao = Objects.requireNonNull(dao, "El DAO no puede ser nulo.");
	}

	public void inicializar() throws Exception {
		ConexionBD.inicializar();
	}

	public void altaComercial(String dni, String nombre, double sueldo, double comision) throws Exception {
		Empleado comercial = crearComercialValidado(dni, nombre, sueldo, comision);

		comprobarDniDisponible(comercial.getDni());

		dao.guardar(comercial);
	}

	public void altaTecnico(String dni, String nombre, double sueldo, String especialidad) throws Exception {
		Empleado tecnico = crearTecnicoValidado(dni, nombre, sueldo, especialidad);

		comprobarDniDisponible(tecnico.getDni());

		dao.guardar(tecnico);
	}

	public List<Empleado> listarTodos() throws Exception {
		return dao.listarTodos();
	}
	public Set<String> obtenerRolesPresentes()
	        throws Exception {

	    Set<String> roles = new LinkedHashSet<>();

	    for (Empleado empleado : dao.listarTodos()) {
	        roles.add(empleado.rolPrincipal());
	    }

	    return roles;
	}
	public Empleado buscarPorDni(String dni) throws Exception {
		String dniValidado = validarDni(dni);
		Empleado empleado = dao.buscarPorDni(dniValidado);

		if (empleado == null) {
			throw new IllegalArgumentException("No existe un empleado con DNI " + dniValidado + ".");
		}

		return empleado;
	}

	public void actualizar(Empleado empleado) throws Exception {
		Objects.requireNonNull(empleado, "El empleado no puede ser nulo.");

		Empleado empleadoValidado;

		if (empleado instanceof Comercial) {
			Comercial comercial = (Comercial) empleado;
			empleadoValidado = crearComercialValidado(comercial.getDni(), comercial.getNombre(),
					comercial.getSueldo(), comercial.getComision());
		} else if (empleado instanceof Tecnico) {
			Tecnico tecnico = (Tecnico) empleado;
			empleadoValidado = crearTecnicoValidado(tecnico.getDni(), tecnico.getNombre(), tecnico.getSueldo(),
					tecnico.getEspecialidad());
		} else {
			throw new IllegalArgumentException("Tipo de empleado no reconocido.");
		}

		if (dao.buscarPorDni(empleadoValidado.getDni()) == null) {
			throw new IllegalArgumentException("No se puede modificar un empleado inexistente.");
		}

		dao.actualizar(empleadoValidado);
	}

	public void eliminar(String dni) throws Exception {
		String dniValidado = validarDni(dni);

		if (dao.buscarPorDni(dniValidado) == null) {
			throw new IllegalArgumentException("No existe un empleado con DNI " + dniValidado + ".");
		}

		dao.eliminar(dniValidado);
	}

	public List<Empleado> filtrarPorNombre(String texto) throws Exception {

		String filtro = texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);

		if (filtro.isEmpty()) {
			return listarTodos();
		}

		List<Empleado> coincidencias = new ArrayList<>();

		for (Empleado empleado : dao.listarTodos()) {
			String nombre = empleado.getNombre().toLowerCase(Locale.ROOT);

			if (nombre.contains(filtro)) {
				coincidencias.add(empleado);
			}
		}

		return coincidencias;
	}

	public double calcularSalarioMedio() throws Exception {
		List<Empleado> empleados = dao.listarTodos();

		if (empleados.isEmpty()) {
			return 0;
		}

		double suma = 0;

		for (Empleado empleado : empleados) {
			suma += empleado.calcularSalarioTotal();
		}

		return suma / empleados.size();
	}

	private void comprobarDniDisponible(String dni) throws Exception {

		if (dao.buscarPorDni(dni) != null) {
			throw new IllegalArgumentException("Ya existe un empleado con DNI " + dni + ".");
		}
	}

	private Comercial crearComercialValidado(String dni, String nombre, double sueldo, double comision)
			throws SalarioInvalidoException {
		String dniValidado = validarDni(dni);
		String nombreValidado = validarNombre(nombre);

		if (!Double.isFinite(comision) || comision < 0) {
			throw new IllegalArgumentException("La comisión debe ser un número finito igual o mayor que cero.");
		}

		return new Comercial(dniValidado, nombreValidado, sueldo, comision);
	}

	private Tecnico crearTecnicoValidado(String dni, String nombre, double sueldo, String especialidad)
			throws SalarioInvalidoException {
		String dniValidado = validarDni(dni);
		String nombreValidado = validarNombre(nombre);
		String especialidadValidada = validarEspecialidad(especialidad);

		return new Tecnico(dniValidado, nombreValidado, sueldo, especialidadValidada);
	}

	private String validarDni(String dni) {
		String valor = validarTexto(dni, "DNI").toUpperCase(Locale.ROOT);

		if (!valor.matches("\\d{8}[A-Z]")) {
			throw new IllegalArgumentException("El DNI debe contener ocho números " + "y una letra.");
		}

		return valor;
	}

	private String validarNombre(String nombre) {
		String valor = validarTexto(nombre, "nombre");

		boolean nombreValido = valor.codePoints().anyMatch(Character::isLetter);

		if (!nombreValido) {
			throw new IllegalArgumentException("Introduce un nombre válido. Debe contener al menos una letra.");
		}

		return valor;
	}

	private String validarEspecialidad(String especialidad) {
		String valor = validarTexto(especialidad, "especialidad");

		if (valor.codePoints().noneMatch(Character::isLetter)) {
			throw new IllegalArgumentException(
					"Introduce una especialidad válida. Debe contener al menos una letra.");
		}

		return valor;
	}

	private String validarTexto(String valor, String nombreCampo) {

		if (valor == null || valor.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo " + nombreCampo + " no puede estar vacío.");
		}

		return valor.trim();
	}
}
