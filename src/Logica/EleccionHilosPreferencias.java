package Logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import Modelo.Categoria;
import Modelo.Hilo;
import Modelo.Nota;
import Modelo.Preferencia;

public class EleccionHilosPreferencias extends EleccionHilos{
	
	private static final double IMPLICANCIA_PREFERENCIAS = 0.15;

	public static ArrayList<Hilo> seleccionHilos(ArrayList<Hilo> hilos, ArrayList<Preferencia> preferencias){
		
		CANTIDAD_A_SELECCIONAR = 30;
		HashMap<Integer,Double> puntajes;
		try{
		    puntajes = valoracionesHilos(hilos, preferencias);
		}
		catch(NullPointerException e) {
			puntajes = valoracionesHilos(hilos, new ArrayList<Preferencia>());
		}
		
		Integer[] indices = rankingHilos(puntajes);
		
		ArrayList<Hilo> hilos_seleccionados = new ArrayList<Hilo>();
		for(int i=0;i<CANTIDAD_A_SELECCIONAR;i++)
			hilos_seleccionados.add(hilos.get(indices[i]));
		
		return hilos_seleccionados;
	}
	
	private static HashMap<Integer,Double> valoracionesHilos(ArrayList<Hilo> hilos, ArrayList<Preferencia> preferencias){
		//Esta función le asigna una valoración a cada hilo para ser mostrado en la aplicación luego. Devuelve los indices del arreglo de hilos para identificar
		//donde se encuentra cada hilo seleccionado, con su respectivo puntaje
		HashMap<Integer,Double> puntajes = new HashMap<Integer,Double>();
		ArrayList<Double> tiempos = new ArrayList<Double>();
		ArrayList<Double> relevancias = new ArrayList<Double>();
		ArrayList<Double> aportes = new ArrayList<Double>();
		ArrayList<Double> comentarios = new ArrayList<Double>();
		
		ArrayList<Double> valores_preferencias = new ArrayList<Double>();
		for(Preferencia preferencia : preferencias)
			valores_preferencias.add(preferencia.getValorPreferencia());
		
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
		ArrayList<Double> preferencias_estandarizadas = estandarizarValores(valores_preferencias);
		
		Hilo hilo;
		double preferencias_hilo;
		double puntaje_hilo;
		int conteo;
		for(int i=0;i<hilos.size();i++) {
			hilo = hilos.get(i);
			preferencias_hilo = 0.0;
			conteo = 0;
			for(Preferencia preferencia : preferencias) {	
				for(Categoria categoria : hilo.getCategorias()) {
					if(categoria.getIdCategoria() == preferencia.getIdCategoria())
						preferencias_hilo += preferencias_estandarizadas.get(conteo);
				}
			    conteo++;
			}
			if(preferencias.size() != 0) 
			    preferencias_hilo = preferencias_hilo/preferencias.size();
			
			puntaje_hilo = funcionPuntuacion(tiempos_estandarizados.get(i), relevancias_estandarizadas.get(i),
					aportes_estandarizados.get(i), comentarios_estandarizados.get(i), preferencias_hilo);
			puntajes.put(i,puntaje_hilo);
			//System.out.println(hilo.getIdHilo());
			//System.out.println(puntaje_hilo);
		}
		return puntajes;
	}
	
	private static double funcionPuntuacion(double tiempo, double relevancia, double aportes, double comentarios, double promedio_preferencias) {
		
		double puntuacion;	
		
		puntuacion = IMPLICANCIA_PREFERENCIAS*promedio_preferencias + IMPLICANCIA_FECHA*(1-tiempo) +
				IMPLICANCIA_RELEVANCIA*relevancia + IMPLICANCIA_APORTES*aportes + 
				IMPLICANCIA_COMENTARIOS*comentarios;
		
		return puntuacion;		
	}
}
