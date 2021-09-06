package Datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.sql.DataSource;

import Modelo.Categoria;
import Modelo.Hilo;
import Modelo.Nota;


public class HiloData{
	
	public Hilo getOne(int id_hilo) throws SQLException {
		
		ComunicadorData cd = new ComunicadorData();
		NotaData nd = new NotaData();
		AporteData ad = new AporteData();
		CategoriaData cad = new CategoriaData();
		
		Hilo hilo = null;
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		try {
			
			String consulta = "Select * from hilos where id_hilo = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1, id_hilo);
			
			resultSet = pst.executeQuery();			
			while(resultSet.next()) {
				
				hilo = new Hilo(resultSet.getInt("id_hilo"),resultSet.getString("usuario"));
				
				hilo.setRelevanciaHilo(resultSet.getInt("relevancia_hilo"));
				hilo.setTitulo(resultSet.getString("titulo"));
				
				hilo.setComunicador(cd.getComunicadorHilo(id_hilo));
				hilo.setNotas(nd.getNotas(hilo.getIdHilo()));
				hilo.setAportes(ad.getAportesHilo(hilo.getIdHilo()));
				hilo.setCategorias(cad.getCategoriaHilo(hilo.getIdHilo()));
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
		return hilo;
	}
	
	public ArrayList<Hilo> getHilosRelevantes() throws SQLException {
		
		ComunicadorData cd = new ComunicadorData();
		NotaData nd = new NotaData();
		AporteData ad = new AporteData();
		CategoriaData cad = new CategoriaData();
		
		ArrayList<Hilo> hilosImportantes = new ArrayList<Hilo>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		try {
			String consulta = "Select * from hilos order by relevancia_hilo desc";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			resultSet = pst.executeQuery();
			
			int i=0;
			Hilo hiloActual;
			
			while(resultSet.next() && i<30) {
				
				hiloActual = new Hilo(resultSet.getInt("id_hilo"),resultSet.getString("usuario"));
				
				hiloActual.setRelevanciaHilo(resultSet.getInt("relevancia_hilo"));
				hiloActual.setTitulo(resultSet.getString("titulo"));
				
				hiloActual.setNotas(nd.getNotas(hiloActual.getIdHilo()));
				hiloActual.setComunicador(cd.getComunicadorHilo(hiloActual.getIdHilo()));
				hiloActual.setAportes(ad.getAportesHilo(hiloActual.getIdHilo()));
				hiloActual.setCategorias(cad.getCategoriaHilo(hiloActual.getIdHilo()));
				
				hilosImportantes.add(hiloActual);
				i++;
			}
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
				//FactoryConnection.getInstancia().closeConnection(con);
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return hilosImportantes;
	}
	
	public ArrayList<Hilo> getHilosMasRecientes(LocalDateTime fecha) throws SQLException{
		
		ArrayList<Hilo> hilosRecientes = new ArrayList<Hilo>();
		String consulta = "select distinct(hilos.id_hilo), usuario, relevancia_hilo, nro_aportes,titulo from hilos\r\n" + 
				"inner join notas on notas.id_hilo = hilos.id_hilo\r\n" + 
				"where fecha_publicacion <= ? \r\n" + 
				"order by fecha_publicacion desc";
		
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		try{
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setTimestamp(1, Timestamp.valueOf(fecha));

			resultSet = pst.executeQuery();
			Hilo hiloActual;
			NotaData nd = new NotaData();
			while(resultSet.next()) {

				hiloActual = new Hilo(resultSet.getInt("id_hilo"), resultSet.getString("usuario"));
				hiloActual.setRelevanciaHilo(resultSet.getInt("relevancia_hilo"));
				hiloActual.setNroAportes(resultSet.getInt("nro_aportes"));
				hiloActual.setTitulo(resultSet.getString("titulo"));

				hiloActual.setNotas(nd.getNotas(hiloActual.getIdHilo()));
				hilosRecientes.add(hiloActual);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally{
			try {
				if(pst != null) pst.close();
				if(resultSet != null) resultSet.close();
				FactoryConnection.getInstancia().closeConnection();
			}
			catch(SQLException e) {
				throw new SQLException(e);
			}
		}
		
		return hilosRecientes;
	}
	
	public ArrayList<Hilo> getHilosGuardados(String nombreUsuario) throws SQLException{
		  
		  ComunicadorData cd = new ComunicadorData();
		  NotaData nd = new NotaData();
		  AporteData ad = new AporteData();
		  CategoriaData cad = new CategoriaData();
		  
		  ArrayList<Hilo> hilos_guardados = new ArrayList<Hilo>();
		  PreparedStatement pst = null;
		  ResultSet resultSet = null;
		  try {
			  
			  String consulta = "select id_hilo,relevancia_hilo,nro_aportes,titulo from hilos\r\n" + 
			  		"inner join hilos_guardados h_g on id_hilo = hilo_guardado\r\n" + 
			  		"where h_g.usuario = ?";
			  
			  pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			  pst.setString(1, nombreUsuario);
			  
			  resultSet = pst.executeQuery();
			  Hilo hiloActual;
			  while(resultSet.next()) {
				  
				  hiloActual = new Hilo(resultSet.getInt("id_hilo"),nombreUsuario);
				
				  hiloActual.setRelevanciaHilo(resultSet.getInt("relevancia_hilo"));
				  hiloActual.setTitulo(resultSet.getString("titulo"));
				  
				  hiloActual.addNota(nd.getNotaPrincipal(hiloActual.getIdHilo()));
				  hiloActual.setComunicador(cd.getComunicadorHilo(hiloActual.getIdHilo()));
				  hiloActual.setAportes(ad.getAportesHilo(hiloActual.getIdHilo()));
				  hiloActual.setCategorias(cad.getCategoriaHilo(hiloActual.getIdHilo()));
				  
				  hilos_guardados.add(hiloActual);
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
		 return hilos_guardados;
	  }

	public ArrayList<Hilo> getHilos(String texto) throws SQLException{
		
		ComunicadorData cd = new ComunicadorData();
		NotaData nd = new NotaData();
		AporteData ad = new AporteData();
		CategoriaData cad = new CategoriaData();
		
		ArrayList<Hilo> hilos_encontrados = new ArrayList<Hilo>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		try {
			
			String texto_a_buscar = "%"+texto+"%"; 
			String consulta = "Select * from hilos where titulo like ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setString(1, texto_a_buscar);
			
			resultSet = pst.executeQuery();
			int i=0;
			Hilo hiloActual;			
			while(resultSet.next() && i<30) {
				
				hiloActual = new Hilo(resultSet.getInt("id_hilo"),resultSet.getString("usuario"));
				
				hiloActual.setRelevanciaHilo(resultSet.getInt("relevancia_hilo"));
				hiloActual.setTitulo(resultSet.getString("titulo"));
				
				hiloActual.addNota(nd.getNotaPrincipal(hiloActual.getIdHilo()));
				hiloActual.setComunicador(cd.getComunicadorHilo(hiloActual.getIdHilo()));
				hiloActual.setAportes(ad.getAportesHilo(hiloActual.getIdHilo()));
				hiloActual.setCategorias(cad.getCategoriaHilo(hiloActual.getIdHilo()));
				
				hilos_encontrados.add(hiloActual);
				i++;
			}
		}
		catch(Exception e) {
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
		return hilos_encontrados;
	}
	
	public ArrayList<Hilo> buscarHilosComunicador(String texto) throws SQLException{
		
		ComunicadorData cd = new ComunicadorData();
		NotaData nd = new NotaData();
		
		ArrayList<Hilo> hilos_encontrados = new ArrayList<Hilo>();
			
		String texto_a_buscar = "%"+texto+"%"; 
		String consulta = "Select * from hilos where usuario like ?";

		PreparedStatement pst = null;
		ResultSet resultSet = null;
		try {
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setString(1, texto_a_buscar);

			resultSet = pst.executeQuery();
			int i=0;
			Hilo hiloActual;			
			while(resultSet.next() && i<30) {

				hiloActual = new Hilo(resultSet.getInt("id_hilo"),resultSet.getString("usuario"));

				hiloActual.setRelevanciaHilo(resultSet.getInt("relevancia_hilo"));
				hiloActual.setTitulo(resultSet.getString("titulo"));
				hiloActual.addNota(nd.getNotaPrincipal(hiloActual.getIdHilo()));
				hiloActual.setComunicador(cd.getComunicadorHilo(hiloActual.getIdHilo()));

				hilos_encontrados.add(hiloActual);
				i++;
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(resultSet != null) resultSet.close();
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return hilos_encontrados;
	}

	public ArrayList<Hilo> getHilosDeUnComunicador(String comunicador) throws SQLException{
		
		ComunicadorData cd = new ComunicadorData();
		NotaData nd = new NotaData();
		CategoriaData cad = new CategoriaData();
		
		ArrayList<Hilo> hilos_encontrados = new ArrayList<Hilo>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		String consulta = "Select * from hilos where usuario = ?";
		try {
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setString(1, comunicador);
			
			resultSet = pst.executeQuery();
			Hilo hiloActual;			
			while(resultSet.next()) {
				
				hiloActual = new Hilo(resultSet.getInt("id_hilo"),resultSet.getString("usuario"));
				
				hiloActual.setRelevanciaHilo(resultSet.getInt("relevancia_hilo"));
				hiloActual.setTitulo(resultSet.getString("titulo"));
				
				hiloActual.addNota(nd.getNotaPrincipal(hiloActual.getIdHilo()));
				hiloActual.setComunicador(cd.getComunicadorHilo(hiloActual.getIdHilo()));
				hiloActual.setNroAportes(resultSet.getInt("nro_aportes"));
				//hiloActual.setAportes(ad.getAportesHilo(hiloActual.getIdHilo()));
				hiloActual.setCategorias(cad.getCategoriaHilo(hiloActual.getIdHilo()));
				
				hilos_encontrados.add(hiloActual);
			}
		}
		catch(Exception e) {
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
		return hilos_encontrados;
	}

	
	public void insert(Hilo hilo) throws SQLException{
		
		PreparedStatement pst = null;
		PreparedStatement pst_categoria = null;
		ResultSet keyResultSet = null;
		try {
			
			String sentencia = "insert into hilos(usuario,relevancia_hilo,nro_aportes,titulo) values(?,?,?,?)";
			
			Connection conexion = FactoryConnection.getInstancia().getConnection(); 
			pst = conexion.prepareStatement(sentencia, PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1,hilo.getComunicador().getNombreUsuario());
			pst.setInt(2,hilo.getRelevanciaHilo());
			pst.setInt(3,hilo.getNroAportes());
			pst.setString(4,hilo.getTitulo());
			
			pst.executeUpdate();
			keyResultSet = pst.getGeneratedKeys();
            if(keyResultSet!=null && keyResultSet.next())
                hilo.setIdHilo(keyResultSet.getInt(1));
            
            String agregar_categoria = "insert into hilos_categorias values(?,?)";
            pst_categoria = conexion.prepareStatement(agregar_categoria);
            
            for(Categoria categoria : hilo.getCategorias()) {
            	pst_categoria.setInt(1, hilo.getIdHilo());
                pst_categoria.setInt(2, categoria.getIdCategoria());
                
                pst_categoria.executeUpdate();	
            }
		} 
		catch (SQLException e) {
			this.delete(hilo);
			throw new SQLException(e);
		}
		finally {
			try {
				if(keyResultSet!=null) keyResultSet.close();
				if(pst!=null) pst.close();
				if(pst_categoria!=null) pst_categoria.close();
				
				FactoryConnection.getInstancia().closeConnection();
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public void delete(Hilo hilo) throws SQLException {
		
		NotaData nd = new NotaData();
		PreparedStatement pst = null;
		try {
			//Borrar notas del hilo
			for(Nota nota : hilo.getNotas())
				nd.delete(nota);
			
			String borrar_hilo = "delete from hilos where id_hilo = ?";
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(borrar_hilo);
			pst.setInt(1,hilo.getIdHilo());
			
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
	
	public void update(Hilo hilo) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String update = "UPDATE hilos SET titulo=? WHERE id_hilo=?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(update);
			pst.setString(1, hilo.getTitulo());
			pst.setInt(2, hilo.getIdHilo());
			
			pst.executeUpdate();
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally {
			if(pst!=null) pst.close();
			FactoryConnection.getInstancia().closeConnection();
		}
	}

	public void delete(int id_hilo) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String borrar_hilo = "delete from hilos where id_hilo = ?";
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(borrar_hilo);
			pst.setInt(1,id_hilo);
			
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
}
