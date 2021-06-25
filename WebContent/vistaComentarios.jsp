<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Modelo.*" %>
<%@ page import="Logica.*" %>		

<div class="comentarios">
    <c:if test="${usuario != null }">
		<div class="row">
			<form id="formComentar" name="formComentar" method="get"
				action="ControladorComentario">
				<input type="hidden" name="instruccion" value="comentar"> 
				<input type="hidden" name="id_hilo" value="${hilo_abierto.getIdHilo() }">
				<input type="hidden" name="fecha_publicacion"
					value="${publicacion.getFechaPublicacion() }">
				<c:if test="${publicacion.getClass().toString().equals('class Modelo.Aporte') }">
					<input type="hidden" name="usuario_aporte"
						value="${publicacion.getComunicador().getNombreUsuario() }">
				</c:if> 

				<div class="input-group mb-4 filtro">
					<input type="text" name="comentario"
						placeholder="Comenta la publicación" aria-label="Recipient's username"
						aria-describedby="btnComentar">
					<button class="btn btn-warning" type="submit" id="btnComentar">Comentar</button>
				</div>
			</form>
		</div>
	</c:if>
	
	<c:forEach var="comentario" items="${COMENTARIOS }">
		<c:set var="comentario" value="${comentario}" scope="request" />
		<c:if test="${publicacion.getClass().toString().equals('class Modelo.Aporte') }">
			<jsp:include page="vistaComentarioAporte.jsp"></jsp:include>
		</c:if>
		<c:if test="${publicacion.getClass().toString().equals('class Modelo.Nota') }">
			<jsp:include page="vistaComentarioNota.jsp"></jsp:include>
		</c:if> 
	</c:forEach>
</div>