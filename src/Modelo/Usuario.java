package Modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Usuario extends BusinessEntity{

	private String nombreUsuario;
	private LocalDate fechaNacimiento;
	private String imagen;
	private String email;
	private String telefono;
	private String contraseña;
	private String repeticionContraseña;
	private ArrayList<Preferencia> preferencias;
	private ArrayList<Hilo> hilosGuardados;
	private ArrayList<Nota> notasRelevantes;
	private ArrayList<Aporte> aportesRelevantes;
	private ArrayList<Comentario> comentariosQueLeGustan;
	
	public Usuario(String nombre_usuario) {
		this.nombreUsuario = nombre_usuario;
		this.imagen = null;
		this.notasRelevantes = new ArrayList<Nota>();
	}
	
	public Usuario(String nombre_usuario, String telefono, LocalDate fecha_nacimiento, String email, String imagen) {
		this.nombreUsuario = nombre_usuario;
		this.telefono = telefono;
		this.fechaNacimiento = fecha_nacimiento;
		this.email = email;
		this.imagen = imagen;		
	}
	
	//Usuario nuevo
	public Usuario(String nombre_usuario, String contraseña, String telefono,
			String email,ArrayList<Categoria> categorias) {
		
		this.nombreUsuario = nombre_usuario;
		this.contraseña = contraseña;
		this.telefono = telefono;
		this.email = email;
		this.imagen = null;
		this.notasRelevantes = new ArrayList<Nota>();
		this.aportesRelevantes = new ArrayList<Aporte>();
		this.hilosGuardados = new ArrayList<Hilo>();
		this.comentariosQueLeGustan = new ArrayList<Comentario>();
		this.inicializarPreferencias(categorias);
	}
	
	private void inicializarPreferencias(ArrayList<Categoria> categorias) {
		
		double valor_preferencias = 1.0/categorias.size();
		this.preferencias = new ArrayList<Preferencia>();
		for(Categoria categoria : categorias) 
			this.preferencias.add(new Preferencia(categoria,valor_preferencias));
	}

	public ArrayList<Comentario> getComentariosQueLeGustan(){
		return this.comentariosQueLeGustan;
	}
	
	public void setComentariosQueLeGustan(ArrayList<Comentario> comentarios) {
		this.comentariosQueLeGustan = comentarios;
	}
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = LocalDate.parse(fechaNacimiento);
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	public ArrayList<Preferencia> getPreferencias() {
		return preferencias;
	}
	
	public Categoria[] getCategoriasPreferidas(){
		Preferencia[] mayoresPreferencias = new Preferencia[3];
		
		for(Preferencia preferencia : this.preferencias) {
			for(int i=0;i<mayoresPreferencias.length;i++) {
				try {
					Preferencia aux;
					if(mayoresPreferencias[i].getValorPreferencia() < preferencia.getValorPreferencia()) {
						aux = mayoresPreferencias[i];
						mayoresPreferencias[i] = preferencia;
						preferencia = aux;
					}
				}
				catch(NullPointerException e) {
					mayoresPreferencias[i] = preferencia;
					break;
				} 
			}
		}
		Categoria[] categoriasPreferidas = {
				mayoresPreferencias[0].getCategoria(),
				mayoresPreferencias[1].getCategoria(),
				mayoresPreferencias[2].getCategoria() };
		return categoriasPreferidas;
	}
	
	public void setPreferencias(ArrayList<Preferencia> preferencias) {
		this.preferencias = preferencias;
	}
	
	public void addPreferencia(Preferencia preferenciaNueva) {
		
		int cant_preferencias = this.preferencias.size();
		double variacion;
		for(Preferencia preferencia : this.preferencias) {
			variacion = -preferencia.getValorPreferencia()/(cant_preferencias+1);
			preferencia.modificarValorPreferencia(variacion);
		}
		this.preferencias.add(preferenciaNueva);
	}
	
	public void removePreferencia(int id_categoria) {
		
		double variacion;
		double valor_preferencia = 0;
		
		for(Preferencia preferencia : this.preferencias) {
			if(preferencia.getIdCategoria() == id_categoria) {
				valor_preferencia = preferencia.getValorPreferencia();
				this.preferencias.remove(preferencia);
				break;
			}
		}		
		for(Preferencia preferencia : this.preferencias) {
			variacion = preferencia.getValorPreferencia()*valor_preferencia/(1-valor_preferencia);
			preferencia.modificarValorPreferencia(variacion);
		}
	}
	
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public ArrayList<Hilo> getHilosGuardados(){
		return this.hilosGuardados;
	}
	
	public void setHilosGuardados(ArrayList<Hilo> hilos_guardados) {
		this.hilosGuardados = hilos_guardados;
	}

	public void setNotasRelevantes(ArrayList<Nota> notas_rel) {
		this.notasRelevantes = notas_rel;
	}
	
	public ArrayList<Nota> getNotasRelevantes(){
		return this.notasRelevantes;
	}
	
	public void setAportesRelevantes(ArrayList<Aporte> aportes_rel) {	
		this.aportesRelevantes = aportes_rel;
	}
	
	public ArrayList<Aporte> getAportesRelevantes() {
		return this.aportesRelevantes;
	}
	
	public void modificarNotasRelevantes(Nota nota, int modificacion) {
		if(modificacion == 1)
			this.addNotaRelevante(nota);
		else
			this.removeNotaRelevante(nota);
	}
	
	public void removeNotaRelevante(Nota nota) {
		Nota notaRelevante;
		for(int i=0;i<this.notasRelevantes.size();i++) {
			notaRelevante = this.notasRelevantes.get(i); 
			if(notaRelevante.getIdHilo() == nota.getIdHilo() && notaRelevante.getFechaPublicacion().equals(nota.getFechaPublicacion())) {
				this.notasRelevantes.remove(i);
				break;
			}
		}
	}
	public void addNotaRelevante(Nota nota) {
		this.notasRelevantes.add(nota);
	}
	
	public void guardarHilo(Hilo hilo) {
		this.hilosGuardados.add(hilo);
	}
	public void quitarHiloGuardado(Hilo hilo) {
		int count = 0;
		for(Hilo hiloGuardado : this.hilosGuardados) {
			if(hiloGuardado.getIdHilo() == hilo.getIdHilo()) {
				this.hilosGuardados.remove(count);
				break;
			}
			count++;
		}
	}
	
	public boolean validarHiloGuardado(int id_hilo) {
		
		hilosGuardados = this.getHilosGuardados();
		
		for(Hilo hiloGuardado: hilosGuardados) {
			if(hiloGuardado.getIdHilo() == id_hilo)
				return true;
		}
		return false;
	}
	
	public boolean validarNotaRelevante(Nota nota) {
		
		for(Nota notaRelevante: notasRelevantes) {
			if(notaRelevante.getIdHilo() == nota.getIdHilo() && notaRelevante.getFechaPublicacion().equals(nota.getFechaPublicacion())) 
				return true;
		}
		return false;
	}
	
	public boolean validarRelevanciaAporte(Aporte aporte) {
		for(Aporte aporteRelevante: this.aportesRelevantes) {
			if(aporteRelevante.getIdHilo() == aporte.getIdHilo() && aporteRelevante.getFechaPublicacion().equals(aporte.getFechaPublicacion())
					&& aporteRelevante.getComunicador().getNombreUsuario().equals(aporte.getComunicador().getNombreUsuario())) 
				return true;
		}
		return false;

	}
	
	public boolean meGustaPuesto(String nombreUsuario, LocalDateTime fecha) {
		for(Comentario comentario: this.comentariosQueLeGustan) {
			if(comentario.getFechaComentario().equals(fecha) && comentario.getNombreUsuario().equals(nombreUsuario))
				return true;
		}
		return false;
	}

	public String getRepeticionContraseña() {
		return repeticionContraseña;
	}

	public void setRepeticionContraseña(String repeticionContraseña) {
		this.repeticionContraseña = repeticionContraseña;
	}

	public void removeAporteRelevante(Aporte aporte) {
		Aporte aporteRelevante;
		for(int i=0;i<this.aportesRelevantes.size();i++) {
			aporteRelevante = this.aportesRelevantes.get(i); 
			if(aporteRelevante.getIdHilo() == aporte.getIdHilo() && aporteRelevante.getFechaPublicacion().equals(aporte.getFechaPublicacion())
					&& aporteRelevante.getComunicador().getNombreUsuario().equals(aporte.getComunicador().getNombreUsuario())) {
				this.aportesRelevantes.remove(i);
				break;
			}
		}
	}

	public void insertAporteRelevante(Aporte aporte) {
		this.aportesRelevantes.add(aporte);
	}

	public void removeLike(Comentario comentarioLike) {
		int i = 0;
		for(Comentario comentario: this.comentariosQueLeGustan) {
			if(comentario.getFechaComentario().equals(comentarioLike.getFechaComentario()) && 
					comentario.getNombreUsuario().equals(comentarioLike.getNombreUsuario())){
				this.comentariosQueLeGustan.remove(i);
				break;
			}
			i++;
		}
	}
	
	public void addLike(Comentario comentarioLike) {
		this.comentariosQueLeGustan.add(comentarioLike);
	}
}
