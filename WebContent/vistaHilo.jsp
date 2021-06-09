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
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/styles-hilo.css">
    <link rel="stylesheet" href="css/styles-comentarios.css">
	<title>Hilo</title>
</head>
<body>
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include>
			<!-- MENU DE OPCIONES -->
		</header>
		<div class="col-7 medium-col">
			<div class="row medium-top">
				<h3>Hilo</h3>
			</div>

			<div class="row">
				<div class="col">
					<c:if test="${Info != null }">
						<div class="alert alert-success" role="alert">${Info }</div>
					</c:if>
					<c:if test="${Error != null }">
						<div class="alert alert-danger" role="alert">${Error }</div>
					</c:if>
				</div>

				<!-- BLOQUE DEL HILO -->
				<c:set var="hilo" value="${hilo_abierto }" scope="request" />

				<div class="container hilo">

					<c:forEach var="nota" items="${hilo_abierto.getNotas()}">
						<c:choose>
							<c:when test="${hilo.getIdNota(nota) != 1}">
								<!-- BLOQUE NOTAS -->
								<c:set var="nota" value="${nota }" scope="request" />
								<c:set var="comunicador" value="${hilo.getComunicador() }" scope="request" />
								<jsp:include page="vistaNota.jsp"></jsp:include>

								<!-- FIN BLOQUE DEL NOTA -->
							</c:when>
							<c:otherwise>
								<jsp:include page="vistaNotaPrincipal.jsp"></jsp:include>
							</c:otherwise>
						</c:choose>

					</c:forEach>
				</div>
				<!-- FIN BLOQUE DEL HILO -->
			</div>
		</div>
		<div class="col-3 seccion-comentarios">
			<div class="row titulo-comentarios">
				<h5>Comentarios</h5>
			</div>
			<c:set var="COMENTARIOS" value="${hilo_abierto.getComentarios()}"
				scope="request" />
			<c:set var="nro_nota" value="1" scope="request" />
			<jsp:include page="vistaComentarios.jsp"></jsp:include>
		</div>
	</div>
<jsp:include page="vistaAvisoRegistroModal.jsp"></jsp:include>
</body>
<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/script.js"></script>
</html>