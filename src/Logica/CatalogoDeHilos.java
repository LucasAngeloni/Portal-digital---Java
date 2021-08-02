package Logica;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Datos.HiloData;
import Modelo.Hilo;
import Modelo.Usuario;

public class CatalogoDeHilos {

	public class LongitudMaximaException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3689718163649355575L;

		public LongitudMaximaException(String msg) {
			super(msg);
		}

	}

	private HiloData hiloData;

	public CatalogoDeHilos() {
		this.hiloData = new HiloData();
	}
	
	public ArrayList<Hilo> getHilosInicio(Usuario usuario) throws SQLException{
		try {
			if(usuario == null)
				return EleccionHilosSinPreferencias.seleccionHilos(this.hiloData.getHilosRelevantes());
			else
				return EleccionHilosPreferencias.seleccionHilos(this.hiloData.getHilosRelevantes(),usuario.getPreferencias());
		}
		catch(SQLException e) {
			throw new SQLException("Error al cargar los hilos");
		}
	}
	
	public ArrayList<Hilo> getHilosMasRecientes(LocalDateTime fecha) throws SQLException{
		
		try {
			return this.hiloData.getHilosMasRecientes(fecha);
		} catch (SQLException e) {
			throw new SQLException("Error al recuperar los hilos");
		}
	}
	
	public ArrayList<Hilo> buscarHilos(String tipo_filtro, String texto) throws SQLException {
		
		try {
			switch(tipo_filtro) {
			case "titulo":
				return this.hiloData.getHilos(texto);
			case "usuario":
				return this.hiloData.buscarHilosComunicador(texto);
			default:
				return null;
			}
		}
		catch(SQLException e) {
			throw new SQLException("ERROR de conexión");
		}
	}
	
	public ArrayList<Hilo> filtrarHilos(ArrayList<Hilo> hilos, String texto){
		
		String exp_reg = ".*"+texto+".*";
		Pattern pat = Pattern.compile(exp_reg);
		
		ArrayList<Hilo> hilos_encontrados = new  ArrayList<Hilo>();
		for(Hilo hilo : hilos) {
			Matcher mat = pat.matcher(hilo.getTitulo());
			if(mat.matches())
				hilos_encontrados.add(hilo);
		}
		return hilos_encontrados;
	}
	
	public void update(Hilo hilo) throws SQLException, LongitudMaximaException {
		
		if(hilo.getTitulo().length() > 80 )
			throw new LongitudMaximaException("El título del hilo no puede tener más de 80 caracteres");
		
		try {
			this.hiloData.update(hilo);
		} catch (SQLException e) {
			throw new SQLException("Error al actualizar el hilo");
		}
		
	}
	
	public Hilo getOne(int id_hilo) throws SQLException, NoExisteHiloException {
		try {
			Hilo hilo = hiloData.getOne(id_hilo);
			if(hilo == null)
				throw new NoExisteHiloException("Este hilo ya no existe");
			else
				return hilo; 
		} catch (SQLException e) {
			throw new SQLException("Error de conexión");
		}		
	}
	
	public void crearHilo(Hilo hilo) throws SQLException, LongitudMaximaException, Logica.CatalogoDeNotas.LongitudMaximaException {
		
		if(hilo.getTitulo().length() > 80 )
			throw new LongitudMaximaException("El título del hilo no puede tener más de 80 caracteres");
		
		CatalogoDeNotas cn = new CatalogoDeNotas();
		try {
			this.hiloData.insert(hilo);
			cn.insert(hilo.getIdHilo(),hilo.getNotas());
			hilo.getComunicador().addHilo(hilo);
		} 
		catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo publicar el hilo");
		}
		
	}
	
	public void deleteHilo(Hilo hilo) throws SQLException {
		try {
			this.hiloData.delete(hilo);
			hilo.getComunicador().removeHilo(hilo);
		} catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo eliminar el hilo");
		}
	}
	
	public class NoExisteHiloException extends Exception{
		private static final long serialVersionUID = 1L;

		public NoExisteHiloException(String msg) {
			super(msg);
		}
	}
}
