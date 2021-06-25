<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="comunicador" value="${hilo.getComunicador() }" />
<c:set var="notaPrincipal" value="${hilo.getNota(1)}" />

<c:url var="linkAbrirHilo" value="ControladorHilo">
	<c:param name="instruccion" value="abrir_hilo"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo() }"></c:param>
</c:url>
<c:url var="linkRelevarNota" value="ControladorNota">
	<c:param name="instruccion" value="relevar_nota"></c:param>
	<c:param name="hilo" value="${hilo.getIdHilo()}"></c:param>
	<c:param name="fecha_publicacion" value="${notaPrincipal.getFechaPublicacion() }"></c:param>
</c:url>
<c:url var="linkQuitarRelevancia" value="ControladorNota">
	<c:param name="instruccion" value="quitar_relevancia"></c:param>
	<c:param name="hilo" value="${hilo.getIdHilo()}"></c:param>
	<c:param name="fecha_publicacion" value="${notaPrincipal.getFechaPublicacion() }"></c:param>
</c:url>
<c:url var="linkGuardarHilo" value="ControladorHilo">
	<c:param name="instruccion" value="guardar_hilo"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
</c:url>
<c:url var="linkDesguardarHilo" value="ControladorHilo">
	<c:param name="instruccion" value="desguardar_hilo"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
</c:url>
<c:url var="linkVerAportes" value="ControladorAporte">
	<c:param name="instruccion" value="ver_aportes"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo() }"></c:param>
</c:url>
<c:url var="linkVerComentarios" value="ControladorComentario">
	<c:param name="instruccion" value="ver_comentarios"></c:param>
	<c:param name="fecha_publicacion" value="${notaPrincipal.getFechaPublicacion() }"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo() }"></c:param>
</c:url>
<c:url var="linkPerfil" value="ControladorUsuario">
	<c:param name="instruccion" value="perfil"></c:param>
	<c:param name="usu" value="${comunicador.getNombreUsuario() }"></c:param>
</c:url>
<c:url var="linkAgregarNota" value="ControladorNota">
	<c:param name="instruccion" value="agregar_nota"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
</c:url>
<c:url var="linkVistaModificarNota" value="ControladorNota">
	<c:param name="instruccion" value="vista_modificar_nota"></c:param> 
	<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
	<c:param name="fecha_publicacion" value="${notaPrincipal.getFechaPublicacion() }"></c:param>
</c:url>
<c:url var="linkEliminarHilo" value="ControladorHilo">
	<c:param name="instruccion" value="eliminar_hilo"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
</c:url>
							
