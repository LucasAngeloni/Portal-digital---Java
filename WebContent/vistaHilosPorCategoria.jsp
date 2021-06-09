<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Modelo.*" %>
<%@ page import="Logica.*" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/styles-hilo.css">
    
	<title>Inicio</title>
</head>

<body>
	<!-- ------------------------------------------------------------------- -->

	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include>
			<!-- MENU DE OPCIONES -->
		</header>

		<div class="col-8 medium-col">
			<div class="row medium-top">
				<form id="formBuscar" name="formBuscar" method="get"
					action="ControladorCategoria">
					<input type="hidden" name="instruccion" value="buscar_hilos">
					<div class="form-row">
						<div class="col-9">
							<input class="form-control" type="search" id="inputBuscar"
								name="texto" placeholder="Busca un hilo">
						</div>
						<div class="col-3">
							<button type="submit" class="btn btn-outline-info my-1 my-sm-0"
								id="btnBuscar">Buscar</button>
						</div>
					</div>
				</form>
				<div class="row">
					<nav class="navbar navbar-expand-lg navbar-light">
						<div class="collapse navbar-collapse" id="navbarNav">
							<ul id="nav-list" class="navbar-nav">
								<c:forEach var="categoria" items="${CATEGORIAS }">
									<c:url var="linkCategoria" value="ControladorCategoria">
										<c:param name="instruccion" value="elegir_categoria"></c:param>
										<c:param name="id" value="${categoria.idCategoria }"></c:param>
									</c:url>
									<li class="nav-item"><c:choose>
											<c:when
												test="${categoria.getIdCategoria() } == ${CATEGORIA }">
												<a class="nav-link active" href="${linkCategoria }">${categoria.getDescripcionCategoria() }</a>
											</c:when>
											<c:otherwise>
												<a class="nav-link" href="${linkCategoria }">${categoria.getDescripcionCategoria() }</a>
											</c:otherwise>
										</c:choose></li>
								</c:forEach>
							</ul>
						</div>
					</nav>
				</div>
			</div>

			<div id="bloque-hilos" class="row">
				<c:forEach var="hilo" items="${hilos}">

					<!-- BLOQUE DEL HILO -->
					<c:set var="hilo" value="${hilo }" scope="request" />

					<div class="container hilo-container">
						<jsp:include page="vistaNotaPrincipal.jsp"></jsp:include>
					</div>
					<!-- FIN DEL BLOQUE HILO -->

				</c:forEach>
			</div>
			<c:if test="${Error != null }">
				<div class="alert alert-danger" role="alert">${Error }</div>
			</c:if>
		</div>
	</div>
</body>
<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/script.js"></script>
</html>