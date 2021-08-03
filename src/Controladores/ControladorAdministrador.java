package Controladores;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Logica.CatalogoDeCategorias;
import Logica.CatalogoDeComentarios;
import Logica.CatalogoDeHilos;
import Logica.CatalogoDeHilos.NoExisteHiloException;
import Logica.CatalogoDeNotas;
import Logica.CatalogoDeUsuarios;
import Modelo.Usuario;
import Modelo.BusinessEntity.States;
import Modelo.Categoria;
import Modelo.Comentario;
import Modelo.Hilo;
import Modelo.Nota;

/**
 * Servlet implementation class ControladorAdministrador
 */
@WebServlet("/ControladorAdministrador")
public class ControladorAdministrador extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CatalogoDeUsuarios cu;
	private CatalogoDeHilos ch;
	private CatalogoDeCategorias cc;
	private CatalogoDeComentarios cco;
	private CatalogoDeNotas cn;
	protected static String DIRECCION_IMGS; 
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorAdministrador() {
        super();
    }
    
    public void init() {
    	
    	this.cu = new CatalogoDeUsuarios();
    	this.ch = new CatalogoDeHilos();
    	this.cc = new CatalogoDeCategorias();
    	this.cco = new CatalogoDeComentarios();
    	this.cn = new CatalogoDeNotas();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		String comando = request.getParameter("instruccion");
		if(comando == null || session.getAttribute("admin") == null)
			comando = "cerrar_sesion";
		
		switch(comando) {
		
		case "usuarios":
			this.verUsuarios(request, response);
			break;
		case "publicaciones":
			this.verPublicaciones(request,response);
			break;
		case "categorias":
			this.verCategorias(request,response);
			break;
		case "comentarios":
			this.verComentarios(request,response);
			break;
		case "eliminar_usuario":
			this.eliminarUsuario(request,response);
			break;
		case "eliminar_categoria":
			this.eliminarCategoria(request,response);
			break;
		case "eliminar_comentario":
			this.eliminarComentario(request,response);
			break;
		case "agregar_categoria":
			this.agregarCategoria(request, response);
			break;
		case "modificar_categoria":
			this.modificarCategoria(request, response);
			break;
		case "filtrar":
			this.filtro(request, response);
			break;
		case "ordenar_comentarios":
			this.ordenarComentarios(request, response);
			break;
		case "ver_notas":
			this.verNotas(request, response);
			break;
		case "eliminar_nota":
			this.eliminarNota(request, response);
			break;
		case "eliminar_hilo":
			this.eliminarHilo(request, response);
			break;
		case "cerrar_sesion":
			this.cerrarSesion(request, response);
			break;
		default:
			break;
		}
	}
	
	private void eliminarHilo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));
		ArrayList<Hilo> hilos = (ArrayList<Hilo>) request.getSession().getAttribute("hilos");
		try {
			this.ch.deleteHilo(id_hilo);
			
			for(Hilo hilo : hilos) {
				if(hilo.getIdHilo() == id_hilo) {
					hilos.remove(hilo);
					break;
				}
			}
			request.setAttribute("Info", "Se ha borrado el hilo correctamente");
		} catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilosAdministrador.jsp");
			dispatcher.forward(request, response);
		}
		
	}

	private void eliminarNota(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));
		LocalDateTime fecha_nota = LocalDateTime.parse(request.getParameter("fecha_nota"));
		ArrayList<Hilo> hilos = (ArrayList<Hilo>) request.getSession().getAttribute("hilos");
		
		Hilo hilo_seleccionado = null;
		for(Hilo hilo : hilos) {
			if(hilo.getIdHilo() == id_hilo) {
				hilo_seleccionado = hilo;
				break;
			}
		}
		String pagina_destino = "/vistaNotasAdministrador.jsp";
		try {
			Nota nota = hilo_seleccionado.getNota(fecha_nota);
			if(hilo_seleccionado.getIdNota(nota) == 1) {
				this.ch.deleteHilo(id_hilo);
				
				for(Hilo hilo : hilos) {
					if(hilo.getIdHilo() == id_hilo) {
						hilos.remove(hilo);
						break;
					}
				}
				request.setAttribute("Info", "Se ha borrado el hilo correctamente");
				pagina_destino = "/vistaHilosAdministrador.jsp";
			}
			else {
				this.cn.delete(hilo_seleccionado, nota);
				request.setAttribute("Info", "Se ha borrado la nota correctamente");
			}
		} catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			request.setAttribute("hilo", hilo_seleccionado);
			request.setAttribute("notas", hilo_seleccionado.getNotas());
			RequestDispatcher dispatcher = request.getRequestDispatcher(pagina_destino);
			dispatcher.forward(request, response);
		}
	}

	private void verNotas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id_hilo = Integer.parseInt(request.getParameter("id_hilo"));
		try {
			Hilo hilo = this.ch.getOne(id_hilo);
			
			request.setAttribute("hilo", hilo);
			request.setAttribute("notas", hilo.getNotas());
		} catch (SQLException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaNotasAdministrador.jsp");
			dispatcher.forward(request, response);
		}
		
	}

	private void ordenarComentarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		ArrayList<Comentario> comentarios = (ArrayList<Comentario>)request.getSession().getAttribute("comentarios");
		
		String tipo_ordenamiento_anterior;
		switch(request.getParameter("atributo")) {
		
		case "fecha":
			tipo_ordenamiento_anterior = request.getParameter("tipo_ordenamiento_fecha");
			
			if(tipo_ordenamiento_anterior.equals("desc")) {
				this.cco.ordenarComentariosPorFechaMenorAMayor(comentarios);
				request.setAttribute("tipo_ordenamiento_fecha", "asc");
			}
			else {
				this.cco.ordenarComentariosPorFechaMayorAMenor(comentarios);
				request.setAttribute("tipo_ordenamiento_fecha", "desc");
			}
			break;
		case "likes":
			tipo_ordenamiento_anterior = request.getParameter("tipo_ordenamiento_likes");
			
			if(tipo_ordenamiento_anterior.equals("desc")) {
				this.cco.ordenarComentariosPorLikesMenorAMayor(comentarios);
				request.setAttribute("tipo_ordenamiento_likes", "asc");
			}
			else {
				this.cco.ordenarComentariosPorLikesMayorAMenor(comentarios);
				request.setAttribute("tipo_ordenamiento_likes", "desc");
			}			
			break;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosAdministrador.jsp");
		dispatcher.forward(request, response);
	}

	private void eliminarComentario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String usuario = request.getParameter("usuario");
		LocalDateTime fecha_comentario = LocalDateTime.parse(request.getParameter("fecha"));
		
		try {
			this.cco.delete(fecha_comentario, usuario);
			request.setAttribute("Info", "Comentario eliminado correctamente");
		} catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		} finally {
			this.verComentarios(request, response);
		}
		
	}

	private void filtro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String texto = request.getParameter("texto");
		String tipo_filtro;
		
		RequestDispatcher dispatcher = null;
		try {
			switch(request.getParameter("filtro")) {

			case "hilos":
				tipo_filtro = request.getParameter("tipo_filtro");
				request.getSession().setAttribute("hilos", this.ch.buscarHilos(tipo_filtro, texto));

				dispatcher = request.getRequestDispatcher("/vistaHilosAdministrador.jsp");
				break;
			case "usuarios":
				tipo_filtro = request.getParameter("tipo_filtro");

				request.setAttribute("usuarios", this.cu.buscarUsuarios(tipo_filtro, texto));
				dispatcher = request.getRequestDispatcher("/vistaUsuariosAdministrador.jsp");			
				break;
			case "categorias":			
				request.setAttribute("categorias", this.cc.buscarCategorias(texto));
				dispatcher = request.getRequestDispatcher("/vistaCategoriasAdministrador.jsp");
				break;
			case "comentarios":
				tipo_filtro = request.getParameter("tipo_filtro");

				request.getSession().setAttribute("comentarios", this.cco.buscarComentarios(tipo_filtro, texto));
				dispatcher = request.getRequestDispatcher("/vistaComentariosAdministrador.jsp");			
				break;
			}
		}
		catch (SQLException e1) {
			request.setAttribute("Error", e1.getMessage());
			dispatcher = request.getRequestDispatcher("/vistaPrincipalAdministrador.jsp");
		}
		finally {
			dispatcher.forward(request, response);
		}
	}

	private void modificarCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String descripcion_categoria = request.getParameter("nombre_categoria");
		String imagen = request.getParameter("imagen_categoria");
		String imagen_categoria;
		try {
			String formato = imagen.split("[.]+")[1];
			String myStorageFolder= "/imgs/categorias/"; // this is folder name in where I want to store files. 
			DIRECCION_IMGS = request.getServletContext().getRealPath(myStorageFolder);
			imagen_categoria =  DIRECCION_IMGS + descripcion_categoria + "." + formato;
		}catch(ArrayIndexOutOfBoundsException e) {
			imagen_categoria = request.getParameter("imagen_categoria_original"); 
		}
		
		int id_cat = Integer.parseInt(request.getParameter("id_categoria"));
		Categoria categoria_modificada = new Categoria(descripcion_categoria, imagen_categoria);
		categoria_modificada.setIdCategoria(id_cat);
		
		String respuesta = null;
		try {
			this.cc.update(categoria_modificada);
			copyFile(imagen,imagen_categoria);
			
			request.setAttribute("categorias", this.cc.getAll());
			request.setAttribute("Info", "La categoría se modificó correctamente");
		} catch (SQLException e) {
			respuesta = e.getMessage();
		} catch (IOException e) {
			respuesta = "La categoría se agregó pero hubo un problema con la imagen";
		}
		finally {
			request.setAttribute("Error", respuesta);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaCategoriasAdministrador.jsp");
			dispatcher.forward(request, response);
		}
		
	}

	private void agregarCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String descripcion_categoria = request.getParameter("nombre_categoria");
		String imagen = request.getParameter("imagen_categoria");			
		String formato = imagen.split("[.]+")[1];	
		String myStorageFolder= "/imgs/categorias/"; // this is folder name in where I want to store files. 
		DIRECCION_IMGS = request.getServletContext().getRealPath(myStorageFolder); 
		String imagen_categoria =  DIRECCION_IMGS + descripcion_categoria + "." + formato;
		
		Categoria categoria_nueva = new Categoria(descripcion_categoria, imagen_categoria);
		String respuesta = null;
		try {
			this.cc.insert(categoria_nueva);
			copyFile(imagen,imagen_categoria);
			request.setAttribute("Info", "La categoría se agregó correctamente");
		} catch (SQLException e) {
			respuesta = e.getMessage();
		} catch (IOException e) {
			respuesta = "La categoría se agregó pero hubo un problema con la imagen";
		}
		finally {
			request.setAttribute("Error", respuesta);
			this.verCategorias(request, response);
		}
	}

	private void eliminarCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id_categoria = Integer.parseInt(request.getParameter("id_categoria"));
		try {
			this.cc.delete(id_categoria);
			request.setAttribute("Info", "La categoria fue eliminada exitosamente");
		} 
		catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			this.verCategorias(request, response);
		}
		
	}

	private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = new Usuario(request.getParameter("nombre_usuario"));
		usuario.setState(States.DELETED);
		try {
			this.cu.save(usuario);		
			request.setAttribute("Info", "El usuario fue eliminado exitosamente");
		} 
		catch (SQLException | Logica.CatalogoDeUsuarios.ExcepcionCampos e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			this.verUsuarios(request, response);
		}		
		
	}

	private void verComentarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.getSession().setAttribute("comentarios", this.cco.getAll());
		} catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosAdministrador.jsp");
			dispatcher.forward(request, response);
		}
		
	}

	private void verCategorias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.setAttribute("categorias", this.cc.getAll());
		} catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		} finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaCategoriasAdministrador.jsp");
			dispatcher.forward(request, response);
		}
		
	}

	private void verPublicaciones(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.getSession().setAttribute("hilos", this.ch.getHilosMasRecientes(LocalDateTime.now()));
		} catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		} finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaHilosAdministrador.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void verUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.setAttribute("usuarios", this.cu.getAll());
		} catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		} finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaUsuariosAdministrador.jsp");
			dispatcher.forward(request, response);
		}	
	}
	
	private void cerrarSesion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Enumeration<String> atributos = session.getAttributeNames();

		while(atributos.hasMoreElements()) 
			session.removeAttribute(atributos.nextElement());

		RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
		dispatcher.forward(request, response);
	}

	public static void copyFile(String origen, String destino) throws IOException {
		
		Path from = Paths.get(origen);
		Path to = Paths.get(destino);
		
		CopyOption[] options = new CopyOption[] {
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES
		};
		
		Files.copy(from, to, options);
	}
}
