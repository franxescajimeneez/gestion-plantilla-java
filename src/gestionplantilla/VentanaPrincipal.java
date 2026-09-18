package gestionplantilla;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Color AZUL = new Color(37, 78, 120);
	private static final Color AZUL_CLARO = new Color(232, 240, 248);
	private static final Color FONDO = new Color(245, 247, 250);
	private static final Color VERDE = new Color(46, 125, 80);
	private static final Color ROJO = new Color(176, 54, 54);

	private final ControladorPlantilla controlador;
	private final EmpleadoTableModel modeloTabla;

	private final JTextField campoDni = new JTextField(18);
	private final JTextField campoNombre = new JTextField(18);
	private final JTextField campoSueldo = new JTextField(18);
	private final JTextField campoDatoEspecifico = new JTextField(18);
	private final JTextField campoFiltro = new JTextField(20);

	private final JComboBox<String> selectorTipo = new JComboBox<>(new String[] { "Comercial", "Técnico" });

	private final JLabel etiquetaDatoEspecifico = new JLabel("Comisión:");

	private final JLabel etiquetaSalarioMedio = new JLabel("Salario medio: 0,00 €");

	private final JTable tabla;

	public VentanaPrincipal(ControladorPlantilla controlador) {

		this.controlador = controlador;
		this.modeloTabla = new EmpleadoTableModel();
		this.tabla = new JTable(modeloTabla);

		configurarVentana();
		construirInterfaz();
		configurarEventos();
		refrescarTabla();
	}

	private void configurarVentana() {
		setTitle("Gestión de Plantilla");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1000, 620));
		setSize(1180, 700);
		setLocationRelativeTo(null);

		getContentPane().setBackground(FONDO);
		getContentPane().setLayout(new BorderLayout(16, 16));

		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
	}

	private void construirInterfaz() {
		add(crearCabecera(), BorderLayout.NORTH);
		add(crearFormulario(), BorderLayout.WEST);
		add(crearPanelTabla(), BorderLayout.CENTER);
		add(crearPie(), BorderLayout.SOUTH);
	}

	private JPanel crearCabecera() {
		JPanel panel = new JPanel(new BorderLayout(20, 10));
		panel.setBackground(AZUL);
		panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

		JLabel titulo = new JLabel("Gestión de Plantilla");
		titulo.setForeground(Color.WHITE);
		titulo.setFont(new Font("SansSerif", Font.BOLD, 26));

		JLabel subtitulo = new JLabel("Administración de empleados");
		subtitulo.setForeground(new Color(220, 230, 240));
		subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

		JPanel textos = new JPanel(new GridLayout(2, 1));
		textos.setOpaque(false);
		textos.add(titulo);
		textos.add(subtitulo);

		JPanel buscador = new JPanel(new BorderLayout(8, 0));
		buscador.setOpaque(false);
		buscador.setBorder(
		        BorderFactory.createEmptyBorder(
		                8,
		                0,
		                8,
		                0));

		campoFiltro.setPreferredSize(
		        new Dimension(255, 32));

		JLabel etiquetaBuscar = new JLabel("Buscar:");
		etiquetaBuscar.setForeground(Color.WHITE);
		etiquetaBuscar.setFont(new Font("SansSerif", Font.BOLD, 13));

		campoFiltro.setToolTipText("Filtrar empleados por nombre");

		buscador.add(etiquetaBuscar, BorderLayout.WEST);
		buscador.add(campoFiltro, BorderLayout.CENTER);

		panel.add(textos, BorderLayout.WEST);
		panel.add(buscador, BorderLayout.EAST);

		return panel;
	}

	private JPanel crearFormulario() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setPreferredSize(new Dimension(330, 0));
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(215, 220, 226)),
				BorderFactory.createEmptyBorder(18, 18, 18, 18)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 18, 0);

		JLabel titulo = new JLabel("Datos del empleado");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
		titulo.setForeground(AZUL);
		panel.add(titulo, gbc);

		gbc.gridwidth = 1;
		gbc.insets = new Insets(5, 0, 5, 10);
		gbc.weightx = 0;

		agregarCampo(panel, gbc, 1, "DNI:", campoDni);
		agregarCampo(panel, gbc, 2, "Nombre:", campoNombre);
		agregarCampo(panel, gbc, 3, "Sueldo base:", campoSueldo);
		agregarCampo(panel, gbc, 4, "Tipo:", selectorTipo);
		agregarCampo(panel, gbc, 5, etiquetaDatoEspecifico, campoDatoEspecifico);

		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel espacioFlexible = new JPanel();
		espacioFlexible.setOpaque(false);
		panel.add(espacioFlexible, gbc);

		JPanel botones = crearPanelBotones();

		gbc.gridy = 7;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(18, 0, 0, 0);
		panel.add(botones, gbc);

		return panel;
	}

	private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String texto, Component componente) {

		agregarCampo(panel, gbc, fila, new JLabel(texto), componente);
	}

	private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, JLabel etiqueta, Component componente) {

		etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
		etiqueta.setForeground(new Color(65, 70, 75));

		gbc.gridx = 0;
		gbc.gridy = fila;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(7, 0, 7, 12);
		panel.add(etiqueta, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(7, 0, 7, 0);
		panel.add(componente, gbc);
	}

	private JPanel crearPanelBotones() {
		JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
		panel.setOpaque(false);

		JButton botonNuevo = crearBoton("Nuevo", AZUL_CLARO, AZUL);

		JButton botonGuardar = crearBoton("Guardar", VERDE, Color.WHITE);

		JButton botonModificar = crearBoton("Modificar", AZUL, Color.WHITE);

		JButton botonEliminar = crearBoton("Eliminar", ROJO, Color.WHITE);

		botonNuevo.addActionListener(e -> limpiarFormulario());

		botonGuardar.addActionListener(e -> guardarEmpleado());

		botonModificar.addActionListener(e -> modificarEmpleado());

		botonEliminar.addActionListener(e -> eliminarEmpleado());

		panel.add(botonNuevo);
		panel.add(botonGuardar);
		panel.add(botonModificar);
		panel.add(botonEliminar);

		return panel;
	}

	private JButton crearBoton(String texto, Color fondo, Color primerPlano) {

		JButton boton = new JButton(texto);
		boton.setBackground(fondo);
		boton.setForeground(primerPlano);
		boton.setFocusPainted(false);
		boton.setFont(new Font("SansSerif", Font.BOLD, 12));
		boton.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

		return boton;
	}

	private JPanel crearPanelTabla() {
		configurarTabla();

		JScrollPane desplazamiento = new JScrollPane(tabla);
		desplazamiento.setBorder(BorderFactory.createLineBorder(new Color(215, 220, 226)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.add(desplazamiento, BorderLayout.CENTER);

		return panel;
	}

	private void configurarTabla() {
		tabla.setRowHeight(30);
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.setAutoCreateRowSorter(true);
		tabla.setFillsViewportHeight(true);
		tabla.setGridColor(new Color(230, 233, 237));
		tabla.setShowVerticalLines(false);
		tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));

		tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tabla.getTableHeader().setBackground(AZUL_CLARO);
		tabla.getTableHeader().setForeground(AZUL);
		tabla.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer moneda = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void setValue(Object valor) {
				if (valor instanceof Number) {
					setText(String.format("%.2f €", ((Number) valor).doubleValue()));
				} else {
					super.setValue(valor);
				}
			}
		};

		moneda.setHorizontalAlignment(SwingConstants.RIGHT);

		tabla.getColumnModel().getColumn(3).setCellRenderer(moneda);

		tabla.getColumnModel().getColumn(5).setCellRenderer(moneda);
		tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(90);
		tabla.getColumnModel().getColumn(3).setPreferredWidth(110);
		tabla.getColumnModel().getColumn(4).setPreferredWidth(210);
		tabla.getColumnModel().getColumn(5).setPreferredWidth(110);
	}

	private JPanel crearPie() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(215, 220, 226)),
				BorderFactory.createEmptyBorder(12, 16, 12, 16)));

		etiquetaSalarioMedio.setFont(new Font("SansSerif", Font.BOLD, 14));
		etiquetaSalarioMedio.setForeground(VERDE);

		JLabel estado = new JLabel("Datos guardados automáticamente en SQLite");
		estado.setForeground(new Color(100, 105, 110));

		panel.add(estado, BorderLayout.WEST);
		panel.add(etiquetaSalarioMedio, BorderLayout.EAST);

		return panel;
	}

	private void configurarEventos() {
		selectorTipo.addActionListener(e -> actualizarEtiquetaEspecifica());

		tabla.getSelectionModel().addListSelectionListener(e -> {

			if (!e.getValueIsAdjusting()) {
				cargarEmpleadoSeleccionado();
			}
		});

		campoFiltro.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				refrescarTabla();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				refrescarTabla();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				refrescarTabla();
			}
		});
	}

	private void actualizarEtiquetaEspecifica() {
		boolean comercial = selectorTipo.getSelectedIndex() == 0;

		etiquetaDatoEspecifico.setText(comercial ? "Comisión:" : "Especialidad:");

		campoDatoEspecifico.setToolTipText(comercial ? "Importe de la comisión" : "Especialidad del técnico");
	}

	private void guardarEmpleado() {
		try {
			String dni = campoDni.getText();
			String nombre = campoNombre.getText();
			double sueldo = leerNumero(campoSueldo, "sueldo");

			if (selectorTipo.getSelectedIndex() == 0) {
				double comision = leerNumero(campoDatoEspecifico, "comisión");

				controlador.altaComercial(dni, nombre, sueldo, comision);
			} else {
				controlador.altaTecnico(dni, nombre, sueldo, campoDatoEspecifico.getText());
			}

			mostrarInformacion("Empleado guardado correctamente.");

			limpiarFormulario();
			refrescarTabla();

		} catch (Exception e) {
			mostrarError(e.getMessage());
		}
	}

	private void modificarEmpleado() {
		int filaVista = tabla.getSelectedRow();

		if (filaVista < 0) {
			mostrarError("Selecciona un empleado para modificarlo.");
			return;
		}

		try {
			String dni = campoDni.getText();
			String nombre = campoNombre.getText();
			double sueldo = leerNumero(campoSueldo, "sueldo");

			Empleado empleado;

			if (selectorTipo.getSelectedIndex() == 0) {
				double comision = leerNumero(campoDatoEspecifico, "comisión");

				empleado = new Comercial(dni, nombre, sueldo, comision);
			} else {
				empleado = new Tecnico(dni, nombre, sueldo, campoDatoEspecifico.getText().trim());
			}

			controlador.actualizar(empleado);

			mostrarInformacion("Empleado modificado correctamente.");

			limpiarFormulario();
			refrescarTabla();

		} catch (Exception e) {
			mostrarError(e.getMessage());
		}
	}

	private void eliminarEmpleado() {
		int filaVista = tabla.getSelectedRow();

		if (filaVista < 0) {
			mostrarError("Selecciona un empleado para eliminarlo.");
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar al empleado seleccionado?",
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			controlador.eliminar(campoDni.getText());

			mostrarInformacion("Empleado eliminado correctamente.");

			limpiarFormulario();
			refrescarTabla();

		} catch (Exception e) {
			mostrarError(e.getMessage());
		}
	}

	private void cargarEmpleadoSeleccionado() {
		int filaVista = tabla.getSelectedRow();

		if (filaVista < 0) {
			return;
		}

		int filaModelo = tabla.convertRowIndexToModel(filaVista);

		Empleado empleado = modeloTabla.getEmpleado(filaModelo);

		campoDni.setText(empleado.getDni());
		campoDni.setEditable(false);
		campoNombre.setText(empleado.getNombre());
		campoSueldo.setText(String.valueOf(empleado.getSueldo()));

		if (empleado instanceof Comercial) {
			Comercial comercial = (Comercial) empleado;

			selectorTipo.setSelectedItem("Comercial");
			campoDatoEspecifico.setText(String.valueOf(comercial.getComision()));
		} else {
			Tecnico tecnico = (Tecnico) empleado;

			selectorTipo.setSelectedItem("Técnico");
			campoDatoEspecifico.setText(tecnico.getEspecialidad());
		}
	}

	private double leerNumero(JTextField campo, String nombreCampo) {

		String texto = campo.getText().trim().replace(',', '.');

		if (texto.isEmpty()) {
			throw new IllegalArgumentException("El campo " + nombreCampo + " no puede estar vacío.");
		}

		try {
			return Double.parseDouble(texto);

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("El campo " + nombreCampo + " debe contener un número válido.");
		}
	}

	private void limpiarFormulario() {
		tabla.clearSelection();

		campoDni.setText("");
		campoDni.setEditable(true);
		campoNombre.setText("");
		campoSueldo.setText("");
		selectorTipo.setSelectedIndex(0);
		campoDatoEspecifico.setText("");

		campoDni.requestFocusInWindow();
	}

	private void refrescarTabla() {
		try {
			modeloTabla.setEmpleados(controlador.filtrarPorNombre(campoFiltro.getText()));

			double media = controlador.calcularSalarioMedio();

			etiquetaSalarioMedio.setText(String.format("Salario medio: %.2f €", media));

		} catch (Exception e) {
			mostrarError("No se pudo actualizar la plantilla: " + e.getMessage());
		}
	}

	private void mostrarInformacion(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Gestión de Plantilla", JOptionPane.INFORMATION_MESSAGE);
	}

	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Revisa los datos", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		try {
			EmpleadoDAO dao = new EmpleadoJdbcDAO();

			ControladorPlantilla controlador = new ControladorPlantilla(dao);

			controlador.inicializar();

			SwingUtilities.invokeLater(() -> {
				VentanaPrincipal ventana = new VentanaPrincipal(controlador);

				ventana.setVisible(true);
			});

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se pudo iniciar la aplicación: " + e.getMessage(),
					"Error de inicio", JOptionPane.ERROR_MESSAGE);
		}
	}
}