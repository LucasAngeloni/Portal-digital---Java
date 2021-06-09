package Logica;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Datos.NotaData;
import Modelo.Hilo;
import Modelo.Nota;
import Modelo.Usuario;

public class CatalogoDeNotas {
	
	public class LongitudMaximaException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4819603414417228798L;

		public LongitudMaximaException(String arg0) {
			super(arg0);
		}

	}

	
	private NotaData notasData;
	
	public CatalogoDeNotas() {
		notasData = new NotaData();
	}
	
	public Nota getOne(int hilo, LocalDateTime fecha_nota) throws SQLException {
		try {
			return this.notasData.getOne(hilo, fecha_nota);
		} catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo recuperar una de las notas");
		}
	}

	public void modificarRelevanciaPublicacion(Hilo hilo, Nota nota, Usuario usuario,int modificacion) throws SQLException {
		
		try {
			this.notasData.saveRelevancia(nota, usuario.getNombreUsuario(), modificacion);
			nota.modificarRelevanciaPublicacion(modificacion);
			hilo.modificarRelevanciaHilo(modificacion);
			usuario.modificarNotasRelevantes(nota, modificacion);
		} 
		catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo relevar la nota");
		}
	}
	
	public void insert(int hilo, ArrayList<Nota> notas) throws LongitudMaximaException, SQLException {
		
		for(Nota nota: notas) {
			if(nota.getDescripcion().length()> 180 )
				throw new LongitudMaximaException("La nota no puede superar los 180 caracteres");
			nota.setIdHilo(hilo);
		}
		
		try {
			this.notasData.insert(notas);
		} catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo crear la nota");
		}
	}
	
	public void delete(Hilo hilo, Nota nota) throws SQLException {
		try {
			this.notasData.delete(nota);
			hilo.removeNota(nota);
		} catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo eliminar la nota");
		}
	}
	
	public void update(Nota nota) throws LongitudMaximaException, SQLException {
		if(nota.getDescripcion().length()> 180 )
			throw new LongitudMaximaException("La nota no puede superar los 180 caracteres");
		try {
			this.notasData.update(nota);
		} catch (SQLException e) {
			throw new SQLException("ERROR. No se pudo modificar la nota");
		}
	}
}
