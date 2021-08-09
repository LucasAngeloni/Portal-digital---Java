<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css">
	
	<title>Crear cuenta</title>
</head>
<body>

<%!
boolean esComunicador = false;
%>

<%
String tipoUsuario = request.getParameter("tipo_usuario");
if(tipoUsuario.equals("Comunicador")) esComunicador = true;
else
	esComunicador = false;
%>

<div class="container">
	<div class="row registro">
		<div class="col-12">
              <h3>Crea una cuenta</h3>
      	</div>
			<div class="col-6">
				<c:if test="${Error != null }">
					<div class="alert alert-danger" role="alert">${Error }</div>
				</c:if>

				<form id="formRegistro" name="formRegistro" method="post"
					action="ControladorLogin" enctype="multipart/form-data">
					<input type="hidden" name="instruccion" value="registrar_usuario">
					<input type="hidden" name="tipo_usuario" value="<%=tipoUsuario%>">

					<c:if test="<%= esComunicador%>">
						<div class="form-group row">
							<label for="nombre" class="col-3 col-form-label">Nombre</label>
							<div class="col-9">
								<input type="text" class="form-control" id="txtNombre"
									name="nombre" placeholder="Nombre" value="${nombre }">
							</div>
						</div>
						<div class="form-group row">
							<label for="apellido" class="col-3 col-form-label">Apellido</label>
							<div class="col-9">
								<input type="text" class="form-control" id="txtApellido"
									name="apellido" placeholder="Apellido" value="${apellido }">
							</div>
						</div>
					</c:if>
					<div class="form-group row">
						<label for="nombreUsuario" class="col-3 col-form-label">Nombre
							de usuario</label>
						<div class="col-9">
							<input type="text" class="form-control" id="txtNombreUsuario"
								name="nombreUsuario" placeholder="Nombre de usuario" value="${nombre_usuario }">
						</div>
					</div>
					<c:if test="<%= esComunicador %>">
						<div class="form-group row">
							<label for="descripcion" class="col-3 col-form-label">Descripcion</label>
							<div class="col-9">
								<input type="text" class="form-control" id="txtDescripcion"
									name="descripcion"
									placeholder="Realiza una breve descripción sobre tu persona, intereses, valores, ideales, lo que prefieras"
									value="${descripcion }">
							</div>
						</div>
					</c:if>
					<div class="form-group row">
						<label for="telefono" class="col-3 col-form-label">Teléfono</label>
						<div class="col-9">
							<input type="text" class="form-control" id="txtTelefono"
								name="telefono" placeholder="Número de teléfono" value="${telefono }">
						</div>
					</div>
					<div class="form-group row">
						<label for="email" class="col-3 col-form-label">Email</label>
						<div class="col-9">
							<input type="text" class="form-control" id="txtEmail"
								name="email" placeholder="email@example.com" value="${email }">
						</div>
					</div>

					<div class="form-group row">
						<label for="fecha_nacimiento" class="col-12"><strong>Fecha
								de nacimiento</strong></label> 
						<input type="date" id="fecha_nacimiento" name="fecha_nacimiento"
									value="${fecha_nacimiento }" min="1940-01-01" max="2021-01-01"> 
					</div>

					<div class="form-group row">
						<label for="imagen_usuario" class="col-4 col-form-label">Imagen
							de tu perfil</label> <input type="file" class="col-8"
							name="imagen_usuario" accept=".jpg,.png,.jfif" />
					</div>

					<div class="form-group row">
						<label for="clave" class="col-3 col-form-label">Clave</label>
						<div class="col-9">
							<input type="text" class="form-control" id="txtClave"
								name="clave" placeholder="Clave">
						</div>
					</div>
					<div class="form-group row">
						<label for="repeticionClave" class="col-3 col-form-label">Repetir
							clave</label>
						<div class="col-9">
							<input type="text" class="form-control" id="txtRepeticionClave"
								name="repeticionClave" placeholder="Repetir la clave">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-auto">
							<a href="preRegistroUsuario.jsp" type="reset"
								class="btn btn-primary" id="btnVolver">Volver</a>
						</div>
						<div class="col-auto">
							<button type="submit" class="btn btn-primary" id="btnRegistro">Registrarse</button>
						</div>
					</div>
				</form>
			</div>
		</div>		
</div>

</body>
</html>