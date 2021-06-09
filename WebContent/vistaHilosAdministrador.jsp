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
		<form method="post" action="ControladorAdministrador">
			<input type="hidden" name="instruccion" value="filtrar">
			<input type="hidden" name="filtro" value="hilos">
			<div class="filtro input-group mb-3">
				<select class="form-control form-select" id="filtro" name="tipo_filtro">
					<option value="titulo">Titulo</option>
					<option value="usuario">Usuario</option>
				</select> <input type="text" class="form-control" name="texto"
					placeholder="Ej:" aria-label="Recipient's username"
					aria-describedby="button-addon2">
				<button class="btn btn-info" type="submit" id="button-addon2">Filtrar</button>
				
				<div class="offset-5"></div>
				
			</div>
		</form>
		<div class="row">
			<div class="col">
				<table class="table table-dark table-striped">
					<thead>
						<tr>
							<th scope="col">#</th>
							<th scope="col">Comunicador</th>
							<th scope="col" align="center">Título</th>
							<th scope="col">Nota Principal</th>
							<th scope="col">Fecha de Publicación</th>
							<th scope="col"></th>
							<th scope="col"></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="hilo" items="${hilos}">
							<c:url var="linkVerNotas" value="ControladorAdministrador">
								<c:param name="instruccion" value="ver_notas"></c:param>
								<c:param name="id_hilo" value="${hilo.getIdHilo() }"></c:param>
							</c:url>
							<tr>
								<th scope="row">${hilo.getIdHilo() }</th>
								<td>${hilo.getComunicador().getNombreUsuario()}</td>
								<td>${hilo.getTitulo()}</td>
								<td>${hilo.getNota(1).getDescripcion()}</td>
								<td>${hilo.getNota(1).getFechaPublicacion().toLocalDate()}</td>
								<td><a href="${linkVerNotas }"> Ver el resto del hilo </a>
								</td>

								<c:url var="linkEliminarHilo" value="ControladorAdministrador">
									<c:param name="instruccion" value="eliminar_hilo"></c:param>
									<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
								</c:url>
								<td><a class="btn btn-sm btn-danger"
									href="${linkEliminarHilo }">Eliminar publicacion</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="offset-5">
					<nav aria-label="Page navigation example">
						<ul class="pagination">
							<li class="page-item"><a class="page-link" href="#">Anterior</a></li>
							<li class="page-item"><a class="page-link" href="#">Más
									Hilos</a></li>
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