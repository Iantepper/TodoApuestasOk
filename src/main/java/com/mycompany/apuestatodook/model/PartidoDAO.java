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
    try (Connection con = ConnectionPool.getInstance().getConnection();
         PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        
        preparedStatement.setString(1, partido.getLocal());
        preparedStatement.setString(2, partido.getVisitante());
        preparedStatement.setString(3, partido.getFecha());
        preparedStatement.executeUpdate();

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                partido.setIdPartido(generatedKeys.getInt(1));
            }
        }
    } catch (SQLException ex) {
        throw new RuntimeException("Error al insertar partido", ex);
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
    String query = "SELECT partido.local, partido.visitante, partido.fecha, resultado.ganador " +
                   "FROM partido " +
                   "JOIN resultado ON resultado.fk_id_partido = partido.id_partido";
    try (Connection con = ConnectionPool.getInstance().getConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
    Partido partido = new Partido(rs.getString("local"), rs.getString("visitante"), rs.getString("fecha"), new Resultado(rs.getString("ganador")));
    partidosConResultado.add(partido);
            }
        
    } catch (SQLException ex) {
        throw new RuntimeException(ex);
    }
    return partidosConResultado;
    }
        
    public List<Partido> getAll() {
        List<Partido>partidos = new LinkedList();
        String query = "SELECT * FROM partido";
        try(Connection con = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();)
        {
            while(rs.next()){
                partidos.add(rsRowToPartido(rs));
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return partidos;
    }
    
 

    public Partido getPartidoPorId(Integer Id) {
        String query = "SELECT * FROM partido WHERE id_partido = ?";
        Partido partido = null;
        try (Connection con = ConnectionPool.getInstance().getConnection(); PreparedStatement preparedStatement = con.prepareStatement(query)) {
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
    
    public void crearResultadoParaPartido(int idPartido, String ganador) {
    String query = "INSERT INTO resultado (ganador, fk_id_partido) VALUES (?, ?)";
    try (Connection con = ConnectionPool.getInstance().getConnection();
         PreparedStatement preparedStatement = con.prepareStatement(query)) {
        
        preparedStatement.setString(1, ganador);
        preparedStatement.setInt(2, idPartido);
        preparedStatement.executeUpdate();
        
    } catch (SQLException ex) {
        throw new RuntimeException("Error al crear resultado", ex);
    }
}
    
}
