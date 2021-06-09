<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Modelo.*" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/styles.css">
<title>Categorías</title>
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
			<input type="hidden" name="filtro" value="categorias">
			<div class="input-group mb-4 filtro">
				<input type="text" class="form-control" name="texto"
					placeholder="Buscar una categoria"
					aria-label="Recipient's username" aria-describedby="button-addon2">
				<button class="btn btn-info" type="submit" id="button-addon2">Filtrar</button>
				<div class="offset-5">
					<a class="btn btn-success" href="vistaAgregarCategoria.jsp">Agregar
						Categoría</a>
				</div>
			</div>			
		</form>
		
		<div class="row">
			<div class="col">
				<table class="table table-dark table-striped">
					<thead>
						<tr>
							<th scope="col">#</th>
							<th scope="col">Imagen</th>
							<th scope="col">Nombre de la categoría</th>
							<th scope="col"></th>
							<th scope="col"></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="categoria" items="${categorias}">
							<tr>
								<th scope="row">${categoria.getIdCategoria() }</th>
								<td><c:choose>
										<c:when test="${categoria.getImagenCategoria() != null }">
											<img id="imagen-usuario"
												src="${categoria.getImagenCategoria()}"
												class="img-fluid rounded" />
										</c:when>
										<c:otherwise>
											<img id="imagen-usuario"
												src="imgs/usuarios/imagen_por_defecto.png"
												class="img-fluid rounded" />
										</c:otherwise>
									</c:choose></td>
								<td>${categoria.getDescripcionCategoria()}</td>
								<c:url var="linkEliminarCategoria"
									value="ControladorAdministrador">
									<c:param name="instruccion" value="eliminar_categoria"></c:param>
									<c:param name="id_categoria"
										value="${categoria.getIdCategoria()}"></c:param>
								</c:url>
								<c:url var="linkModificarCategoria"
									value="vistaModificarCategoria.jsp">
									<c:param name="id_categoria"
										value="${categoria.getIdCategoria()}"></c:param>
									<c:param name="descripcion_categoria"
										value="${categoria.getDescripcionCategoria()}"></c:param>
									<c:param name="imagen_categoria"
										value="${categoria.getImagenCategoria()}"></c:param>
								</c:url>
								<td><a class="btn btn-sm btn-info"
									href="${linkModificarCategoria }">Modificar categoría</a></td>
								<td><a class="btn btn-sm btn-danger"
									href="${linkEliminarCategoria }">Eliminar categoría</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>