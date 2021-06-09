package Datos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Modelo.Aporte;
import Modelo.Comentario;
import Modelo.Nota;

public class ComentarioData{

	public ArrayList<Comentario> getAll() throws SQLException {
		
		ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		UsuarioData ud = new UsuarioData();
		try {	
			String consulta = "select * from comentarios";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			
			resultSet = pst.executeQuery();
			Comentario comentarioActual;
			while(resultSet.next()) {
				
				Comentario comentarioPrincipal = null;
				String usuario_com_princ = resultSet.getString("nombre_usuario_comentario_principal");
				if(usuario_com_princ != null) 
					comentarioPrincipal = this.getOne(resultSet.getTimestamp("fecha_comentario_principal").toLocalDateTime(), usuario_com_princ);
				
				comentarioActual = new Comentario(ud.getOne(resultSet.getString("nombre_usuario")),
						resultSet.getTimestamp("fecha_comentario").toLocalDateTime(), 
						comentarioPrincipal);
				comentarioActual.setDescripcionComentario(resultSet.getString("desc_comentario"));
				comentarioActual.setLikes(resultSet.getInt("likes"));
				
				comentarios.add(comentarioActual);
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
		return comentarios;
	}

	public ArrayList<Comentario> getComentarios(Nota nota) throws SQLException{
		
		ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		UsuarioData ud = new UsuarioData();
		try {
			String consulta = "select * from comentarios\r\n" + 
					"where hilo = ? and fecha_publicacion = ? and fecha_comentario_principal is null";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1, nota.getIdHilo());
			pst.setTimestamp(2, Timestamp.valueOf(nota.getFechaPublicacion()));
			
			resultSet = pst.executeQuery();
			
			Comentario comentarioActual;
			while(resultSet.next()) {
				
				comentarioActual = new Comentario(ud.getOne(resultSet.getString("nombre_usuario")),nota);
				comentarioActual.setDescripcionComentario(resultSet.getString("desc_comentario"));
				comentarioActual.setFechaComentario(resultSet.getTimestamp("fecha_comentario").toLocalDateTime());
				comentarioActual.setLikes(resultSet.getInt("likes"));
				comentarioActual.setSubcomentarios(this.getSubcomentarios(comentarioActual));
				//comentarioActual.setNroSubcomentarios(this.getNroSubcomentarios(resultSet.getTimestamp("fecha_comentario"), 
				//		resultSet.getString("nombre_usuario")));
				
				comentarios.add(comentarioActual);
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
		return comentarios;
	}
	
	public ArrayList<Comentario> getComentarios(Aporte aporte) throws SQLException{
		
		ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		UsuarioData ud = new UsuarioData();
		try {
			String consulta = "select * from comentarios\r\n" + 
					"where hilo = ? and fecha_publicacion = ? and usuario_aporte = ? and fecha_comentario_principal is null";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1, aporte.getIdHilo());
			pst.setTimestamp(2, Timestamp.valueOf(aporte.getFechaPublicacion()));
			pst.setString(3,aporte.getComunicador().getNombreUsuario());
			
			resultSet = pst.executeQuery();
			
			Comentario comentarioActual;
			while(resultSet.next()) {
				
				comentarioActual = new Comentario(ud.getOne(resultSet.getString("nombre_usuario")),aporte);
				comentarioActual.setDescripcionComentario(resultSet.getString("desc_comentario"));
				comentarioActual.setFechaComentario(resultSet.getTimestamp("fecha_comentario").toLocalDateTime());
				comentarioActual.setLikes(resultSet.getInt("likes"));
				comentarioActual.setSubcomentarios(this.getSubcomentarios(comentarioActual));
				//comentarioActual.setNroSubcomentarios(this.getNroSubcomentarios(resultSet.getTimestamp("fecha_comentario"), 
				//		resultSet.getString("nombre_usuario")));
				
				comentarios.add(comentarioActual);
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
		return comentarios;
	}
	
	public int getNroSubcomentarios(Timestamp fecha, String usuario) throws SQLException {
		
		int nro_comentarios = 0;
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		try {
			String consulta_nroComentarios="select count(fecha_comentario) as cantidad from comentarios "
					+ "where fecha_comentario_principal = ? and nombre_usuario_comentario_principal = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta_nroComentarios);
			
			pst.setTimestamp(1, fecha);
			pst.setString(2, usuario);
				
			resultSet = pst.executeQuery();
			while(resultSet.next())
				nro_comentarios = resultSet.getInt("cantidad");
			
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst != null) pst.close();				
				FactoryConnection.getInstancia().closeConnection();
			}
			catch(SQLException e) {
				throw new SQLException(e);
			}
		}
		return nro_comentarios;
	}
	
	public ArrayList<Comentario> getSubcomentarios(Comentario comentarioPadre) throws SQLException{
		
		ArrayList<Comentario> subcomentarios = new ArrayList<Comentario>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		UsuarioData ud = new UsuarioData();
		try {
			String consulta = "select * from comentarios "
					+ "where fecha_comentario_principal = ? and nombre_usuario_comentario_principal= ? ";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setTimestamp(1, Timestamp.valueOf(comentarioPadre.getFechaComentario()));
			pst.setString(2, comentarioPadre.getNombreUsuario());
			
			resultSet = pst.executeQuery();
			Comentario subcomentarioActual;
			while(resultSet.next()) {
				
				subcomentarioActual = new Comentario(ud.getOne(resultSet.getString("nombre_usuario")),
						new Nota(resultSet.getInt("hilo"),
						resultSet.getTimestamp("fecha_publicacion").toLocalDateTime(),
						null));
				
				subcomentarioActual.setComentarioPrincipal(comentarioPadre);
				subcomentarioActual.setDescripcionComentario(resultSet.getString("desc_comentario"));
				subcomentarioActual.setFechaComentario(resultSet.getTimestamp("fecha_comentario").toLocalDateTime());
				subcomentarioActual.setLikes(resultSet.getInt("likes"));
				subcomentarioActual.setNroSubcomentarios(this.getNroSubcomentarios(resultSet.getTimestamp("fecha_comentario"), 
						resultSet.getString("nombre_usuario")));
				
				subcomentarios.add(subcomentarioActual);
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
		return subcomentarios;
	}
	
	public ArrayList<Comentario> getComentariosGustados(String usuario) throws SQLException{
		
		ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		UsuarioData ud = new UsuarioData();
		try {
			
			String consulta = "select com.fecha_comentario,com.nombre_usuario from comentarios as com\r\n" + 
					"inner join likes on com.fecha_comentario = likes.fecha_comentario and usuario_comentador = com.nombre_usuario\r\n" + 
					"where usuario = ?";

			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setString(1, usuario);
			
			resultSet = pst.executeQuery();
			
			Comentario comentarioActual;
			while(resultSet.next()) {
				comentarioActual = new Comentario(ud.getOne(resultSet.getString("nombre_usuario")),resultSet.getTimestamp("fecha_comentario").toLocalDateTime(),null);
				comentarios.add(comentarioActual);
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
		
		return comentarios;
	}
	
	public void deleteLike(LocalDateTime fecha_comentario, String usuario_comentador, String usuario_dislike) throws SQLException{
		
		PreparedStatement pst = null;
		try {
			
			String insert = "delete from likes where fecha_comentario = ? and usuario = ? and usuario_comentador = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(insert);
			pst.setTimestamp(1, Timestamp.valueOf(fecha_comentario));
			pst.setString(2, usuario_dislike);
			pst.setString(3, usuario_comentador);
			
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

	public void insertLike(LocalDateTime fecha_comentario, String usuario_comentador, String usuario_like) throws SQLException{
		
		PreparedStatement pst = null;
		try {
			String insertar = "insert into likes values(?,?,?)";

			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(insertar);
			
			pst.setString(1, usuario_like);
			pst.setTimestamp(2, Timestamp.valueOf(fecha_comentario));
			pst.setString(3, usuario_comentador);
			
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
	
	public Comentario getOne(LocalDateTime fecha_com, String usuario) throws SQLException {
		
		Comentario comentario = null;
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		UsuarioData ud = new UsuarioData();
		NotaData nd = new NotaData();
		AporteData ad = new AporteData();
		try {	
			String consulta = "select * from comentarios\r\n" + 
					"where fecha_comentario = ? and nombre_usuario = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setTimestamp(1, Timestamp.valueOf(fecha_com));
			pst.setString(2, usuario);
			
			resultSet = pst.executeQuery();
			while(resultSet.next()) {
				
				Comentario comentarioPrincipal = null;
				String usuario_com_princ = resultSet.getString("nombre_usuario_comentario_principal");
				if(usuario_com_princ != null)
					comentarioPrincipal = this.getOne(resultSet.getTimestamp("fecha_comentario_principal").toLocalDateTime(), usuario_com_princ);
				
				comentario = new Comentario(ud.getOne(usuario),fecha_com, comentarioPrincipal);
				if(resultSet.getString("usuario_aporte") == null)
					comentario.setPublicacion(nd.getOne(resultSet.getInt("hilo"), 
							resultSet.getTimestamp("fecha_publicacion").toLocalDateTime()));
				else
					comentario.setPublicacion(ad.getOne(resultSet.getInt("hilo"), 
							resultSet.getString("usuario_aporte"), 
							resultSet.getTimestamp("fecha_publicacion").toLocalDateTime()));
				
				comentario.setDescripcionComentario(resultSet.getString("desc_comentario"));
				comentario.setLikes(resultSet.getInt("likes"));
				comentario.setSubcomentarios(this.getSubcomentarios(comentario));
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
		return comentario;
	}
	
	public void insertComentario(Comentario comentario) throws SQLException{
		
		PreparedStatement pst = null;
		try {
			String insert = "insert into comentarios values(?,?,?,?,?,?,null,null,?)";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(insert);
			
			pst.setTimestamp(1, Timestamp.valueOf(comentario.getFechaComentario()));
			pst.setString(2, comentario.getNombreUsuario());
			pst.setInt(3, comentario.getIdHilo());
			pst.setTimestamp(4, Timestamp.valueOf(comentario.getFechaNota()));
			pst.setString(5, comentario.getDescripcionComentario());
			pst.setInt(6, comentario.getLikes());
			
			if(comentario.getPublicacion().getClass().getName() == "Modelo.Aporte")
			{
				Aporte aporte_comentario = (Aporte)comentario.getPublicacion();
				pst.setString(7, aporte_comentario.getComunicador().getNombreUsuario());
			}
			else
				pst.setString(7, null);
			
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
	
	public void insertSubComentario(Comentario comentario) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String insert = "insert into comentarios values(?,?,?,?,?,?,?,?,?)";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(insert);
			
			Comentario comentarioPrincipal = comentario.getComentarioPrincipal();
			pst.setTimestamp(1, Timestamp.valueOf(comentario.getFechaComentario()));
			pst.setString(2, comentario.getNombreUsuario());
			pst.setInt(3, comentarioPrincipal.getIdHilo());
			pst.setTimestamp(4, Timestamp.valueOf(comentarioPrincipal.getFechaNota()));
			pst.setString(5, comentario.getDescripcionComentario());
			pst.setInt(6, comentario.getLikes());
			pst.setTimestamp(7, Timestamp.valueOf(comentarioPrincipal.getFechaComentario()));
			pst.setString(8, comentarioPrincipal.getNombreUsuario());
			
			if(comentario.getPublicacion().getClass().getName() == "Modelo.Aporte")
			{
				Aporte aporte_comentario = (Aporte)comentario.getPublicacion();
				pst.setString(9, aporte_comentario.getComunicador().getNombreUsuario());
			}
			else
				pst.setString(9, null);
			
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
	
	public void delete(LocalDateTime fecha_comentario, String usuario) throws SQLException{
		
		PreparedStatement pst = null;
		try {
			this.deleteSubcomentarios(fecha_comentario, usuario);
			
			String sentencia = "delete from comentarios where fecha_comentario = ? and nombre_usuario = ?";

			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			pst.setTimestamp(1, Timestamp.valueOf(fecha_comentario));
			pst.setString(2, usuario);

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
	
	public void deleteComentariosNota(Nota nota) throws SQLException{
		
		PreparedStatement pst = null;
		try {			
			//Eliminar comentarios
			String sentencia = "delete from comentarios where hilo = ? and fecha_publicacion = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			pst.setInt(1, nota.getIdHilo());
			pst.setTimestamp(2, Timestamp.valueOf(nota.getFechaPublicacion()));

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
	
	public void deleteComentariosAporte(Aporte aporte) throws SQLException{
		
		PreparedStatement pst = null;
		String sentencia = "delete from comentarios where hilo = ? and fecha_publicacion = ? and usuario_aporte = ?";
		try {
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			pst.setInt(1, aporte.getIdHilo());
			pst.setTimestamp(2, Timestamp.valueOf(aporte.getFechaPublicacion()));
			pst.setString(3, aporte.getComunicador().getNombreUsuario());

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();	
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public void deleteSubcomentarios(LocalDateTime fecha_comentario_principal, String usuario_comentario_principal) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String sentencia = "delete from comentarios where fecha_comentario_principal = ? and nombre_usuario_comentario_principal = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			pst.setTimestamp(1, Timestamp.valueOf(fecha_comentario_principal));
			pst.setString(2, usuario_comentario_principal);
			
			pst.executeUpdate();
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst != null)	pst.close();
				FactoryConnection.getInstancia().closeConnection();
			}
			catch(SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public ArrayList<Comentario> buscarComentarios(String tipo_filtro, String texto) throws SQLException {
		
		ArrayList<Comentario> comentarios_encontrados = new ArrayList<Comentario>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		UsuarioData ud = new UsuarioData();
		try {
			String texto_a_buscar = "%"+texto+"%";
			String consulta = "select * from comentarios where "+ tipo_filtro + " like ?";
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setString(1, texto_a_buscar);

			resultSet = pst.executeQuery();
			Comentario comentarioActual;
			while(resultSet.next()) {
				
				Comentario comentarioPrincipal = null;
				String usuario_com_princ = resultSet.getString("nombre_usuario_comentario_principal");
				if(usuario_com_princ != null)
					comentarioPrincipal = this.getOne(resultSet.getTimestamp("fecha_comentario_principal").toLocalDateTime(), usuario_com_princ);
				
				comentarioActual = new Comentario(ud.getOne(resultSet.getString("nombre_usuario")),
						resultSet.getTimestamp("fecha_comentario").toLocalDateTime(), 
						comentarioPrincipal);
				comentarioActual.setDescripcionComentario(resultSet.getString("desc_comentario"));
				comentarioActual.setLikes(resultSet.getInt("likes"));
				
				comentarios_encontrados.add(comentarioActual);
			}
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			if(resultSet != null) resultSet.close();
			if(pst!=null) pst.close();
			FactoryConnection.getInstancia().closeConnection();
		}
		return comentarios_encontrados;
	}
}
