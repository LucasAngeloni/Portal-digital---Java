package Modelo;

//Momentaneamente esta clase depende unicamente de la entidad Usuario y no de las categorias, por lo que se implementará como
//parte de los usuarios
public class Preferencia extends BusinessEntity{
	
	private Categoria categoria;
	public Categoria getCategoria() {
		return categoria;
	}
	
	public int getIdCategoria() {
		return categoria.getIdCategoria();
	}

	public double getValorPreferencia() {
		return valorPreferencia;
	}

	private double valorPreferencia;
	
	public Preferencia(Categoria categoria, int nro_categorias) {		
		this.categoria = categoria;
		this.valorPreferencia = 1.0/nro_categorias;
	}
	public Preferencia(Categoria categoria, double valor_preferencia) {		
		this.categoria = categoria;
		this.valorPreferencia = valor_preferencia;
	}
	
	public void modificarValorPreferencia(double variacion) {
		this.valorPreferencia = this.valorPreferencia + variacion;
	}

	public void setValorPreferencia(double valor_preferencia) {
		this.valorPreferencia = valor_preferencia;		
	}

}
