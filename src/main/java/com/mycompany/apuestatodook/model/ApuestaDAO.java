package com.mycompany.apuestatodook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ApuestaDAO {

    public void add(Apuesta apuesta) {
    System.out.println("🔍 DEBUG APUESTA - Monto: " + apuesta.getMonto() + 
                      ", Por: " + apuesta.getpor_quien() + 
                      ", ID Usuario: " + apuesta.getIdUsuario() +
                      ", ID Partido: " + apuesta.getIdPartido() +
                      ", ID Resultado: " + apuesta.getFk_id_resultado());
    
    String query = "INSERT INTO apuesta (monto, por_quien, fk_id_usuario, fk_id_partido, fk_id_resultado) VALUES (?, ?, ?, ?, ?)";
    try (Connection con = ConnectionPool.getInstance().getConnection(); 
         PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        
        preparedStatement.setInt(1, apuesta.getMonto());
        preparedStatement.setString(2, apuesta.getpor_quien());
        preparedStatement.setInt(3, apuesta.getIdUsuario());
        preparedStatement.setInt(4, apuesta.getIdPartido());
        preparedStatement.setInt(5, apuesta.getFk_id_resultado());
        
        System.out.println("🚀 Ejecutando INSERT apuesta...");
        preparedStatement.executeUpdate();
        System.out.println("✅ Apuesta insertada OK");

    } catch (SQLException ex) {
        System.out.println("💥 ERROR SQL: " + ex.getMessage());
        System.out.println("💥 ERROR StackTrace: " + ex.getStackTrace());
        throw new RuntimeException("Error al insertar apuesta", ex);
    }
}

    public void updateEstado(Apuesta apuesta) {
        String query = "UPDATE apuesta SET estado = ? WHERE id_apuesta = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            preparedStatement.setString(1, String.valueOf(apuesta.getEstado()));
            preparedStatement.setInt(2, apuesta.getIdApuesta());
            preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar estado de apuesta", ex);
        }
    }

    public List<Apuesta> getAllApuestasConResultado() {
    List<Apuesta> apuestasConResultado = new ArrayList<>();
    String query = "SELECT a.por_quien, a.monto, p.local, p.visitante, p.fecha, u.usuario as nombre_usuario " +
            "FROM apuesta a " +
            "JOIN resultado r ON r.fk_id_partido = a.fk_id_partido " +
            "JOIN partido p ON p.id_partido = a.fk_id_partido " +
            "JOIN usuario u ON u.id_usuario = a.fk_id_usuario";  // JOIN con usuario

    try (Connection con = ConnectionPool.getInstance().getConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            String por_quien = rs.getString("por_quien");
            int monto = rs.getInt("monto");
            String local = rs.getString("local");
            String visitante = rs.getString("visitante");
            String fecha = rs.getString("fecha");
            String nombreUsuario = rs.getString("nombre_usuario"); // Nuevo campo

            Apuesta apuesta = new Apuesta(local, visitante, fecha, monto, por_quien);
            apuesta.setNombreUsuario(nombreUsuario); // Asignar el nombre de usuario
            apuestasConResultado.add(apuesta);
        }
    } catch (SQLException ex) {
        throw new RuntimeException(ex);
    }
    return apuestasConResultado;
}

  
    private List<Apuesta> ejecutarQueryApuestas(String query) {
        List<Apuesta> apuestas = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                apuestas.add(crearApuestaDesdeResultSet(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error en query de apuestas", ex);
        }
        return apuestas;
    }

    private Apuesta crearApuestaDesdeResultSet(ResultSet rs) throws SQLException {
        String local = rs.getString("local");
        String visitante = rs.getString("visitante");
        String fecha = rs.getString("fecha");
        int monto = rs.getInt("monto");
        String por_quien = rs.getString("por_quien");
        
        return new Apuesta(local, visitante, fecha, monto, por_quien);
    }
    
    public List<Apuesta> getApuestasConResultadoPorUsuario(int idUsuario) {
    List<Apuesta> apuestasConResultado = new ArrayList<>();
    String query = "SELECT a.por_quien, a.monto, p.local, p.visitante, p.fecha, u.usuario as nombre_usuario " +
            "FROM apuesta a " +
            "JOIN resultado r ON r.fk_id_partido = a.fk_id_partido " +
            "JOIN partido p ON p.id_partido = a.fk_id_partido " +
            "JOIN usuario u ON u.id_usuario = a.fk_id_usuario " +
            "WHERE a.fk_id_usuario = ?";  // Condición por usuario

    try (Connection con = ConnectionPool.getInstance().getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        
        ps.setInt(1, idUsuario);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String por_quien = rs.getString("por_quien");
                int monto = rs.getInt("monto");
                String local = rs.getString("local");
                String visitante = rs.getString("visitante");
                String fecha = rs.getString("fecha");
                String nombreUsuario = rs.getString("nombre_usuario");

                Apuesta apuesta = new Apuesta(local, visitante, fecha, monto, por_quien);
                apuesta.setNombreUsuario(nombreUsuario);
                apuestasConResultado.add(apuesta);
            }
        }
    } catch (SQLException ex) {
        throw new RuntimeException("Error al obtener apuestas por usuario", ex);
    }
    return apuestasConResultado;
}
}