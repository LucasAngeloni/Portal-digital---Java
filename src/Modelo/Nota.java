package Modelo;

import java.time.LocalDateTime;

public class Nota extends Publicacion{

	public Nota(int hilo, LocalDateTime fecha_nota, String descripcion) {		
		super(hilo, fecha_nota);
		this.setDescripcion(descripcion);
	}
	
	public Nota(String descripcion) {
		super(descripcion);
	}
	
}
