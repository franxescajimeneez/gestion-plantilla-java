package gestionplantilla;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class ConexionBD {

	private static final String URL = "jdbc:sqlite:plantilla.db";

	private ConexionBD() {

	}
	public static Connection obtenerConexion() throws SQLException {
		return DriverManager.getConnection(URL);

	}
	public static void inicializar() throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS empleados "
		        + "(dni TEXT PRIMARY KEY NOT NULL, "
		        + "nombre TEXT NOT NULL, "
		        + "sueldo REAL NOT NULL CHECK (sueldo > 0), "
		        + "tipo TEXT NOT NULL CHECK (tipo IN ('COMERCIAL', 'TECNICO')), "
		        + "comision REAL, "
		        + "especialidad TEXT)";

		try (Connection conexion = obtenerConexion();
				Statement sentencia = conexion.createStatement()) {
			sentencia.executeUpdate(sql);
		}
	}
}
