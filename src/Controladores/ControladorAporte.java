package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Logica.CatalogoDeAportes;
import Logica.CatalogoDeAportes.LongitudMaximaException;
import Logica.CatalogoDeHilos;
import Logica.CatalogoDeUsuarios;
import Modelo.Aporte;
import Modelo.Comunicador;
import Modelo.Hilo;
import Modelo.Usuario;

/**
 * Servlet implementation class ControladorAporte
 */
@WebServlet("/ControladorAporte")
public class ControladorAporte extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	private CatalogoDeHilos ch;
	private CatalogoDeAportes ca;
	private CatalogoDeUsuarios cu;
	private ArrayList<Aporte> aportes;
	
    public ControladorAporte() {
        super();
    }
    
    @Override
	public void init() throws ServletException {
    	
        this.ch = new CatalogoDeHilos();
        this.ca = new CatalogoDeAportes();
        this.cu = new CatalogoDeUsuarios();
        this.aportes = new ArrayList<Aporte>();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String comando = request.getParameter("instruccion");
		if(comando == null)
			comando = "abrir_hilo";
		
		switch(comando) {
		case "ver_aportes":
			this.verAportes(request,response);
			break;
		case "relevar_aporte":
			this.relevarAporte(request,response);
			break;
		case "quitar_relevancia_aporte":
			this.quitarRelevanciaAporte(request,response);
			break;
		case "agregar_aporte":
			this.agregarAporte(request,response);
			break;
		case "eliminar_aporte":
			this.eliminarAporte(request,response);
			break;
		case "modificar_aporte":
			this.modificarAporte(request,response);
			break;
		case "vista_modificar_aporte":
			this.vistaModificarAporte(request,response);
			break;
		case "buscar_aporte":
			this.buscarAporte(request,response);
			break;
		default:
			break;
		}
	}
	
	private void buscarAporte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String texto = request.getParameter("texto");
		
		this.aportes = this.ca.filtrarAportes(this.aportes, texto);
		request.setAttribute("aportes", this.aportes);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaAportesNoModal.jsp");
		dispatcher.forward(request, response);	
		
	}

	private void quitarRelevanciaAporte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		
		LocalDateTime fecha_aporte = LocalDateTime.parse(request.getParameter("fecha_aporte"));
		String comunicador_aporte = request.getParameter("comunicador_aporte");
		try {
			Aporte aporte = hilo.getAporte(fecha_aporte, comunicador_aporte);
			this.ca.removeRelevancia(aporte, usuario);
		} 
		catch (SQLException e) {
			request.setAttribute("Error", "Hubo un ERROR, no se ejecutó la acción");
		}
		finally {
			request.setAttribute("hilo", hilo);
			request.setAttribute("aportes", hilo.getAportes());
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaAportesNoModal.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void relevarAporte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		
		LocalDateTime fecha_aporte = LocalDateTime.parse(request.getParameter("fecha_aporte"));
		String comunicador_aporte = request.getParameter("comunicador_aporte");
		
		try {
			Aporte aporte = hilo.getAporte(fecha_aporte, comunicador_aporte);
			this.ca.insertRelevancia(aporte, usuario);
		} 
		catch (SQLException e) {
			request.setAttribute("Error", "Hubo un ERROR, no se ejecutó la acción");
		}
		finally {
			request.setAttribute("hilo", hilo);
			request.setAttribute("aportes", hilo.getAportes());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaAportesNoModal.jsp");
			dispatcher.forward(request, response);
		}
		
	}
	
	private void vistaModificarAporte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Hilo hilo;
		LocalDateTime fecha = LocalDateTime.parse(request.getParameter("fecha_aporte"));

		hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");

		Aporte aporte = hilo.getAporte(fecha, (Comunicador)request.getSession().getAttribute("usuario"));
		request.setAttribute("aporte", aporte);
		request.setAttribute("hilo", hilo);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaModificarAporte.jsp");
		dispatcher.forward(request, response);
		
	}
	private void modificarAporte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LocalDateTime fecha_aporte = LocalDateTime.parse(request.getParameter("fecha_aporte"));
		String descripcion = request.getParameter("descripcion_aporte");
		
		Hilo hilo;
		try {
			hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
			Aporte aporteModificado = hilo.getAporte(fecha_aporte, (Comunicador)request.getSession().getAttribute("usuario"));
			aporteModificado.setDescripcion(descripcion);
			
			this.ca.modificarAporte(aporteModificado);
			
			request.setAttribute("hilo", hilo);
			request.setAttribute("aportes", hilo.getAportes());
			request.setAttribute("Info", "Aporte modificado correctamente");
		} 
		catch (SQLException | LongitudMaximaException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaAportesNoModal.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void eliminarAporte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Comunicador comunicador = (Comunicador)request.getSession().getAttribute("usuario");		
		String fecha = request.getParameter("fecha_aporte");
		LocalDateTime fecha_aporte = LocalDateTime.parse(fecha);
		try {
			Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
			
			Aporte aporteEliminado = hilo.getAporte(fecha_aporte, comunicador);
			aporteEliminado.setIdHilo(hilo.getIdHilo());
			
			this.ca.borrarAporte(aporteEliminado,hilo);			
			
			request.setAttribute("hilo", hilo);
			request.setAttribute("aportes", hilo.getAportes());
			request.setAttribute("Info", "Aporte eliminado correctamente");
		} 
		catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaAportesNoModal.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void verAportes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));
		Hilo hilo;
		try {
			hilo = this.ch.getOne(id_hilo);
			this.aportes = hilo.getAportes();
			
			request.getSession().setAttribute("hilo_abierto", hilo);
			request.setAttribute("aportes", this.aportes);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaAportesNoModal.jsp");
			dispatcher.forward(request, response);
		}
		catch (SQLException e) {
			request.setAttribute("Error", "ERROR al intentar cargar los aportes");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void agregarAporte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String descripcion = request.getParameter("descripcion");				
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		try {
			Comunicador comunicador = (Comunicador)request.getSession().getAttribute("usuario");
			
			Aporte aporteNuevo = new Aporte(hilo.getIdHilo(),descripcion,comunicador);
			this.ca.agregarAporte(aporteNuevo,hilo);
			this.cu.updatePreferencias(comunicador, "aporte", hilo.getCategorias());
			request.setAttribute("Info", "Se ha realizado el aporte correctamente");
		} 
		catch (LongitudMaximaException | SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			request.setAttribute("hilo", hilo);
			request.setAttribute("aportes", hilo.getAportes());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaAportesNoModal.jsp");
			dispatcher.forward(request, response);
		}
		
	}

}
