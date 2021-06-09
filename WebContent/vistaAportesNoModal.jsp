<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Modelo.*" %>
<%@ page import="Logica.*" %>

<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="open-iconic-master/font/css/open-iconic-bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/styles-hilo.css">
    <link rel="stylesheet" href="css/styles-aportes.css">    
    
	<title>Aportes</title>

</head>

<body>
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include> <!-- MENU DE OPCIONES -->
		</header>
		
		<div class="col-8 medium-col">
			<div class="row medium-top">
		 		<form id="formBuscar" name="formBuscar" 
		 			method="get" action="ControladorAporte">
					<input type="hidden" name="instruccion" value="buscar_aporte">
					<div class="form-row">
						<div class="col-9">
							<input class="form-control" type="search" id="inputBuscar" name="texto" placeholder="Busca un aporte">
						</div>
						<div class="col-3">
							<button type="submit" class="btn btn-outline-info my-1 my-sm-0" id="btnBuscar">Buscar</button>
						</div>
					</div>
				</form>
				<c:if test="${usuario.getClass().toString().equals('class Modelo.Usuario') }">
				   <a id="btnComunicador" class="btn btn-sm btn-success col-2" role="button" data-toggle="modal" data-target="#comunicador">Quiero ser Comunicador </a>
				</c:if>
			</div>
			<div class="row">
				<c:if test="${Info != null }">
					<div class="alert alert-success" role="alert">${Info }</div>
				</c:if>
				<c:if test="${Error != null }">
					<div class="alert alert-danger" role="alert">${Error }</div>
				</c:if>
			</div>

			<c:set var="hilo" value="${hilo_abierto }" scope="request"/>
			<jsp:include page="vistaNotaPrincipal.jsp"></jsp:include>
			
			<div class="row titulo-aporte">
				<div class="col-3">
					<h3>Aportes</h3>
				</div>
				<c:if
					test="${usuario.getClass().toString().equals('class Modelo.Comunicador') }">
					<div class="offset-7">
						<a type="button" class="btn btn-success" data-toggle="modal"
							data-target="#agregar_aporte" aria-label="Agregar aporte">
							Agregar + </a>
					</div>
				</c:if>
			</div>

			<c:forEach var="aporte" items="${aportes}">
				<!-- BLOQUE DEL APORTE -->
				<c:set var="aporte" value="${aporte}" scope="request" />
				<jsp:include page="vistaAporte.jsp"></jsp:include>

			</c:forEach>

		</div>
	</div>
	<jsp:include page="vistaModalTransformarAComunicador.jsp"></jsp:include>
	<jsp:include page="vistaAgregarAporte.jsp"></jsp:include>
	<jsp:include page="vistaAvisoRegistroModal.jsp"></jsp:include>
</body>
<script src="js/jquery-3.5.0.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/script.js"></script>	
</html>		