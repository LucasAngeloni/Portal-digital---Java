package Modelo;

import java.time.LocalDateTime;

public class Aporte extends Publicacion {
	
	private Comunicador comunicador;
	
	public Comunicador getComunicador() {
		return this.comunicador;
	}
	public void setComunicador(Comunicador com) {
		this.comunicador = com;
	}

	public Aporte(int hilo, String descripcion, Comunicador usu) {
		
		super(hilo,descripcion);
		this.comunicador = usu;
	}
	
	public Aporte(int hilo, LocalDateTime fecha_publicacion, Comunicador com) {
		super(hilo,fecha_publicacion);
		this.comunicador = com;		
	}
	public Aporte(int hilo, LocalDateTime fecha_publicacion) {
		super(hilo,fecha_publicacion);
	}
}
