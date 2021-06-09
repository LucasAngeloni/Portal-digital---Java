package Logica;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class EleccionHilos {
	
	protected static final double IMPLICANCIA_FECHA = 0.35;
	protected static final double IMPLICANCIA_RELEVANCIA = 0.2;
	protected static final double IMPLICANCIA_APORTES = 0.2;
	protected static final double IMPLICANCIA_COMENTARIOS = 0.1;
	protected static int CANTIDAD_A_SELECCIONAR;
	
	protected static Double[] maximoYMinimo(ArrayList<Double> valores) {
    	
    	Double[] maximo_minimo = new Double[2];
    	maximo_minimo[0] = valores.get(0);
    	maximo_minimo[1] = valores.get(0);
    	
    	for(double valor : valores) {
    	    if(valor > maximo_minimo[0]) 
    		    maximo_minimo[0] = valor;
    	    if(valor < maximo_minimo[1]) 
    		    maximo_minimo[1] = valor;
    	}
    	    	
    	return maximo_minimo;
    }
	
	protected static ArrayList<Double> estandarizarValores(ArrayList<Double> valores_variables) {
		
    	ArrayList<Double> valores_estandarizados = new ArrayList<Double>();
    	Double valor_estandarizado;
    	Double[] maximo_minimo = maximoYMinimo(valores_variables);
    	
    	if(maximo_minimo[0] - maximo_minimo[1] == 0.0) {
    		for(int i=0; i<valores_variables.size();i++)
    			valores_estandarizados.add(0.0);
    	}
    	else {
    	    for(Double valor_variable : valores_variables) {
    		    valor_estandarizado = (valor_variable - maximo_minimo[1])/(maximo_minimo[0] - maximo_minimo[1]);
    		    valores_estandarizados.add(valor_estandarizado);
    		}
    	}
    	return valores_estandarizados;
	}
	
	protected static Integer[] rankingHilos(HashMap<Integer,Double> puntajes){
		
		if(puntajes.size() < CANTIDAD_A_SELECCIONAR)
			CANTIDAD_A_SELECCIONAR = puntajes.size();
		
		Integer[] indices = new Integer[CANTIDAD_A_SELECCIONAR];
		for(int i=0; i<CANTIDAD_A_SELECCIONAR;i++)
			indices[i] = i;
		
		for(int i=0;i<CANTIDAD_A_SELECCIONAR-1;i++) {
			int aux;
		    for(int j=i+1; j<CANTIDAD_A_SELECCIONAR ;j++) {
				if(puntajes.get(indices[i]) < puntajes.get(indices[j])){
					aux = indices[i];
					indices[i] = indices[j];
					indices[j] = aux;
				}
			}
		}
		return indices;
	}
}
