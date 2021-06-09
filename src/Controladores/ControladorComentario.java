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
			request.setAttribute("aporte", aporte);
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
		request.setAttribute("aporte", aporte);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
		dispatcher.forward(request, response);
	}

	private void eliminarComentario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		
		String str = request.getParameter("fecha_comentario");
		LocalDateTime fecha = LocalDateTime.parse(str);
		
		Publicacion publicacion;
		try {
			int nro_nota = Integer.parseInt(request.getParameter("nro_nota"));
			publicacion = hilo.getNota(nro_nota);
			
			request.setAttribute("nro_nota", nro_nota);
			request.setAttribute("nota", (Nota)publicacion);
		}
		catch(NumberFormatException e) {
			LocalDateTime fecha_aporte = LocalDateTime.parse(request.getParameter("fecha_aporte"));
			String comunicador = request.getParameter("usuario_aporte");	
			publicacion = hilo.getAporte(fecha_aporte, comunicador);
			
			request.setAttribute("aporte", (Aporte)publicacion);
		}	
		try {
			this.cco.delete(fecha, usuario.getNombreUsuario());
			publicacion.removeComentario(fecha, usuario.getNombreUsuario());
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
		
		String nombre_usuario = request.getParameter("usuario_comentario");
        String str = request.getParameter("fecha_comentario");		
		
		LocalDateTime fecha_comentario = LocalDateTime.parse(str);
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		
		Comentario comentarioPrincipal;
		try {
			int nro_nota = Integer.parseInt(request.getParameter("nro_nota"));
			comentarioPrincipal = hilo.buscarComentario(nro_nota, nombre_usuario, fecha_comentario);
			request.setAttribute("nro_nota", nro_nota);
		}
		catch(NumberFormatException e) {
			LocalDateTime fecha_aporte = LocalDateTime.parse(request.getParameter("fecha_aporte"));
			String comunicador = request.getParameter("usuario_aporte");
			
			Aporte aporte = hilo.getAporte(fecha_aporte, comunicador);
			request.setAttribute("aporte", aporte);
			comentarioPrincipal = aporte.getComentario(nombre_usuario, fecha_comentario);
		}
		if(comentarioPrincipal == null)
			//El null sucede porque al buscar los comentarios unicamente encuentra los principales y no las respuestas a comentarios
			try {
				comentarioPrincipal = this.cco.getOne(fecha_comentario, nombre_usuario);
			} catch (SQLException e1) {
				request.setAttribute("Error",e1.getMessage());
			}
		
		String texto_comentario = request.getParameter("comentario");
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		Comentario comentario = new Comentario(usuario,LocalDateTime.now(),comentarioPrincipal);
		comentario.setDescripcionComentario(texto_comentario);
		try {
			this.cco.insertSubComentario(comentario);
			
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
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");	
		
		Comentario comentario;
		Publicacion publicacion;
		try {
			int nro_nota = Integer.parseInt(request.getParameter("nro_nota"));
			publicacion = hilo.getNota(nro_nota);
			comentario = new Comentario(usuario,(Nota)publicacion);
			
			publicacion.addComentario(comentario);
			request.setAttribute("nota", (Nota)publicacion);
			request.setAttribute("nro_nota", nro_nota);
		}
		catch(NumberFormatException e) {
			LocalDateTime fecha_aporte = LocalDateTime.parse(request.getParameter("fecha_aporte"));
			String comunicador = request.getParameter("usuario_aporte");
			
			publicacion = hilo.getAporte(fecha_aporte, comunicador);
			comentario = new Comentario(usuario,(Aporte)publicacion);
			
			publicacion.addComentario(comentario);
			request.setAttribute("aporte", (Aporte)publicacion);
		}
		comentario.setDescripcionComentario(texto_comentario);
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
		
		int nro_nota = Integer.parseInt(request.getParameter("nro_nota"));
		Hilo hilo;
		try {
			if(request.getParameter("id_hilo") == null || request.getParameter("id_hilo") == "")
				hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
			else
				hilo = this.ch.getOne(Integer.parseInt(request.getParameter("id_hilo")));

			Nota nota = hilo.getNota(nro_nota);

			request.setAttribute("COMENTARIOS", nota.getComentarios());
			request.setAttribute("nota", nota);
			request.setAttribute("nro_nota", nro_nota);
			
			request.getSession().setAttribute("hilo_abierto", hilo);
			
		} catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
		}
		finally {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
			dispatcher.forward(request, response);	
		}
	}

	private void quitarMeGusta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		String usuario_comentador = request.getParameter("usuario_comentario");
		LocalDateTime fecha = LocalDateTime.parse(request.getParameter("fecha"));
		try {
			this.cco.deleteLike(fecha,usuario_comentador, usuario);
			this.verSubcomentarios(request, response);
		} 
		catch (SQLException e) {
			request.setAttribute("Error", e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void meGusta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		String usuario_comentador = request.getParameter("usuario_comentario");
		LocalDateTime fecha_comentario = LocalDateTime.parse(request.getParameter("fecha"));
		try {
			this.cco.insertLike(fecha_comentario,usuario_comentador, usuario);
			this.verSubcomentarios(request, response);
		}		
		catch (SQLException | NullPointerException e) {
			request.setAttribute("Error", e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaComentariosNota.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void verSubcomentarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LocalDateTime fecha = LocalDateTime.parse(request.getParameter("fecha"));
		String nombre_usuario = request.getParameter("usuario_comentario");
		
		Comentario comentario = null;
		Hilo hilo = (Hilo)request.getSession().getAttribute("hilo_abierto");
		try {
			int nro_nota = Integer.parseInt(request.getParameter("nro_nota"));
			request.setAttribute("nro_nota", nro_nota);
			comentario = hilo.buscarComentario(nro_nota, nombre_usuario, fecha);
		}
		catch(NumberFormatException e) {
			LocalDateTime fecha_aporte = LocalDateTime.parse(request.getParameter("fecha_aporte"));
			String comunicador = request.getParameter("usuario_aporte");
			
			Aporte aporte = hilo.getAporte(fecha_aporte, comunicador);
			request.setAttribute("aporte", aporte);
			comentario = hilo.buscarComentario(fecha_aporte, comunicador, nombre_usuario, fecha);
		}
		finally {
			if(comentario == null)
				try {
					comentario = this.cco.getOne(fecha, nombre_usuario);
				} catch (SQLException e) {
					request.setAttribute("Error", e.getMessage());
				}
			request.setAttribute("comentario", comentario);
			request.setAttribute("SUBCOMENTARIOS", comentario.getSubcomentarios());

			RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaSubcomentarios.jsp");
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
