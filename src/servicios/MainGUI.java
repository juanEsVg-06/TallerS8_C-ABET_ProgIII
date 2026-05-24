package servicios;

import estructura.ListaDobleReportes;
import modelo.EstadoReporte;
import modelo.Reporte;


import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

    private GestorReportes gestor;

    // Componentes visuales
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

        // --- PANEL SUPERIOR: Formulario de Entrada ---
        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 5, 5));
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

        panelFormulario.add(new JLabel("Seleccionar Estado:"));
        comboEstado = new JComboBox<>(EstadoReporte.values());
        panelFormulario.add(comboEstado);

        // Botones de acción principal
        JButton btnAgregar = new JButton("Registrar Reporte");
        JButton btnEliminar = new JButton("Eliminar por ID");
        JButton btnActualizar = new JButton("Actualizar Estado");

        panelFormulario.add(btnAgregar);
        panelFormulario.add(btnEliminar);
        panelFormulario.add(btnActualizar);
        panelFormulario.add(new JLabel(""));

        add(panelFormulario, BorderLayout.NORTH);

        // --- PANEL CENTRAL: Área de Resultados ---
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Historial y Consultas"));
        add(scroll, BorderLayout.CENTER);

        // --- PANEL INFERIOR: Botones de Filtro (Se añade el de Gravedad) ---
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        JButton btnMostrarPendientes = new JButton("Solo Pendientes");
        JButton btnMostrarPorGravedad = new JButton("Filtrar por Gravedad"); // ¡Nuevo Botón!

        panelFiltros.add(btnMostrarTodos);
        panelFiltros.add(btnMostrarPendientes);
        panelFiltros.add(btnMostrarPorGravedad); // Añadido al panel
        add(panelFiltros, BorderLayout.SOUTH);

        // --- EVENTOS DE LOS BOTONES ---

        // 1. Registrar
        btnAgregar.addActionListener(e -> {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String desc = txtDescripcion.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
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

        // 2. Eliminar
        btnEliminar.addActionListener(e -> {
            String idBorrar = txtId.getText().trim();
            if (idBorrar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el ID del reporte a eliminar en el campo superior.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            gestor.eliminarReporte(idBorrar);
            JOptionPane.showMessageDialog(this, "Operación finalizada. Verifique el historial.");
            areaResultados.setText(gestor.mostrarTodos());
            txtId.setText("");
        });

        // 3. Actualizar Estado (Corregido para aislar solo el ID)
        btnActualizar.addActionListener(e -> {
            // Corrección: Forzamos la limpieza de los otros campos para que no confundan al usuario
            txtNombre.setText("");
            txtDescripcion.setText("");

            String id = txtId.getText().trim();
            EstadoReporte nuevoEstado = (EstadoReporte) comboEstado.getSelectedItem();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese únicamente el ID del reporte a actualizar.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean exito = gestor.actualizarEstado(id, nuevoEstado);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Estado del reporte " + id + " actualizado a " + nuevoEstado + " con éxito.");
                areaResultados.setText(gestor.mostrarTodos());
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún reporte con el ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 4. Filtros de visualización
        btnMostrarTodos.addActionListener(e -> areaResultados.setText(gestor.mostrarTodos()));
        btnMostrarPendientes.addActionListener(e -> areaResultados.setText(gestor.mostrarPendientes()));

        // ¡Nuevo Evento para filtrar por gravedad!
        btnMostrarPorGravedad.addActionListener(e -> {
            int gravedadSeleccionada = (Integer) comboGravedad.getSelectedItem();
            areaResultados.setText(gestor.mostrarPorGravedad(gravedadSeleccionada));
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