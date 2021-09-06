package Datos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Modelo.BusinessEntity;
import Modelo.BusinessEntity.States;
import Modelo.Usuario;

public class UsuarioData{
	
	  public Usuario getOne(String usu,String pass) throws SQLException, EsUsuarioAdministradorException{        
    	  
		  Usuario usuario = null;    
		  PreparedStatement pst = null;
		  ResultSet resultSet = null;
		  
		  ComentarioData cd = new ComentarioData();
		  NotaData nd = new NotaData();
		  HiloData hd = new HiloData();
		  PreferenciasData pd = new PreferenciasData();
		  AporteData ad = new AporteData();
    	  try {            
    		  
    		  String consulta = "Select * from usuarios where nombre_usuario = ? and contraseña = ?";                       
    		
    		  Connection con = FactoryConnection.getInstancia().getConnection(); 
    		  pst = con.prepareStatement(consulta);            
    		  pst.setString(1, usu);            
    		  pst.setString(2, pass);
    		   
    		  resultSet = pst.executeQuery();                        
    		  while(resultSet.next()){   
    			  
    			  if(resultSet.getString("nombre_usuario").equals("admin") && resultSet.getString("contraseña").equals("admin"))
    				  throw new EsUsuarioAdministradorException();
    			  
    			  usuario = new Usuario(resultSet.getString("nombre_usuario"));
    			  usuario.setContraseña(resultSet.getString("contraseña"));
    			  usuario.setTelefono(resultSet.getString("telefono"));
    			  usuario.setEmail(resultSet.getString("email"));
    			  usuario.setFechaNacimiento(resultSet.getDate("fecha_nacimiento").toLocalDate());
    			  usuario.setImagen(resultSet.getString("imagen"));
    			  
    			  usuario.setPreferencias(pd.getPreferenciasUsuario(usu));
    			  usuario.setComentariosQueLeGustan(cd.getComentariosGustados(usuario.getNombreUsuario()));
    			  usuario.setNotasRelevantes(nd.getNotasRelevantes(usuario.getNombreUsuario()));
    			  usuario.setHilosGuardados(hd.getHilosGuardados(usu));
    			  usuario.setAportesRelevantes(ad.getAportesRelevantes(usu));
    	      }
          } 
    	  catch (SQLException e) {        
    		  throw new SQLException(e);        
	      }     	  
    	  finally {
    		  try {
  				if(resultSet != null) resultSet.close();
  				if(pst!=null) pst.close();
  				FactoryConnection.getInstancia().closeConnection();
  		      } 
    	      catch (SQLException e) {
    	    	  throw new SQLException(e);
  		      }
    	  }
    	  return usuario;
	  }
	  
