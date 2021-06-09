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
		
		Double accion_categoria_elegida = 0.0;
		Double accion_categoria_no_elegida = 0.0;
		switch(accion) {
		case "abrir_hilo":
			accion_categoria_elegida = 1.0/12;
    		accion_categoria_no_elegida = -1.0/12;
			break;
		case "relevar_nota":
			accion_categoria_elegida = 1.0/6;
    		accion_categoria_no_elegida = -1.0/6;	
			break;
		case "comentar":
			accion_categoria_elegida = 1.0/6;
    		accion_categoria_no_elegida = -1.0/6;
			break;
		case "elegir_categoria":
			accion_categoria_elegida = 1.0/3;
    		accion_categoria_no_elegida = -1.0/3;
			break;
		case "aporte":
			accion_categoria_elegida = 1.0/3;
    		accion_categoria_no_elegida = -1.0/3;
			break;
		default:
			break;
		}	
		for(Categoria categoria : this.categoriasHilo) {
    		ArrayList<Preferencia> preferencias_r = this.validarModificacionPreferencia(this.preferencias,categoria.getIdCategoria());
    		accion_categoria_elegida = accion_categoria_elegida/preferencias_r.size();
    		accion_categoria_no_elegida = accion_categoria_no_elegida/(preferencias_r.size()*(preferencias_r.size()-1));
    		
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
    		if(preferencia.getValorPreferencia() > 0) {
    			preferencias_result.add(preferencia);
    		}
    	}
    	return preferencias_result;
    }
}
