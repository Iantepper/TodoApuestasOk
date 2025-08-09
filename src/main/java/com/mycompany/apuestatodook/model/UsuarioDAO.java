package com.mycompany.apuestatodook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class UsuarioDAO {

     
 public List<Usuario> getAll() {
        List<Usuario>usuarios = new LinkedList();
        String query = "SELECT * FROM usuario";
        try(Connection con = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();)
        {
            while(rs.next()){
                usuarios.add(rsRowToUsuario(rs));
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return usuarios;
    }
 
    public Usuario autenticar(String usuario, String contrasenia) {
    String query = "SELECT id_usuario, usuario, contrasenia, dinero, tipo FROM usuario WHERE usuario = ? AND contrasenia = ?";
    Usuario validado = null;
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

private Usuario rsRowToUsuario(ResultSet rs) throws SQLException {
    int IDusuario = rs.getInt("id_usuario");
    String usuario = rs.getString("usuario");
    String contrasenia = rs.getString("contrasenia");
    
 
    double dinero = rs.getDouble("dinero");
    String tipo = rs.getString("tipo");

    return new Usuario(IDusuario, usuario, contrasenia, dinero, tipo);
}

    public int add(String usuario, String contrasenia) {
        String query = "INSERT INTO usuario (usuario, contrasenia) VALUES (?, ?)";
        try (Connection con = ConnectionPool.getInstance().getConnection(); PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, contrasenia);
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
    
    
    public double getDineroPorIdUsuario (Integer Id) {
        
        String query = "SELECT dinero FROM usuario WHERE id_usuario = ?";
        
        double dinero = 0.0;
        
        try (Connection con = ConnectionPool.getInstance().getConnection(); PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
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
 
 
        public void updateDinero(Usuario usuario) {
        String query = "UPDATE usuario SET dinero = ? WHERE id_usuario = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setDouble(1, usuario.getDinero());
            preparedStatement.setInt(2, usuario.getIDusuario());
            preparedStatement.executeUpdate();
            System.out.println("Actualización del dinero del usuario en la base de datos. Nuevo saldo: " + usuario.getDinero());
        } catch (SQLException ex) {
            System.out.println("Error al actualizar el dinero del usuario: " + ex.getMessage());     
            throw new RuntimeException(ex);
        }
    }
}


