package Logica;

import java.sql.SQLException;
import java.util.ArrayList;
import Datos.CategoriaData;
import Datos.PreferenciasData;
import Datos.UsuarioData;
import Modelo.Categoria;
import Modelo.Hilo;
import Modelo.Preferencia;
import Modelo.Usuario;

public class CatalogoDeCategorias {

	private CategoriaData categoriaData;
	
	public CatalogoDeCategorias() {
		categoriaData = new CategoriaData();
	}
	
	public ArrayList<Categoria> getAll() throws SQLException{
		try {
			return categoriaData.getAll();
		} catch (SQLException e) {
			throw new SQLException("ERROR al cargar las categorías");
		}
	}
	
	public ArrayList<Hilo> getHilosCategoria(int id_categoria) throws SQLException{
		try {
			return EleccionHilosSinPreferencias.seleccionHilos(this.categoriaData.getHilosCategoria(id_categoria));
		} catch (SQLException e) {
			throw new SQLException("ERROR al cargar los hilos");
		}
	}
	
	public int cantidadCategorias() throws SQLException {
		try {
			return categoriaData.getCantidadCategorias();
		} catch (SQLException e) {
			throw new SQLException("ERROR de conexión");
		}
	}
	
	public void delete(int id_categoria) throws SQLException {
		try {			
			/*UsuarioData ud = new UsuarioData();
			
			ArrayList<Usuario> usuarios = ud.getAll();
			for(Usuario usuario : usuarios) {
				usuario.removePreferencia(id_categoria);				
				PreferenciasData.updatePreferencias(usuario.getPreferencias(), usuario.getNombreUsuario());
			}*/
			
			this.categoriaData.delete(id_categoria);
		} catch (SQLException e) {
			throw new SQLException("Error al eliminar la categoria");
		}
	}
	
	public void insert(Categoria categoria) throws SQLException {
		
		try {
			this.categoriaData.insert(categoria);
			/*int cant_categorias = this.cantidadCategorias();
			
			UsuarioData ud = new UsuarioData();
			PreferenciasData pd = new PreferenciasData();
			
			ArrayList<Usuario> usuarios = ud.getAll();
			Preferencia preferenciaNueva = new Preferencia(categoria, 1.0/cant_categorias);
			for(Usuario usuario : usuarios) {
				ArrayList<Preferencia> preferencias = new ArrayList<Preferencia>();
				preferencias.add(preferenciaNueva);
				
				pd.insertPreferencias(preferencias, usuario.getNombreUsuario());
				usuario.addPreferencia(preferenciaNueva);
				PreferenciasData.updatePreferencias(usuario.getPreferencias(), usuario.getNombreUsuario());
			}*/
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 0)
				throw new SQLException("El nombre de la categoría ya existe");
			else
				throw new SQLException("Error de conexion");
		}
	}
	
	public void update(Categoria categoria) throws SQLException {
		
		try {
			this.categoriaData.update(categoria);
		}
		catch (SQLException e) {
			if(e.getErrorCode() == 0)
				throw new SQLException("El nombre de la categoría ya existe");
			else
				throw new SQLException("Error de conexion");
		}
	}
	
	public ArrayList<Categoria> buscarCategorias(String texto) throws SQLException{
		
		try {
			return this.categoriaData.buscarCategorias(texto);
		} catch (SQLException e) {
			throw new SQLException("Ha ocurrido un ERROR al buscar las categorias");
		}
		
	}
}
