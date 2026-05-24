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
        // Inicializamos tu estructura de datos
        gestor = new ListaDobleReportes();

        setTitle("Sistema de Gestión de Reportes Éticos");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        //Formulario de Entrada ---
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

        panelFormulario.add(new JLabel("Estado:"));
        comboEstado = new JComboBox<>(EstadoReporte.values());
        panelFormulario.add(comboEstado);

        JButton btnAgregar = new JButton("Registrar Reporte");
        JButton btnEliminar = new JButton("Eliminar por ID");
        panelFormulario.add(btnAgregar);
        panelFormulario.add(btnEliminar);

        add(panelFormulario, BorderLayout.NORTH);

        //Área de Resultados
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Historial y Consultas"));
        add(scroll, BorderLayout.CENTER);

        //Botones de Filtro
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        JButton btnMostrarPendientes = new JButton("Solo Pendientes");

        panelFiltros.add(btnMostrarTodos);
        panelFiltros.add(btnMostrarPendientes);
        add(panelFiltros, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String desc = txtDescripcion.getText().trim();

            // VALIDACIONES DE SEGURIDAD
            if (id.isEmpty() || nombre.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Regex: Valida que el nombre solo tenga letras (mayúsculas/minúsculas) y espacios
            if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                JOptionPane.showMessageDialog(this, "El nombre no puede contener números ni caracteres especiales.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Si pasa las validaciones, creamos el reporte
            int gravedad = (Integer) comboGravedad.getSelectedItem();
            EstadoReporte estado = (EstadoReporte) comboEstado.getSelectedItem();

            Reporte nuevoReporte = new Reporte(id, nombre, desc, gravedad, estado);
            gestor.agregarReporte(nuevoReporte);

            JOptionPane.showMessageDialog(this, "Reporte registrado con éxito.");
            limpiarCampos();
        });

        btnEliminar.addActionListener(e -> {
            String idBorrar = txtId.getText().trim();
            if (idBorrar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el ID del reporte a eliminar en el campo superior.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            gestor.eliminarReporte(idBorrar);
            JOptionPane.showMessageDialog(this, "Operación de eliminación finalizada. Verifique en consola el resultado.");
        });

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
            ventana.setLocationRelativeTo(null); // Centra la ventana
        });
    }
}