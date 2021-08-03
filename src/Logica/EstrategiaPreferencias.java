package Logica;

import java.sql.SQLException;
import java.util.ArrayList;

import Modelo.Categoria;
import Modelo.Preferencia;

public class EstrategiaPreferencias {
	
	ArrayList<Preferencia> preferencias;
	ArrayList<Categoria> categoriasHilo;
	
	public EstrategiaPreferencias(ArrayList<Preferencia> preferencias, ArrayList<Categoria> categorias_hilo) throws SQLException {
		this.preferencias = preferencias;
		this.categoriasHilo = categorias_hilo;
	}
	
	public ArrayList<Preferencia> updatePreferencias(String accion){
		
		Double valor_accion;
		switch(accion) {
		case "abrir_hilo":
			valor_accion = 1.0/12;    		
			break;
		case "relevar_nota":
			valor_accion = 1.0/6;
			break;
		case "comentar":
			valor_accion = 1.0/6;
			break;
		case "elegir_categoria":
			valor_accion = 1.0/3;
			break;
		case "aporte":
			valor_accion = 1.0/3;
			break;
		default:
			valor_accion = 0.0;
			break;
		}	
		for(Categoria categoria : this.categoriasHilo) {
    		ArrayList<Preferencia> preferencias_r = this.validarModificacionPreferencia(this.preferencias,categoria.getIdCategoria());
    		Double accion_categoria_no_elegida = -valor_accion/(preferencias_r.size()*(preferencias_r.size()-1));
    		Double accion_categoria_elegida = valor_accion/preferencias_r.size();
    		
    		for(Preferencia preferencia : this.preferencias) {
    			if(preferencias_r.contains(preferencia)) {
    				if(preferencia.getIdCategoria() == categoria.getIdCategoria())
    					preferencia.modificarValorPreferencia(accion_categoria_elegida);
    				else
    					preferencia.modificarValorPreferencia(accion_categoria_no_elegida);
    			}
    		}
    	}
		return this.preferencias;
	}
    
    private ArrayList<Preferencia> validarModificacionPreferencia(ArrayList<Preferencia> preferencias, int id_categoria) {
    	
    	ArrayList<Preferencia> preferencias_result = new ArrayList<Preferencia>();
    	for(Preferencia preferencia : preferencias) {
    		if(preferencia.getIdCategoria() == id_categoria && preferencia.getValorPreferencia() >= 0.3)
    			return new ArrayList<Preferencia>();
    		if(preferencia.getValorPreferencia() > 0)
    			preferencias_result.add(preferencia);
    	}
    	return preferencias_result;
    }
}
