package com.mycompany.apuestatodook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class UsuarioDAO {

    public List<UsuarioBase> getAll() {
        List<UsuarioBase> usuarios = new LinkedList();
        String query = "SELECT * FROM usuario";
        try(Connection con = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();) {
            while(rs.next()){
                usuarios.add(rsRowToUsuario(rs));
            }
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return usuarios;
    }

    public UsuarioBase autenticar(String usuario, String contrasenia) {
        String query = "SELECT id_usuario, usuario, contrasenia, dinero, tipo FROM usuario WHERE usuario = ? AND contrasenia = ?";
        UsuarioBase validado = null;
        try (Connection con = ConnectionPool.getInstance().getConnection(); 
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, contrasenia);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    validado = rsRowToUsuario(resultSet);
                    System.out.println(validado);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return validado;
    }

private UsuarioBase rsRowToUsuario(ResultSet rs) throws SQLException {
    int IDusuario = rs.getInt("id_usuario");
    String usuario = rs.getString("usuario");
    String contrasenia = rs.getString("contrasenia");
    String tipo = rs.getString("tipo");
    double dinero = rs.getDouble("dinero");

    if ("admin".equalsIgnoreCase(tipo)) {
        return new Admin(IDusuario, usuario, contrasenia);
    } else {
        // Cargar datos personales si existen
        try {
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String dni = rs.getString("dni");
            int edad = rs.getInt("edad");
            
  
            if (nombre != null) {
                return new Usuario(IDusuario, usuario, contrasenia, dinero, dni, nombre, apellido, edad);
            } else {

                return new Usuario(IDusuario, usuario, contrasenia, dinero, "", "", "", 0);
            }
        } catch (SQLException e) {
            return new Usuario(IDusuario, usuario, contrasenia, dinero, "", "", "", 0);
        }
    }
}

 
    public int addConDatosPersonales(String usuario, String contrasenia, String nombre, String apellido, int edad, String dni) {
        String query = "INSERT INTO usuario (usuario, contrasenia, nombre, apellido, edad, dni) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectionPool.getInstance().getConnection(); 
             PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, contrasenia);
            preparedStatement.setString(3, nombre);
            preparedStatement.setString(4, apellido);
            preparedStatement.setInt(5, edad);
            preparedStatement.setString(6, dni);
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            if (key.next()) {
                return key.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID de usuario generado automáticamente.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public double getDineroPorIdUsuario(Integer Id) {
        String query = "SELECT dinero FROM usuario WHERE id_usuario = ?";
        double dinero = 0.0;
        try (Connection con = ConnectionPool.getInstance().getConnection(); 
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, Id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) { 
                    dinero = resultSet.getDouble(1);
                }  
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return dinero;
    }

    public void updateDinero(UsuarioBase usuario) {

        if (usuario instanceof Usuario) {
            Usuario usuarioNormal = (Usuario) usuario;
            String query = "UPDATE usuario SET dinero = ? WHERE id_usuario = ?";
            try (Connection con = ConnectionPool.getInstance().getConnection();
                 PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setDouble(1, usuarioNormal.getDinero());
                preparedStatement.setInt(2, usuarioNormal.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException("Error al actualizar dinero", ex);
            }
        }

    }
    
}

