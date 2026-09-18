package gestionplantilla;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoJdbcDAO implements EmpleadoDAO {

	@Override
	public void guardar(Empleado empleado) throws Exception {
		String sql = "INSERT INTO empleados " + "(dni, nombre, sueldo, tipo, comision, especialidad) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conexion = ConexionBD.obtenerConexion();
				PreparedStatement sentencia = conexion.prepareStatement(sql)) {

			sentencia.setString(1, empleado.getDni());
			asignarDatosComunes(sentencia, empleado, 2);
			asignarDatosEspecificos(sentencia, empleado, 4);

			sentencia.executeUpdate();
		}
	}

	@Override
	public List<Empleado> listarTodos() throws Exception {
		String sql = "SELECT dni, nombre, sueldo, tipo, " + "comision, especialidad "
				+ "FROM empleados ORDER BY nombre";

		List<Empleado> empleados = new ArrayList<>();

		try (Connection conexion = ConexionBD.obtenerConexion();
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				ResultSet resultados = sentencia.executeQuery()) {

			while (resultados.next()) {
				empleados.add(crearEmpleado(resultados));
			}
		}

		return empleados;
	}

	@Override
	public Empleado buscarPorDni(String dni) throws Exception {
		String sql = "SELECT dni, nombre, sueldo, tipo, " + "comision, especialidad " + "FROM empleados WHERE dni = ?";

		try (Connection conexion = ConexionBD.obtenerConexion();
				PreparedStatement sentencia = conexion.prepareStatement(sql)) {

			sentencia.setString(1, dni);

			try (ResultSet resultado = sentencia.executeQuery()) {
				if (resultado.next()) {
					return crearEmpleado(resultado);
				}
			}
		}

		return null;
	}

	@Override
	public void actualizar(Empleado empleado) throws Exception {
		String sql = "UPDATE empleados SET " + "nombre = ?, sueldo = ?, tipo = ?, " + "comision = ?, especialidad = ? "
				+ "WHERE dni = ?";

		try (Connection conexion = ConexionBD.obtenerConexion();
				PreparedStatement sentencia = conexion.prepareStatement(sql)) {

			asignarDatosComunes(sentencia, empleado, 1);
			asignarDatosEspecificos(sentencia, empleado, 3);
			sentencia.setString(6, empleado.getDni());

			sentencia.executeUpdate();
		}
	}

	@Override
	public void eliminar(String dni) throws Exception {
		String sql = "DELETE FROM empleados WHERE dni = ?";

		try (Connection conexion = ConexionBD.obtenerConexion();
				PreparedStatement sentencia = conexion.prepareStatement(sql)) {

			sentencia.setString(1, dni);
			sentencia.executeUpdate();
		}
	}

	private void asignarDatosComunes(PreparedStatement sentencia, Empleado empleado, int posicionInicial)
			throws SQLException {

		sentencia.setString(posicionInicial, empleado.getNombre());

		sentencia.setDouble(posicionInicial + 1, empleado.getSueldo());
	}

	private void asignarDatosEspecificos(PreparedStatement sentencia, Empleado empleado, int posicionInicial)
			throws SQLException {

		if (empleado instanceof Comercial) {
			Comercial comercial = (Comercial) empleado;

			sentencia.setString(posicionInicial, "COMERCIAL");
			sentencia.setDouble(posicionInicial + 1, comercial.getComision());
			sentencia.setNull(posicionInicial + 2, Types.VARCHAR);

		} else if (empleado instanceof Tecnico) {
			Tecnico tecnico = (Tecnico) empleado;

			sentencia.setString(posicionInicial, "TECNICO");
			sentencia.setNull(posicionInicial + 1, Types.REAL);
			sentencia.setString(posicionInicial + 2, tecnico.getEspecialidad());

		} else {
			throw new SQLException("Tipo de empleado no reconocido.");
		}
	}

	private Empleado crearEmpleado(ResultSet resultado) throws SQLException, SalarioInvalidoException {

		String dni = resultado.getString("dni");
		String nombre = resultado.getString("nombre");
		double sueldo = resultado.getDouble("sueldo");
		String tipo = resultado.getString("tipo");

		if ("COMERCIAL".equals(tipo)) {
			double comision = resultado.getDouble("comision");

			return new Comercial(dni, nombre, sueldo, comision);
		}

		if ("TECNICO".equals(tipo)) {
			String especialidad = resultado.getString("especialidad");

			return new Tecnico(dni, nombre, sueldo, especialidad);
		}

		throw new SQLException("Tipo de empleado desconocido en la base de datos: " + tipo);
	}
}