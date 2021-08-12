<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Modelo.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="open-iconic-master/font/css/open-iconic-bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/styles-perfil.css">
    <link rel="stylesheet" href="css/styles-hilo.css">
    <link rel="stylesheet" href="css/styles-aportes.css">
    <title>Perfil de ${COMUNICADOR.getNombreUsuario() }</title> 
</head>
<c:url var="linkPublicaciones" value="ControladorUsuario">
	<c:param name="instruccion" value="perfil"></c:param>
	<c:param name="usu" value="${COMUNICADOR.getNombreUsuario()}"></c:param>
</c:url>
<c:url var="linkAportes" value="ControladorUsuario">
	<c:param name="instruccion" value="ver_aportes"></c:param>
</c:url>
<c:url var="linkRelevancias" value="ControladorUsuario">
	<c:param name="instruccion" value="ver_relevancias"></c:param>
</c:url>

<body>
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include>
			<!-- MENU DE OPCIONES -->
		</header>
		<div class="col-8 perfil">
			<div class="row cabecera">
				<h4>
					<strong>Perfil</strong>
				</h4>
			</div>
			<div class="row datos-principales">
				<div class="col-3">
					<img src="${COMUNICADOR.getImagen()}" class="img-fluid rounded"
						title="${COMUNICADOR.getNombreUsuario()}" />
				</div>
				<div class="col-9">
					<h2>
						<strong>${COMUNICADOR.getNombreUsuario() }</strong>
					</h2>
					<h6>Comunicador</h6>
					<div class="row descripcion_comunicador">
						${COMUNICADOR.getDescripcion() }</div>
				</div>
			</div>
			<div class="row bar">
				<nav class="navbar navbar-expand-lg navbar-light">
					<div class="collapse navbar-collapse" id="navbarNav">
						<ul id="nav-list" class="navbar-nav">
							<li class="nav-item"><a class="nav-link"
								href="${linkPublicaciones }">Publicaciones</a></li>
							<li class="nav-item"><a class="nav-link"
								href="${linkAportes }">Aportes</a></li>
							<li class="nav-item"><a class="nav-link"
								href="${linkRelevancias }">Relevancias</a></li>
						</ul>
					</div>
				</nav>
			</div>
			<c:choose>
				<c:when test="${HILOS != null}">
					<div id="bloque-hilos" class="row">
						<c:forEach var="hilo" items="${HILOS }">
							<!-- BLOQUE DEL HILO -->
							<c:set var="hilo" value="${hilo }" scope="request" />

							<div class="hilo-container container">
								<jsp:include page="vistaNotaPrincipal.jsp"></jsp:include>
							</div>
							<!-- FIN DEL BLOQUE HILO -->
						</c:forEach>
					</div>
				</c:when>
				<c:when test="${NOTAS != null}">
					<c:forEach var="nota" items="${NOTAS}">
						<!-- BLOQUE NOTAS -->
						<c:set var="nota" value="${nota }" scope="request" />
						<c:set var="comunicador" value="${COMUNICADOR }" scope="request" />
						<jsp:include page="vistaNota.jsp"></jsp:include>

						<!-- FIN BLOQUE DEL NOTA -->
					</c:forEach>
				</c:when>

				<c:otherwise>
					<c:forEach var="aporte" items="${APORTES}">
						<!-- BLOQUE DEL HILO -->
						<c:set var="aporte" value="${aporte}" scope="request" />

						<jsp:include page="vistaAporte.jsp"></jsp:include>
						<!-- FIN DEL BLOQUE HILO -->
					</c:forEach>
				</c:otherwise>
			</c:choose>

			<c:if test="${Info != null }">
				<div class="alert alert-success" role="alert">${Info }</div>
			</c:if>
		</div>
	</div>
</body>
<script src="js/script.js"></script>
<script src="js/jquery-3.5.0.min.js"></script>
</html>