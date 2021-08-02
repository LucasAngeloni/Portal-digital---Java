package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Logica.CatalogoDeComentarios;
import Logica.CatalogoDeHilos;
import Logica.CatalogoDeHilos.NoExisteHiloException;
import Logica.CatalogoDeUsuarios;
import Modelo.Aporte;
import Modelo.Comentario;
import Modelo.Hilo;
import Modelo.Nota;
import Modelo.Publicacion;
import Modelo.Usuario;

/**
 * Servlet implementation class ControladorComentario
 */
@WebServlet("/ControladorComentario")
public class ControladorComentario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CatalogoDeComentarios cco;
	private CatalogoDeUsuarios cu;
	private CatalogoDeHilos ch;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorComentario() {
        super();
        this.cco = new CatalogoDeComentarios();
        this.cu = new CatalogoDeUsuarios();
        this.ch = new CatalogoDeHilos();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String comando = request.getParameter("instruccion");
		if(comando == null)
			comando = "inicio";
		
		switch(comando) {
		
		case "ver_comentarios":
			this.verComentarios(request,response);
			break;
		case "ver_subcomentarios":
			this.verSubcomentarios(request,response);
			break;
		case "me_gusta":
			this.meGusta(request,response);
			break;
		case "quitar_me_gusta":
			this.quitarMeGusta(request,response);
			break;
		case "comentar":
			this.comentar(request,response);
			break;
		case "responder":
			this.responder(request,response);
			break;
		case "eliminar_comentario":
			this.eliminarComentario(request,response);
			break;
		case "ver_comentarios_aporte":
			this.verComentariosAporte(request,response);
			break;
		case "comentar_aporte":
			this.comentarAporte(request, response);
			break;
		}
	}
	private void comentarAporte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String texto_comentario = request.getParameter("comentario");
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		
		LocalDateTime fecha_aporte = LocalDateTime.parse(request.getParameter("fecha_aporte"));
		String comunicador = request.getParameter("usuario_aporte");
		
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo");
		Aporte aporte = hilo.getAporte(fecha_aporte, comunicador);
		
		Comentario comentario = new Comentario(usuario, aporte);
		comentario.setDescripcionComentario(texto_comentario);
		comentario.setPublicacion(aporte);
		try {
			this.cco.insertComentario(comentario);
			this.cu.updatePreferencias(usuario, "comentar", hilo.getCategorias());
			aporte.addComentario(comentario);
		} 
		catch (SQLException e) {
			request.setAttribute("Error",e.getMessage());
		}
		finally {
			request.setAttribute("COMENTARIOS", aporte.getComentarios());
			request.setAttribute("publicacion", aporte);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void verComentariosAporte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LocalDateTime fecha_aporte = LocalDateTime.parse(request.getParameter("fecha_aporte"));
		String comunicador = request.getParameter("usuario_aporte");
		
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		Aporte aporte = hilo.getAporte(fecha_aporte, comunicador);

		request.setAttribute("COMENTARIOS", aporte.getComentarios());
		request.setAttribute("publicacion", aporte);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
		dispatcher.forward(request, response);
	}

	private void eliminarComentario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		LocalDateTime fecha_publicacion = LocalDateTime.parse(request.getParameter("fecha_publicacion"));
		
		String str = request.getParameter("fecha_comentario");
		LocalDateTime fecha_comentario = LocalDateTime.parse(str);
		System.out.println(fecha_comentario);
		Publicacion publicacion;
		String comunicador = request.getParameter("usuario_aporte");
		if(comunicador == null) {
			publicacion = hilo.getNota(fecha_publicacion);
			request.setAttribute("publicacion", (Nota)publicacion);
		}
		else {
			publicacion = hilo.getAporte(fecha_publicacion, comunicador);
			request.setAttribute("publicacion", (Aporte)publicacion);
		}	
		
		try {
			this.cco.delete(fecha_comentario, usuario.getNombreUsuario());
			publicacion.removeComentario(fecha_comentario, usuario.getNombreUsuario());
		} 
		catch (SQLException e) {
			request.setAttribute("Error",e.getMessage());
		}
		finally {
			request.setAttribute("COMENTARIOS", publicacion.getComentarios());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void responder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		Comentario comentarioPrincipal = this.buscarComentario(request, response);
		
		String texto_comentario = request.getParameter("comentario");
		Comentario comentario = new Comentario(usuario,LocalDateTime.now(),comentarioPrincipal);
		comentario.setDescripcionComentario(texto_comentario);
		try {
			this.cco.insertSubComentario(comentarioPrincipal, comentario);
			
			request.setAttribute("Exito", "El comentario fue realizado exitosamente");
			request.setAttribute("comentario", comentarioPrincipal);
			request.setAttribute("SUBCOMENTARIOS", comentarioPrincipal.getSubcomentarios());
		} 
		catch (SQLException e) {
			request.setAttribute("Error",e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaSubcomentarios.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void comentar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String texto_comentario = request.getParameter("comentario");
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		LocalDateTime fecha_publicacion = LocalDateTime.parse(request.getParameter("fecha_publicacion"));
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");	
		
		Comentario comentario;
		Publicacion publicacion;
		String comunicador = request.getParameter("usuario_aporte");
		if(comunicador == null) {
			publicacion = hilo.getNota(fecha_publicacion);
			comentario = new Comentario(usuario,(Nota)publicacion);
			request.setAttribute("publicacion", (Nota)publicacion);
		}
		else {
			publicacion = hilo.getAporte(fecha_publicacion, comunicador);
			comentario = new Comentario(usuario,(Aporte)publicacion);
			request.setAttribute("publicacion", (Aporte)publicacion);
		}
		comentario.setDescripcionComentario(texto_comentario);
		publicacion.addComentario(comentario);
		try {
			this.cco.insertComentario(comentario);
			this.cu.updatePreferencias(usuario, "comentar", hilo.getCategorias());
		} 
		catch (SQLException e) {
			request.setAttribute("Error",e.getMessage());
		}
		finally {
			request.setAttribute("COMENTARIOS", publicacion.getComentarios());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void verComentarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LocalDateTime fecha_nota = LocalDateTime.parse(request.getParameter("fecha_publicacion"));
		Hilo hilo;
		try {
			if(request.getParameter("id_hilo") == null || request.getParameter("id_hilo") == "")
				hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
			else
				hilo = this.ch.getOne(Integer.parseInt(request.getParameter("id_hilo")));

			Nota nota = hilo.getNota(fecha_nota);

			request.setAttribute("COMENTARIOS", nota.getComentarios());
			request.setAttribute("publicacion", nota);
			
			request.getSession().setAttribute("hilo_abierto", hilo);
			
		} catch (SQLException | NumberFormatException | NoExisteHiloException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
			dispatcher.forward(request, response);	
		}
	}

	private void quitarMeGusta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		try {
			Comentario comentario = this.buscarComentario(request, response);
			this.cco.deleteLike(comentario, usuario);
			request.setAttribute("comentario", comentario);
			request.setAttribute("SUBCOMENTARIOS", comentario.getSubcomentarios());

			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaSubcomentarios.jsp");
			dispatcher.forward(request, response);
		} 
		catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void meGusta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		try {
			Comentario comentario = this.buscarComentario(request, response);
			this.cco.insertLike(comentario, usuario);
			request.setAttribute("comentario", comentario);
			request.setAttribute("SUBCOMENTARIOS", comentario.getSubcomentarios());

			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaSubcomentarios.jsp");
			dispatcher.forward(request, response);
		}		
		catch (SQLException | NullPointerException e) {
			request.setAttribute("Error", e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void verSubcomentarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Comentario comentario = this.buscarComentario(request, response);
		request.setAttribute("comentario", comentario);
		request.setAttribute("SUBCOMENTARIOS", comentario.getSubcomentarios());

		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaSubcomentarios.jsp");
		dispatcher.forward(request, response);				
	}
	
	private Comentario buscarComentario(HttpServletRequest request, HttpServletResponse response) {
		LocalDateTime fecha_comentario = LocalDateTime.parse(request.getParameter("fecha_comentario"));
		String nombre_usuario = request.getParameter("usuario_comentario");
		LocalDateTime fecha_publicacion = LocalDateTime.parse(request.getParameter("fecha_publicacion"));
		
		Comentario comentario = null;
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		
		String comunicador = request.getParameter("usuario_aporte");
		if(comunicador == null) {
			Nota nota = hilo.getNota(fecha_publicacion);
			comentario = nota.getComentario(nombre_usuario, fecha_comentario);
			request.setAttribute("publicacion", nota);
		}
		else {
			Aporte aporte = hilo.getAporte(fecha_publicacion, comunicador);
			comentario = aporte.getComentario(nombre_usuario, fecha_comentario);
			request.setAttribute("publicacion", aporte);
		}
		/*if(comentario == null)
			try {
				comentario = this.cco.getOne(fecha_comentario, nombre_usuario);
			} catch (SQLException e) {
				request.setAttribute("Error", e.getMessage());
			}*/
		return comentario;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
