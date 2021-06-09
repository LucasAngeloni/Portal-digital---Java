<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal fade" id="escribir" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
     <div class="modal-dialog" role="document">
         <div class="modal-content">
             <div class="modal-header">
                 <h5 class="modal-title" id="exampleModalLabel">Crea tu hilo</h5>
                 <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                     <span aria-hidden="true">&times;</span>
                 </button>
             </div>
             
             <form method="get" action="ControladorHilo" accept-charset="utf-8">
				<input type="hidden" name="instruccion" value="publicar">
				
				<div class="modal-body">
					<div class="form-group">
						<label for="txtTitulo" class="col-form-label">Titulo:</label> 
						<input name="titulo" type="text" class="form-control" id="txtTitulo">
					</div>
					<div class="form-group">
						<label for="txtDescripcion" class="col-form-label">Descripción:</label>
						<textarea name="descripcion" class="form-control" id="txtDescripcion"></textarea>
					</div>
					<div class="form-group">
						<label for="cbCategoria" class="col-form-label">Categoria:</label>
						<select name="categoria" class="form-control col-6" id="cbCategoria">
							<c:forEach var="categoria" items="${Categorias }">
								<option>${categoria.getDescripcionCategoria() }</option>
							</c:forEach>

						</select>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-success" id="btnPublicar"
						type="submit">Publicar</button>
				</div>
			</form>
            
         </div>
     </div>
</div>