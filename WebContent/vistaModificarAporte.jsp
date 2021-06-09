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
    <link rel="stylesheet" href="css/styles-aportes.css">
	<title>Hilo</title>
</head>
<body>
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include> <!-- MENU DE OPCIONES -->
		</header>
		<div class="col-9 medium-col">
			<div class="hilo-container container">
				<jsp:include page="vistaNotaPrincipal.jsp"></jsp:include>
			</div>
			<div class="row">
				<strong>Aporte</strong>
			</div>
			<hr>
			
			<c:set var="modificar_aporte" value="${true}" scope="request" />
			<div class="hilo-container container">
				<jsp:include page="vistaAporte.jsp"></jsp:include>
			</div>
		</div>
	</div>
</body>
<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/script.js"></script>
</html>