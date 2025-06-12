package Formularios;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import conexion.Coneccion;

public class Departamento extends JFrame {
    private JTextField txtId, txtNombre, txtTelefono, txtFax;
    private JButton btnNuevo, btnGuardar, btnEliminar, btnActualizar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public Departamento() {
        setTitle("DEPARTAMENTOS");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("DEPARTAMENTOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(0, 10, 600, 30);
        add(lblTitulo);

        JLabel lblId = new JLabel("id del Departamento:");
        lblId.setBounds(30, 50, 150, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(180, 50, 150, 25);
        add(txtId);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(320, 50, 60, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(390, 50, 150, 25);
        add(txtNombre);

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(30, 90, 150, 25);
        add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(180, 90, 150, 25);
        add(txtTelefono);

        JLabel lblFax = new JLabel("FAX:");
        lblFax.setBounds(320, 90, 60, 25);
        add(lblFax);

        txtFax = new JTextField();
        txtFax.setBounds(390, 90, 150, 25);
        add(txtFax);

        btnNuevo = new JButton("Nuevo");
        btnNuevo.setBounds(30, 130, 100, 30);
        add(btnNuevo);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(140, 130, 100, 30);
        add(btnGuardar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(250, 130, 100, 30);
        add(btnEliminar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(360, 130, 100, 30);
        add(btnActualizar);

        modelo = new DefaultTableModel();
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Telefono");
        modelo.addColumn("Fax");

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 180, 510, 150);
        add(scroll);

        mostrarDatos();

        btnNuevo.addActionListener(e -> limpiarCampos());

        btnGuardar.addActionListener(e -> {
            try (Connection con = Coneccion.getConnection()) {
                String sql = "INSERT INTO departamento (idDepartamentos, Nombre, Telefono, Fax) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtId.getText()));
                ps.setString(2, txtNombre.getText());
                ps.setString(3, txtTelefono.getText());
                ps.setString(4, txtFax.getText());
                ps.executeUpdate();
                mostrarDatos();
                limpiarCampos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage());
            }
        });

        btnEliminar.addActionListener(e -> {
            try (Connection con = Coneccion.getConnection()) {
                String sql = "DELETE FROM departamento WHERE idDepartamentos = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtId.getText()));
                ps.executeUpdate();
                mostrarDatos();
                limpiarCampos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex.getMessage());
            }
        });

        btnActualizar.addActionListener(e -> {
            try (Connection con = Coneccion.getConnection()) {
                String sql = "UPDATE departamento SET Nombre=?, Telefono=?, Fax=? WHERE idDepartamentos=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, txtNombre.getText());
                ps.setString(2, txtTelefono.getText());
                ps.setString(3, txtFax.getText());
                ps.setInt(4, Integer.parseInt(txtId.getText()));
                ps.executeUpdate();
                mostrarDatos();
                limpiarCampos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage());
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                txtId.setText(tabla.getValueAt(fila, 0).toString());
                txtNombre.setText(tabla.getValueAt(fila, 1).toString());
                txtTelefono.setText(tabla.getValueAt(fila, 2).toString());
                txtFax.setText(tabla.getValueAt(fila, 3).toString());
            }
        });

        setVisible(true);
    }

    private void mostrarDatos() {
        modelo.setRowCount(0);
        try (Connection con = Coneccion.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM departamento");
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idDepartamentos"),
                    rs.getString("Nombre"),
                    rs.getString("Telefono"),
                    rs.getString("Fax")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al mostrar: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtFax.setText("");
    }

    public static void main(String[] args) {
        new Departamento();
    }
}

