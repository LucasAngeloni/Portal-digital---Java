<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/styles-comentarios.css">
    <link rel="stylesheet" href="css/styles-hilo.css">
<title>Comentarios</title>
</head>
<body>
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include>
		</header>
		<div class="col-8 medium-col seccion-comentarios">
			<div class="row titulo-comentarios">
				<h5>Comentarios</h5>
			</div>
			
			<c:set var="hilo" value="${hilo_abierto}" scope="request" />
			<c:if test="${publicacion.getClass().toString().equals('class Modelo.Nota') }">
				<div class="nota-container">
					<c:set var="comunicador" value="${hilo.getComunicador() }" scope="request" />
					<c:set var="nota" value="${publicacion}" scope="request" />
					<jsp:include page="vistaNota.jsp"></jsp:include>
				</div>
			</c:if>
			<c:if test="${publicacion.getClass().toString().equals('class Modelo.Aporte')}">
				<div class="nota-container">
					<c:set var="aporte" value="${publicacion}" scope="request" />
					<jsp:include page="vistaAporte.jsp"></jsp:include>
				</div>
			</c:if>
			<jsp:include page="vistaComentarios.jsp"></jsp:include>
		</div>
		<c:if test="${Error != null }">
			<div class="alert alert-danger" role="alert">${Error }</div>
		</c:if>
	</div>
<jsp:include page="vistaAvisoRegistroModal.jsp"></jsp:include>
</body>
<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/script.js"></script>
</html>