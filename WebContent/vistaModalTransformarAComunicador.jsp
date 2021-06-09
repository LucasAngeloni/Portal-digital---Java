<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal fade" id="comunicador" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
     <div class="modal-dialog" role="document">
         <div class="modal-content">
             <div class="modal-header">
                 <h5 class="modal-title" id="exampleModalLabel">Volverse Comunicador</h5>
                 <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                     <span aria-hidden="true">&times;</span>
                 </button>
             </div>
             
            <div class="modal-body">
             	<form>
                     <div class="form-group">
                         <label for="txtNombre" class="col-form-label">Nombre:</label>
                         <input type="text" class="form-control" id="txtNombre">
                     </div>
                     <div class="form-group">
                         <label for="txtApellido" class="col-form-label">Apellido:</label>
                         <input type="text" class="form-control" id="txtApellido">
                     </div>
                     <div class="form-group">
                         <label for="txtDescripcionComunicador" class="col-form-label">Descripción personal:</label>
                         <textarea class="form-control" id="txtDescripcionComunicador"></textarea>
                     </div>
             	</form>     
	         </div>
             <div class="modal-footer">
             	<button class="btn btn-success" id="btnRegistrar" onclick="registrar()">Registrarse como comunicador</button>            
             </div>
            
         </div>
     </div>
</div>
<script>
function registrar(){
	
	var txtNombre = $("#txtNombre").val();
	var txtDescripcion = $("#txtDescripcionComunicador").val();
	var txtApellido = $("#txtApellido").val();
	
	$.ajax({
		  url: "ControladorUsuario",
		  data: {
		    nombre: txtNombre,
		    descripcion: txtDescripcion,
		    apellido: txtApellido,
		    instruccion: "transformar_a_comunicador"
		  },
		  success: function procesarRespuesta(datosRespuesta){
				       $("#respuesta").html(datosRespuesta);
		           }
		});
}
</script>