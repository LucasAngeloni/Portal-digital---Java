package Datos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Modelo.Nota;

public class NotaData{

	public Nota getOne(int hilo, LocalDateTime fecha_nota) throws SQLException{
		
		Nota nota = null;
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		ComentarioData cd = new ComentarioData();
		try {
			
			String consulta = "select * from notas where id_hilo = ? and fecha_publicacion = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1, hilo);
			pst.setTimestamp(2, Timestamp.valueOf(fecha_nota));
			
			resultSet = pst.executeQuery();
			while(resultSet.next()) {
				nota = new Nota(hilo,
						resultSet.getTimestamp("fecha_publicacion").toLocalDateTime(),
						resultSet.getString("descripcion"));
				nota.setComentarios(cd.getComentarios(nota));
				nota.setRelevancia(resultSet.getInt("relevancia"));
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
		return nota;
	}
	
	public ArrayList<Nota> getNotas(int hilo) throws SQLException{
		
		ArrayList<Nota> notas = new ArrayList<Nota>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		ComentarioData cd = new ComentarioData();
		
		try {
			String consulta = "SELECT * from notas where id_hilo = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1, hilo);
			
			resultSet = pst.executeQuery();
			Nota notaActual;
			while(resultSet.next()) {				
				notaActual = new Nota(hilo,
						resultSet.getTimestamp("fecha_publicacion").toLocalDateTime(),
						resultSet.getString("descripcion"));
				notaActual.setComentarios(cd.getComentarios(notaActual));
				notaActual.setRelevancia(resultSet.getInt("relevancia"));

				notas.add(notaActual);
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
		return notas;
	}
	
	public Nota getNotaPrincipal(int hilo) throws SQLException{
		
		Nota nota = null;
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		ComentarioData cd = new ComentarioData();
		try {
			String consulta = "SELECT fecha_publicacion,descripcion\r\n" + 
					" FROM telar.notas\r\n" + 
					"inner join hilos on notas.id_hilo = hilos.id_hilo\r\n" + 
					"where hilos.id_hilo =?\r\n"
					+ "order by fecha_publicacion";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1,hilo);
			
			resultSet = pst.executeQuery();
			if(resultSet.next()) {
				nota = new Nota(hilo,
						resultSet.getTimestamp(1).toLocalDateTime(),
						resultSet.getString(2));
				nota.setComentarios(cd.getComentarios(nota));
			}
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst != null)	pst.close();
				FactoryConnection.getInstancia().closeConnection();
			}
			catch(SQLException e) {
				throw new SQLException(e);
			}
		}
		return nota;
	}
	
	public void saveRelevancia(Nota nota, String usuario, int valor) throws SQLException{		
		
		if(valor == 1) 
			this.insertRelevancia(nota, usuario);
		
		if(valor == -1) 
			this.deleteRelevancia(nota, usuario);
		
	}
	
	private void deleteRelevancia(Nota nota, String usuario) throws SQLException{
		
		PreparedStatement pst = null;
		try {
			
			String insert = "delete from relevancia_notas where hilo = ? and fecha_nota = ? and usuario = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(insert);
			pst.setTimestamp(2, Timestamp.valueOf(nota.getFechaPublicacion()));
			pst.setInt(1, nota.getIdHilo());
			pst.setString(3, usuario);
			
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

	public void insertRelevancia(Nota nota,String usuario) throws SQLException {
		
		PreparedStatement pst = null;
		
		try {
			String insertar = "insert into relevancia_notas values(?,?,?)";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(insertar);
			
			pst.setInt(1, nota.getIdHilo());
			pst.setTimestamp(2, Timestamp.valueOf(nota.getFechaPublicacion()));
			pst.setString(3, usuario);
			
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
	
	public ArrayList<Nota> getNotasRelevantes(String nombreUsuario) throws SQLException{

		ArrayList<Nota> notas_relevantes = new ArrayList<Nota>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;

		ComentarioData cd = new ComentarioData();
		try {
			String consulta = "select id_hilo,fecha_publicacion,descripcion,relevancia,cant_comentarios from notas\r\n" + 
					"inner join relevancia_notas rn on (rn.hilo = notas.id_hilo and rn.fecha_nota = notas.fecha_publicacion)\r\n" + 
					"where usuario = ?";

			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setString(1, nombreUsuario);

			resultSet = pst.executeQuery();
			Nota notaActual;
			while(resultSet.next()) {
				notaActual = new Nota(resultSet.getInt("id_hilo"),
						resultSet.getTimestamp("fecha_publicacion").toLocalDateTime(),
						resultSet.getString("descripcion"));
				notaActual.setRelevancia(resultSet.getInt("relevancia"));
				//notaActual.setCantComentarios(resultSet.getInt("cant_comentarios"));
				notaActual.setComentarios(cd.getComentarios(notaActual));

				notas_relevantes.add(notaActual);
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

		return notas_relevantes;
	}
	
	public void insert(ArrayList<Nota> notas) throws SQLException {
		
		PreparedStatement pst = null;
		ResultSet keyResultSet=null;
		try {			
			String sentencia = "insert into notas(id_hilo,fecha_publicacion,descripcion,relevancia,cant_comentarios) values(?,?,?,?,?)";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			
			for(Nota nota : notas) {
				
				pst.setInt(1, nota.getIdHilo());
				pst.setTimestamp(2, Timestamp.valueOf(nota.getFechaPublicacion()));
				pst.setString(3, nota.getDescripcion());
				pst.setInt(4, nota.getRelevancia());
				pst.setInt(5, nota.getCantComentarios());
				
				pst.executeUpdate();
			}
		} 
		catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(keyResultSet!=null) keyResultSet.close();
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public void delete(Nota nota) throws SQLException {
		
		PreparedStatement pst2 = null;
		PreparedStatement pst3 = null;
		try {

			//Actualizar relevancia del hilo
			String actualizar_relevancia = "update hilos set relevancia_hilo = relevancia_hilo - ? where id_hilo=?";

			pst2 = FactoryConnection.getInstancia().getConnection().prepareStatement(actualizar_relevancia);
			pst2.setInt(1,nota.getRelevancia());
			pst2.setInt(2,nota.getIdHilo());

			pst2.executeUpdate();

			//Eliminar nota
			String eliminar_nota = "delete from notas where fecha_publicacion = ? and id_hilo=?";

			pst3 = FactoryConnection.getInstancia().getConnection().prepareStatement(eliminar_nota);
			pst3.setTimestamp(1,Timestamp.valueOf(nota.getFechaPublicacion()));
			pst3.setInt(2,nota.getIdHilo());

			pst3.executeUpdate();
		}
		catch (SQLException e) {        
			throw new SQLException(e);       
		} 
		finally{
			try {				
				if(pst2!=null) pst2.close();
				if(pst3!=null) pst3.close();

				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public void update(Nota nota) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			
			String update_nota = "update notas SET descripcion = ?, relevancia = ?, cant_comentarios = ? where id_hilo=? and fecha_publicacion=?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(update_nota);
			pst.setString(1,nota.getDescripcion());
			pst.setInt(2, nota.getRelevancia());
			pst.setInt(3,nota.getCantComentarios());
			pst.setInt(4,nota.getIdHilo());
			pst.setTimestamp(5,Timestamp.valueOf(nota.getFechaPublicacion()));
			
			pst.executeUpdate();
		}
		catch (SQLException e) {        
			throw new SQLException(e);        
		} 
		finally{
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
