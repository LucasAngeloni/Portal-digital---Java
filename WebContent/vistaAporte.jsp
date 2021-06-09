<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="comunicadorDelAporte" value="${aporte.getComunicador()}" />

<c:url var="linkVerComentarios" value="ControladorComentario">
	<c:param name="instruccion" value="ver_comentarios_aporte"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo() }"></c:param>
	<c:param name="usuario_aporte" value="${comunicadorDelAporte.getNombreUsuario() }"></c:param>
	<c:param name="fecha_aporte" value="${aporte.getFechaPublicacion() }"></c:param>
</c:url>
<c:url var="linkPerfil" value="ControladorUsuario">
	<c:param name="instruccion" value="perfil"></c:param>
	<c:param name="usu" value="${comunicadorDelAporte.getNombreUsuario() }"></c:param>
</c:url>
<c:url var="linkEliminarAporte" value="ControladorAporte">
	<c:param name="instruccion" value="eliminar_aporte"></c:param>
	<c:param name="fecha_aporte" value="${aporte.getFechaPublicacion()}"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
</c:url>
<c:url var="linkVistaModificarAporte" value="ControladorAporte">
	<c:param name="instruccion" value="vista_modificar_aporte"></c:param>
	<c:param name="id_hilo" value="${hilo.getIdHilo()}"></c:param>
	<c:param name="fecha_aporte" value="${aporte.getFechaPublicacion() }"></c:param>
</c:url>
<c:url var="linkRelevarAporte" value="ControladorAporte">
	<c:param name="instruccion" value="relevar_aporte"></c:param>
	<c:param name="id_hilo" value="${aporte.getIdHilo()}"></c:param>
	<c:param name="fecha_aporte" value="${aporte.getFechaPublicacion()}"></c:param>
	<c:param name="comunicador_aporte" value="${comunicadorDelAporte.getNombreUsuario()}"></c:param>
</c:url>
<c:url var="linkQuitarRelevancia" value="ControladorAporte">
	<c:param name="instruccion" value="quitar_relevancia_aporte"></c:param>
	<c:param name="id_hilo" value="${aporte.getIdHilo()}"></c:param>
	<c:param name="fecha_aporte" value="${aporte.getFechaPublicacion()}"></c:param>
	<c:param name="comunicador_aporte" value="${comunicadorDelAporte.getNombreUsuario()}"></c:param>
</c:url>

<div class="row aporte">
	<div class="col">
		<div class="row datos-aporte">
			<div class="col-2">
				<a href="${linkPerfil }"> <c:choose>
						<c:when test="${comunicadorDelAporte.getImagen() != null }">
							<img src="${comunicadorDelAporte.getImagen()}"
								class="img-fluid rounded"
								title="${comunicadorDelAporte.getNombreUsuario()}" />
						</c:when>
						<c:otherwise>
							<img id="imagen-aporte"
								src="imgs/usuarios/imagen_por_defecto.png"
								class="img-fluid rounded"
								title="${comunicadorDelAporte.getNombreUsuario()}" />
						</c:otherwise>
					</c:choose>
				</a>
			</div>
			<div class="col-6 descripcion">
				<c:choose>
					<c:when test="${modificar_aporte}">
						<div class="row">
							<div class="col-12">
								<form class="form" id="formDescripcion" name="formDescripcion"
									method="post" action="ControladorAporte">
									<input type="hidden" name="instruccion"
										value="modificar_aporte"> <input type="hidden"
										name="fecha_aporte" value="${aporte.getFechaPublicacion() }">
									<input type="hidden" name="id_hilo"
										value="${hilo.getIdHilo() }">
									<textarea class="form-control" name="descripcion_aporte">${aporte.getDescripcion() }</textarea>
									<div class="col">
										<button type="submit" class="btn btn-success">
											Editar
											<svg xmlns="http://www.w3.org/2000/svg" width="16"
												height="16" fill="white" class="bi bi-check-square"
												viewBox="0 0 16 16">
  <path
													d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z" />
  <path
													d="M10.97 4.97a.75.75 0 0 1 1.071 1.05l-3.992 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425a.235.235 0 0 1 .02-.022z" />
</svg>
										</button>
									</div>
								</form>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<p>${aporte.getDescripcion()}</p>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="col-2 tiempo">
				<p>hace ${aporte.cantidadDeTiempo()}</p>
			</div>
			<div class="col-2">
				<c:if
					test="${comunicadorDelAporte.getNombreUsuario() == usuario.getNombreUsuario() }">
					<div class="btn-group me-2" role="group" aria-label="Second group">
						<a href="${linkEliminarAporte}" type="button"
							class="btn btn-outline-info btn-sm" title="Eliminar aporte">
							<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
								fill="White" class="bi bi-trash" viewBox="0 0 16 16">
                     <path
									d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z" />
                     <path fill-rule="evenodd"
									d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z" />
                    </svg>
						</a> <a type="button" class="btn btn-outline-info btn-sm"
							href="${linkVistaModificarAporte }" title="Modificar aporte">
							<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
								fill="White" class="bi bi-pencil-fill" viewBox="0 0 16 16">
                            <path
									d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z" />
                        </svg>
						</a>
					</div>
				</c:if>
			</div>
		</div>

		<div class="row">
			<div class="col-3"></div>
			<div class="col-2">
				<label id="relevancia">${aporte.getRelevancia()}</label>
				<c:if test="${usuario != null }">
					<c:choose>
						<c:when test="${usuario.validarRelevanciaAporte(aporte)}">
							<a href="${linkQuitarRelevancia }" class="btn btn-default btn-sm"
								id="btnQuitarRelevancia"> <svg width="1em" height="1em"
									viewBox="0 0 16 16" class="bi bi-heart-fill" fill="Yellow"
									xmlns="http://www.w3.org/2000/svg">
						  <path fill-rule="evenodd"
										d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z" />
						</svg>
							</a>
						</c:when>
						<c:otherwise>
							<a href="${linkRelevarAporte }" class="btn btn-default btn-sm"
								id="btnRelevarNota"> <svg width="1em" height="1em"
									viewBox="0 0 16 16" class="bi bi-heart" fill="Yellow"
									xmlns="http://www.w3.org/2000/svg">
						    <path fill-rule="evenodd"
										d="M8 2.748l-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z" />
						</svg>
							</a>
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${usuario == null }">
					<a type="button" data-toggle="modal" data-target="#registrarse"
						class="btn btn-default btn-sm" id="btnRelevarNota"> <svg
							width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-heart"
							fill="Yellow" xmlns="http://www.w3.org/2000/svg">
						    <path fill-rule="evenodd"
								d="M8 2.748l-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z" />
						</svg>
					</a>

				</c:if>
			</div>
			<div class="col-1"></div>
			<div class="col">
				<label>${aporte.getCantComentarios()}</label> <a
					href="${linkVerComentarios }"> <svg width="1em" height="1em"
						viewBox="0 0 16 16" class="bi bi-chat" fill="White"
						xmlns="http://www.w3.org/2000/svg">
  					<path fill-rule="evenodd"
							d="M2.678 11.894a1 1 0 0 1 .287.801 10.97 10.97 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8.06 8.06 0 0 0 8 14c3.996 0 7-2.807 7-6 0-3.192-3.004-6-7-6S1 4.808 1 8c0 1.468.617 2.83 1.678 3.894zm-.493 3.905a21.682 21.682 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a9.68 9.68 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9.06 9.06 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105z" />
				</svg>
				</a>
			</div>
		</div>
	</div>
</div>