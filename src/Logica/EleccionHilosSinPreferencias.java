package Logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import Modelo.Hilo;
import Modelo.Nota;

public class EleccionHilosSinPreferencias extends EleccionHilos {
	
	public static ArrayList<Hilo> seleccionHilos(ArrayList<Hilo> hilos){
		
		CANTIDAD_A_SELECCIONAR = 30;
		try {
		    HashMap<Integer,Double> puntajes = valoracionesHilos(hilos);
		
		    Integer[] indices = rankingHilos(puntajes);
		
		    ArrayList<Hilo> hilos_seleccionados = new ArrayList<Hilo>();
		    for(int i=0;i<CANTIDAD_A_SELECCIONAR;i++)
			    hilos_seleccionados.add(hilos.get(indices[i]));
		
		    return hilos_seleccionados;
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private static HashMap<Integer,Double> valoracionesHilos(ArrayList<Hilo> hilos){
		//Esta función le asigna una valoración a cada hilo para ser mostrado en la aplicación luego. Devuelve los indices del arreglo de hilos para identificar
		//donde se encuentra cada hilo seleccionado, con su respectivo puntaje
		HashMap<Integer,Double> puntajes = new HashMap<Integer,Double>();
		ArrayList<Double> tiempos = new ArrayList<Double>();
		ArrayList<Double> relevancias = new ArrayList<Double>();
		ArrayList<Double> aportes = new ArrayList<Double>();
		ArrayList<Double> comentarios = new ArrayList<Double>();
		
		LocalDateTime fechaActual = LocalDateTime.now();
		for(Hilo hilo : hilos) {
			relevancias.add((double) hilo.getRelevanciaHilo());
			aportes.add((double)hilo.getNroAportes());
			
			double nro_comentarios = 0;
			for(Nota nota : hilo.getNotas())
			     nro_comentarios += nota.getCantComentarios();
			comentarios.add(nro_comentarios);
			
			tiempos.add((double)hilo.getTiempo(fechaActual)/(24*60));
		}
		
		ArrayList<Double> tiempos_estandarizados = estandarizarValores(tiempos);
		ArrayList<Double> relevancias_estandarizadas = estandarizarValores(relevancias);
		ArrayList<Double> aportes_estandarizados = estandarizarValores(aportes);
		ArrayList<Double> comentarios_estandarizados = estandarizarValores(comentarios);
		
		double puntaje_hilo;
		for(int i=0;i<hilos.size();i++) {			
			puntaje_hilo = funcionPuntuacion(tiempos_estandarizados.get(i), relevancias_estandarizadas.get(i),
					aportes_estandarizados.get(i), comentarios_estandarizados.get(i));
			puntajes.put(i,puntaje_hilo);
			
			//System.out.println(i);
			//System.out.println(puntaje_hilo);
		}
		return puntajes;
	}
	
	private static double funcionPuntuacion(double tiempo, double relevancia, double aportes, double comentarios) {
		
		double puntuacion;	
		
		puntuacion = IMPLICANCIA_FECHA*(1-tiempo) +	IMPLICANCIA_RELEVANCIA*relevancia + IMPLICANCIA_APORTES*aportes + 
				IMPLICANCIA_COMENTARIOS*comentarios;
		
		return puntuacion;		
	}
}
