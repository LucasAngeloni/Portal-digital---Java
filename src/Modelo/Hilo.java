package Modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Hilo {

	private int idHilo;
	private int relevanciaHilo;
	private int nroAportes;
	private String titulo;
	private ArrayList<Nota> notas;
	private ArrayList<Aporte> aportes;
	private Comunicador comunicador;
	private ArrayList<Categoria> categorias;
	
	public Hilo(String titulo, Comunicador usuario) {
		
		this.comunicador = usuario;
		this.titulo = titulo;
		relevanciaHilo = 0;
		aportes = new ArrayList<Aporte>();
		
		notas = new ArrayList<Nota>();
		categorias = new ArrayList<Categoria>();
	}
	
	public Hilo(int id_hilo ,String nombreUsuario) {
		
		this.comunicador = new Comunicador(nombreUsuario);
		this.idHilo = id_hilo;
		relevanciaHilo = 0;
		aportes = new ArrayList<Aporte>();
		
		notas = new ArrayList<Nota>();
	}
	
	public Hilo(int id_hilo) {
		this.idHilo = id_hilo;
	}
	
	public void addNota(Nota nota) {
		this.notas.add(nota);
	}
	
	public void removeNota(Nota nota) {
		this.notas.remove(nota);
	}
	
	public Nota getNota(int id_nota) {
		return this.notas.get(id_nota-1);
	}
	
	public Nota getNota(LocalDateTime fecha_nota) {
		for(Nota nota : this.notas) {
			if(nota.getFechaPublicacion().equals(fecha_nota))
				return nota;
		}
		return null;
	}
	
	public int getIdNota(Nota nota) {
		
		for(int i=0;i<this.notas.size();i++) {
			if(this.notas.get(i).getFechaPublicacion().equals(nota.getFechaPublicacion()))
				return i+1;
		}
		return 0;
	}
	
	public void setNotas(ArrayList<Nota> notas) {
		this.notas = notas;
	}
	
	public ArrayList<Nota> getNotas(){
		return this.notas;
	}
	
	public void setAportes(ArrayList<Aporte> aps) {
		this.aportes = aps;
		this.nroAportes = aps.size();
	}
	
	public ArrayList<Aporte> getAportes(){
		return this.aportes;
	}
	
	public Aporte getAporte(LocalDateTime fecha, Comunicador comunicador) {
		for(Aporte aporte : this.aportes) {			
			if(aporte.getFechaPublicacion().equals(fecha) && aporte.getComunicador().getNombreUsuario().equals(comunicador.getNombreUsuario())) 
				return aporte;
		}
		return null;
	}
	
	public Aporte getAporte(LocalDateTime fecha, String comunicador) {
		for(Aporte aporte : this.aportes) {			
			if(aporte.getFechaPublicacion().equals(fecha) && aporte.getComunicador().getNombreUsuario().equals(comunicador)) 
				return aporte;
		}
		return null;
	}
	
	public void addAporte(Aporte aporte) {
		this.aportes.add(aporte);
		this.nroAportes++;
	}
	
	public void removeAporte(Aporte aporte) {
		this.aportes.remove(aporte);
	}
	
	public void setNroAportes(int num) {
		this.nroAportes = num;
	}
	
	public int getIdHilo() {
		return idHilo;
	}
	public void setIdHilo(int idHilo) {
		this.idHilo = idHilo;
	}
	public int getRelevanciaHilo() {
		return relevanciaHilo;
	}
	public void setRelevanciaHilo(int relevanciaHilo) {
		this.relevanciaHilo = relevanciaHilo;
	}
	public int getNroAportes() {
		return this.nroAportes;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Comunicador getComunicador() {
		return this.comunicador;
	}
	
	public void setComunicador(Comunicador comun) {
		
		this.comunicador = comun;
	}
	
	public void modificarRelevanciaHilo(int modificacion) {
		
		this.relevanciaHilo += modificacion;
	}
	
	public ArrayList<Categoria> getCategorias() {
		return categorias;
	}
	public void setCategorias(ArrayList<Categoria> categorias) {
		this.categorias = categorias;
	}
	public void addCategoria(Categoria categoria) {
		this.categorias.add(categoria);
	}
	
	public Comentario buscarComentario(LocalDateTime fecha_nota,String nombre_usuario, LocalDateTime fecha_comentario) {
		return this.getNota(fecha_nota).getComentario(nombre_usuario, fecha_comentario);
	}
	
	public Comentario buscarComentario(LocalDateTime fecha_aporte, String comunicador,String nombre_usuario, LocalDateTime fecha_comentario) {
		return this.getAporte(fecha_aporte, comunicador).getComentario(nombre_usuario, fecha_comentario);
	}
	
	public ArrayList<Comentario> getComentarios(){
		return this.getNota(1).getComentarios();
	}
	
	public int getTiempo(LocalDateTime fechaActual) {
		//en minutos
		
		LocalDateTime fecha = this.getNota(1).getFechaPublicacion(); 
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
		
		return tiempo;
	}
	
}
