package Logica;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Datos.CategoriaData;
import Modelo.Categoria;
import Modelo.Hilo;

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
			this.categoriaData.delete(id_categoria);
		} catch (SQLException e) {
			throw new SQLException("Error al eliminar la categoria");
		}
	}
	
	public void insert(Categoria categoria) throws SQLException, ExcepcionImagen {
		
		try {
			this.validarImagen(categoria.getImagenCategoria());
			this.categoriaData.insert(categoria);			
		} catch (SQLException e) {
			if(e.getErrorCode() == 0)
				throw new SQLException("El nombre de la categoría ya existe");
			else
				throw new SQLException("Error de conexion");
		}
	}
	
	public void update(Categoria categoria) throws SQLException, ExcepcionImagen {
		
		try {
			this.validarImagen(categoria.getImagenCategoria());
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
	
	private void validarImagen(String imagen) throws ExcepcionImagen {
		Pattern pat = Pattern.compile(".+(.jpg|.png|.jfif)$");
		Matcher mat = pat.matcher(imagen);
		if(!mat.matches())
			throw new ExcepcionImagen("El formato de la imagen no es válida. Debe ser .jpg, .png o .jfif");
	}
	
	public class ExcepcionImagen extends Exception{
		private static final long serialVersionUID = 1L;

		public ExcepcionImagen(String msg) {
			super(msg);
		}
	}
}
