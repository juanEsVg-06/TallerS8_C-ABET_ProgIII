package servicios;

import estructura.ListaDobleReportes;
import modelo.EstadoReporte;
import modelo.Reporte;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

    private GestorReportes gestor;

    // Componentes visuales del formulario principal (Exclusivo para Registrar)
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JComboBox<Integer> comboGravedad;
    private JComboBox<EstadoReporte> comboEstado;
    private JTextArea areaResultados;

    public MainGUI() {
        gestor = new ListaDobleReportes();

        setTitle("Sistema de Gestión de Reportes Éticos");
        setSize(850, 550); // Un poco más de ancho para acomodar la barra inferior holgadamente
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL SUPERIOR: Formulario de Registro ---
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

        // Este botón es el único que usa los textos de arriba
        JButton btnAgregar = new JButton("Registrar Nuevo Reporte");
        btnAgregar.setBackground(new Color(230, 255, 230)); // Verde clarito
        panelFormulario.add(btnAgregar);
        panelFormulario.add(new JLabel("")); // Relleno de diseño

        add(panelFormulario, BorderLayout.NORTH);

        // --- PANEL CENTRAL: Área de Resultados ---
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Historial y Consultas"));
        add(scroll, BorderLayout.CENTER);

        // --- PANEL INFERIOR: Barra de Herramientas Dinámica ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        JButton btnMostrarPendientes = new JButton("Solo Pendientes");
        JButton btnMostrarPorGravedad = new JButton("Filtrar por Gravedad");
        JButton btnBuscarPorId = new JButton("Buscar por ID");
        JButton btnActualizar = new JButton("Actualizar Estado");

        // ¡AQUÍ ESTÁ TU BOTÓN DE ELIMINAR!
        JButton btnEliminar = new JButton("Eliminar por ID");

        // Estilizamos los botones para organizar visualmente las acciones
        btnBuscarPorId.setBackground(new Color(255, 255, 220)); // Amarillo suave
        btnActualizar.setBackground(new Color(230, 242, 255));  // Azul suave
        btnEliminar.setBackground(new Color(255, 200, 200));    // Rojo suave para destacar peligro

        panelFiltros.add(btnMostrarTodos);
        panelFiltros.add(btnMostrarPendientes);
        panelFiltros.add(btnMostrarPorGravedad);
        panelFiltros.add(btnBuscarPorId);
        panelFiltros.add(btnActualizar);
        panelFiltros.add(btnEliminar); // Se añade a la barra inferior

        add(panelFiltros, BorderLayout.SOUTH);

        // =========================================================
        // =               EVENTOS DE LOS BOTONES                  =
        // =========================================================

        // 1. REGISTRAR
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

            int gravedad = (Integer) comboGravedad.getSelectedItem();
            EstadoReporte estado = (EstadoReporte) comboEstado.getSelectedItem();

            Reporte nuevoReporte = new Reporte(id, nombre, desc, gravedad, estado);
            gestor.agregarReporte(nuevoReporte);

            JOptionPane.showMessageDialog(this, "Reporte registrado con éxito.");
            areaResultados.setText(gestor.mostrarTodos());
            limpiarCampos();
        });

        // 2. MOSTRAR TODOS
        btnMostrarTodos.addActionListener(e -> areaResultados.setText(gestor.mostrarTodos()));

        // 3. MOSTRAR PENDIENTES
        btnMostrarPendientes.addActionListener(e -> areaResultados.setText(gestor.mostrarPendientes()));

        // 4. FILTRAR POR GRAVEDAD (Sub-ventana)
        btnMostrarPorGravedad.addActionListener(e -> {
            Integer[] opcionesGravedad = {1, 2, 3, 4, 5};
            Integer seleccion = (Integer) JOptionPane.showInputDialog(
                    this,
                    "Seleccione el nivel de gravedad para filtrar el historial:",
                    "Filtrar por Gravedad",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesGravedad,
                    1
            );

            if (seleccion != null) {
                areaResultados.setText(gestor.mostrarPorGravedad(seleccion));
            }
        });

        // 5. BUSCAR POR ID (Sub-ventana aislada)
        btnBuscarPorId.addActionListener(e -> {
            String idBuscar = JOptionPane.showInputDialog(this, "Ingrese el ID del reporte a buscar:", "Buscar Reporte por ID", JOptionPane.QUESTION_MESSAGE);

            if (idBuscar == null) return;
            idBuscar = idBuscar.trim();

            if (idBuscar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Reporte reporteEncontrado = gestor.obtenerReporte(idBuscar);

            if (reporteEncontrado != null) {
                JOptionPane.showMessageDialog(this, reporteEncontrado.toString(), "Detalle del Reporte - ID: " + idBuscar, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún reporte con el ID: " + idBuscar, "Búsqueda Fallida", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 6. ACTUALIZAR ESTADO (Flujo de dos pasos)
        btnActualizar.addActionListener(e -> {
            String idBuscar = JOptionPane.showInputDialog(this, "Ingrese el ID del reporte a modificar:", "Actualizar Estado - Paso 1", JOptionPane.QUESTION_MESSAGE);

            if (idBuscar == null) return;
            idBuscar = idBuscar.trim();

            if (idBuscar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Reporte reporteEncontrado = gestor.obtenerReporte(idBuscar);
            if (reporteEncontrado == null) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún reporte con el ID: " + idBuscar, "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EstadoReporte nuevoEstado = (EstadoReporte) JOptionPane.showInputDialog(
                    this,
                    "Reporte de: " + reporteEncontrado.getNombreUser() + "\nSeleccione el nuevo estado del caso:",
                    "Actualizar Estado - Paso 2",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    EstadoReporte.values(),
                    reporteEncontrado.getEstado()
            );

            if (nuevoEstado != null) {
                gestor.actualizarEstado(idBuscar, nuevoEstado);
                JOptionPane.showMessageDialog(this, "Estado del caso '" + idBuscar + "' actualizado correctamente.");
                areaResultados.setText(gestor.mostrarTodos());
            }
        });

        // 7. ¡ELIMINAR POR ID! (Sub-ventana de advertencia)
        btnEliminar.addActionListener(e -> {
            String idBorrar = JOptionPane.showInputDialog(this, "Ingrese el ID del reporte que desea ELIMINAR:", "Eliminar Reporte", JOptionPane.WARNING_MESSAGE);

            if (idBorrar == null) return;
            idBorrar = idBorrar.trim();

            if (idBorrar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            gestor.eliminarReporte(idBorrar);
            JOptionPane.showMessageDialog(this, "Operación procesada. Revisa el historial de reportes.");
            areaResultados.setText(gestor.mostrarTodos());
        });
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
            ventana.setLocationRelativeTo(null);
        });
    }
}