	  public Usuario getOne(String nombreUsuario) throws SQLException{        
    	  
		  Usuario usuario = null;    
		  PreparedStatement pst = null;
		  ResultSet resultSet = null;		  
    	  try {            
    		  
    		  String consulta = "Select * from usuarios where nombre_usuario = ?";                       
    		
    		  pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);            
    		  pst.setString(1, nombreUsuario);
    		   
    		  resultSet = pst.executeQuery();                        
    		  while(resultSet.next()){    			  
    			  usuario = new Usuario(resultSet.getString("nombre_usuario"));
    			  usuario.setContraseña(resultSet.getString("contraseña"));
    			  usuario.setTelefono(resultSet.getString("telefono"));
    			  usuario.setEmail(resultSet.getString("email"));
    			  usuario.setFechaNacimiento(resultSet.getDate("fecha_nacimiento").toLocalDate());
    			  usuario.setImagen(resultSet.getString("imagen"));
    		  }
          } 
    	  catch (SQLException e) {        
    		  throw new SQLException(e);        
    	  }     	  
    	  finally {
    		  try {
    			  if(resultSet != null) resultSet.close();
    			  if(pst != null) pst.close();
    			  FactoryConnection.getInstancia().closeConnection();
    		  }
    		  catch (SQLException e) {
    			  throw new SQLException(e);
    		  }
    	  }
		  return usuario;
	  }
	  
	  public ArrayList<Usuario> getAll() throws SQLException {
		  
		  PreparedStatement pst = null;
		  ResultSet resultSet = null;
		  ArrayList<Usuario> usuarios = new ArrayList<Usuario>(); 
		  PreferenciasData pd = new PreferenciasData();

		  String consulta = "select * from usuarios";
		  try {
			  pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			  resultSet = pst.executeQuery();
			  Usuario usuarioActual;
			  while(resultSet.next()) {

				  try {
					  usuarioActual = new Usuario(resultSet.getString("nombre_usuario"),
							  resultSet.getString("telefono"),
							  resultSet.getDate("fecha_nacimiento").toLocalDate(),
							  resultSet.getString("email"),
							  resultSet.getString("imagen"));

					  usuarioActual.setPreferencias(pd.getPreferenciasUsuario(usuarioActual.getNombreUsuario()));

					  usuarios.add(usuarioActual);
				  }
				  catch(NullPointerException e) {}
			  }

		  } catch (SQLException e) {
			  throw new SQLException(e);
		  }
		  finally {
			  try {
    			  if(resultSet != null) resultSet.close();
    			  if(pst != null) pst.close();
    			  FactoryConnection.getInstancia().closeConnection();
    		  }
    		  catch (SQLException e) {
    			  throw new SQLException(e);
    		  }  
		  }
		  return usuarios;
	  }
	  
	  public void save(Usuario usuario) throws SQLException, IOException {

		  if(usuario.getState() == States.NEW) this.insert(usuario);

		  else if(usuario.getState() == States.DELETED) 
			  delete(usuario.getNombreUsuario());

		  else if(usuario.getState() == States.MODIFIED) this.modify(usuario);

		  usuario.setState(BusinessEntity.States.UNMODIFIED); 
	  }
	  
	  protected void delete(String nombreUsuario) throws SQLException {
		  
		  PreparedStatement pst = null;
		  try {
			  String consulta = "delete from usuarios where nombre_usuario = ?";
			
			  pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			  pst.setString(1,nombreUsuario);
			  
			  pst.executeUpdate();
		  }
		  catch (SQLException e) {        
			  throw new SQLException(e);        
		  } 
		  finally{
			try {
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		  }
	  }
	  
	  protected void insert(Usuario usuario) throws SQLException, IOException {
		  
		  PreparedStatement pst = null;
		  try {
			  String consulta = "insert into usuarios values(?,?,?,null,null,?,null,?,?)";
			  
			  pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			  pst.setString(1, usuario.getNombreUsuario());
			  pst.setString(2, usuario.getTelefono());
			  pst.setString(3, usuario.getEmail());
			  pst.setDate(4, Date.valueOf(usuario.getFechaNacimiento()));
			  pst.setString(5, usuario.getImagen());
			  pst.setString(6, usuario.getContraseña());
			  
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

	  private void modify(Usuario usuario) throws SQLException {
		
		 PreparedStatement pst = null;
		 try {
			 String actualizar_usuario = "UPDATE usuarios SET telefono=?, email=?, fecha_nacimiento=?, imagen=? WHERE nombre_usuario=?";
			 
			 pst = FactoryConnection.getInstancia().getConnection().prepareStatement(actualizar_usuario);
			 pst.setString(1,usuario.getTelefono());
			 pst.setString(2,usuario.getEmail());
			 pst.setDate(3, Date.valueOf(usuario.getFechaNacimiento()));
			 pst.setString(4,usuario.getImagen());
			 pst.setString(5,usuario.getNombreUsuario());
			 
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

	  public void guardarHilo(int id_hilo, String nombre_usuario) throws SQLException {
		 
		 PreparedStatement pst = null;
		 try {
			
			String insertar = "insert into hilos_guardados values(?,?,?)";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(insertar);
			pst.setString(1, nombre_usuario);
			pst.setInt(2, id_hilo);
			pst.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
			
			pst.executeUpdate();
			
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
	 
	  public void desguardarHilo(int id_hilo, String nombre_usuario) throws SQLException {
		 
		  PreparedStatement pst = null;
		  try {

			  String borrar = "delete from hilos_guardados where usuario = ? and hilo_guardado = ?";

			  pst = FactoryConnection.getInstancia().getConnection().prepareStatement(borrar);
			  pst.setString(1, nombre_usuario);
			  pst.setInt(2, id_hilo);

			  pst.executeUpdate();
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
	 
	  public void cambiarClave(String nombreUsuario, String claveNueva) throws SQLException {

		  PreparedStatement pst = null;
		  try {

			  String comandoActualizarClave = "UPDATE usuarios SET contraseña=? where nombre_usuario=?";

			  pst = FactoryConnection.getInstancia().getConnection().prepareStatement(comandoActualizarClave);
			  pst.setString(1, claveNueva);
			  pst.setString(2, nombreUsuario);

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
	  public void lectorAComunicador(String nombre_usuario, String nombre, String apellido, String descripcion) throws SQLException {

		  PreparedStatement pst = null;
		  try {
			  String actualizarUsuario = "UPDATE usuarios SET nombre=?, apellido=?, descripcion=? where nombre_usuario=?";

			  pst = FactoryConnection.getInstancia().getConnection().prepareStatement(actualizarUsuario);
			  pst.setString(1,nombre);
			  pst.setString(2,apellido);
			  pst.setString(3,descripcion);
			  pst.setString(4,nombre_usuario);

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
	  
	  public ArrayList<Usuario> buscarUsuarios(String tipo_filtro, String texto) throws SQLException {

		  ArrayList<Usuario> usuarios_encontrados = new ArrayList<Usuario>();
		  String texto_a_buscar = "%"+texto+"%";
		  String consulta = "select * from usuarios where "+ tipo_filtro + " like ?";
		  PreparedStatement pst = null;
		  ResultSet resultSet = null;
		  try {
			  pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			  pst.setString(1, texto_a_buscar);

			  resultSet = pst.executeQuery();
			  Usuario usuarioActual;
			  while(resultSet.next()) {

				  try {
					  usuarioActual = new Usuario(resultSet.getString("nombre_usuario"),
							  resultSet.getString("telefono"),
							  resultSet.getDate("fecha_nacimiento").toLocalDate(),
							  resultSet.getString("email"),
							  resultSet.getString("imagen"));

					  usuarios_encontrados.add(usuarioActual);
				  }
				  catch(NullPointerException e) { }
			  }

		  } catch (SQLException e) {
			  throw new SQLException(e);
		  }
		  finally {
			  try {
				  if(resultSet != null) resultSet.close();
				  if(pst != null) pst.close();
				  FactoryConnection.getInstancia().closeConnection();
			  }
			  catch (SQLException e) {
				  throw new SQLException(e);
			  }
		  }		  
		  return usuarios_encontrados;
	  }
}
