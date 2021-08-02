package Logica;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Datos.EsUsuarioAdministradorException;
import Datos.PreferenciasData;
import Datos.UsuarioData;
import Modelo.Usuario;
import Modelo.BusinessEntity.States;
import Modelo.Categoria;
import Modelo.Hilo;

public class CatalogoDeUsuarios {
	
	public static void main(String[] args) {
		CatalogoDeUsuarios cu = new CatalogoDeUsuarios();
		
		try {
			cu.validarImagen("aa.jfifs");
			System.out.println("Válida");
		} catch (ExcepcionCampos e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private UsuarioData UsuarioData;	
	
	public CatalogoDeUsuarios() {
		UsuarioData = new UsuarioData();
	}
	
	public Usuario getOne(String nom_usu, String contra) throws SQLException, EsUsuarioAdministradorException {
		
		try {
			return UsuarioData.getOne(nom_usu, contra);
		} catch (SQLException e) {
			throw new SQLException("Error al recuperar al usuario");
		} catch (EsUsuarioAdministradorException e) {
			throw e;
		}
	}
	
	public Usuario getOne(String nom_usu) throws SQLException {
		
		try {
			return (Usuario)UsuarioData.getOne(nom_usu);
		} catch (SQLException e) {
			throw new SQLException("Error al recuperar un usuario");
		}
	}
	
	public ArrayList<Usuario> getAll() throws SQLException{
		
		try {
			return this.UsuarioData.getAll();
		} catch (SQLException e) {
			throw new SQLException("Error al recuperar la lista de usuarios");
		}
	}
	
	public void guardarHilo(Hilo hilo, Usuario usuario) throws SQLException {
				
		try {	
			this.UsuarioData.guardarHilo(hilo.getIdHilo(), usuario.getNombreUsuario());
			usuario.guardarHilo(hilo);
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
	}
	
	public void desguardarHilo(Hilo hilo, Usuario usuario) throws SQLException {
		
		try {
			this.UsuarioData.desguardarHilo(hilo.getIdHilo(), usuario.getNombreUsuario());
			usuario.quitarHiloGuardado(hilo);
		}
		catch (SQLException e) {
			throw new SQLException(e);
		}
	}
	
	public void save(Usuario usuario) throws ExcepcionCampos, SQLException{
		
		if(usuario.getState() == States.NEW) 
			this.validarCampos(usuario);
		if(usuario.getState() == States.MODIFIED)
			this.validarFormatosDatos(usuario);
			
		try {
			this.UsuarioData.save(usuario);
		} 
		catch (SQLException e) {
			if(e.getErrorCode() == 0)
				throw new SQLException("El nombre de usuario elegido ya existe");
			else
				throw new SQLException("Error de conexion");
		}
	}

	public void cambiarImagen(Usuario usuario, String imagen, String direccion_imgs) throws ExcepcionCampos, SQLException, IOException {
		this.validarImagen(imagen);
		String formato = imagen.split("[.]+")[1];
		String imagen_usuario = direccion_imgs + usuario.getNombreUsuario() + "." + formato;
		
		usuario.setImagen(imagen_usuario);
		usuario.setState(States.MODIFIED);
		
		this.save(usuario);
		this.copyFile(imagen,imagen_usuario);
	}
	
	private void validarImagen(String imagen) throws ExcepcionCampos {
		Pattern pat = Pattern.compile(".+(.jpg|.png|.jfif)$");
		Matcher mat = pat.matcher(imagen);
		if(!mat.matches())
			throw new ExcepcionCampos("El formato de la imagen no es válida. Debe ser .jpg, .png o .jfif");
	}
	
	private void validarCampos(Usuario usuario) throws ExcepcionCampos{
		
		if(!usuario.getContraseña().equals(usuario.getRepeticionContraseña()))
			throw new ExcepcionCampos("Las claves ingresadas no coinciden");
		
		if(usuario.getNombreUsuario().equals("") || usuario.getEmail().equals("") || usuario.getTelefono().equals("") || usuario.getImagen().equals("") ||
				usuario.getContraseña().equals(""))
			throw new ExcepcionCampos("Se deben completar todos los campos");
		
		this.validarFormatosDatos(usuario);
		this.validarClave(usuario.getContraseña());
	}
	
	private void validarFormatosDatos(Usuario usuario) throws ExcepcionCampos {
		
		this.validarEmail(usuario.getEmail());
		this.validarTelefono(usuario.getTelefono());
	}
	
	private void validarTelefono(String telefono) throws ExcepcionCampos {
		
		Pattern pat = Pattern.compile("\\d{10}");
		Matcher mat = pat.matcher(telefono);
		if(!mat.matches())
			throw new ExcepcionCampos("Formato del teléfono inválido");
	}

	private void validarClave(String clave) throws ExcepcionCampos {
		
		if(clave.length() < 8 )
			throw new ExcepcionCampos("La clave no puede contener menos de 8 caracteres");
		Pattern pat = Pattern.compile("\\w+");
		Matcher mat = pat.matcher(clave);
		if(!mat.matches())
			throw new ExcepcionCampos("La clave solo puede contener digitos o letras minúsculas o mayúsculas");
	}
	
	private void validarEmail(String email) throws ExcepcionCampos {
		
		Pattern pat = Pattern.compile("[^@]+@[^@]+\\.[a-zA-Z]{2,}");
		Matcher mat = pat.matcher(email);
		if(!mat.matches())
			throw new ExcepcionCampos("Formato de email inválido");
	}

	public void cambiarClave(Usuario usuario, String claveActual, String claveNueva) throws SQLException, ExcepcionCampos {
		if(usuario.getContraseña().equals(claveActual)) {
			this.validarClave(claveNueva);
			try {
				this.UsuarioData.cambiarClave(usuario.getNombreUsuario(),claveNueva);
			} 
			catch (SQLException e) {
				throw new SQLException("Error de conexión");
			}
		}
		else 
			throw new ExcepcionCampos("La clave que se desea modificar no es la correcta");		
	}
	
	public void lectorAComunicador(String nombre_usuario, String nombre, String apellido, String descripcion) throws SQLException {
		try {
			this.UsuarioData.lectorAComunicador(nombre_usuario, nombre, apellido, descripcion);
		} catch (SQLException e) {
			throw new SQLException("Error de conexion");
		}
	}
	
	public void updatePreferencias(Usuario usuario, String accion, ArrayList<Categoria> categorias_hilo) throws SQLException {

		EstrategiaPreferencias est = new EstrategiaPreferencias(usuario.getPreferencias(),categorias_hilo);
		est.updatePreferencias(accion);
	}
	
	public void cerrarSesion(Usuario usuario) throws SQLException {
		PreferenciasData.updatePreferencias(usuario.getPreferencias(),usuario.getNombreUsuario());	
	}
	
	public ArrayList<Usuario> buscarUsuarios(String tipo_filtro, String texto) throws SQLException{
		
		try {
			switch(tipo_filtro) {
			case "usuario":
				return this.UsuarioData.buscarUsuarios("nombre_usuario", texto);
			case "email":
				return this.UsuarioData.buscarUsuarios(tipo_filtro, texto);
			default:
				return null;
			}
		}
		catch(SQLException e) {
			throw new SQLException("ERROR de conexión");
		}
	}
	
	public class ExcepcionCampos extends Exception{
		private static final long serialVersionUID = 1L;

		public ExcepcionCampos(String msg) {
			super(msg);
		}
	}
	
	private void copyFile(String origen, String destino) throws IOException {
		
		Path from = Paths.get(origen);
		Path to = Paths.get(destino);
		
		CopyOption[] options = new CopyOption[] {
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES
		};
		
		Files.copy(from, to, options);
	}
}
