package Datos;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Modelo.BusinessEntity;
import Modelo.Comunicador;

public class ComunicadorData{
	
	public Comunicador getOne(String nombreUsuario) throws SQLException{
		
		Comunicador comunicador = null;    
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		HiloData hd = new HiloData();
		NotaData nd = new NotaData();
		try {
					  
  		  String consulta = "Select * from usuarios where nombre_usuario = ?";                       
  		
  		  Connection con = FactoryConnection.getInstancia().getConnection(); 
  		  pst = con.prepareStatement(consulta);            
  		  pst.setString(1, nombreUsuario);
  		   
  		  resultSet = pst.executeQuery();                        
  		  while(resultSet.next()){   
  			  
  			  comunicador = new Comunicador(resultSet.getString("nombre_usuario"));
  			  comunicador.setDescripcion(resultSet.getString("descripcion"));
  			  comunicador.setNombre(resultSet.getString("nombre"));
  			  comunicador.setApellido(resultSet.getString("apellido"));
  			  comunicador.setTelefono(resultSet.getString("telefono"));
  			  comunicador.setEmail(resultSet.getString("email"));
  			  comunicador.setFechaNacimiento(resultSet.getDate("fecha_nacimiento").toLocalDate());
  			  comunicador.setImagen(resultSet.getString("imagen"));
  			  
  			  comunicador.setHilos(hd.getHilosDeUnComunicador(nombreUsuario));  			  
  			  comunicador.setNotasRelevantes(nd.getNotasRelevantes(nombreUsuario));
  		  }
	    }
		catch (SQLException e) {        
			throw new SQLException(e);       
	  	}     	  
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst != null)	pst.close();
				FactoryConnection.getInstancia().closeConnection();				
			}
  		  	catch (SQLException e) {
  		  		throw new SQLException(e);
			}
  	    }
	    return comunicador;
	}
    
	public Comunicador getOne(String nombreUsuario, String clave) throws SQLException,ConnectException, EsUsuarioAdministradorException{
		
		Comunicador comunicador = null;    
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		HiloData hd = new HiloData();
		NotaData nd = new NotaData();
		ComentarioData cd = new ComentarioData();
		PreferenciasData pd = new PreferenciasData();
		AporteData ad = new AporteData();
		try {
					  
  		  String consulta = "Select * from usuarios where nombre_usuario = ? and contraseña=?";                       
  		
  		  Connection con = FactoryConnection.getInstancia().getConnection(); 
  		  pst = con.prepareStatement(consulta);            
  		  pst.setString(1, nombreUsuario);
  		  pst.setString(2, clave);
  		   
  		  resultSet = pst.executeQuery();                        
  		  while(resultSet.next()){
  			  if(resultSet.getString("nombre_usuario").equals("admin") && resultSet.getString("contraseña").equals("admin"))
				  throw new EsUsuarioAdministradorException();
  			  
  			  comunicador = new Comunicador(resultSet.getString("nombre_usuario"));
  			  comunicador.setDescripcion(resultSet.getString("descripcion"));
  			  comunicador.setNombre(resultSet.getString("nombre"));
  			  comunicador.setApellido(resultSet.getString("apellido"));
  			  comunicador.setTelefono(resultSet.getString("telefono"));
  			  comunicador.setEmail(resultSet.getString("email"));
  			  comunicador.setFechaNacimiento(resultSet.getDate("fecha_nacimiento").toLocalDate());
  			  comunicador.setImagen(resultSet.getString("imagen"));
  			  
  			  comunicador.setHilos(hd.getHilosDeUnComunicador(nombreUsuario));
  			  comunicador.setComentariosQueLeGustan(cd.getComentariosGustados(comunicador.getNombreUsuario()));
  			  comunicador.setNotasRelevantes(nd.getNotasRelevantes(nombreUsuario));
  			  comunicador.setHilosGuardados(hd.getHilosGuardados(nombreUsuario));
  			  comunicador.setPreferencias(pd.getPreferenciasUsuario(nombreUsuario));
  			  comunicador.setAportesRelevantes(ad.getAportesRelevantes(nombreUsuario));
  		  }
	    }
		catch (SQLException e) {
			throw new SQLException(e);        
	  	}     	  
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst != null)	pst.close();
				FactoryConnection.getInstancia().closeConnection();				
			}
  		  	catch (SQLException e) {
  		  		throw new SQLException(e);
			}
  	    }
	    return comunicador;
	}
	
	public Comunicador getComunicadorHilo(int id_hilo) throws SQLException {
		
		Comunicador comunicador = null;
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		try {
			
			String consulta = "select nombre_usuario,imagen from usuarios\r\n" + 
						"inner join hilos on hilos.usuario = nombre_usuario\r\n" + 
						"where id_hilo = ?";
				
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1, id_hilo);
				
			resultSet = pst.executeQuery();
			while(resultSet.next()) {
				comunicador = new Comunicador(resultSet.getString("nombre_usuario"));
				comunicador.setImagen(resultSet.getString("imagen"));
			}
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally
		{
		    try {
				if(resultSet != null) resultSet.close();
				if(pst != null)	pst.close();
				FactoryConnection.getInstancia().closeConnection();
			}
			catch(SQLException e) {
				throw new SQLException(e);
			}
		}
		return comunicador;
	}
	
	public void save(Comunicador comunicador) throws SQLException {

		if(comunicador.getState() == BusinessEntity.States.NEW) 
			this.insert(comunicador);
		else if(comunicador.getState() == BusinessEntity.States.DELETED) 
			this.delete(comunicador.getNombreUsuario());
		else if(comunicador.getState() == BusinessEntity.States.MODIFIED) 
			this.update(comunicador);

		comunicador.setState(BusinessEntity.States.UNMODIFIED); 
	}

	private void update(Comunicador comunicador) throws SQLException {
		PreparedStatement pst = null;
		try {
			String actualizar_usuario = "UPDATE usuarios SET telefono=?, email=?, nombre=?, apellido=?, fecha_nacimiento=?, descripcion=?, imagen=? WHERE nombre_usuario=?";

			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(actualizar_usuario);
			pst.setString(1,comunicador.getTelefono());
			pst.setString(2,comunicador.getEmail());
			pst.setString(3,comunicador.getNombre());
			pst.setString(4,comunicador.getApellido());
			pst.setDate(5, Date.valueOf(comunicador.getFechaNacimiento()));
			pst.setString(6,comunicador.getDescripcion());
			pst.setString(7,comunicador.getImagen());
			pst.setString(8,comunicador.getNombreUsuario());

			pst.executeUpdate();
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			}
			catch(SQLException e) {
				throw new SQLException(e);
			}
		}

	}

	private void delete(String nombreUsuario) {
		// TODO Auto-generated method stub
		
	}

	protected void insert(Comunicador usuario) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String consulta = "insert into usuarios values(?,?,?,?,?,?,?,?,?)";
			  
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setString(1, usuario.getNombreUsuario());
			pst.setString(2, usuario.getTelefono());
			pst.setString(3, usuario.getEmail());
			pst.setString(4, usuario.getNombre());
			pst.setString(5, usuario.getApellido());
			pst.setDate(6, Date.valueOf(usuario.getFechaNacimiento()));
			pst.setString(7, usuario.getDescripcion());
			pst.setString(8, usuario.getImagen());
			pst.setString(9, usuario.getContraseña());
			  
			pst.executeUpdate();
			PreferenciasData.insertPreferencias(usuario.getPreferencias(), usuario.getNombreUsuario());
		}
		catch (SQLException e) {        
			throw new SQLException(e);
		} 
		finally {
			try {
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
}