<div class="row hilo">
	<div class="col-2">
		<a href="${linkPerfil }"> 
		<c:choose>
				<c:when test="${comunicador.getImagen() != null }">
					<img id="imagen-usuario" src="${comunicador.getImagen()}"
						class="img-fluid rounded"
						title="${comunicador.getNombreUsuario()}" />
				</c:when>
				<c:otherwise>
					<img id="imagen-usuario" src="imgs/usuarios/imagen_por_defecto.png"
						class="img-fluid rounded"
						title="${comunicador.getNombreUsuario()}" />
				</c:otherwise>
			</c:choose>
		</a>
	</div>

	<div class="col-10">
		<div id="datos-hilo" class="row">
			<div class="col-8 titulo">
				<c:choose>
					<c:when test="${modificar_nota != null && modificar_nota}">
						<form class="form" id="formDescripcion" name="formDescripcion"
							method="get" action="ControladorHilo" accept-charset="utf-8">

							<input type="hidden" name="instruccion" value="modificar_titulo">
							<input type="hidden" name="id_hilo" value="${hilo.getIdHilo() }">

							<div class="row input-group mb-3">
								<input type="text" class="form-control" name="titulo"
									aria-describedby="button-modificar" value="${hilo.getTitulo()}">
								<button type="submit" class="btn btn-success"
									id="button-modificar">Modificar</button>
							</div>
						</form>
					</c:when>
					<c:otherwise>
						<p>${hilo.getTitulo()}</p>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="col fecha">
				<p>hace ${notaPrincipal.cantidadDeTiempo()}</p>
			</div>

			<div class="col">
				<c:if
					test="${comunicador.getNombreUsuario() == usuario.getNombreUsuario() }">
					<div class="btn-group me-2" role="group" aria-label="Second group">
						<a type="button" class="btn btn-outline-info btn-sm"
							href="${linkAgregarNota }" title="Agregar una nota"> <svg
								xmlns="http://www.w3.org/2000/svg" width="16" height="16"
								fill="White" class="bi bi-plus-circle-fill" viewBox="0 0 16 16">
                     <path
									d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM8.5 4.5a.5.5 0 0 0-1 0v3h-3a.5.5 0 0 0 0 1h3v3a.5.5 0 0 0 1 0v-3h3a.5.5 0 0 0 0-1h-3v-3z" />
                </svg>
						</a> <a type="button" class="btn btn-outline-info btn-sm"
							href="${linkEliminarHilo }" title="Eliminar hilo"> <svg
								xmlns="http://www.w3.org/2000/svg" width="16" height="16"
								fill="White" class="bi bi-trash" viewBox="0 0 16 16">
                     <path
									d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z" />
                     <path fill-rule="evenodd"
									d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z" />
                    </svg></a> <a type="button"
							class="btn btn-outline-info btn-sm"
							href="${linkVistaModificarNota }" title="Modificar"> <svg
								xmlns="http://www.w3.org/2000/svg" width="16" height="16"
								fill="White" class="bi bi-pencil-fill" viewBox="0 0 16 16">
                            <path
									d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z" />
                        </svg>
						</a>

					</div>
				</c:if>
			</div>
		</div>

		<div id="datos-nota" class="row">
			<c:choose>
				<c:when test="${modificar_nota != null && modificar_nota}">
					<div class="col">
						<form class="form" id="formDescripcion" name="formDescripcion"
							method="get" action="ControladorNota" accept-charset="utf-8">
							<input type="hidden" name="instruccion" value="modificar_nota">
							<input type="hidden" name="fecha_publicacion" value="${nota.getFechaPublicacion() }">
							<input type="hidden" name="id_hilo" value="${hilo.getIdHilo() }">

							<div class="row input-group mb-3 ">
								<textarea class="form-control" name="descripcion_nota"
									aria-describedby="button-modificar">${notaPrincipal.getDescripcion()}</textarea>
								<button type="submit" class="btn btn-success"
									id="button-modificar">Modificar</button>
							</div>
						</form>
					</div>
				</c:when>
				<c:otherwise>
					<div class="col">
						<a href="${linkAbrirHilo }" id="linkAbrirHilo" class="text-dark">
							<p>${notaPrincipal.getDescripcion()}</p>
						</a>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="row">
			<div class="col-3">
				<c:if test="${usuario != null }">
					<form class="form-inline justify-content-center"
						id="formRelevancia" name="formRelevancia">
						<label id="relevancia">${hilo.relevanciaHilo}</label>
						<c:choose>
							<c:when test="${usuario.validarNotaRelevante(notaPrincipal)}">
								<a href="${linkQuitarRelevancia }"
									class="btn btn-default btn-sm" id="btnQuitarRelevancia"
									title="Ya no me parece relevante"> <svg width="1em"
										height="1em" viewBox="0 0 16 16" class="bi bi-heart-fill"
										fill="Yellow" xmlns="http://www.w3.org/2000/svg">
								  <path fill-rule="evenodd"
											d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z" />
								</svg>
								</a>
							</c:when>
							<c:otherwise>
								<a href="${linkRelevarNota }" class="btn btn-default btn-sm"
									id="btnRelevarNota" title="Me parece relevante"> <svg
										width="1em" height="1em" viewBox="0 0 16 16"
										class="bi bi-heart" fill="Yellow"
										xmlns="http://www.w3.org/2000/svg">
								  <path fill-rule="evenodd"
											d="M8 2.748l-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z" />
								</svg>
								</a>
							</c:otherwise>
						</c:choose>
					</form>
				</c:if>
				<c:if test="${usuario == null }">
					<label id="relevancia">${hilo.relevanciaHilo}</label>
					<a type="button" data-toggle="modal" data-target="#registrarse"
						class="btn btn-default btn-sm" id="btnRelevarNota"
						title="Me parece relevante"> <svg width="1em" height="1em"
							viewBox="0 0 16 16" class="bi bi-heart" fill="Yellow"
							xmlns="http://www.w3.org/2000/svg">
								  <path fill-rule="evenodd"
								d="M8 2.748l-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z" />
								</svg>
					</a>

				</c:if>
			</div>
			<div class="col-3" style="margin-left: 10%">
				<label>${hilo.getNroAportes()}</label> <a
					class="btn btn-default btn-sm" href="${linkVerAportes }"
					title="Ver los aportes"> <svg width="1em" height="1em"
						viewBox="0 0 16 16" class="bi bi-chat-square-dots-fill"
						fill="White" xmlns="http://www.w3.org/2000/svg">
	  					<path fill-rule="evenodd"
							d="M0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2h-2.5a1 1 0 0 0-.8.4l-1.9 2.533a1 1 0 0 1-1.6 0L5.3 12.4a1 1 0 0 0-.8-.4H2a2 2 0 0 1-2-2V2zm5 4a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm3 1a1 1 0 1 0 0-2 1 1 0 0 0 0 2z" />
					</svg>
				</a>
			</div>
			<div>
				<label>${notaPrincipal.getCantComentarios()}</label> <a
					href="${linkVerComentarios }" class="btn btn-default btn-sm"
					title="Comentarios"> <svg width="1em" height="1em"
						viewBox="0 0 16 16" class="bi bi-chat" fill="White"
						xmlns="http://www.w3.org/2000/svg">
  					<path fill-rule="evenodd"
							d="M2.678 11.894a1 1 0 0 1 .287.801 10.97 10.97 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8.06 8.06 0 0 0 8 14c3.996 0 7-2.807 7-6 0-3.192-3.004-6-7-6S1 4.808 1 8c0 1.468.617 2.83 1.678 3.894zm-.493 3.905a21.682 21.682 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a9.68 9.68 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9.06 9.06 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105z" />
				</svg>
				</a>
			</div>
			<div class="btnGuardado">
				<c:if test="${usuario != null }">
					<c:choose>
						<c:when test="${usuario.validarHiloGuardado(hilo.getIdHilo()) }">
							<a href="${linkDesguardarHilo }" class="btn btn-default btn-sm"
								title="Dejar de tener guardado el hilo"> <svg width="1.3em"
									height="1.3em" viewBox="0 0 16 16" class="bi bi-bookmark-dash"
									fill="White" xmlns="http://www.w3.org/2000/svg">
								<path fill-rule="evenodd"
										d="M11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5zM4.5 2a.5.5 0 0 0-.5.5v11.066l4-2.667 4 2.667V8.5a.5.5 0 0 1 1 0v6.934l-5-3.333-5 3.333V2.5A1.5 1.5 0 0 1 4.5 1h4a.5.5 0 0 1 0 1h-4z" />
							</svg>
							</a>
						</c:when>
						<c:otherwise>
							<a href="${linkGuardarHilo }" class="btn btn-default btn-sm"
								title="Guardar hilo"> <svg width="1.3em" height="1.3em"
									viewBox="0 0 16 16" class="bi bi-bookmark-plus" fill="White"
									xmlns="http://www.w3.org/2000/svg">
								<path fill-rule="evenodd"
										d="M4.5 2a.5.5 0 0 0-.5.5v11.066l4-2.667 4 2.667V8.5a.5.5 0 0 1 1 0v6.934l-5-3.333-5 3.333V2.5A1.5 1.5 0 0 1 4.5 1h4a.5.5 0 0 1 0 1h-4zm9-1a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1 0-1H13V1.5a.5.5 0 0 1 .5-.5z" />
								<path fill-rule="evenodd"
										d="M13 3.5a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0v-2z" />
							</svg>
							</a>
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${usuario == null }">
					<a type="button" data-toggle="modal" data-target="#registrarse"
						class="btn btn-default btn-sm" title="Guardar hilo"> <svg
							width="1.3em" height="1.3em" viewBox="0 0 16 16"
							class="bi bi-bookmark-plus" fill="White"
							xmlns="http://www.w3.org/2000/svg">
								<path fill-rule="evenodd"
								d="M4.5 2a.5.5 0 0 0-.5.5v11.066l4-2.667 4 2.667V8.5a.5.5 0 0 1 1 0v6.934l-5-3.333-5 3.333V2.5A1.5 1.5 0 0 1 4.5 1h4a.5.5 0 0 1 0 1h-4zm9-1a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1 0-1H13V1.5a.5.5 0 0 1 .5-.5z" />
								<path fill-rule="evenodd"
								d="M13 3.5a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0v-2z" />
							</svg>
					</a>
				</c:if>
			</div>
		</div>
	</div>	
</div>