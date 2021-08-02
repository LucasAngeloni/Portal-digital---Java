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
    <link rel="stylesheet" href="css/styles-hilo.css">
    <link rel="stylesheet" href="css/styles.css">
	<title>Agregar nota</title>
</head>
<body>
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include>
			<!-- MENU DE OPCIONES -->
		</header>
		<div class="col-8 medium-col">
			<div class="row medium-top">
				<h3>Hilo</h3>
			</div>

			<div class="row">
				<!-- BLOQUE DEL HILO -->
				<div class="container hilo">
					<c:forEach var="nota" items="${hilo_abierto.getNotas()}">
						<!-- BLOQUE NOTAS -->
						<c:choose>  
						    <c:when test="${hilo_abierto.getIdNota(nota) != 1}">  
						       <!-- BLOQUE NOTAS -->
								<hr>
								<c:set var="nota" value="${nota }" scope="request"/>
								<c:set var="comunicador" value="${hilo_abierto.getComunicador() }" scope="request" />
								<div class="nota-container">
									<jsp:include page="vistaNota.jsp"></jsp:include>
								</div>
								
								<!-- FIN BLOQUE DEL NOTA -->  
						    </c:when>  
						    <c:otherwise>
								<c:set var="hilo" value="${hilo_abierto}" scope="request" />
								<jsp:include page="vistaNotaPrincipal.jsp"></jsp:include>  
						    </c:otherwise>  
						</c:choose>
					</c:forEach>
					<hr>
					<div class="col-9">
						<form id="formAgregarNota" name="formAgregarNota" method="post"
							action="ControladorNota">
							<input type="hidden" name="instruccion" value="agregar_nota">
							<input type="hidden" name="id_hilo"
								value="${hilo_abierto.getIdHilo() }">
							<div class="row input-group mb-3 offset-1">
								<textarea class="form-control col-10" id="txtDescripcionNota"
									name="descripcionNota" aria-describedby="btnRegistro"></textarea>
								<button type="submit" class="btn btn-success" id="btnRegistro">Agregar</button>
							</div>
							<div class="form-group offset-1">
								
							</div>
						</form>
					</div>
				</div>

				<!-- FIN BLOQUE DEL HILO -->
			</div>
		</div>
	</div>
</body>
<script src="js/jquery-3.5.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/script.js"></script>
</html>