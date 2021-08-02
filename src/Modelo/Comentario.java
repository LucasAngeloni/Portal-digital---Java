package Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Comentario {

	private String descripcionComentario;
	private LocalDateTime fechaComentario;
	private int likes;
	private Comentario comentarioPrincipal;
	private ArrayList<Comentario> subcomentarios;
	private int nroSubcomentarios;
	private Usuario usuarioCreador;
	private Publicacion publicacion;
	
	public Comentario(Usuario usuario, LocalDateTime fecha,Comentario comentario_padre) {
		this.usuarioCreador = usuario;
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha_texto = fecha.format(formatter);
		this.fechaComentario = LocalDateTime.parse(fecha_texto, formatter);
		
		this.likes = 0;
		this.comentarioPrincipal = comentario_padre;
		this.setSubcomentarios(new ArrayList<Comentario>());
	}
	
	public Comentario(Usuario usuario, Nota nota) {
		this.usuarioCreador = usuario;
		this.publicacion = nota;		
		
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha_texto = now.format(formatter);
		this.fechaComentario = LocalDateTime.parse(fecha_texto, formatter);
		
		this.likes = 0;
		this.comentarioPrincipal = null;
		this.setSubcomentarios(new ArrayList<Comentario>());
	}
	
	public Comentario(Usuario usuario, Aporte aporte) {
		this.usuarioCreador = usuario;
		this.publicacion = aporte;		

		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha_texto = now.format(formatter);
		this.fechaComentario = LocalDateTime.parse(fecha_texto, formatter);
		
		this.likes = 0;
		this.comentarioPrincipal = null;
		this.setSubcomentarios(new ArrayList<Comentario>());
	}
	
	public Comentario(Usuario usuario, Aporte aporte, LocalDateTime fecha_comentario) {
		this.usuarioCreador = usuario;
		this.publicacion = aporte;		
		this.fechaComentario = fecha_comentario;
	}

	public String getDescripcionComentario() {
		return descripcionComentario;
	}
	public void setDescripcionComentario(String desc_comentario) {
		this.descripcionComentario = desc_comentario;
	}
	public LocalDateTime getFechaComentario() {
		return fechaComentario;
	}
	public void setFechaComentario(LocalDateTime fecha_comentario) {
		this.fechaComentario = fecha_comentario;
	}
	public int getLikes() {
		return this.likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public void quitarLike() {
		this.likes--;
	}
	public void agregarLike() {
		this.likes++;
	}
	public Comentario getComentarioPrincipal() {
		return comentarioPrincipal;
	}
	public void setComentarioPrincipal(Comentario comentario_principal) {
		this.comentarioPrincipal = comentario_principal;
	}
	public int getNroSubcomentarios() {
		return this.nroSubcomentarios;
	}
	public void setNroSubcomentarios(int nro_subcomentarios) {
		this.nroSubcomentarios = nro_subcomentarios;
	}
	
	public String getNombreUsuario() {
		return this.usuarioCreador.getNombreUsuario();
	}
	
	public Usuario getUsuario() {
		return this.usuarioCreador;
	}
	
	public LocalDateTime getFechaNota() {
		if(this.comentarioPrincipal != null)
			return this.comentarioPrincipal.getFechaNota();
		else
		    return this.publicacion.getFechaPublicacion();
	}
	
	public int getIdHilo() {
		if(this.comentarioPrincipal != null)
			return this.comentarioPrincipal.getIdHilo();
		else
		    return this.publicacion.getIdHilo();
	}
	
	public Publicacion getPublicacion() {
		if(this.comentarioPrincipal != null)
			return this.comentarioPrincipal.getPublicacion();
		else
			return this.publicacion;
	}
	public void setPublicacion(Publicacion publicacion) {
		this.publicacion = publicacion;
	}
	
	public String cantidadDeTiempo() {
		//en minutos
		
		String respuesta;
		LocalDateTime fechaActual = LocalDateTime.now();
		LocalDateTime fecha = this.fechaComentario;
		
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
		}
		if(tiempo > 24) {
			tiempo = tiempo/24;
			respuesta = tiempo + " dias";
		}
		if(tiempo > 7) {
			tiempo = tiempo/7;
			respuesta = tiempo + " semanas";
		}
		if(tiempo > 30.0/7) {
			tiempo = tiempo/4;
			respuesta = tiempo + " meses";
		}
		if(tiempo > 12) {
			tiempo = tiempo/12;
			respuesta = tiempo + " años";
		}
		
		return respuesta;
	}

	public ArrayList<Comentario> getSubcomentarios() {
		return this.subcomentarios;
	}
	public void addSubcomentario(Comentario subcomentario) {
		this.subcomentarios.add(subcomentario);
		this.nroSubcomentarios++;
	}

	public void setSubcomentarios(ArrayList<Comentario> subcomentarios) {
		this.subcomentarios = subcomentarios;
		this.nroSubcomentarios = subcomentarios.size();
	}

	public Comentario buscarSubcomentario(String nombre_usuario, LocalDateTime fecha_comentario) {
		
		for(Comentario subcomentario : this.subcomentarios) {
			if(subcomentario.getNombreUsuario().equals(nombre_usuario) && subcomentario.getFechaComentario().equals(fecha_comentario))
				return subcomentario;
		}
		for(Comentario subcomentario : this.subcomentarios) {
			Comentario subcom = subcomentario.buscarSubcomentario(nombre_usuario, fecha_comentario);
			if(subcom != null)
				return subcom;
		}
		return null;
	}

	public boolean eliminarSubcomentario(LocalDateTime fecha_comentario, String nombre_usuario) {
		
		for(Comentario subcomentario : this.subcomentarios) {
			if(subcomentario.getNombreUsuario().equals(nombre_usuario) && subcomentario.getFechaComentario().equals(fecha_comentario)) {
				this.subcomentarios.remove(subcomentario);
				this.nroSubcomentarios--;
				return true;
			}
		}
		for(Comentario subcomentario : this.subcomentarios) {
			subcomentario.eliminarSubcomentario(fecha_comentario, nombre_usuario);
		}
		return false;
		
	}
}
