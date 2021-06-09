package Controladores;

import java.io.File;
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

import Logica.CatalogoDeAportes;
import Logica.CatalogoDeComunicadores;
import Logica.CatalogoDeUsuarios;
import Logica.CatalogoDeUsuarios.ExcepcionCampos;
import Modelo.BusinessEntity.States;
import Modelo.Comunicador;
import Modelo.Nota;
import Modelo.Usuario;

/**
 * Servlet implementation class ControladorUsuario
 */
@WebServlet("/ControladorUsuario")
public class ControladorUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private CatalogoDeUsuarios cu;
	private CatalogoDeComunicadores cc;
	private CatalogoDeAportes ca;
	
    public ControladorUsuario() {
        super();
        
        cu = new CatalogoDeUsuarios();
        cc = new CatalogoDeComunicadores();
        ca = new CatalogoDeAportes();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String comando = request.getParameter("instruccion");
		if(comando == null)
			comando = "inicio";
		
		switch(comando) {
		
		case "perfil":
			this.perfil(request,response);
			break;
		case "cambiar_clave":
			this.cambiarClave(request,response);
			break;
		case "cambiar_descripcion":
			this.cambiarDescripcion(request,response);
			break;
		case "cambiar_imagen":
			this.cambiarImagen(request,response);
			break;
		case "editar_perfil":
			this.editarPerfil(request,response);
			break;
		case "ver_aportes":
			this.verAportes(request,response);
			break;
		case "ver_relevancias":
			this.verRelevancias(request,response);
			break;
		case "transformar_a_comunicador":
			this.transformarAComunicador(request,response);
			break;
		case "eliminar_usuario":
			this.eliminarUsuario(request,response);
			break;
		default:
			break;
		}
	}

	private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		usuario.setState(States.DELETED);
		
		String respuesta = "El usuario fue eliminado exitosamente";
		try {
			this.cu.save(usuario);		
		} 
		catch (SQLException | ExcepcionCampos e) {
			respuesta = e.getMessage();
		}
		finally {
			request.setAttribute("Error", respuesta);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
			dispatcher.forward(request, response);
		}		
	}

	private void cambiarDescripcion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String descripcion = request.getParameter("descripcion");
		Comunicador comunicador = (Comunicador)request.getSession().getAttribute("usuario");
		try {
			comunicador.setDescripcion(descripcion);
			comunicador.setState(States.MODIFIED);
			this.cc.save(comunicador);
			request.setAttribute("Info", "Descripcion modificada");
		} 
		catch (SQLException | Logica.CatalogoDeComunicadores.ExcepcionCampos e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/perfil.jsp");
			dispatcher.forward(request, response);
		}				
	}
	
	private void cambiarImagen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		
		String imagen = request.getParameter("imagen_usuario");
		String formato = imagen.split("[.]+")[1]; 
		String DIRECCION_IMGS = new File (".").getAbsolutePath()+"\\PortalDigital\\WebContent\\imgs\\usuarios\\";
		
		String imagen_usuario =  DIRECCION_IMGS + usuario.getNombreUsuario() + "." + formato;
		
		System.out.println(imagen_usuario);
		
		String imagen_anterior = usuario.getImagen();
		usuario.setImagen(imagen_usuario);
		usuario.setState(States.MODIFIED);
		try {
			this.cu.save(usuario);
			ControladorLogin.copyFile(imagen,imagen_usuario);
			request.setAttribute("Info","Imagen modificada");
		} 
		catch (SQLException | ExcepcionCampos e) {
			usuario.setImagen(imagen_anterior);
			request.setAttribute("Error",e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/perfil.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void editarPerfil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String telefono = request.getParameter("telefono");
		String fecha_nacimiento = request.getParameter("fecha_nacimiento");
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		Usuario usuarioModificado = new Usuario(usuario.getNombreUsuario());
		
		usuarioModificado.setContraseña(usuario.getContraseña());
		usuarioModificado.setComentariosQueLeGustan(usuario.getComentariosQueLeGustan());
		usuarioModificado.setHilosGuardados(usuario.getHilosGuardados());
		usuarioModificado.setImagen(usuario.getImagen());
		usuarioModificado.setNotasRelevantes(usuario.getNotasRelevantes());
		usuarioModificado.setPreferencias(usuario.getPreferencias());
		
		usuarioModificado.setEmail(email);
		usuarioModificado.setTelefono(telefono);
		usuarioModificado.setFechaNacimiento(fecha_nacimiento);
		usuarioModificado.setState(States.MODIFIED);
		try {
			this.cu.save(usuarioModificado);
			request.getSession().setAttribute("usuario",usuarioModificado);
			request.setAttribute("Info","Datos modificados");
		}
		catch (SQLException | ExcepcionCampos e) {
			request.setAttribute("Error",e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/perfil.jsp");
			dispatcher.forward(request, response);
		}
		
	}

	private void transformarAComunicador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nombre = request.getParameter("nombre");
		String apellido = request.getParameter("apellido");
		String descripcion = request.getParameter("descripcion");
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		try {
			this.cu.lectorAComunicador(usuario.getNombreUsuario(), nombre, apellido, descripcion);
			request.getSession().setAttribute("usuario", this.cc.getOne(usuario.getNombreUsuario()));
			request.setAttribute("Info","EXITO. Ahora eres un comunicador");
		}
		catch (SQLException e) {
			request.setAttribute("Error",e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaPrincipal.jsp");
			dispatcher.forward(request, response);	
		}
	}

	private void verRelevancias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Comunicador com = (Comunicador)request.getSession().getAttribute("COMUNICADOR");
		ArrayList<Nota> notasRelevantes = com.getNotasRelevantes();
		request.setAttribute("NOTAS", notasRelevantes);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/perfilComunicador.jsp");
		dispatcher.forward(request, response);
	}

	private void verAportes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Comunicador com = (Comunicador)request.getSession().getAttribute("COMUNICADOR");
		try {
			request.setAttribute("APORTES", this.ca.getAportesComunicador(com));
		}
		catch (SQLException e) {
			request.setAttribute("Error",e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/perfilComunicador.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void cambiarClave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String claveActual = request.getParameter("claveActual");
		String claveNueva = request.getParameter("claveNueva");
		String usuario = request.getParameter("usuario");
		try {
			this.cu.cambiarClave(this.cu.getOne(usuario),claveActual,claveNueva);
			request.setAttribute("Info", "Clave modificada correctamente");
		} 
		catch (SQLException | ExcepcionCampos e) {
			request.setAttribute("Error", e.getMessage());
		}		
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/perfil.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void perfil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario)session.getAttribute("usuario");
		
		String usuarioPerfil = request.getParameter("usu");
		
		if(usuario != null && usuario.getNombreUsuario().equals(usuarioPerfil)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/perfil.jsp");
			dispatcher.forward(request, response);	
		}
		else {
			Comunicador com;
			try {
				com = this.cc.getOne(usuarioPerfil);
				session.setAttribute("COMUNICADOR", com);
				request.setAttribute("HILOS", com.getHilos());
			}
			catch (SQLException e) {
				request.setAttribute("Error", e.getMessage());
			}
			finally {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/perfilComunicador.jsp");
				dispatcher.forward(request, response);
			}
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
