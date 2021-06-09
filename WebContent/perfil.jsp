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
<c:url var="linkEliminarUsuario" value="ControladorUsuario">
	<c:param name="instruccion" value="eliminar_usuario"></c:param>
</c:url>
<body>
	<div class="row">
		<header class="menu">
			<jsp:include page="menu.jsp"></jsp:include>
			<!-- MENU DE OPCIONES -->
		</header>
		<div class="col-8 perfil">
			<div class="row titulo">
				<div class="col-2">
					<h4>
						<strong>Perfil</strong>
					</h4>
				</div>
				<div class="col-7"></div>
				<a class="btn btn-sm btn-success" href="editarPerfil.jsp">Editar
					perfil</a> 
				<a class="btn btn-sm btn-danger" href="${linkEliminarUsuario }">Eliminar
					usuario</a>
			</div>
			<div class="row">
				<c:if test="${Info != null }">
					<div class="alert alert-success" role="alert">${Info }</div>
				</c:if>
				<c:if test="${Error != null }">
					<div class="alert alert-danger" role="alert">${Error }</div>
				</c:if>
			</div>
			<div class="row datos-principales">
				<div class="col-3">
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
				</div>
				<div class="col-9">
					<h2>
						<strong>${usuario.getNombreUsuario() }</strong>
					</h2>
					<c:choose>
						<c:when test="${usuario.getClass() == 'class Modelo.Usuario' }">
							<h6>Lector</h6>
						</c:when>
						<c:otherwise>
							<h6>Comunicador</h6>
							<div class="row descripcion_comunicador">
								${usuario.getDescripcion() }</div>
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
					<p>
						<strong>Fecha de Nacimiento:</strong>
						${usuario.getFechaNacimiento() }
					</p>
					<p>
						<strong>Email:</strong> ${usuario.getEmail() }
					</p>
					<p>
						<strong>Teléfono:</strong> ${usuario.getTelefono() }
					</p>
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
							<label for="claveActual" class="col-2 col-form-label">Clave
								actual</label>
							<div class="col-6">
								<input type="text" class="form-control" id="txtClaveActual"
									name="claveActual">
							</div>
						</div>
						<div class="form-group row">
							<label for="claveNueva" class="col-2 col-form-label">Clave
								nueva</label>
							<div class="col-6">
								<input type="text" class="form-control" id="txtClaveNueva"
									name="claveNueva">
							</div>
						</div>
						<div class="form-group row offset-2">
							<div class="col-auto">
								<button type="submit" class="btn btn-secondary btn-sm">Cambiar</button>
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
<script src="js/bootstrap.min.js"></script>
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