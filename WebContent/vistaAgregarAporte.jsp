<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal fade" id="agregar_aporte" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">Crear aporte</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>

			<form method="post" action="ControladorAporte">
				<input type="hidden" name="instruccion" value="agregar_aporte"> 
				<input type="hidden" name="id_hilo" value="${hilo.getIdHilo() }">
				<div class="modal-body">
					<div class="form-group">
						<label for="txtTitulo" class="col-form-label"> Hilo:
							${hilo.getTitulo()} </label>
					</div>
					<div class="form-group">
						<label for="txtDescripcion" class="col-form-label">Aporte:</label>
						<textarea name="descripcion" class="form-control"
							id="txtDescripcion"></textarea>
					</div>

				</div>
				<div class="modal-footer">
					<button class="btn btn-success" type="submit">Publicar</button>
				</div>
			</form>

		</div>
	</div>
</div>