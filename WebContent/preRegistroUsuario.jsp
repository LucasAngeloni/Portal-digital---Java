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
<title>Registro</title>


</head>

<body>
<div class="container">
	<div class="row">
		<div class="col-12">
			<h2>Tipo de Usuario</h2>
		</div>
		<div class="col-6">
		<form action="registrarUsuario.jsp">
			<div class="form-group row">
				<label for="tipoUsuario" class="col-6 col-form-label"><strong>Tipo de Usuario</strong></label>
				<select id="tipoUsuario" class="form-control col-6" name="tipo_usuario">
					<option>Comunicador</option>
					<option>Lector</option>
				</select>
			</div>
			<div class="form-group row">
   				<div class="col-auto">
   					<button class="btn btn-primary btn-sm" type="submit">Seguir</button>
	      		</div>
   			</div>
   		</form>
		</div>
	</div>
</div>
</body>
</html>