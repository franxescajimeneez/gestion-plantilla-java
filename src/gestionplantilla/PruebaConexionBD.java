package gestionplantilla;
import java.sql.SQLException;
public class PruebaConexionBD {

	public static void main(String[] args) {
		try {
			ConexionBD.inicializar();
			System.out.println("Base de datos inicializada correctamente.");

		} catch (SQLException e) {
			System.err.println("No se pudo inicializar la base de datos: " + e.getMessage());

			e.printStackTrace();
		}

	}

}
