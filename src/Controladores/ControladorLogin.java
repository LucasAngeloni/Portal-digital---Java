package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import Datos.EsUsuarioAdministradorException;
import Logica.CatalogoDeCategorias;
import Logica.CatalogoDeComunicadores;
import Logica.CatalogoDeUsuarios;
import Logica.CatalogoDeUsuarios.ExcepcionCampos;
import Modelo.Categoria;
import Modelo.Comunicador;
import Modelo.Usuario;

/**
 * Servlet implementation class ControladorLogin
 */
@WebServlet("/ControladorLogin")
@MultipartConfig
public class ControladorLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CatalogoDeUsuarios cu;
	private CatalogoDeComunicadores cc;
	private CatalogoDeCategorias ccat;
	protected static String DIRECCION_IMGS = "imgs\\usuarios\\";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorLogin() {
        super();

        this.cu = new CatalogoDeUsuarios();
        this.cc = new CatalogoDeComunicadores();
        this.ccat = new CatalogoDeCategorias();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String comando = request.getParameter("instruccion");
		if(comando == null)
			comando = "login";
		
		switch(comando) {
		
		case "login":
			this.login(request,response);
			break;
		case "inicio_sesion":
			this.iniciaSesion(request, response);
			break;
		case "cerrar_sesion":
			this.cerrarSesion(request,response);
			break;
		case "registrar_usuario":
			this.registrarUsuario(request,response);
			break;
		}
		
	}
	
	private void cerrarSesion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario)session.getAttribute("usuario");
		try {
			this.cu.cerrarSesion(usuario);
		} 
		catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}		
		finally {
			Enumeration<String> atributos = session.getAttributeNames();
			
			while(atributos.hasMoreElements()) 
				session.removeAttribute(atributos.nextElement());
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Enumeration<String> atributos = session.getAttributeNames();
		
		while(atributos.hasMoreElements()) 
			session.removeAttribute(atributos.nextElement());
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
		dispatcher.forward(request, response);
	}

	private void registrarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuarioNuevo;
		String tipo_usuario = request.getParameter("tipo_usuario");
		String nombre_usuario = request.getParameter("nombreUsuario");
		String telefono = request.getParameter("telefono");
		String email = request.getParameter("email");
		String fecha_nacimiento = request.getParameter("fecha_nacimiento");
		String nombre = null;
		String apellido = null;
		String descripcion = null;
		try {
			String clave = request.getParameter("clave");
			String repeticion_clave = request.getParameter("repeticionClave");
			Part part = request.getPart("imagen_usuario");		
			String imagen = Paths.get(part.getSubmittedFileName()).getFileName().toString();
			//String imagen = request.getParameter("imagen_usuario");
			String imagen_usuario = DIRECCION_IMGS + imagen;

			ArrayList<Categoria> categorias;
			categorias = this.ccat.getAll();

			if(tipo_usuario.equals("Comunicador")) {
				nombre = request.getParameter("nombre");
				descripcion = request.getParameter("descripcion");
				apellido = request.getParameter("apellido");

				usuarioNuevo = new Comunicador(nombre_usuario,clave,telefono,email,descripcion,nombre,apellido,categorias);
			}
			else 
				usuarioNuevo = new Usuario(nombre_usuario,clave,telefono,email,categorias);
			
			usuarioNuevo.setImagen(imagen_usuario);
			usuarioNuevo.setFechaNacimiento(fecha_nacimiento);
			usuarioNuevo.setRepeticionContraseña(repeticion_clave);

			if(tipo_usuario.equals("Comunicador"))
				this.cc.save((Comunicador)usuarioNuevo);
			else
				this.cu.save(usuarioNuevo);
			
			part.write(request.getServletContext().getRealPath(DIRECCION_IMGS) + File.separator + imagen);
			
			HttpSession session = request.getSession();
			session.setAttribute("usuario", usuarioNuevo);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("ControladorVistaPrincipal");
			dispatcher.forward(request, response);	
		}
		catch(SQLException | ExcepcionCampos | Logica.CatalogoDeComunicadores.ExcepcionCampos  e ) {
			request.setAttribute("tipo_usuario", tipo_usuario);
			request.setAttribute("nombre_usuario", nombre_usuario);
			request.setAttribute("email", email);
			request.setAttribute("telefono", telefono);
			request.setAttribute("fecha_nacimiento", fecha_nacimiento);
			if(tipo_usuario.equals("Comunicador")) {
				request.setAttribute("nombre", nombre);
				request.setAttribute("apellido", apellido);
				request.setAttribute("descripcion", descripcion);
			}
			request.setAttribute("Error", e.getMessage());
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("registrarUsuario.jsp");
			dispatcher.forward(request, response);
		}
		
	}

	private void iniciaSesion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nombre_usuario = request.getParameter("nombreUsuario");
		String clave = request.getParameter("clave");
		HttpSession session = request.getSession();
		
		Comunicador comunicador;
		try {
			comunicador = this.cc.getOne(nombre_usuario, clave);
			if(comunicador == null) {
				request.setAttribute("Error","Nombre de usuario y/o clave incorrectos");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
				dispatcher.forward(request, response);
			}
			else {				
				if(comunicador.getNombre() == null) {
					session.setAttribute("tipo_usuario", "lector");
				    session.setAttribute("usuario", comunicador.castUsuario());
				}
				else {
					session.setAttribute("tipo_usuario", "comunicador");
					session.setAttribute("usuario", comunicador);
				}			
				RequestDispatcher dispatcher = request.getRequestDispatcher("ControladorVistaPrincipal");
				dispatcher.forward(request, response);	
			}
		}
		catch (SQLException | ConnectException e) {
			request.setAttribute("Error",e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
			dispatcher.forward(request, response);
		}
		catch(EsUsuarioAdministradorException e) {
			session.setAttribute("admin", true);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipalAdministrador.jsp");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
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
