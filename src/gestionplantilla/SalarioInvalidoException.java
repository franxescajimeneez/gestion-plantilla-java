package gestionplantilla;

public class SalarioInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public SalarioInvalidoException(double sueldo) {
        super("El sueldo debe ser un número finito mayor que cero. Valor recibido: " + sueldo);
    }
}
