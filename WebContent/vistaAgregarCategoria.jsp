<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css">
	
	<title>Crear categoría</title>
</head>
<body>

	<div class="container">
		<div class="row registro">
			<div class="col-12">
				<h3>Crear categoría</h3>
			</div>
			<div class="col-6">
				<form id="formRegistro" name="formRegistro" method="get"
					action="ControladorAdministrador" enctype="multipart/form-data">
					<input type="hidden" name="instruccion" value="agregar_categoria">

					<div class="form-group row">
						<label for="nombre_categoria" class="col-3 col-form-label">Nombre
							de la categoría</label>
						<div class="col-9">
							<input type="text" class="form-control" id="txtNombreCategoria"
								name="nombre_categoria" placeholder="Ej: Deportes"
								value="${nombre_usuario }">
						</div>
					</div>
					<div class="form-group row">
						<label for="imagen_categoria" class="col-4 col-form-label">Imagen
							de la categoria</label> <input type="file" class="col-8"
							name="imagen_categoria" accept=".jpg,.png,.jfif" />
					</div>
					<div class="form-group row">
						<div class="col-auto">
							<button type="submit" class="btn btn-success" id="btnRegistro">Agregar</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>