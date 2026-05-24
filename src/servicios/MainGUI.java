package servicios;

import estructura.ListaDobleReportes;
import modelo.EstadoReporte;
import modelo.Reporte;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

    private GestorReportes gestor;

    // Componentes visuales del formulario principal (Ahora exclusivo para Registrar)
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JComboBox<Integer> comboGravedad;
    private JComboBox<EstadoReporte> comboEstado;
    private JTextArea areaResultados;

    public MainGUI() {
        gestor = new ListaDobleReportes();

        setTitle("Sistema de Gestión de Reportes Éticos");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL SUPERIOR: Formulario de Registro ---
        // Reducimos a 6 filas porque los botones de acciones especiales ya no dependen de este panel
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelFormulario.add(new JLabel("ID del Reporte:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre del Empleado:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Descripción del Incidente:"));
        txtDescripcion = new JTextField();
        panelFormulario.add(txtDescripcion);

        panelFormulario.add(new JLabel("Nivel de Gravedad (1-5):"));
        comboGravedad = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        panelFormulario.add(comboGravedad);

        panelFormulario.add(new JLabel("Estado Inicial:"));
        comboEstado = new JComboBox<>(EstadoReporte.values());
        panelFormulario.add(comboEstado);

        JButton btnAgregar = new JButton("Registrar Reporte");
        panelFormulario.add(btnAgregar);
        panelFormulario.add(new JLabel("")); // Espacio en blanco para cuadrar la cuadrícula

        add(panelFormulario, BorderLayout.NORTH);

        // --- PANEL CENTRAL: Área de Resultados ---
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Historial y Consultas"));
        add(scroll, BorderLayout.CENTER);

        // --- PANEL INFERIOR: Barra de Herramientas Dinámica ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        JButton btnMostrarPendientes = new JButton("Solo Pendientes");
        JButton btnMostrarPorGravedad = new JButton("Filtrar por Gravedad");
        JButton btnActualizar = new JButton("Actualizar Estado");
        JButton btnEliminar = new JButton("Eliminar por ID");

        // Estilizamos un poco los botones de acción para diferenciarlos de los filtros
        btnActualizar.setBackground(new Color(230, 242, 255));
        btnEliminar.setBackground(new Color(255, 230, 230));

        panelFiltros.add(btnMostrarTodos);
        panelFiltros.add(btnMostrarPendientes);
        panelFiltros.add(btnMostrarPorGravedad);
        panelFiltros.add(btnActualizar);
        panelFiltros.add(btnEliminar);
        add(panelFiltros, BorderLayout.SOUTH);

        // --- EVENTOS DE LOS BOTONES ---

        // 1. Acción: Registrar Reporte
        btnAgregar.addActionListener(e -> {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String desc = txtDescripcion.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios para el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                JOptionPane.showMessageDialog(this, "El nombre no puede contener números ni caracteres especiales.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int NewsGravedad = (Integer) comboGravedad.getSelectedItem();
            EstadoReporte NewsEstado = (EstadoReporte) comboEstado.getSelectedItem();

            Reporte nuevoReporte = new Reporte(id, nombre, desc, NewsGravedad, NewsEstado);
            gestor.agregarReporte(nuevoReporte);

            JOptionPane.showMessageDialog(this, "Reporte registrado con éxito.");
            areaResultados.setText(gestor.mostrarTodos());
            limpiarCampos();
        });

        // 2. Acción: Eliminar por ID (También migrado a ventana pequeña para consistencia)
        btnEliminar.addActionListener(e -> {
            String idBorrar = JOptionPane.showInputDialog(this, "Ingrese el ID del reporte que desea eliminar:", "Eliminar Reporte", JOptionPane.WARNING_MESSAGE);

            if (idBorrar == null) return; // Canceló la acción
            idBorrar = idBorrar.trim();
            if (idBorrar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            gestor.eliminarReporte(idBorrar);
            areaResultados.setText(gestor.mostrarTodos());
        });

        // 3. ¡NUEVO FLUJO: Actualizar Estado paso a paso!
        btnActualizar.addActionListener(e -> {
            // PASO A: Ventana pequeña para pedir estrictamente el ID
            String idBuscar = JOptionPane.showInputDialog(this, "Ingrese el ID del reporte a modificar:", "Actualizar Estado - Paso 1", JOptionPane.QUESTION_MESSAGE);

            if (idBuscar == null) return; // El usuario presionó cancelar
            idBuscar = idBuscar.trim();

            if (idBuscar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validación: Verificar si el reporte existe en la lista enlazada
            Reporte reporteEncontrado = gestor.obtenerReporte(idBuscar);
            if (reporteEncontrado == null) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún reporte con el ID: " + idBuscar, "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // PASO B: Si existe, se abre la segunda ventana pequeña con el menú desplegable (Enum)
            EstadoReporte nuevoEstado = (EstadoReporte) JOptionPane.showInputDialog(
                    this,
                    "Reporte de: " + reporteEncontrado.getNombreUser() + "\nSeleccione el nuevo estado del caso:",
                    "Actualizar Estado - Paso 2",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    EstadoReporte.values(),      // Pasa el array de opciones de tu Enum
                    reporteEncontrado.getEstado() // Pone como predeterminado el estado actual
            );

            // PASO C: Si selecciona una opción y da clic en aceptar/confirmar
            if (nuevoEstado != null) {
                gestor.actualizarEstado(idBuscar, nuevoEstado);
                JOptionPane.showMessageDialog(this, "Estado del caso '" + idBuscar + "' actualizado correctamente.");
                areaResultados.setText(gestor.mostrarTodos()); // Actualiza el JTextArea inmediatamente
            }
        });

        // 4. ¡NUEVO FLUJO: Filtrar por Gravedad en ventana independiente!
        btnMostrarPorGravedad.addActionListener(e -> {
            Integer[] opcionesGravedad = {1, 2, 3, 4, 5};

            // Ventana pequeña desplegando la lista de números
            Integer gravedadSeleccionada = (Integer) JOptionPane.showInputDialog(
                    this,
                    "Seleccione el nivel de gravedad para filtrar el historial:",
                    "Filtrar por Gravedad",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesGravedad,
                    1 // Valor inicial por defecto
            );

            // Al dar clic en confirmar (si no es nulo)
            if (gravedadSeleccionada != null) {
                areaResultados.setText(gestor.mostrarPorGravedad(gravedadSeleccionada));
            }
        });

        // Filtros directos de visualización general
        btnMostrarTodos.addActionListener(e -> areaResultados.setText(gestor.mostrarTodos()));
        btnMostrarPendientes.addActionListener(e -> areaResultados.setText(gestor.mostrarPendientes()));
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        comboGravedad.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI ventana = new MainGUI();
            ventana.setVisible(true);
            ventana.setLocationRelativeTo(null); // Centra la ventana en pantalla
        });
    }
}