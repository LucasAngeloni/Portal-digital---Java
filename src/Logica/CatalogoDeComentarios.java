package Logica;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Datos.ComentarioData;
import Modelo.Aporte;
import Modelo.Comentario;
import Modelo.Nota;
import Modelo.Usuario;

public class CatalogoDeComentarios {

	private ComentarioData comentarioData;
	
	public CatalogoDeComentarios() {
		comentarioData = new ComentarioData();
	}
	public ArrayList<Comentario> getComentarios(Nota nota) throws SQLException {
		try {
			return comentarioData.getComentarios(nota);
		} catch (SQLException e) {
			throw new SQLException("ERROR al cargar los comentarios");
		}
	}
	
	public ArrayList<Comentario> getComentarios(Aporte aporte) throws SQLException{
		try {
			return comentarioData.getComentarios(aporte);
		} catch (SQLException e) {
			throw new SQLException("ERROR al cargar los comentarios");
		}
	}
	
	public ArrayList<Comentario> getSubcomentarios(Comentario comentarioPadre) throws SQLException{
		try {
			return this.comentarioData.getSubcomentarios(comentarioPadre);
		} catch (SQLException e) {
			throw new SQLException("ERROR al cargar los subcomentarios");
		}
	}
	
	public Comentario getOne(LocalDateTime fecha_com, String usuario) throws SQLException{
		try {
			return this.comentarioData.getOne(fecha_com, usuario);
		} catch (SQLException e) {
			throw new SQLException("ERROR al cargar el comentario");
		}
	}
	
	public ArrayList<Comentario> getComentariosGustados(String usuario) throws SQLException {
		try {
			return comentarioData.getComentariosGustados(usuario);
		} catch (SQLException e) {
			throw new SQLException("ERROR de conexión");
		}
	}
	
	public void deleteLike(LocalDateTime fecha_comentario, String usuario_comentador, Usuario usuario) throws SQLException {
		
		try {
			this.comentarioData.deleteLike(fecha_comentario, usuario_comentador, usuario.getNombreUsuario());
			usuario.removeLike(fecha_comentario,usuario_comentador);
		} 
		catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo quitar el like");
		}		
	}
	
	public void insertLike(LocalDateTime fecha_comentario, String usuario_comentador,Usuario usuario) throws SQLException {
		
		try {
			this.comentarioData.insertLike(fecha_comentario,usuario_comentador, usuario.getNombreUsuario());
			usuario.addLike(fecha_comentario,usuario_comentador);
		}
		catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo poner el like");
		}
	}
	
	public void insertComentario(Comentario comentario) throws SQLException{
		try {
			this.comentarioData.insertComentario(comentario);
		} catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo insertar el comentario");
		}
	}
	
	public void insertSubComentario(Comentario comentario) throws SQLException {
		try {
			this.comentarioData.insertSubComentario(comentario);
			comentario.getComentarioPrincipal().addSubcomentario(comentario);
		} catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo insertar el subcomentario");
		}
	}
	
	public void delete(LocalDateTime fecha_comentario, String usuario) throws SQLException {
		
		try {
			this.comentarioData.delete(fecha_comentario,usuario);
		} catch (SQLException e) {
			throw new SQLException("Ocurrió un ERROR. No se pudo eliminar el comentario");
		}
	}
	
	public ArrayList<Comentario> getAll() throws SQLException {
		
		try {
			return this.comentarioData.getAll();
		} catch (SQLException e) {
			throw new SQLException("Ocurrió un ERROR al recuperar los datos");
		}
	}
	
	public ArrayList<Comentario> buscarComentarios(String tipo_filtro, String texto) throws SQLException{
		
		ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
		try {
			switch(tipo_filtro) {
			case "usuario":
				comentarios = this.comentarioData.buscarComentarios("nombre_usuario", texto);
				break;
			case "descripcion":
				comentarios = this.comentarioData.buscarComentarios("desc_comentario", texto);
				break;
			case "com_padre":
				comentarios = null;
				break;
			}
		}
		catch(SQLException e) {
			throw new SQLException("Ocurrió un ERROR");
		}
		return comentarios;
	}
	
	public void ordenarComentariosPorFechaMayorAMenor(ArrayList<Comentario> comentarios) {		
		
		Comentario aux;
		for(int i=1;i<comentarios.size();i++) {
			for(int j=i;j>0;j--) {
				if(comentarios.get(j).getFechaComentario().isAfter(comentarios.get(j-1).getFechaComentario())) {
					aux = comentarios.get(j-1);
					comentarios.set(j-1, comentarios.get(j));
					comentarios.set(j, aux);
				}
			}
		}
	}
	
	public void ordenarComentariosPorFechaMenorAMayor(ArrayList<Comentario> comentarios) {		
		
		Comentario aux;
		for(int i=1;i<comentarios.size();i++) {
			for(int j=i;j>0;j--) {
				if(comentarios.get(j).getFechaComentario().isBefore(comentarios.get(j-1).getFechaComentario())) {
					aux = comentarios.get(j-1);
					comentarios.set(j-1, comentarios.get(j));
					comentarios.set(j, aux);
				}
			}
		}
	}
	
	public void ordenarComentariosPorLikesMayorAMenor(ArrayList<Comentario> comentarios) {		
		
		Comentario aux;
		for(int i=1;i<comentarios.size();i++) {
			for(int j=i;j>0;j--) {
				if(comentarios.get(j).getLikes() > comentarios.get(j-1).getLikes()) {
					aux = comentarios.get(j-1);
					comentarios.set(j-1, comentarios.get(j));
					comentarios.set(j, aux);
				}
			}
		}
	}
	
	public void ordenarComentariosPorLikesMenorAMayor(ArrayList<Comentario> comentarios) {		
		
		Comentario aux;
		for(int i=1;i<comentarios.size();i++) {
			for(int j=i;j>0;j--) {
				if(comentarios.get(j).getLikes() < comentarios.get(j-1).getLikes()) {
					aux = comentarios.get(j-1);
					comentarios.set(j-1, comentarios.get(j));
					comentarios.set(j, aux);
				}
			}
		}
	}
}
