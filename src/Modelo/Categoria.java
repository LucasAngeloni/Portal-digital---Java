package Modelo;

public class Categoria {
	
	private int idCategoria;
	private String descripcion;
	private String imagen;
	//private ArrayList<Hilo> hilosCategoria;
	
	public Categoria(int id, String descripcion) {
		this.idCategoria = id;
		this.descripcion = descripcion;
	}
	
	public Categoria(int id) {
		this.idCategoria = id;
	}
	
	public Categoria(String descripcion_categoria, String imagen) {
		this.descripcion = descripcion_categoria;
		this.imagen = imagen;
	}
	
	public String getImagenCategoria() {
		return imagen;
	}

	public void setImagenCategoria(String imagenCategoria) {
		this.imagen = imagenCategoria;
	}

	public int getIdCategoria() {
		return this.idCategoria;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getDescripcionCategoria() {
		return descripcion;
	}
	public void setDescripcionCategoria(String descCategoria) {
		this.descripcion = descCategoria;
	}
}
