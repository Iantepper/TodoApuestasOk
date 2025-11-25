
package com.mycompany.apuestatodook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    
    public void agregarPersona(int idUsuario, String nombre, String apellido, int edad, String dni) {
        String query = "INSERT INTO persona (nombre, apellido, edad, dni, fk_id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectionPool.getInstance().getConnection(); 
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, apellido);
            preparedStatement.setInt(3, edad);
            preparedStatement.setString(4, dni);
            preparedStatement.setInt(5, idUsuario);
            preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar persona", ex);
        }
    }
    
    public Persona getPersonaPorId(Integer idUsuario) {
        String query = "SELECT * FROM persona WHERE fk_id_usuario = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection(); 
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            preparedStatement.setInt(1, idUsuario);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return rsRowToPersona(resultSet);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al obtener persona por ID", ex);
        }
        return null;
    }

    private Persona rsRowToPersona(ResultSet rs) throws SQLException {
        int id_persona = rs.getInt("id_persona");
        String dni = rs.getString("dni");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        int edad = rs.getInt("edad");
        int fk_id_usuario = rs.getInt("fk_id_usuario");
       
        return new Persona(id_persona, dni, nombre, apellido, edad, fk_id_usuario);
    }
}