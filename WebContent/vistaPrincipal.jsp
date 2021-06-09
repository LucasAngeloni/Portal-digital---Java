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
    <link rel="stylesheet" href="css/styles-hilo.css">    
	<title>Inicio</title>
</head>

<c:url var="linkCargarHilos" value="ControladorVistaPrincipal">
	<c:param name="instruccion" value="cargar_mas_hilos"></c:param>
	<c:param name="hilos" value="${hilos }"></c:param>
</c:url>
<body>
	<c:set var="hilosGuardados" value="${usuario.getHilosGuardados() }"/>
	<!-- ------------------------------------------------------------------- -->
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include> <!-- MENU DE OPCIONES -->
		</header>
		
		<div class="col-8 medium-col">
			<div class="row medium-top">
		 		<form id="formBuscar" name="formBuscar" 
		 			method="get" action="ControladorVistaPrincipal">
					<input type="hidden" name="instruccion" value="buscador">
					<c:set var="hilos" value="${hilos}" scope="request"/>
					<div class="form-row">
						<div class="col-9">
							<input class="form-control" type="search" id="inputBuscar" name="texto" placeholder="Busca un hilo">
						</div>
						<div class="col-3">
							<button type="submit" class="btn btn-outline-info my-1 my-sm-0" id="btnBuscar">Buscar</button>
						</div>
					</div>
				</form>
				<c:if test="${usuario.getClass().toString().equals('class Modelo.Comunicador') }">
				   <a class="btn btn-warning" role="button" data-toggle="modal" data-target="#escribir">Escribir</a>
				</c:if>
				<c:if test="${usuario.getClass().toString().equals('class Modelo.Usuario') }">
				   <a id="btnComunicador" class="btn btn-sm btn-success col-2" role="button" data-toggle="modal" data-target="#comunicador">Quiero ser Comunicador </a>
				</c:if>
			</div>
			
			<div id="bloque-hilos" class="row">
				<div class="col">
					<c:if test="${Info != null }">
						<div class="alert alert-success" role="alert">${Info }</div>
					</c:if>
					<c:if test="${Error != null }">
						<div class="alert alert-danger" role="alert">${Error }</div>
					</c:if>
				</div>
				<c:forEach var="hilo" items="${hilos}">		
					
					<!-- BLOQUE DEL HILO -->
					<c:set var="hilo" value="${hilo }" scope="request"/>
					
					<div class="hilo-container container">								
						<jsp:include page="vistaNotaPrincipal.jsp"></jsp:include>									
					</div>
					<!-- FIN DEL BLOQUE HILO -->
									
				</c:forEach>
			</div>
			<div class="col-2 offset-5">
				<a href="${linkCargarHilos }" type="button" class="btn btn-primary">Ver mas</a>
			</div>			
		</div>
	</div>
	<jsp:include page="vistaModalTransformarAComunicador.jsp"></jsp:include>
	<jsp:include page="vistaAvisoRegistroModal.jsp"></jsp:include>
</body>
<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/script.js"></script>
</html>