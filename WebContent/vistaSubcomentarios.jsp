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
    <link rel="stylesheet" href="css/styles-comentarios.css">
	
<title>Respuestas</title>
</head>
<body>
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include>
		</header>
		<div class="col-8 medium-col seccion-comentarios">
			<div class="row titulo-comentarios">
				<h5>Comentario</h5>
			</div>

			<strong> <c:if
					test="${publicacion.getClass().toString().equals('class Modelo.Aporte') }">
					<jsp:include page="vistaComentarioAporte.jsp"></jsp:include>
				</c:if> <c:if
					test="${publicacion.getClass().toString().equals('class Modelo.Nota') }">
					<jsp:include page="vistaComentarioNota.jsp"></jsp:include>
				</c:if>
			</strong>

			<div class="row titulo-comentarios">
				<h6>Respuestas</h6>
			</div>
			
			<c:forEach var="subcomentario" items="${SUBCOMENTARIOS }">
				<c:set var="comentario" value="${subcomentario}" scope="request" />
				<c:if
					test="${publicacion.getClass().toString().equals('class Modelo.Aporte') }">
					<jsp:include page="vistaComentarioAporte.jsp"></jsp:include>
				</c:if>
				<c:if
					test="${publicacion.getClass().toString().equals('class Modelo.Nota') }">
					<jsp:include page="vistaComentarioNota.jsp"></jsp:include>
				</c:if>
			</c:forEach>

		</div>
	</div>
	<jsp:include page="vistaAvisoRegistroModal.jsp"></jsp:include>
</body>
<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</html>