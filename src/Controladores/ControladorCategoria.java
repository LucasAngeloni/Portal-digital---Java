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

import Logica.CatalogoDeCategorias;
import Logica.CatalogoDeHilos;
import Logica.CatalogoDeUsuarios;
import Modelo.Categoria;
import Modelo.Hilo;
import Modelo.Usuario;

/**
 * Servlet implementation class ControladorCategoria
 */
@WebServlet("/ControladorCategoria")
public class ControladorCategoria extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CatalogoDeCategorias cc;
	private CatalogoDeUsuarios cu;
	private CatalogoDeHilos ch;
	private ArrayList<Hilo> hilosCategoria;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorCategoria() {
        super();
        
        this.cc = new CatalogoDeCategorias();
        this.cu = new CatalogoDeUsuarios();
        this.ch = new CatalogoDeHilos();
        this.hilosCategoria = new ArrayList<Hilo>();
    }
    
    public void init() throws ServletException{
    	super.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String comando = request.getParameter("instruccion");
		if(comando == null)
			comando = "inicio";
		
		switch(comando) {
		
		case "categorias":
			this.mostrarCategorias(request, response);
			break;
		case "elegir_categoria":
			this.elegirCategoria(request,response);
			break;
		case "buscar_hilos":
			this.mostrarHilosEncontrados(request, response);
			break;
		default:
			break;
		}
	}
	
	public void mostrarCategorias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<Categoria> categorias;
		try {
			categorias = this.cc.getAll();
			request.setAttribute("CATEGORIAS", categorias);			
		} catch (SQLException e) {
			request.setAttribute("Error",e.getMessage());
		} finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaCategorias.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void elegirCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id_categoria = Integer.parseInt(request.getParameter("id"));
		
		try {
			this.hilosCategoria = this.cc.getHilosCategoria(id_categoria);
			ArrayList<Categoria> categorias = this.cc.getAll();
			
			ArrayList<Categoria> categoria_preferencia = new ArrayList<Categoria>();
			categoria_preferencia.add(new Categoria(id_categoria));
			
			try {
				this.cu.updatePreferencias((Usuario)request.getSession().getAttribute("usuario"),request.getParameter("instruccion"),categoria_preferencia);
			}
			catch(NullPointerException e) {	}
			
			request.setAttribute("CATEGORIAS", categorias);
			request.setAttribute("CATEGORIA", id_categoria);
			request.setAttribute("hilos", this.hilosCategoria);

		} catch (SQLException e) {
			request.setAttribute("Error",e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilosPorCategoria.jsp");
			dispatcher.forward(request, response);
		}
	}

	public void mostrarHilosEncontrados(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String texto = request.getParameter("texto");
		try {
			request.setAttribute("CATEGORIAS", this.cc.getAll());
			this.hilosCategoria = this.ch.filtrarHilos(this.hilosCategoria, texto);
			request.setAttribute("hilos", this.hilosCategoria);
			
		} catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilosPorCategoria.jsp");
		dispatcher.forward(request, response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
