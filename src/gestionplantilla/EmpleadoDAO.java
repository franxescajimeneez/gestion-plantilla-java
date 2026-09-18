package gestionplantilla;

import java.util.List;

public interface EmpleadoDAO {

	void guardar(Empleado empleado) throws Exception;

	List<Empleado> listarTodos() throws Exception;

	Empleado buscarPorDni(String dni) throws Exception;

	void actualizar(Empleado empleado) throws Exception;

	void eliminar(String dni) throws Exception;
}
