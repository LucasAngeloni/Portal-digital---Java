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
    <link rel="stylesheet" href="css/styles-login.css">
	
	<title>Login</title>
</head>

<body>
	<div class="container">
		<div class="row offset-3 login">
			<div class="col-12">
	              <h3>Iniciar Sesión</h3>
	      	</div>
	      	<div class="col-6">
	      		<form method="get" action="ControladorLogin">		
	      			<input type="hidden" name="instruccion" value="inicio_sesion">
	      			<div class="form-group row">
	      				<label for="nombreUsuario" class="col-3 col-form-label">Nombre de usuario</label>
	      				<div class="col-9">
	      					<input type="text" class="form-control" id="txtNombreUsuario" 
	      						name="nombreUsuario" placeholder="Nombre de usuario">
	      				</div>				
	      			</div>
	      			<div class="form-group row">
	      				<label for="clave" class="col-3 col-form-label">Clave</label>
	      				<div class="col-9">
	      					<input type="text" class="form-control" id="txtClave" 
	      						name="clave" placeholder="Clave">
	      				</div>				
	      			</div>
	      			<div class="form-group row">
						<div class="">
							<a href="ControladorVistaPrincipal" type="button"
								class="btn btn-outline-primary"
								style="color: white; border-color: white;">
								Seguir sin loguearse</a>
						</div>
						<div class="offset-4">
	      					<button type="submit" class="btn btn-primary" id="btnIngresar">Ingresar</button>
	      				</div>
	      			</div>
				</form>
				<div class="text-center">
					<a href="preRegistroUsuario.jsp" style="color: white;">
						Registrate en el Portal
					</a>
					<div id="texto">
					</div>
				</div>
	      	</div>		
		</div>
	</div>
	<c:if test="${Error != null }">
		<div class="alert alert-danger" role="alert">${Error }</div>
	</c:if>
	<script src="js/jquery-3.5.0.min.js"></script>
</body>
</html>