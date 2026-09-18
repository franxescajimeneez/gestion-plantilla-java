package gestionplantilla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class EmpleadoTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = {
            "DNI",
            "Nombre",
            "Tipo",
            "Sueldo base",
            "Información adicional",
            "Salario total"
    };

    private List<Empleado> empleados = new ArrayList<>();

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = new ArrayList<>(empleados);
        fireTableDataChanged();
    }

    public Empleado getEmpleado(int fila) {
        return empleados.get(fila);
    }

    @Override
    public int getRowCount() {
        return empleados.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int columna) {
        return COLUMNAS[columna];
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Empleado empleado = empleados.get(fila);

        switch (columna) {
        case 0:
            return empleado.getDni();
        case 1:
            return empleado.getNombre();
        case 2:
            return empleado.rolPrincipal();
        case 3:
            return empleado.getSueldo();
        case 4:
            return obtenerInformacionAdicional(empleado);
        case 5:
            return empleado.calcularSalarioTotal();
        default:
            return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columna) {
        if (columna == 3 || columna == 5) {
            return Double.class;
        }

        return String.class;
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }

    private String obtenerInformacionAdicional(
            Empleado empleado) {

        if (empleado instanceof Comercial) {
            Comercial comercial = (Comercial) empleado;

            return String.format(
                    "Comisión: %.2f €",
                    comercial.getComision());
        }

        if (empleado instanceof Tecnico) {
            Tecnico tecnico = (Tecnico) empleado;
            return "Especialidad: " + tecnico.getEspecialidad();
        }

        return "";
    }
}