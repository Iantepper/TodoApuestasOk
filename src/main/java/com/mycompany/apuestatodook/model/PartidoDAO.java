package com.mycompany.apuestatodook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.sql.Statement;

public class PartidoDAO {
    private List<Partido> partidos;

    public PartidoDAO() {
        this.partidos = new ArrayList<>();
    }
     
    public void add(Partido partido) {
        String query = "INSERT INTO partido (local, visitante, fecha) VALUES (?, ?, ?)";
        
        // Usar UNA sola conexión para todo
        try (Connection con = ConnectionPool.getInstance().getConnection()) {
            con.setAutoCommit(false); // Desactivar autocommit
            
            try {
                // Insertar partido
                PreparedStatement psPartido = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                psPartido.setString(1, partido.getLocal());
                psPartido.setString(2, partido.getVisitante());
                psPartido.setString(3, partido.getFecha());
                psPartido.executeUpdate();

                // Obtener ID generado
                ResultSet generatedKeys = psPartido.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idPartidoGenerado = generatedKeys.getInt(1);
                    partido.setIdPartido(idPartidoGenerado);
                    System.out.println("✅ Partido creado ID: " + idPartidoGenerado);
                    
                    // Crear resultado con la MISMA conexión - usar "pendiente" con p minúscula
                    crearResultadoParaPartido(con, idPartidoGenerado, "pendiente");
                }
                
                con.commit(); // Confirmar ambas operaciones
                System.out.println("✅ TRANSACCIÓN COMPLETADA - Partido y resultado creados");
                
            } catch (SQLException ex) {
                con.rollback(); // Revertir en caso de error
                throw new RuntimeException("Error en transacción", ex);
            }
            
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar partido", ex);
        }
    }

    // MÉTODO PRIVADO para crear resultado (usa misma conexión)
    private void crearResultadoParaPartido(Connection con, int idPartido, String ganador) throws SQLException {
        System.out.println("🎯 CREANDO resultado con conexión compartida - Partido ID: " + idPartido);
        
        String query = "INSERT INTO resultado (ganador, fk_id_partido) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, ganador);
        ps.setInt(2, idPartido);
        int filas = ps.executeUpdate();
        
        System.out.println("✅ Resultado creado - Filas: " + filas);
        
        // Verificar inmediatamente
        verificarResultadoCreado(con, idPartido);
    }
    
    // Verificación interna
    private void verificarResultadoCreado(Connection con, int idPartido) throws SQLException {
        String query = "SELECT id_resultado, ganador FROM resultado WHERE fk_id_partido = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, idPartido);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            System.out.println("🔍 VERIFICACIÓN: Resultado ID " + rs.getInt("id_resultado") + 
                             " - Ganador: '" + rs.getString("ganador") + "'");
        } else {
            System.out.println("❌ VERIFICACIÓN: No se encontró resultado para partido " + idPartido);
        }
    }

    public void delete(int idPartido) {
        String query = "DELETE FROM partido WHERE id_partido = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            preparedStatement.setInt(1, idPartido);
            preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar partido", ex);
        }
    }
            
    public List<Partido> getAllPartidosConResultado() {
        List<Partido> partidosConResultado = new LinkedList<>();
        String query = "SELECT p.id_partido, p.local, p.visitante, p.fecha, r.ganador, r.id_resultado " +
                       "FROM partido p " +
                       "JOIN resultado r ON r.fk_id_partido = p.id_partido";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Resultado resultado = new Resultado(
                    rs.getInt("id_resultado"),
                    rs.getString("ganador"),
                    rs.getInt("id_partido")
                );
                
                Partido partido = new Partido(
                    rs.getString("local"), 
                    rs.getString("visitante"), 
                    rs.getString("fecha"), 
                    resultado 
                );
                partido.setIdPartido(rs.getInt("id_partido"));
                
                partidosConResultado.add(partido);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return partidosConResultado;
    }
            
    public List<Partido> getAll() {
        List<Partido> partidos = new LinkedList();
        String query = "SELECT * FROM partido";
        try(Connection con = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();) {
            while(rs.next()){
                partidos.add(rsRowToPartido(rs));
            }
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return partidos;
    }
    
    public Partido getPartidoPorId(Integer Id) {
        String query = "SELECT * FROM partido WHERE id_partido = ?";
        Partido partido = null;
        try (Connection con = ConnectionPool.getInstance().getConnection(); 
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, Id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    partido = rsRowToPartido(resultSet);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return partido;
    }

    private Partido rsRowToPartido(ResultSet rs) throws SQLException {
        int id_partido = rs.getInt(1);
        String local = rs.getString(2);
        String visitante = rs.getString(3);
        String fecha = rs.getString(4);
        return new Partido(local, visitante, fecha, id_partido);
    }
    
    // ELIMINAR el método público crearResultadoParaPartido - ya no es necesario
    // porque usamos el método privado con transacción
    
    public void actualizarResultado(int idPartido, String ganador) {
        String checkQuery = "SELECT id_resultado FROM resultado WHERE fk_id_partido = ?";
        String updateQuery = "UPDATE resultado SET ganador = ? WHERE fk_id_partido = ?";
        String insertQuery = "INSERT INTO resultado (ganador, fk_id_partido) VALUES (?, ?)";
        
        try (Connection con = ConnectionPool.getInstance().getConnection()) {
            
            // Verificar si ya existe resultado
            boolean existeResultado = false;
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, idPartido);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    existeResultado = rs.next();
                }
            }
            
            // Actualizar o insertar
            if (existeResultado) {
                try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, ganador);
                    updateStmt.setInt(2, idPartido);
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, ganador);
                    insertStmt.setInt(2, idPartido);
                    insertStmt.executeUpdate();
                }
            }
            
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar resultado", ex);
        }
    }
        
    public List<Partido> getPartidosFuturos() {
        List<Partido> partidos = new ArrayList<>();
        String query = "SELECT * FROM partido WHERE fecha > NOW() ORDER BY fecha ASC";
        try(Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();) {
            while(rs.next()){
                partidos.add(rsRowToPartido(rs));
            }
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return partidos;
    }

    public Resultado getResultadoPorPartido(int idPartido) {
        String query = "SELECT * FROM resultado WHERE fk_id_partido = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setInt(1, idPartido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Resultado(
                        rs.getInt("id_resultado"),
                        rs.getString("ganador"),
                        rs.getInt("fk_id_partido")
                    );
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al obtener resultado", ex);
        }
        return null;
    }
}