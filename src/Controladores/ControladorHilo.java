package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Logica.CatalogoDeHilos;
import Logica.CatalogoDeHilos.LongitudMaximaException;
import Logica.CatalogoDeHilos.NoExisteHiloException;
import Logica.CatalogoDeUsuarios;
import Modelo.Categoria;
import Modelo.Comunicador;
import Modelo.Hilo;
import Modelo.Nota;
import Modelo.Usuario;

/**
 * Servlet implementation class ControladorHilo
 */
@WebServlet("/ControladorHilo")
public class ControladorHilo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CatalogoDeHilos ch;
	private CatalogoDeUsuarios cu;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorHilo() {
        super();
    }
    @Override
	public void init() throws ServletException {
    	
        this.cu = new CatalogoDeUsuarios();
        this.ch = new CatalogoDeHilos();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String comando = request.getParameter("instruccion");
		if(comando == null)
			comando = "abrir_hilo";
		
		switch(comando) {
		
		case "abrir_hilo":
			this.abrirHilo(request, response);
			break;
		case "guardar_hilo":
			this.guardarHilo(request,response);
			break;
		case "desguardar_hilo":
			this.desguardarHilo(request,response);
			break;
		case "publicar":
			this.publicar(request,response);
			break;
		case "eliminar_hilo":
			this.eliminarHilo(request,response);
			break;
		case "modificar_titulo":
			this.modificarTitulo(request,response);
		default:
			break;
		}
	}
	
	private void modificarTitulo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String titulo = request.getParameter("titulo");
		int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));
		
		try {
			Hilo hilo_a_editar = this.ch.getOne(id_hilo);
			hilo_a_editar.setTitulo(titulo);
			
			this.ch.update(hilo_a_editar);
			request.getSession().setAttribute("hilo_abierto", hilo_a_editar);
			
		} catch (SQLException | LongitudMaximaException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
			dispatcher.forward(request, response);
		}
		
		
	}
	private void eliminarHilo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			Hilo hilo = this.ch.getOne(Integer.parseInt(request.getParameter("id_hilo")));
			this.ch.deleteHilo(hilo);
			
			request.setAttribute("Info", "El hilo se ha eliminado correctamente");
		} 
		catch (NumberFormatException | SQLException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
		} finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("ControladorVistaPrincipal");
			dispatcher.forward(request, response);
		}
	}	
	private void publicar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String titulo = request.getParameter("titulo");
		String descripcion = request.getParameter("descripcion");
		String categoria = request.getParameter("categoria");
		Comunicador comunicador = (Comunicador)request.getSession().getAttribute("usuario");
				
        Hilo hiloNuevo = new Hilo(titulo,comunicador);
		hiloNuevo.addNota(new Nota(descripcion));
		
		@SuppressWarnings("unchecked")
		ArrayList<Categoria> categorias = (ArrayList<Categoria>) request.getSession().getAttribute("Categorias");
		for (Categoria cat : categorias){
			if(cat.getDescripcionCategoria().equals(categoria)) {
				hiloNuevo.addCategoria(cat);
				break;
			}
		}
		try {
			this.ch.crearHilo(hiloNuevo);
			request.getSession().setAttribute("hilo_abierto", hiloNuevo);
			request.setAttribute("Info", "ÉXITO. Se ha subido el hilo correctamente");
		} 
		catch (SQLException | LongitudMaximaException | Logica.CatalogoDeNotas.LongitudMaximaException e) {
			request.setAttribute("Info", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void abrirHilo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));
		HttpSession session = request.getSession();
		Hilo hilo;
		try {
			hilo = this.ch.getOne(id_hilo);
			try {
				this.cu.updatePreferencias((Usuario)session.getAttribute("usuario"),request.getParameter("instruccion"), hilo.getCategorias());
			}
			catch(NullPointerException e) {	}
			session.setAttribute("hilo_abierto", hilo);
		}
		catch (SQLException | NoExisteHiloException e) {
			session.removeAttribute("hilo_abierto");
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilo.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void desguardarHilo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));
			Hilo hilo = this.ch.getOne(id_hilo);
			Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
			this.cu.desguardarHilo(hilo, usuario);
			
			request.setAttribute("DATOSHILOS", usuario.getHilosGuardados());
			request.setAttribute("Info", "Se ha quitado el hilo");
		} 
		catch (SQLException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void guardarHilo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));
		try {
			Hilo hilo = this.ch.getOne(id_hilo);
			Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
			this.cu.guardarHilo(hilo,usuario);
			
			request.setAttribute("DATOSHILOS", usuario.getHilosGuardados());
			request.setAttribute("Info", "Se ha guardado el hilo");
		}
		catch (SQLException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}