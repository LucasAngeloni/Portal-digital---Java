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

import Logica.CatalogoDeHilos;
import Logica.CatalogoDeHilos.NoExisteHiloException;
import Logica.CatalogoDeNotas;
import Logica.CatalogoDeNotas.LongitudMaximaException;
import Logica.CatalogoDeUsuarios;
import Modelo.Hilo;
import Modelo.Nota;
import Modelo.Usuario;

/**
 * Servlet implementation class ControladorNota
 */
@WebServlet("/ControladorNota")
public class ControladorNota extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CatalogoDeHilos ch;
	private CatalogoDeUsuarios cu;
	private CatalogoDeNotas cn;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorNota() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
	public void init() throws ServletException {
    	
        this.cu = new CatalogoDeUsuarios();
        this.ch = new CatalogoDeHilos();
        this.cn = new CatalogoDeNotas();
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
		case "relevar_nota":
			this.relevarNota(request,response);
			break;
		case "quitar_relevancia":
			this.quitarRelevancia(request,response);
			break;
		case "agregar_nota":
			this.agregarNota(request,response);
			break;
		case "vista_modificar_nota":
			this.vistaModificarNota(request,response);
			break;
		case "modificar_nota":
			this.modificarNota(request,response);
			break;
		case "eliminar_nota":
			this.eliminarNota(request,response);
			break;
		default:
			break;
		}
	}

	private void modificarNota(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LocalDateTime fecha_nota = LocalDateTime.parse(request.getParameter("fecha_publicacion"));
		int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));
		String descripcion = request.getParameter("descripcion_nota");
		
		Hilo hilo;
		try {
			hilo = this.ch.getOne(id_hilo);
			Nota notaModificada = hilo.getNota(fecha_nota);
			notaModificada.setDescripcion(descripcion);
			
			this.cn.update(notaModificada);
			request.getSession().setAttribute("hilo_abierto", hilo);
			request.setAttribute("Info", "La nota se ha modificado correctamente");
		} 
		catch (SQLException | LongitudMaximaException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void eliminarNota(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LocalDateTime fecha_nota = LocalDateTime.parse(request.getParameter("fecha_publicacion"));
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		try {
			this.cn.delete(hilo, hilo.getNota(fecha_nota));
			request.setAttribute("Info", "La nota ha sido eliminada correctamente");
		} 
		catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void vistaModificarNota(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Hilo hilo;
		LocalDateTime fecha_nota = LocalDateTime.parse(request.getParameter("fecha_publicacion"));
		try {
			if(request.getParameter("id_hilo") == null)
				hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
			else
				hilo = this.ch.getOne(Integer.parseInt(request.getParameter("id_hilo")));

			request.setAttribute("fecha_nota_modificar", fecha_nota);
			request.setAttribute("hilo_abierto", hilo);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaModificarNota.jsp");
			dispatcher.forward(request, response);
		}
		catch (SQLException | NumberFormatException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void agregarNota(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String descripcion = request.getParameter("descripcionNota");
		int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));

		Hilo hilo = null;		
		try {
			hilo = this.ch.getOne(id_hilo);
			if(descripcion == null) {
				request.setAttribute("hilo_abierto", hilo);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("/agregarNota.jsp");
				dispatcher.forward(request, response);
			}
			else {
				Nota notaNueva = new Nota(descripcion);

				hilo.addNota(notaNueva);
				ArrayList<Nota> notas = new ArrayList<Nota>();
				notas.add(notaNueva);

				this.cn.insert(hilo.getIdHilo(), notas);
				
				request.getSession().setAttribute("hilo_abierto", hilo);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
				dispatcher.forward(request, response);
			}
		} 
		catch (SQLException | LongitudMaximaException | NoExisteHiloException e) {
			request.setAttribute("hilo_abierto", hilo);
			request.setAttribute("Error", e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
			dispatcher.forward(request, response);
		}	
	}
	
	private void quitarRelevancia(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		Hilo hilo = null;
		try {
			if(request.getParameter("id_hilo") == null)
				hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
			else
				hilo = this.ch.getOne(Integer.parseInt(request.getParameter("id_hilo")));			

			Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
			Nota nota = hilo.getNota(LocalDateTime.parse(request.getParameter("fecha_publicacion")));

			this.cn.modificarRelevanciaPublicacion(hilo,nota,usuario,-1);
		}
		catch (SQLException | NumberFormatException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			request.setAttribute("hilo_abierto", hilo);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void relevarNota(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Hilo hilo = null;
		try {
			if(request.getParameter("id_hilo") == null)
				hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
			else
				hilo = this.ch.getOne(Integer.parseInt(request.getParameter("id_hilo")));			

			Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
			Nota nota = hilo.getNota(LocalDateTime.parse(request.getParameter("fecha_publicacion")));

			this.cn.modificarRelevanciaPublicacion(hilo,nota,usuario,1);
			this.cu.updatePreferencias(usuario,request.getParameter("instruccion"), hilo.getCategorias());
		}
		catch (SQLException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			request.getSession().setAttribute("hilo_abierto", hilo);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
			dispatcher.forward(request, response);
		}
	}
	
}
