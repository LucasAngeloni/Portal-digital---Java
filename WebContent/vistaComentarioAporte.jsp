<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url var="linkMeGusta" value="ControladorComentario">
	<c:param name="instruccion" value="me_gusta"></c:param>
	<c:param name="usuario_comentario"
		value="${comentario.getNombreUsuario() }"></c:param>
	<c:param name="fecha_comentario"
		value="${comentario.getFechaComentario()}"></c:param>
	<c:param name="fecha_publicacion" value="${publicacion.getFechaPublicacion()}"></c:param>
	<c:param name="usuario_aporte" value="${publicacion.getComunicador().getNombreUsuario() }"></c:param>
</c:url>
<c:url var="linkQuitarMeGusta" value="ControladorComentario">
	<c:param name="instruccion" value="quitar_me_gusta"></c:param>
	<c:param name="usuario_comentario"
		value="${comentario.getNombreUsuario() }"></c:param>
	<c:param name="fecha_comentario"
		value="${comentario.getFechaComentario()}"></c:param>
	<c:param name="fecha_publicacion" value="${publicacion.getFechaPublicacion() }"></c:param>
	<c:param name="usuario_aporte" value="${publicacion.getComunicador().getNombreUsuario() }"></c:param>
</c:url>

<div class="comentario">
	<div class="row">
		<c:set var="usuario_comentario" value="${comentario.getUsuario() }" />
		<div class="col-2">
			<a href="${linkPerfil }"> <c:choose>
					<c:when test="${usuario_comentario.getImagen() != null }">
						<img id="imagen-usuario" src="${usuario_comentario.getImagen()}"
							class="img-fluid rounded"
							title="${usuario_comentario.getNombreUsuario()}" />
					</c:when>
					<c:otherwise>
						<img id="imagen-usuario"
							src="imgs/usuarios/imagen_por_defecto.png"
							class="img-fluid rounded"
							title="${usuario_comentario.getNombreUsuario()}" />
					</c:otherwise>
				</c:choose>
			</a>
		</div>
		<div class="col-6 descripcion">
			<p>${comentario.getDescripcionComentario() }</p>
		</div>
		<div class="col-2 fecha">
			<p>${comentario.cantidadDeTiempo() }</p>
		</div>

		<c:if
			test="${usuario.getNombreUsuario() == comentario.getNombreUsuario() }">
			<div class="col-2">
				<form id="formEliminarComentario" name="formEliminarComentario"
					method="get" action="ControladorComentario">
					<input type="hidden" name="instruccion" value="eliminar_comentario">
					<input type="hidden" name="fecha_comentario"
						value="${comentario.getFechaComentario() }">  
					<input
						type="hidden" name="id_hilo" value="${aporte.getIdHilo() }">
					<input type="hidden" name="fecha_publicacion"
						value="${publicacion.getFechaPublicacion() }"> <input
						type="hidden" name="usuario_aporte"
						value="${publicacion.getComunicador().getNombreUsuario() }">
					<button type="submit" class="btn btn-outline-info btn-sm"
						title="Eliminar">
						<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
							fill="White" class="bi bi-trash" viewBox="0 0 16 16">
                     <path
								d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z" />
                     <path fill-rule="evenodd"
								d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z" />
                    </svg>
					</button>
				</form>
			</div>
		</c:if>

	</div>
	<div class="row">
		<div class="col">
			<label>${comentario.getLikes() }</label>
			<c:if test="${usuario != null }">
				<c:choose>
					<c:when
						test="${usuario.meGustaPuesto(comentario.getNombreUsuario(),comentario.getFechaComentario()) }">
						<a href="${linkQuitarMeGusta }" class="btn btn-default btn-sm">
							<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
								fill="White" class="bi bi-hand-thumbs-up-fill"
								viewBox="0 0 16 16">
                                <path
									d="M6.956 1.745C7.021.81 7.908.087 8.864.325l.261.066c.463.116.874.456 1.012.964.22.817.533 2.512.062 4.51a9.84 9.84 0 0 1 .443-.05c.713-.065 1.669-.072 2.516.21.518.173.994.68 1.2 1.273.184.532.16 1.162-.234 1.733.058.119.103.242.138.363.077.27.113.567.113.856 0 .289-.036.586-.113.856-.039.135-.09.273-.16.404.169.387.107.819-.003 1.148a3.162 3.162 0 0 1-.488.9c.054.153.076.313.076.465 0 .306-.089.626-.253.912C13.1 15.522 12.437 16 11.5 16H8c-.605 0-1.07-.081-1.466-.218a4.826 4.826 0 0 1-.97-.484l-.048-.03c-.504-.307-.999-.609-2.068-.722C2.682 14.464 2 13.846 2 13V9c0-.85.685-1.432 1.357-1.616.849-.231 1.574-.786 2.132-1.41.56-.626.914-1.279 1.039-1.638.199-.575.356-1.54.428-2.59z" />
                            </svg>
						</a>
					</c:when>
					<c:otherwise>
						<a href="${linkMeGusta }" class="btn btn-default btn-sm"> <svg
								xmlns="http://www.w3.org/2000/svg" width="16" height="16"
								fill="White" class="bi bi-hand-thumbs-up" viewBox="0 0 16 16">
                                <path
									d="M8.864.046C7.908-.193 7.02.53 6.956 1.466c-.072 1.051-.23 2.016-.428 2.59-.125.36-.479 1.013-1.04 1.639-.557.623-1.282 1.178-2.131 1.41C2.685 7.288 2 7.87 2 8.72v4.001c0 .845.682 1.464 1.448 1.545 1.07.114 1.564.415 2.068.723l.048.03c.272.165.578.348.97.484.397.136.861.217 1.466.217h3.5c.937 0 1.599-.477 1.934-1.064a1.86 1.86 0 0 0 .254-.912c0-.152-.023-.312-.077-.464.201-.263.38-.578.488-.901.11-.33.172-.762.004-1.149.069-.13.12-.269.159-.403.077-.27.113-.568.113-.857 0-.288-.036-.585-.113-.856a2.144 2.144 0 0 0-.138-.362 1.9 1.9 0 0 0 .234-1.734c-.206-.592-.682-1.1-1.2-1.272-.847-.282-1.803-.276-2.516-.211a9.84 9.84 0 0 0-.443.05 9.365 9.365 0 0 0-.062-4.509A1.38 1.38 0 0 0 9.125.111L8.864.046zM11.5 14.721H8c-.51 0-.863-.069-1.14-.164-.281-.097-.506-.228-.776-.393l-.04-.024c-.555-.339-1.198-.731-2.49-.868-.333-.036-.554-.29-.554-.55V8.72c0-.254.226-.543.62-.65 1.095-.3 1.977-.996 2.614-1.708.635-.71 1.064-1.475 1.238-1.978.243-.7.407-1.768.482-2.85.025-.362.36-.594.667-.518l.262.066c.16.04.258.143.288.255a8.34 8.34 0 0 1-.145 4.725.5.5 0 0 0 .595.644l.003-.001.014-.003.058-.014a8.908 8.908 0 0 1 1.036-.157c.663-.06 1.457-.054 2.11.164.175.058.45.3.57.65.107.308.087.67-.266 1.022l-.353.353.353.354c.043.043.105.141.154.315.048.167.075.37.075.581 0 .212-.027.414-.075.582-.05.174-.111.272-.154.315l-.353.353.353.354c.047.047.109.177.005.488a2.224 2.224 0 0 1-.505.805l-.353.353.353.354c.006.005.041.05.041.17a.866.866 0 0 1-.121.416c-.165.288-.503.56-1.066.56z" />
                            </svg>
						</a>
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${usuario == null }">
				<a type="button" data-toggle="modal" data-target="#registrarse"
					class="btn btn-default btn-sm"> <svg
						xmlns="http://www.w3.org/2000/svg" width="16" height="16"
						fill="White" class="bi bi-hand-thumbs-up" viewBox="0 0 16 16">
                                <path
							d="M8.864.046C7.908-.193 7.02.53 6.956 1.466c-.072 1.051-.23 2.016-.428 2.59-.125.36-.479 1.013-1.04 1.639-.557.623-1.282 1.178-2.131 1.41C2.685 7.288 2 7.87 2 8.72v4.001c0 .845.682 1.464 1.448 1.545 1.07.114 1.564.415 2.068.723l.048.03c.272.165.578.348.97.484.397.136.861.217 1.466.217h3.5c.937 0 1.599-.477 1.934-1.064a1.86 1.86 0 0 0 .254-.912c0-.152-.023-.312-.077-.464.201-.263.38-.578.488-.901.11-.33.172-.762.004-1.149.069-.13.12-.269.159-.403.077-.27.113-.568.113-.857 0-.288-.036-.585-.113-.856a2.144 2.144 0 0 0-.138-.362 1.9 1.9 0 0 0 .234-1.734c-.206-.592-.682-1.1-1.2-1.272-.847-.282-1.803-.276-2.516-.211a9.84 9.84 0 0 0-.443.05 9.365 9.365 0 0 0-.062-4.509A1.38 1.38 0 0 0 9.125.111L8.864.046zM11.5 14.721H8c-.51 0-.863-.069-1.14-.164-.281-.097-.506-.228-.776-.393l-.04-.024c-.555-.339-1.198-.731-2.49-.868-.333-.036-.554-.29-.554-.55V8.72c0-.254.226-.543.62-.65 1.095-.3 1.977-.996 2.614-1.708.635-.71 1.064-1.475 1.238-1.978.243-.7.407-1.768.482-2.85.025-.362.36-.594.667-.518l.262.066c.16.04.258.143.288.255a8.34 8.34 0 0 1-.145 4.725.5.5 0 0 0 .595.644l.003-.001.014-.003.058-.014a8.908 8.908 0 0 1 1.036-.157c.663-.06 1.457-.054 2.11.164.175.058.45.3.57.65.107.308.087.67-.266 1.022l-.353.353.353.354c.043.043.105.141.154.315.048.167.075.37.075.581 0 .212-.027.414-.075.582-.05.174-.111.272-.154.315l-.353.353.353.354c.047.047.109.177.005.488a2.224 2.224 0 0 1-.505.805l-.353.353.353.354c.006.005.041.05.041.17a.866.866 0 0 1-.121.416c-.165.288-.503.56-1.066.56z" />
                            </svg>
				</a>
			</c:if>
		</div>
		<c:url var="linkVerSubComentarios" value="ControladorComentario">
			<c:param name="instruccion" value="ver_subcomentarios"></c:param>
			<c:param name="fecha_comentario" value="${comentario.getFechaComentario() }"></c:param>
			<c:param name="fecha_publicacion"
				value="${publicacion.getFechaPublicacion()}"></c:param>
			<c:param name="usuario_aporte"
				value="${publicacion.getComunicador().getNombreUsuario() }"></c:param>
			<c:param name="usuario_comentario"
				value="${comentario.getNombreUsuario() }"></c:param>
		</c:url>
		<div class="col">
			<label>${comentario.getNroSubcomentarios()}</label> <a
				href="${linkVerSubComentarios }" title="Ver respuestas a este comentario"> <svg width="1em" height="1em"
					viewBox="0 0 16 16" class="bi bi-chat" fill="White"
					xmlns="http://www.w3.org/2000/svg">
  					<path fill-rule="evenodd"
						d="M2.678 11.894a1 1 0 0 1 .287.801 10.97 10.97 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8.06 8.06 0 0 0 8 14c3.996 0 7-2.807 7-6 0-3.192-3.004-6-7-6S1 4.808 1 8c0 1.468.617 2.83 1.678 3.894zm-.493 3.905a21.682 21.682 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a9.68 9.68 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9.06 9.06 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105z" />
				</svg>
			</a>
		</div>
	</div>

	<div class="row">
		<c:if test="${usuario != null }">
			<form id="formResponder" name="formResponder" method="post"
				action="ControladorComentario">
				<input type="hidden" name="instruccion" value="responder"> <input
					type="hidden" name="fecha_comentario"
					value="${comentario.getFechaComentario() }"> <input
					type="hidden" name="usuario_comentario"
					value="${comentario.getNombreUsuario() }"> <input
					type="hidden" name="fecha_publicacion"
					value="${publicacion.getFechaPublicacion() }"> <input
					type="hidden" name="usuario_aporte"
					value="${publicacion.getComunicador().getNombreUsuario() }">

				<div class="input-group filtro">
					<input type="text" class="" name="comentario" placeholder="Di algo"
						aria-label="Recipient's username" aria-describedby="btnComentar">
					<button class="btn btn-info" type="submit" id="btnResponder">Responder</button>
				</div>
			</form>
		</c:if>
	</div>
</div>