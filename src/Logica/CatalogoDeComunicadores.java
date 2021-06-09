package Logica;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Datos.ComunicadorData;
import Datos.EsUsuarioAdministradorException;
import Modelo.Comunicador;
import Modelo.Usuario;
import Modelo.BusinessEntity.States;

public class CatalogoDeComunicadores {
	
	private ComunicadorData ComunicadorData;
	
	public CatalogoDeComunicadores() {
		this.ComunicadorData = new ComunicadorData();
	}
	
	public Comunicador getOne(String nom_usu) throws SQLException {
		try {
			return this.ComunicadorData.getOne(nom_usu);
		} catch (SQLException e) {
			throw new SQLException("Error al recuperar el usuario");
		}
	}
	
	public Comunicador getOne(String nom_usu, String clave) throws SQLException, ConnectException, EsUsuarioAdministradorException {
		try {
			return this.ComunicadorData.getOne(nom_usu, clave);
		} catch (SQLException e) {
			throw new SQLException("Error al recuperar el usuario");
		} catch (EsUsuarioAdministradorException e) {
			throw new EsUsuarioAdministradorException();
		}
	}
	
	public void save(Comunicador comunicador) throws SQLException, ExcepcionCampos{
		if(comunicador.getState() == States.NEW) 
			this.validarCampos(comunicador);
		try {
			this.ComunicadorData.save(comunicador);
		} 
		catch (SQLException e) {
			throw new SQLException("Error de conexión");
		}
	}
	
	private void validarCampos(Comunicador usuario) throws ExcepcionCampos{
		
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
    
	public class ExcepcionCampos extends Exception{
		private static final long serialVersionUID = 1L;

		public ExcepcionCampos(String msg) {
			super(msg);
		}
	}

}
