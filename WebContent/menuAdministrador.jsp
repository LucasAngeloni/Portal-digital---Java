<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <link rel="stylesheet" href="css/styles-menu.css">
</head>

<c:url var="linkCategorias" value="ControladorAdministrador">
	<c:param name="instruccion" value="categorias"></c:param>
</c:url>
<c:url var="linkUsuarios" value="ControladorAdministrador">
	<c:param name="instruccion" value="usuarios"></c:param>
</c:url>
<c:url var="linkPublicaciones" value="ControladorAdministrador">
	<c:param name="instruccion" value="publicaciones"></c:param>
</c:url>
<c:url var="linkComentarios" value="ControladorAdministrador">
	<c:param name="instruccion" value="comentarios"></c:param>
</c:url>
<c:url var="linkCerrarSesion" value="ControladorAdministrador">
	<c:param name="instruccion" value="cerrar_sesion"></c:param>
</c:url>

<h1 class="logo">Portal</h1>

<nav class="navbar navbar-expand-lg navbar-light">
	<div class="container">
		<div class="collapse navbar-collapse" id="navbarNav">
			<ul id="nav-list" class="navbar-nav">
				<li class="nav-item"><a class="nav-link active"
					aria-current="page" href="${linkUsuarios }">Usuarios</a></li>
				<li class="nav-item"><a class="nav-link"
					href="${linkCategorias }">Categorias</a></li>
				<li class="nav-item"><a class="nav-link"
					href="${linkPublicaciones }">Publicaciones</a></li>
				<li class="nav-item"><a class="nav-link"
					href="${linkComentarios }">Comentarios</a></li>
			</ul>
			<a id="btnCerrarSesion" href="${linkCerrarSesion }"> <span
				class="badge bg-secondary">Cerrar sesión</span>
			</a>
		</div>
	</div>
</nav>
