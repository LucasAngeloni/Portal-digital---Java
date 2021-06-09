<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/styles.css">
<link rel="stylesheet" href="css/styles-categorias.css">
<title>Inicio</title>
</head>

<c:url var="linkUsuarios" value="ControladorAdministrador">
	<c:param name="instruccion" value="usuarios"></c:param>
</c:url>
<c:url var="linkCategorias" value="ControladorAdministrador">
	<c:param name="instruccion" value="categorias"></c:param>
</c:url>
<c:url var="linkPublicaciones" value="ControladorAdministrador">
	<c:param name="instruccion" value="publicaciones"></c:param>
</c:url>
<c:url var="linkComentarios" value="ControladorAdministrador">
	<c:param name="instruccion" value="comentarios"></c:param>
</c:url>
<body>
	<div class="container">
		<div class="row">
			<h3><strong>Elige una opción</strong></h3>
		</div>
		<hr>
		<div class="row image-group">
			<div class="col-6">
				<div class="card mb-4">
					<a href="${linkUsuarios }"> <img id="imagen-opciones-admin"
						src="imgs/opciones_admin/usuarios.png" alt="Usuarios"
						class="img-fluid rounded" title="Usuarios" />
					</a>
					<div class="card-body">
						<p class="card-text">Usuarios</p>
					</div>
				</div>
			</div>
			<div class="col-6">
				<div class="card mb-4">
					<a href="${linkPublicaciones }"> <img id="imagen-opciones-admin"
						src="imgs/opciones_admin/publicaciones.png" alt="Publicaciones"
						class="img-fluid rounded" title="Publicaciones" />
					</a>
					<div class="card-body">
						<p class="card-text">Publicaciones</p>
					</div>
				</div>
			</div>
			<div class="col-6">
				<div class="card mb-4">
					<a href="${linkCategorias }"> <img id="imagen-opciones-admin"
						src="imgs/opciones_admin/categorias.png" alt="Categorias"
						class="img-fluid rounded" title="Categorias" />
					</a>
					<div class="card-body">
						<p class="card-text">Categorias</p>
					</div>
				</div>
			</div>
			<div class="col-6">
				<div class="card mb-4">
					<a href="${linkComentarios }"> <img id="imagen-opciones-admin"
						src="imgs/opciones_admin/comentarios.png" alt="Comentarios"
						class="img-fluid rounded" title="Comentarios" />
					</a>
					<div class="card-body">
						<p class="card-text">Comentarios</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>