package Logica;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Datos.AporteData;
import Modelo.Aporte;
import Modelo.Comunicador;
import Modelo.Hilo;
import Modelo.Usuario;

public class CatalogoDeAportes {
	
	public class LongitudMaximaException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public LongitudMaximaException(String msg) {
			super(msg);
		}

	}

	public static void main(String[] args) {
	}

	private AporteData aportesData;
	
	public CatalogoDeAportes() {
		this.aportesData = new AporteData();
	}
	
	public ArrayList<Aporte> getAportesHilo(int hilo) throws SQLException{
		return this.aportesData.getAportesHilo(hilo);
	}
	
	public ArrayList<Aporte> getAportesComunicador(Comunicador com) throws SQLException{
		return this.aportesData.getAportesComunicador(com);
	}
	
	public void agregarAporte(Aporte aporte, Hilo hilo) throws SQLException, LongitudMaximaException {
		
		try {
			if(aporte.getDescripcion().length() > 80)
				throw new LongitudMaximaException("No se puede realizar un aporte con más de 80 caracteres");
			this.aportesData.insert(aporte);
			hilo.addAporte(aporte);
		} 
		catch (SQLException e) {
			throw new SQLException("Ocurrió un ERROR. No se pudo publicar el Aporte");
		}
	}
	
	public void borrarAporte(Aporte aporte, Hilo hilo) throws SQLException {
		
		try {
			this.aportesData.delete(aporte);
			hilo.removeAporte(aporte);
		} 
		catch (SQLException e) {
			throw new SQLException("Ocurrió un ERROR. No se pudo eliminar el Aporte");
		}
	}
	
	public void modificarAporte(Aporte aporte) throws SQLException, LongitudMaximaException {
		
		try {
			if(aporte.getDescripcion().length() > 80)
				throw new LongitudMaximaException("No se puede realizar un aporte con más de 80 caracteres");
			this.aportesData.modificar(aporte);
		} 
		catch (SQLException e) {
			throw new SQLException("Ocurrió un ERROR. No se pudo modificar el Aporte");
		}
	}
	
	public void insertRelevancia(Aporte aporte, Usuario usuario) throws SQLException {
		
		try {
			this.aportesData.insertRelevancia(aporte, usuario.getNombreUsuario());
			usuario.insertAporteRelevante(aporte);
			aporte.modificarRelevanciaPublicacion(1);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Ocurrió un ERROR. No se pudo relevar el Aporte");
		}
	}
	
	public void removeRelevancia(Aporte aporte, Usuario usuario) throws SQLException {
		
		try {
			this.aportesData.removeRelevancia(aporte, usuario.getNombreUsuario());
			usuario.removeAporteRelevante(aporte);
			aporte.modificarRelevanciaPublicacion(-1);
		} 
		catch (SQLException e) {
			throw new SQLException("Ocurrió un ERROR. No se pudo quitar la relevancia del Aporte");
		}
	}
	
	public ArrayList<Aporte> filtrarAportes(ArrayList<Aporte> aportes, String texto){
		
		String exp_reg = ".*"+texto+".*";
		Pattern pat = Pattern.compile(exp_reg);
		
		ArrayList<Aporte> aportes_encontrados = new  ArrayList<Aporte>();
		for(Aporte aporte : aportes) {
			Matcher mat = pat.matcher(aporte.getDescripcion());
			if(mat.matches())
				aportes_encontrados.add(aporte);
		}
		return aportes_encontrados;
	}

}
