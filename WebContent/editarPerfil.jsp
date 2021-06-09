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
    <link rel="stylesheet" href="css/styles-perfil.css">
    
    <title>${usuario.getNombreUsuario() }</title> 
</head>
<body>

	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include>
			<!-- MENU DE OPCIONES -->
		</header>
		<div class="col-8 perfil">
			<div class="row titulo">
				<h4><strong>Perfil</strong>	</h4>
			</div>
			<div class="row datos-principales">
				<div class="col-5">
					<form id="formCambiarImagen" name="formCambiarImagen" method="get"
						action="ControladorUsuario">
						<input type="hidden" name="instruccion" value="cambiar_imagen">

						<c:choose>
							<c:when test="${usuario.getImagen() != null }">
								<img src="${usuario.getImagen()}" class="img-fluid rounded"
									title="${usuario.getNombreUsuario()}" />
							</c:when>
							<c:otherwise>
								<img src="imgs/usuarios/imagen_por_defecto.png"
									class="img-fluid rounded" title="${usuario.getNombreUsuario()}" />
							</c:otherwise>
						</c:choose>
						<div class="input-group mb-2">
							<input type="file" aria-describedby="button-addon2"
								name="imagen_usuario" accept=".jpg,.png,.jfif" />
							<button type="submit" class="btn btn-success" id="btnEditar"
								id="button-addon2">Cambiar imagen</button>
						</div>

					</form>
				</div>
				<div class="col-7">
					<h2><strong>${usuario.getNombreUsuario() }</strong></h2>
					<c:choose>
						<c:when test="${usuario.getClass() == 'class Modelo.Usuario' }">
							<h6>Lector</h6>
						</c:when>
						<c:otherwise>
							<h6>Comunicador</h6>
							<form id="formCambiarDescripcion" name="formCambiarDescripcion"
								method="get" action="ControladorUsuario">
								<input type="hidden" name="instruccion"
									value="cambiar_descripcion">

								<div class="row input-group mb-3 descripcion_comunicador">
									<input type="text" class="form-control"
										id="txtDescripcionUsuario" name="descripcion"
										placeholder="${usuario.getDescripcion() }"
										value="${usuario.getDescripcion() }"
										aria-describedby="button-modificar">
									<button type="submit" class="btn btn-success"
										id="button-modificar">Modificar</button>
								</div>
							</form>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="row datos-secundarios">
				<div class="col-6">
					<strong>Categorías preferidas:</strong>
					<ul>
						<c:forEach var="categoria"
							items="${usuario.getCategoriasPreferidas() }">
							<li>${categoria.getDescripcionCategoria() }</li>
						</c:forEach>
					</ul>
				</div>
				<div class="col-6">
					<form id="formEditarPerfil" name="formEditarPerfil" method="get"
						action="ControladorUsuario">
						<input type="hidden" name="instruccion" value="editar_perfil">
						<input type="hidden" name="tipo_usuario"
							value="${usuario.getClass() }">

						<div class="form-group row">
							<label for="fecha_nacimiento" class="col-3 col-form-label"><strong>Fecha
									de Nacimiento</strong></label>
							<div class="col-9">
								<input type="date" id="fecha_nacimiento" name="fecha_nacimiento"
									value="${usuario.getFechaNacimiento() }" min="1940-01-01" max="2021-01-01"> 
							</div>
						</div>
						<div class="form-group row">
							<label for="email" class="col-3 col-form-label"><strong>Email</strong></label>
							<div class="col-9">
								<input type="text" class="form-control" id="txtEmail"
									name="email" placeholder="${usuario.getEmail() }"
									value="${usuario.getEmail() }">
							</div>
						</div>
						<div class="form-group row">
							<label for="telefono" class="col-3 col-form-label"><strong>Telefono</strong></label>
							<div class="col-9">
								<input type="text" class="form-control" id="txtTelefono"
									name="telefono" placeholder="${usuario.getTelefono() }"
									value="${usuario.getTelefono() }">
							</div>
						</div>
						<div class="col-auto">
							<button type="submit" class="btn btn-success" id="btnEditar">Editar</button>
						</div>
					</form>
				</div>
			</div>
			<div id="clave" class="row">
				<div class="col-2">
					<a type="button" onclick="cambiarVisibilidadForm()">Cambiar
						clave</a>
				</div>
				<div id="formCambiarClave" class="col" style="visibility: hidden">
					<form method="get" action="ControladorUsuario">
						<input type="hidden" name="usuario"
							value="${usuario.getNombreUsuario()}"> <input
							type="hidden" name="instruccion" value="cambiar_clave">
						<div class="form-group row">
							<label for="claveActual" class="col-3 col-form-label">Clave
								actual</label>
							<div class="col-6">
								<input type="text" class="form-control" id="txtClaveActual"
									name="claveActual">
							</div>
						</div>
						<div class="form-group row">
							<label for="claveNueva" class="col-3 col-form-label">Clave
								nueva</label>
							<div class="col-6">
								<input type="text" class="form-control" id="txtClaveNueva"
									name="claveNueva">
							</div>
						</div>
						<div class="form-group row offset-2">
							<div class="col-auto">
								<button type="submit" class="btn btn-warning btn-sm">Cambiar</button>
							</div>
							<div class="col-auto">
								<button type="reset" class="btn btn-secondary btn-sm"
									onclick="cambiarVisibilidadForm()">Cancelar</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script src="js/script.js"></script>
<script src="js/jquery-3.5.0.min.js"></script>
<script>
function cambiarVisibilidadForm(){
	if(document.getElementById("formCambiarClave").style.visibility == "hidden")
		document.getElementById("formCambiarClave").style.visibility = "visible";
	else
		document.getElementById("formCambiarClave").style.visibility = "hidden";
}
</script>
</body>
</html>