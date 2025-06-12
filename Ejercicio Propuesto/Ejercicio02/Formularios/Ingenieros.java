package formularios;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import conexion.Coneccion;

public class Ingenieros extends JFrame {
    private JTextField txtId, txtEspecialidad, txtCargo, txtIdProyecto;
    private JButton btnInsertar, btnActualizar, btnEliminar, btnMostrar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public Ingenieros() {
        setTitle("Gestión de Ingenieros");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("ID Ingeniero:"));
        txtId = new JTextField();
        panel.add(txtId);

        panel.add(new JLabel("Especialidad:"));
        txtEspecialidad = new JTextField();
        panel.add(txtEspecialidad);

        panel.add(new JLabel("Cargo:"));
        txtCargo = new JTextField();
        panel.add(txtCargo);

        panel.add(new JLabel("ID Proyecto:"));
        txtIdProyecto = new JTextField();
        panel.add(txtIdProyecto);

        btnInsertar = new JButton("Insertar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnMostrar = new JButton("Mostrar");

        panel.add(btnInsertar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnMostrar);

        add(panel, BorderLayout.NORTH);

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID", "Especialidad", "Cargo", "ID Proyecto"});
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnInsertar.addActionListener(e -> insertar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnMostrar.addActionListener(e -> mostrar());
    }

    private void insertar() {
        String sql = "INSERT INTO ingenieros (idIngeniero, Especialidad, Cargo, idProyecto) VALUES (?, ?, ?, ?)";
        try (Connection conn = Coneccion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.setString(2, txtEspecialidad.getText());
            stmt.setString(3, txtCargo.getText());
            stmt.setInt(4, Integer.parseInt(txtIdProyecto.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Ingeniero insertado exitosamente.");
            mostrar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + e.getMessage());
        }
    }

    private void actualizar() {
        String sql = "UPDATE ingenieros SET Especialidad = ?, Cargo = ?, idProyecto = ? WHERE idIngeniero = ?";
        try (Connection conn = Coneccion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txtEspecialidad.getText());
            stmt.setString(2, txtCargo.getText());
            stmt.setInt(3, Integer.parseInt(txtIdProyecto.getText()));
            stmt.setInt(4, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Ingeniero actualizado exitosamente.");
            mostrar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminar() {
        String sql = "DELETE FROM ingenieros WHERE idIngeniero = ?";
        try (Connection conn = Coneccion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Ingeniero eliminado exitosamente.");
            mostrar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    private void mostrar() {
        modelo.setRowCount(0);
        String sql = "SELECT * FROM ingenieros";
        try (Connection conn = Coneccion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("idIngeniero"),
                        rs.getString("Especialidad"),
                        rs.getString("Cargo"),
                        rs.getInt("idProyecto")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar datos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ingenieros().setVisible(true));
    }
}
