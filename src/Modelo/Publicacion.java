package Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class Publicacion extends BusinessEntity{

	protected int relevancia;
	private ArrayList<Comentario> comentarios;
	private String descripcion;
	private LocalDateTime fechaPublicacion;
	private int idHilo;
	
	public Publicacion(int hilo, String descripcion) {
		
		this.idHilo = hilo;
		this.relevancia = 0;
		this.comentarios = new ArrayList<Comentario>();
		this.descripcion = descripcion;
		
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha_texto = now.format(formatter);
		this.fechaPublicacion = LocalDateTime.parse(fecha_texto, formatter);
	}
	
	public Publicacion(String descripcion) {
		this.relevancia = 0;
		this.comentarios = new ArrayList<Comentario>();
		this.descripcion = descripcion;
		
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha_texto = now.format(formatter);
		this.fechaPublicacion = LocalDateTime.parse(fecha_texto, formatter);
	}
	
	public Publicacion(int hilo, LocalDateTime fecha_publicacion) {
		this.idHilo = hilo;
		this.fechaPublicacion = fecha_publicacion;
	}
	
	public void setIdHilo(int hilo) {
		this.idHilo = hilo;
	}	
	
	public int getIdHilo() {	
		return this.idHilo;
	}
	
	public int getCantComentarios() {
		return this.comentarios.size();
	}
	public LocalDateTime getFechaPublicacion() {
		return this.fechaPublicacion;
	}
	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public int getRelevancia() {
		return relevancia;
	}
	public void modificarRelevanciaPublicacion(int modificacion) {
		
		this.relevancia += modificacion;
	}
	
	public void setRelevancia(int relevancia) {
		this.relevancia = relevancia;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ArrayList<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(ArrayList<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	public void addComentario(Comentario comentario) {
		this.comentarios.add(comentario);
	}
	public void removeComentario(LocalDateTime fecha_comentario, String usuario) {
		for(Comentario comentario : this.comentarios) {
			if(comentario.getFechaComentario().equals(fecha_comentario) && comentario.getNombreUsuario().equals(usuario)) {
				this.comentarios.remove(comentario);
				break;
			}
		}
		for(Comentario comentario : this.comentarios) {
			if(comentario.eliminarSubcomentario(fecha_comentario, usuario))
				break;
		}
	}
	public Comentario getComentario(String nombre_usuario, LocalDateTime fecha_comentario) {
		for(Comentario com : this.comentarios) {
			if(com.getNombreUsuario().equals(nombre_usuario) && com.getFechaComentario().equals(fecha_comentario))
				return com;
		}
		for(Comentario com : this.comentarios) {
			Comentario subcom = com.buscarSubcomentario(nombre_usuario,fecha_comentario);
			if(subcom != null)
				return subcom;
		}
		return null;
	}
	
	public String cantidadDeTiempo() {
		//en minutos
		
		String respuesta;
		LocalDateTime fechaActual = LocalDateTime.now();
		LocalDateTime fecha = this.fechaPublicacion;
		
		int anio = fecha.getYear();
		int mes = fecha.getMonthValue();
		int dia = fecha.getDayOfMonth();
		int hora = fecha.getHour();
		int minuto = fecha.getMinute();
		
		int tiempo = 0;
		
		tiempo += (fechaActual.getYear() - anio)*365*24*60;
		tiempo += (fechaActual.getMonthValue() - mes)*30.4166*24*60;
		tiempo += (fechaActual.getDayOfMonth() - dia)*24*60;		
		tiempo += (fechaActual.getHour() - hora)*60;
		tiempo += fechaActual.getMinute() - minuto;		
		
		respuesta = tiempo + " minutos";
		if(tiempo > 60) {
			tiempo = tiempo/60;
			respuesta = tiempo + " horas";
			if(tiempo > 24) {
				tiempo = tiempo/24;
				respuesta = tiempo + " dias";
				if(tiempo > 7) {
					tiempo = tiempo/7;
					respuesta = tiempo + " semanas";
					if(tiempo > 30.0/7) {
						tiempo = tiempo/4;
						respuesta = tiempo + " meses";
						if(tiempo > 12) {
							tiempo = tiempo/12;
							respuesta = tiempo + " años";
						}
					}
				}
				
			}
		}
		return respuesta;
	}
}
