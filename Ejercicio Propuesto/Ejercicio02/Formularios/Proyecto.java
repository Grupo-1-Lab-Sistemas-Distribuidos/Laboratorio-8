package formularios;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import conexion.Coneccion;

public class Proyecto extends JFrame {
    private JTextField txtId, txtNombre, txtFecInicio, txtFecFinal, txtIdDepartamento;
    private JButton btnInsertar, btnActualizar, btnEliminar, btnMostrar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public Proyecto() {
        setTitle("Gestión de Proyectos");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblId = new JLabel("ID Proyecto:");
        lblId.setBounds(10, 10, 100, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(120, 10, 150, 25);
        add(txtId);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 40, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(120, 40, 150, 25);
        add(txtNombre);

        JLabel lblFecInicio = new JLabel("Fecha Inicio (YYYY-MM-DD):");
        lblFecInicio.setBounds(10, 70, 200, 25);
        add(lblFecInicio);

        txtFecInicio = new JTextField();
        txtFecInicio.setBounds(210, 70, 150, 25);
        add(txtFecInicio);

        JLabel lblFecFinal = new JLabel("Fecha Final (YYYY-MM-DD):");
        lblFecFinal.setBounds(10, 100, 200, 25);
        add(lblFecFinal);

        txtFecFinal = new JTextField();
        txtFecFinal.setBounds(210, 100, 150, 25);
        add(txtFecFinal);

        JLabel lblIdDepartamento = new JLabel("ID Departamento:");
        lblIdDepartamento.setBounds(10, 130, 120, 25);
        add(lblIdDepartamento);

        txtIdDepartamento = new JTextField();
        txtIdDepartamento.setBounds(140, 130, 150, 25);
        add(txtIdDepartamento);

        btnInsertar = new JButton("Insertar");
        btnInsertar.setBounds(10, 170, 100, 25);
        add(btnInsertar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(120, 170, 100, 25);
        add(btnActualizar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(230, 170, 100, 25);
        add(btnEliminar);

        btnMostrar = new JButton("Mostrar");
        btnMostrar.setBounds(340, 170, 100, 25);
        add(btnMostrar);

        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Fecha Inicio");
        modelo.addColumn("Fecha Final");
        modelo.addColumn("ID Departamento");

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 210, 660, 140);
        add(scroll);

        btnInsertar.addActionListener(e -> insertar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnMostrar.addActionListener(e -> mostrar());

        setVisible(true);
    }

    private void insertar() {
        try (Connection conn = Coneccion.getConnection()) {
            String sql = "INSERT INTO proyecto (idProyecto, Nombre, Fec_Inicio, Fec_Final, idDepartamentos) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.setString(2, txtNombre.getText());
            stmt.setDate(3, Date.valueOf(txtFecInicio.getText()));
            stmt.setDate(4, Date.valueOf(txtFecFinal.getText()));
            stmt.setInt(5, Integer.parseInt(txtIdDepartamento.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro insertado correctamente.");
            mostrar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + e.getMessage());
        }
    }

    private void actualizar() {
        try (Connection conn = Coneccion.getConnection()) {
            String sql = "UPDATE proyecto SET Nombre=?, Fec_Inicio=?, Fec_Final=?, idDepartamentos=? WHERE idProyecto=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNombre.getText());
            stmt.setDate(2, Date.valueOf(txtFecInicio.getText()));
            stmt.setDate(3, Date.valueOf(txtFecFinal.getText()));
            stmt.setInt(4, Integer.parseInt(txtIdDepartamento.getText()));
            stmt.setInt(5, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");
            mostrar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminar() {
        try (Connection conn = Coneccion.getConnection()) {
            String sql = "DELETE FROM proyecto WHERE idProyecto=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
            mostrar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    private void mostrar() {
        modelo.setRowCount(0);
        try (Connection conn = Coneccion.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM proyecto");
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("idProyecto"),
                    rs.getString("Nombre"),
                    rs.getDate("Fec_Inicio"),
                    rs.getDate("Fec_Final"),
                    rs.getInt("idDepartamentos")
                };
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Proyecto();
    }
}
