package Modelo;

import java.util.ArrayList;

public class Comunicador extends Usuario{

	private String descripcion;
	private String nombre;
	private String apellido;
	private ArrayList<Hilo> hilosCreados;
	private ArrayList<Aporte> aportesRealizados;
	
	public Comunicador(String nombre_usuario) {
		super(nombre_usuario);
		
		this.hilosCreados = new ArrayList<Hilo>();
		this.setAportesRealizados(new ArrayList<Aporte>());
	}
	public Comunicador(String nombre_usuario, String contraseña, String telefono,
			String email,String descripcion, String nombre, String apellido, ArrayList<Categoria> categorias) {
		
		super(nombre_usuario,contraseña,telefono,email,categorias);
		this.descripcion = descripcion;
		this.nombre = nombre;
		this.apellido = apellido;
		
		this.hilosCreados = new ArrayList<Hilo>();
		this.setAportesRealizados(new ArrayList<Aporte>());
	}
	
	public void setHilos(ArrayList<Hilo> hilos) {
		this.hilosCreados = hilos;
	}
	public ArrayList<Hilo> getHilos(){
		return this.hilosCreados;
	}
	
	public void addHilo(Hilo hilo) {
		this.hilosCreados.add(hilo);
	}
	
	public void removeHilo(Hilo hilo) {
		for(int i=0;i<this.hilosCreados.size();i++) {
			if(this.hilosCreados.get(i).getIdHilo() == hilo.getIdHilo()) {
				this.hilosCreados.remove(i);
				break;
			}
		}
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public ArrayList<Aporte> getAportesRealizados() {
		return aportesRealizados;
	}

	public void setAportesRealizados(ArrayList<Aporte> aportesRealizados) {
		this.aportesRealizados = aportesRealizados;
	}
	
	public Usuario castUsuario() {
		
		Usuario usuario = new Usuario(this.getNombreUsuario());
		usuario.setContraseña(this.getContraseña());
		usuario.setTelefono(this.getTelefono());
		usuario.setEmail(this.getEmail());
		usuario.setPreferencias(this.getPreferencias());
		usuario.setComentariosQueLeGustan(this.getComentariosQueLeGustan());
		usuario.setFechaNacimiento(this.getFechaNacimiento());
		usuario.setHilosGuardados(this.getHilosGuardados());
		usuario.setImagen(this.getImagen());
		usuario.setNotasRelevantes(this.getNotasRelevantes());
		usuario.setAportesRelevantes(this.getAportesRelevantes());
		usuario.setPreferencias(this.getPreferencias());
		
		return usuario;
	}	
	
}
