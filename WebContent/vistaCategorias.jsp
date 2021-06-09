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
    <link rel="stylesheet" href="css/styles-categorias.css">
	<title>Categorias</title>
</head>

<body>	
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include> <!-- MENU DE OPCIONES -->
		</header>
			
		<div class="col-8 medium-col justify-content-center">
			<div class="row medium-top">
				<h3>Categorías</h3>				
			</div>
			
			<div class="row image-group">
				<c:forEach var="categoria" items="${CATEGORIAS}">
					<c:url var="linkCategoria" value="ControladorCategoria">
						<c:param name="instruccion" value="elegir_categoria"></c:param>
						<c:param name="id" value="${categoria.getIdCategoria() }"></c:param>
						<c:param name="descripcion" value="${categoria.getDescripcionCategoria() }"></c:param>
					</c:url>
				 
				 	<div class="col-4" >
				 		<div class="card mb-4">
					 		<a href="${linkCategoria }">
					 			<img id="imagen-categoria" src="${categoria.getImagenCategoria() }" 
					 				alt="${categoria.getDescripcionCategoria()}" class="img-fluid rounded"
					 				title="${categoria.getDescripcionCategoria() }"/>
					 		</a>
					 		<div class="card-body">
	            				<p class="card-text">${categoria.getDescripcionCategoria()}</p>
	            			</div>
				 		</div>
				 	</div>
				</c:forEach>
			</div>
			<c:if test="${Error != null }">
				<div class="alert alert-danger" role="alert">${Error }</div>
			</c:if>
		</div>
	</div>
</body>
<script src="js/bootstrap.min.js"></script>
<script src="js/script.js"></script>
<script src="js/jquery-3.5.0.min.js"></script>
</html>