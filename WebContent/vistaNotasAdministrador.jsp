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
<title>Hilos</title>
</head>
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

		<div class="row">
			<div class="col">
				<h3>Hilo</h3>
				<table class="table table-dark table-striped">
					<thead>
						<tr>
							<th scope="col">#</th>
							<th scope="col">Comunicador</th>
							<th scope="col" align="center">Título</th>
							<th scope="col"></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th scope="row">${hilo.getIdHilo() }</th>
							<td>${hilo.getComunicador().getNombreUsuario()}</td>
							<td>${hilo.getTitulo()}</td>

							<c:url var="linkEliminarHilo" value="ControladorAdministrador">
								<c:param name="instruccion" value="eliminar_hilo"></c:param>
								<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
							</c:url>
							<td><a class="btn btn-sm btn-danger"
								href="${linkEliminarHilo }">Eliminar publicacion</a></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<h3>Notas</h3>
				<table class="table table-dark table-striped">
					<thead>
						<tr>
							<th scope="col">Fecha de Publicación</th>
							<th scope="col">Descripción de la Nota</th>
							<th scope="col">Relevancia</th>
							<th scope="col"></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="nota" items="${notas}">
							<tr>
								<th scope="row">${nota.getFechaPublicacion().toLocalDate() }</th>
								<td>${nota.getDescripcion()}</td>
								<td>${nota.getRelevancia()}</td>
								<c:url var="linkEliminarNota" value="ControladorAdministrador">
									<c:param name="instruccion" value="eliminar_nota"></c:param>
									<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
									<c:param name="fecha_nota"
										value="${nota.getFechaPublicacion()}"></c:param>
								</c:url>
								<td><a class="btn btn-sm btn-danger"
									href="${linkEliminarNota }">Eliminar nota</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</body>
</html>