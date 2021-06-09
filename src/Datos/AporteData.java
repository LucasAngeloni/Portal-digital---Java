package Datos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Modelo.Aporte;
import Modelo.Comunicador;

public class AporteData {

	public ArrayList<Aporte> getAportesHilo(int hilo) throws SQLException{
		
		PreparedStatement pst = null;
		ResultSet resultSet = null;	
		ArrayList<Aporte> aportes = new ArrayList<Aporte>();
		
		ComunicadorData cd = new ComunicadorData();
		ComentarioData comd = new ComentarioData();
		try {
			String consulta = "SELECT * from aportes where id_hilo = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1, hilo);
			
			resultSet = pst.executeQuery();
			Aporte aporteActual;
			Comunicador comunicadorActual;
			while(resultSet.next()) {
				
				comunicadorActual = cd.getOne(resultSet.getString("nombre_usuario"));
				aporteActual = new Aporte(hilo, resultSet.getString("descripcion_aporte"),
						comunicadorActual);
				aporteActual.setFechaPublicacion(resultSet.getTimestamp("fecha_publicacion").toLocalDateTime());
				aporteActual.setRelevancia(resultSet.getInt("relevancia"));
				aporteActual.setComentarios(comd.getComentarios(aporteActual));
				
				aportes.add(aporteActual);
			}
		} 
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return aportes;
	}
	
	public Aporte getOne(int hilo, String usuario, LocalDateTime fecha_aporte) throws SQLException{
		
		PreparedStatement pst = null;
		ResultSet resultSet = null;	
		Aporte aporte = null;
		
		ComunicadorData cd = new ComunicadorData();
		ComentarioData comd = new ComentarioData();
		try {
			String consulta = "SELECT * from aportes where id_hilo = ? and nombre_usuario = ? and fecha_publicacion = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1, hilo);
			pst.setString(2, usuario);
			pst.setTimestamp(3, Timestamp.valueOf(fecha_aporte));
			
			resultSet = pst.executeQuery();
			Comunicador comunicadorActual;
			while(resultSet.next()) {
				
				comunicadorActual = cd.getOne(resultSet.getString("nombre_usuario"));
				aporte = new Aporte(hilo, resultSet.getString("descripcion_aporte"),
						comunicadorActual);
				aporte.setFechaPublicacion(resultSet.getTimestamp("fecha_publicacion").toLocalDateTime());
				aporte.setRelevancia(resultSet.getInt("relevancia"));			
				aporte.setComentarios(comd.getComentarios(aporte));
			}
		} 
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return aporte;
	}

	public ArrayList<Aporte> getAportesComunicador(Comunicador comunicador) throws SQLException{
		
		PreparedStatement pst = null;
		ResultSet resultSet = null;	
		ArrayList<Aporte> aportes = new ArrayList<Aporte>();
		
		ComentarioData cd = new ComentarioData();
		try {
			String consulta = "SELECT * from aportes where nombre_usuario = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setString(1, comunicador.getNombreUsuario());
			
			resultSet = pst.executeQuery();
			Aporte aporteActual;
			while(resultSet.next()) {
				
				aporteActual = new Aporte(resultSet.getInt("id_hilo"),
						resultSet.getTimestamp("fecha_publicacion").toLocalDateTime());
				
				aporteActual.setComunicador(comunicador);
				aporteActual.setDescripcion(resultSet.getString("descripcion_aporte"));
				aporteActual.setRelevancia(resultSet.getInt("relevancia"));
				aporteActual.setComentarios(cd.getComentarios(aporteActual));
				
				aportes.add(aporteActual);
			}
			
		} 
		catch (SQLException e) {
			throw new SQLException(e);	
		}
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			}
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return aportes;
	}
	
	public void insert(Aporte aporte) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String agregar_aporte = "INSERT INTO aportes values(?,?,?,?,?)";
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(agregar_aporte);
			pst.setString(1,aporte.getComunicador().getNombreUsuario());
			pst.setInt(2, aporte.getIdHilo());
			pst.setTimestamp(3,Timestamp.valueOf(aporte.getFechaPublicacion()));
			pst.setString(4,aporte.getDescripcion());
			pst.setInt(5,aporte.getRelevancia());
			
			pst.executeUpdate();
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
			    if(pst != null) pst.close();
			    FactoryConnection.getInstancia().closeConnection();
			}
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public void delete(Aporte aporte) throws SQLException {
		
		PreparedStatement pst = null;
		ComentarioData cd = new ComentarioData();
		try {
			String eliminar_aporte = "DELETE from aportes where id_hilo=? and fecha_publicacion=? and nombre_usuario=?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(eliminar_aporte);
			pst.setInt(1, aporte.getIdHilo());
			pst.setTimestamp(2, Timestamp.valueOf(aporte.getFechaPublicacion()));
			pst.setString(3, aporte.getComunicador().getNombreUsuario());
			
			pst.executeUpdate();
			
			cd.deleteComentariosAporte(aporte);
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
			    if(pst != null) pst.close();
			    FactoryConnection.getInstancia().closeConnection();
			}
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public void modificar(Aporte aporte) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String modificar_aporte = "update aportes set descripcion_aporte=? where id_hilo=? and fecha_publicacion=? and nombre_usuario=?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(modificar_aporte);
			pst.setString(1, aporte.getDescripcion());
			pst.setInt(2, aporte.getIdHilo());
			pst.setTimestamp(3, Timestamp.valueOf(aporte.getFechaPublicacion()));
			pst.setString(4, aporte.getComunicador().getNombreUsuario());
			
			pst.executeUpdate();
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
			    if(pst != null) pst.close();
			    FactoryConnection.getInstancia().closeConnection();
			}
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public ArrayList<Aporte> getAportesRelevantes(String nombreUsuario) throws SQLException{

		ArrayList<Aporte> aportes_relevantes = new ArrayList<Aporte>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		ComunicadorData cd = new ComunicadorData();
		try {
			String consulta = "select hilo_aporte,fecha_aporte,usuario_aporte from relevancias_aportes\r\n" + 
					"where usuario_relevancia = ?";

			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setString(1, nombreUsuario);

			resultSet = pst.executeQuery();
			Aporte aporteActual;
			while(resultSet.next()) {

				aporteActual = new Aporte(resultSet.getInt("hilo_aporte"),
						resultSet.getTimestamp("fecha_aporte").toLocalDateTime(),
						cd.getOne(resultSet.getString("usuario_aporte")));

				aportes_relevantes.add(aporteActual);
			}
		} 
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return aportes_relevantes;
	}
	
	public void insertRelevancia(Aporte aporte, String usuario) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String insertar = "insert into relevancias_aportes values(?,?,?,?)";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(insertar);
			
			pst.setInt(1, aporte.getIdHilo());
			pst.setTimestamp(2, Timestamp.valueOf(aporte.getFechaPublicacion()));
			pst.setString(3, aporte.getComunicador().getNombreUsuario());
			pst.setString(4, usuario);
			
			pst.executeUpdate();
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public void removeRelevancia(Aporte aporte, String usuario) throws SQLException{
		
		PreparedStatement pst = null;
		try {
			
			String insert = "delete from relevancias_aportes where hilo_aporte = ? and fecha_aporte = ? and usuario_aporte = ? and usuario_relevancia = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(insert);
			pst.setInt(1, aporte.getIdHilo());
			pst.setTimestamp(2, Timestamp.valueOf(aporte.getFechaPublicacion()));
			pst.setString(3, aporte.getComunicador().getNombreUsuario());
			pst.setString(4, usuario);
			
			pst.executeUpdate();
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
}
