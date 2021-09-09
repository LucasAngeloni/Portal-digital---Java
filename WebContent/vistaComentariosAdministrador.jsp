<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Modelo.*" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet"
	href="open-iconic-master/font/css/open-iconic-bootstrap.min.css">
<link rel="stylesheet" href="css/styles.css">
<link rel="stylesheet" href="css/styles-hilo.css">
<title>Comentarios</title>
</head>

<c:url var="linkVerMas" value="ControladorAdministrador">
	<c:param name="instruccion" value="comentarios"></c:param>
	<c:param name="accion" value="siguiente"></c:param>
	<c:param name="indice" value="${indice }"></c:param>
</c:url>
<c:url var="linkAnterior" value="ControladorAdministrador">
	<c:param name="instruccion" value="comentarios"></c:param>
	<c:param name="accion" value="anterior"></c:param>
	<c:param name="indice" value="${indice }"></c:param>
</c:url>

<body>
	<div class="container-fluid">
		<header class="menu-administrador row">
			<jsp:include page="menuAdministrador.jsp"></jsp:include>
		</header>

		<div class="row">
			<c:if test="${Info != null }">
				<div class="alert alert-success" role="alert">${Info }</div>
			</c:if>
			<c:if test="${Error != null }">
				<div class="alert alert-danger" role="alert">${Error }</div>
			</c:if>
		</div>
		<form method="post" action="ControladorAdministrador">
			<input type="hidden" name="instruccion" value="filtrar"> <input
				type="hidden" name="filtro" value="comentarios">
			<div class="filtro input-group mb-3">
				<select class="form-control form-select" id="filtro"
					name="tipo_filtro">
					<option value="usuario">Usuario</option>
					<option value="descripcion">Descripción</option>
					<option value="com_padre">Comentario Padre</option>
				</select> <input type="text" class="form-control" name="texto"
					placeholder="Ej:" aria-label="Recipient's username"
					aria-describedby="button-addon2">
				<button class="btn btn-info" type="submit" id="button-addon2">Filtrar</button>

				<div class="offset-5"></div>

			</div>
		</form>
		<div class="row">
			<div class="col">
				<c:url var="linkOrdenarPorFecha" value="ControladorAdministrador">
					<c:param name="instruccion" value="ordenar_comentarios"></c:param>
					<c:param name="atributo" value="fecha"></c:param>
					<c:param name="tipo_ordenamiento_fecha"
						value="${tipo_ordenamiento_fecha }"></c:param>
				</c:url>
				<c:url var="linkOrdenarPorLikes" value="ControladorAdministrador">
					<c:param name="instruccion" value="ordenar_comentarios"></c:param>
					<c:param name="atributo" value="likes"></c:param>
					<c:param name="tipo_ordenamiento_likes"
						value="${tipo_ordenamiento_likes }"></c:param>
				</c:url>

				<table class="table table-dark table-striped">
					<thead>
						<tr>
							<th scope="col">Usuario</th>
							<th scope="col"><a href="${linkOrdenarPorFecha }">Fecha
									del Comentario</a></th>
							<th scope="col">Descripción</th>
							<th scope="col"><a href="${linkOrdenarPorLikes }">Likes</a></th>
							<th scope="col">Comentario Padre</th>
							<th scope="col"></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="comentario" items="${comentarios}">
							<tr>
								<th scope="row">${comentario.getNombreUsuario() }</th>
								<td>${comentario.getFechaComentario().toLocalDate()} a las
									${comentario.getFechaComentario().toLocalTime() }</td>
								<td>${comentario.getDescripcionComentario()}</td>
								<td>${comentario.getLikes()}</td>
								<c:choose>
									<c:when test="${comentario.getComentarioPrincipal() != null }">
										<td>${comentario.getComentarioPrincipal().getDescripcionComentario()}</td>
									</c:when>
									<c:otherwise>
										<td>No tiene</td>
									</c:otherwise>
								</c:choose>

								<c:url var="linkEliminarComentario"
									value="ControladorAdministrador">
									<c:param name="instruccion" value="eliminar_comentario"></c:param>
									<c:param name="usuario"
										value="${comentario.getNombreUsuario()}"></c:param>
									<c:param name="fecha"
										value="${comentario.getFechaComentario()}"></c:param>
								</c:url>
								<td><a class="btn btn-sm btn-danger"
									href="${linkEliminarComentario }">Eliminar comentario</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="offset-5">
					<nav aria-label="Page navigation example">
						<ul class="pagination">
							<li class="page-item"><a class="page-link" href="${linkAnterior}">Anterior</a></li>
							<li class="page-item"><a class="page-link" href="${linkVerMas}">Más</a></li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</div>
	<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</body>
</html>