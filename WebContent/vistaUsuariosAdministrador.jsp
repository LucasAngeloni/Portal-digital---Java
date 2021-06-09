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
<link rel="stylesheet" href="css/styles.css">
<title>Usuarios</title>
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
			<input type="hidden" name="filtro" value="usuarios">
			<div class="input-group mb-3 filtro">
				<select class="form-control form-select" id="filtro" name="tipo_filtro">
					<option value="usuario">Nombre de Usuario</option>
					<option value="email">Email</option>
				</select> <input type="text" class="form-control" name="texto" 
				placeholder="Ej:"
					aria-label="Recipient's username" aria-describedby="button-addon2">
				<button class="btn btn-info" type="submit" id="button-addon2">Filtrar</button>
				
				<div class="offset-5"></div>
			</div>
		</form>
		<div class="row">
			<div class="col">
				<table class="table table-dark table-striped">
					<thead>
						<tr>
							<th scope="col">Imagen</th>
							<th scope="col">Nombre de Usuario</th>
							<th scope="col">Teléfono</th>
							<th scope="col">Email</th>
							<th scope="col">Fecha de Nacimiento</th>
							<th scope="col"></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="usuario" items="${usuarios}">
							<tr>
								<th scope="row"><c:choose>
										<c:when test="${usuario.getImagen() != null }">
											<img id="imagen-usuario" src="${usuario.getImagen()}"
												class="img-fluid rounded"
												title="${usuario.getNombreUsuario()}" />
										</c:when>
										<c:otherwise>
											<img id="imagen-usuario"
												src="imgs/usuarios/imagen_por_defecto.png"
												class="img-fluid rounded"
												title="${usuario.getNombreUsuario()}" />
										</c:otherwise>
									</c:choose></th>
								<td>${usuario.getNombreUsuario()}</td>
								<td>${usuario.getTelefono()}</td>
								<td>${usuario.getEmail()}</td>
								<td>${usuario.getFechaNacimiento()}</td>

								<c:url var="linkEliminarUsuario"
									value="ControladorAdministrador">
									<c:param name="instruccion" value="eliminar_usuario"></c:param>
									<c:param name="nombre_usuario"
										value="${usuario.getNombreUsuario()}"></c:param>
								</c:url>
								<td><a class="btn btn-sm btn-danger"
									href="${linkEliminarUsuario }">Eliminar usuario</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/script.js"></script>
</html>