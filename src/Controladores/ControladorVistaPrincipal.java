package Controladores;

import java.io.IOException;
import java.sql.SQLException;
/*import java.net.InetAddress;
import java.net.UnknownHostException;*/
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import Datos.FactoryConnection;
import Logica.*;
import Modelo.Categoria;
import Modelo.Hilo;
import Modelo.Usuario;
import Modelo.Comunicador;

/**
 * Servlet implementation class ControladorVistaPrincipal
 */
@WebServlet("/ControladorVistaPrincipal")
public class ControladorVistaPrincipal extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CatalogoDeHilos ch;
	private CatalogoDeCategorias cc;
	private ArrayList<Hilo> hilos;
	private int indiceHilos;
       

	
    @Override
	public void init() throws ServletException {
		super.init();
		this.ch = new CatalogoDeHilos();
		this.cc = new CatalogoDeCategorias();
	}

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorVistaPrincipal() {
        super();        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String comando = request.getParameter("instruccion");
		if(comando == null)
			comando = "inicio";
		
		switch(comando) {
		
		case "inicio":
			this.mostrarInicio(request, response);
			break;
		case "hilos_guardados":
			this.mostrarHilosGuardados(request, response);
			break;
		case "mis_publicaciones":
			this.getMisPublicaciones(request, response);
			break;
		case "buscador":
			this.mostrarHilosEncontrados(request, response);
			break;
		case "cargar_mas_hilos":
			this.cargarHilos(request,response);
			break;
		default:
			this.mostrarInicio(request, response);
		}		
		
	}

	private void getMisPublicaciones(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.hilos = ((Comunicador)request.getSession().getAttribute("usuario")).getHilos();
		request.setAttribute("hilos", this.hilos);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
		dispatcher.forward(request, response);
	}

	private void cargarHilos(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		ArrayList<Hilo> hilosInicio = new ArrayList<Hilo>();
		
		int limite = this.indiceHilos + 4;
		int i = 0;
		
		while(this.hilos!=null && i < limite && i < this.hilos.size() ) {
			hilosInicio.add(this.hilos.get(i));
			i += 1;
			this.indiceHilos += 1;
		}
		request.setAttribute("hilos", hilosInicio);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public void mostrarInicio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		try {
			this.hilos = this.ch.getHilosInicio(usuario);
			ArrayList<Hilo> hilosInicio = new ArrayList<Hilo>();
			
			this.indiceHilos = 0;
			while(this.indiceHilos < 4 && this.indiceHilos < this.hilos.size() ) {
				hilosInicio.add(this.hilos.get(this.indiceHilos));
				this.indiceHilos += 1;
			}
			
			ArrayList<Categoria> categorias = this.cc.getAll();
			request.getSession().setAttribute("Categorias", categorias);			
			request.setAttribute("hilos", hilosInicio);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
			dispatcher.forward(request, response);	
		} 
		catch (SQLException e) {
			request.setAttribute("Error",e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
			dispatcher.forward(request, response);
		}
		catch(NullPointerException e) {
			request.setAttribute("Info","No hay noticias publicadas");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	public void mostrarHilosGuardados(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		
		this.hilos = usuario.getHilosGuardados();
		request.setAttribute("hilos", this.hilos);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
		dispatcher.forward(request, response);	
	}
	
	public void mostrarHilosEncontrados(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String texto = request.getParameter("texto");
		
		this.hilos = this.ch.filtrarHilos(this.hilos, texto);
		request.setAttribute("hilos", this.hilos);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
		dispatcher.forward(request, response);	
	}
}
