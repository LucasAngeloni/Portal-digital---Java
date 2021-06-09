<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <link rel="stylesheet" href="css/styles-menu.css">
</head>
<c:url var="linkInicio" value="ControladorVistaPrincipal">
	<c:param name="instruccion" value="inicio"></c:param>
</c:url>
<c:url var="linkCategorias" value="ControladorCategoria">
	<c:param name="instruccion" value="categorias"></c:param>
</c:url>
<c:url var="linkHilosGuardados" value="ControladorVistaPrincipal">
	<c:param name="instruccion" value="hilos_guardados"></c:param>
</c:url>
<c:url var="linkMisPublicaciones" value="ControladorVistaPrincipal">
	<c:param name="instruccion" value="mis_publicaciones"></c:param>
</c:url>
<c:url var="linkPerfil" value="ControladorUsuario">
	<c:param name="instruccion" value="perfil"></c:param>
	<c:param name="usu" value="${hilo.getComunicador().getNombreUsuario() }"></c:param>
</c:url>
<c:url var="linkCerrarSesion" value="ControladorLogin">
	<c:param name="instruccion" value="cerrar_sesion"></c:param>
</c:url>

<div class="col-2">
	<h1 class="logo">Portal</h1>
		
	<ul id="nav-list" class="nav navbar-nav navbar-right">
        <li>
           <a href="${linkInicio }"><span class="glyphicon glyphicon-cutlery"></span><br class="hidden-xs">Inicio</a>
        </li>
        <li>
          <a href="${linkCategorias }"><span class="glyphicon glyphicon-info-sign"></span><br class="hidden-xs">Categorias</a>
        </li>
		<c:if
			test="${usuario.getClass().toString().equals('class Modelo.Comunicador') }">
			<li><a href="${linkMisPublicaciones}"><span class="glyphicon glyphicon-certificate"></span><br
					class="hidden-xs">Mis publicaciones</a></li>
		</c:if>
		<c:if test="${usuario != null }">
			<li><a href="${linkHilosGuardados}"><span
					class="glyphicon glyphicon-certificate"></span><br
					class="hidden-xs">Hilos Guardados</a></li>

			<li><a href="perfil.jsp"><span
					class="glyphicon glyphicon-certificate"></span><br
					class="hidden-xs">Perfil</a></li>
		</c:if>
		
     </ul><!-- #nav-list -->
     <div id="botones">
		<c:if
			test="${usuario.getClass().toString().equals('class Modelo.Comunicador') }">
			<a id="btnEscribir" class="btn btn-warning btn-lg" type="button"
				data-toggle="modal" data-target="#escribir">Escribir</a>
		</c:if>

		<c:if test="${usuario == null }">
			<a id="btnRegistrarse" class="btn btn-warning" type="button"
				href="preRegistroUsuario.jsp">Registrarse</a>
			<br>
			<a id="btnLogin" class="btn btn-warning" type="button"
				href="login.jsp">Iniciar sesión</a>
		</c:if>

		<c:if test="${usuario != null }">
			<a id="btnCerrarSesion" href="${linkCerrarSesion }"> <span
				class="badge bg-secondary">Cerrar sesión</span>
			</a>
		</c:if>
	</div>
</div>
<jsp:include page="vistaAgregarHilo.jsp"></jsp:include>