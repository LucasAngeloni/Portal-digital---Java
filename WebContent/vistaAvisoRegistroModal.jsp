<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal fade" id="registrarse" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
     <div class="modal-dialog" role="document">
         <div class="modal-content">
             <div class="modal-header">
                 <h5 class="modal-title" id="exampleModalLabel">Regístrate</h5>
                 <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                     <span aria-hidden="true">&times;</span>
                 </button>
             </div>
             
             <form accept-charset="utf-8">
				<div class="modal-body">
					<div class="form-group">
						<label for="txtTitulo" class="col-form-label">Para realizar esta acción debes crearte una cuenta</label>
					</div>
				</div>
				<div class="modal-footer">
					<a href="preRegistroUsuario.jsp" class="btn btn-success">Crear una cuenta</a>
				</div>
			</form>
            
         </div>
     </div>
</div>