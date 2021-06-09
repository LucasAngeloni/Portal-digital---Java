package Datos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Modelo.Categoria;
import Modelo.Hilo;

public class CategoriaData {

	public int getCantidadCategorias() throws SQLException {
		
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		int cantidad_categorias = 0;
		String consulta = "Select count(*) from categorias";
		try {
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			resultSet = pst.executeQuery();
			if(resultSet.next())
				cantidad_categorias = resultSet.getInt(1);
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return cantidad_categorias;	
	}
	
	public ArrayList<Categoria> getAll() throws SQLException{
		
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		String consulta = "Select * from categorias";
		try {			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			resultSet = pst.executeQuery();
			
			Categoria categoria;
			while(resultSet.next()) {
				categoria = new Categoria(resultSet.getInt("id_categoria"),resultSet.getString("desc_categoria"));
				categoria.setImagenCategoria(resultSet.getString("imagen_categoria"));
				
				categorias.add(categoria);
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
		return categorias;
	}
    public ArrayList<Categoria> getCategoriaHilo(int hilo) throws SQLException {
    	
    	PreparedStatement pst = null;
    	ResultSet resultSet = null;
    	ArrayList<Categoria> categorias = new ArrayList<Categoria>();
    	try {
    		String sentencia = "SELECT cat.id_categoria, desc_categoria from hilos_categorias hc\r\n"
    				+ "right join categorias cat on hc.id_categoria = cat.id_categoria where id_hilo=?";
    		
    		pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
    		pst.setInt(1,hilo);
    		
    		resultSet = pst.executeQuery();
    		Categoria categoria;
    		while(resultSet.next()) {
    			categoria = new Categoria(resultSet.getInt("id_categoria"), resultSet.getString("desc_categoria"));
    			categorias.add(categoria);
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
			} 
			catch (SQLException e) {
				throw new SQLException(e);
			}
		}
    	return categorias;
    }
	
	public ArrayList<Hilo> getHilosCategoria(int id_categoria) throws SQLException{
		
		NotaData nd = new NotaData();
		ComunicadorData cd = new ComunicadorData();
		AporteData ad = new AporteData();
		
		ArrayList<Hilo> hilosCategoria = new ArrayList<Hilo>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		try {
			String consulta = "select hilos.id_hilo,usuario,relevancia_hilo,nro_aportes,titulo from hilos\r\n" + 
					"inner join hilos_categorias hc on hc.id_hilo = hilos.id_hilo\r\n" + 
					"where id_categoria = ?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(consulta);
			pst.setInt(1, id_categoria);
			
			resultSet = pst.executeQuery();
			Hilo hiloActual;
			while(resultSet.next()) {
				
				hiloActual = new Hilo(resultSet.getInt("id_hilo"),resultSet.getString("usuario"));				
				hiloActual.setRelevanciaHilo(resultSet.getInt("relevancia_hilo"));
				hiloActual.setTitulo(resultSet.getString("titulo"));
				
				hiloActual.addNota(nd.getNotaPrincipal(hiloActual.getIdHilo()));
				hiloActual.setComunicador(cd.getComunicadorHilo(hiloActual.getIdHilo()));
				hiloActual.setAportes(ad.getAportesHilo(hiloActual.getIdHilo()));
				hiloActual.setCategorias(this.getCategoriaHilo(hiloActual.getIdHilo()));
				
				hilosCategoria.add(hiloActual);
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
		return hilosCategoria;
	}
	
	public void delete(int id_categoria) throws SQLException {
		
		String borrar_categoria = "delete from categorias where id_categoria=?";
		PreparedStatement pst = null;
		try {
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(borrar_categoria);
			pst.setInt(1, id_categoria);
			
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst!=null) pst.close();
				FactoryConnection.getInstancia().closeConnection();	
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public void insert(Categoria categoria) throws SQLException {
		
		String agregar_categoria = "insert into categorias(desc_categoria,imagen_categoria) values(?,?)";
		
		PreparedStatement pst = null;
		try {
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(agregar_categoria, PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1,categoria.getDescripcionCategoria());
			pst.setString(2,categoria.getImagenCategoria());
			
			pst.executeUpdate();
			ResultSet keyResultSet = pst.getGeneratedKeys();
			
			if(keyResultSet != null && keyResultSet.next())
				categoria.setIdCategoria(keyResultSet.getInt(1));
			if(keyResultSet!=null) keyResultSet.close();
			
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst!=null) pst.close();		
				FactoryConnection.getInstancia().closeConnection();	
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public void update(Categoria categoria) throws SQLException {
		
		String modificar_categoria = "update categorias set desc_categoria = ?,imagen_categoria = ? where id_categoria=?";
		
		PreparedStatement pst = null;
		try {
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(modificar_categoria);

			pst.setString(1,categoria.getDescripcionCategoria());
			pst.setString(2,categoria.getImagenCategoria());
			pst.setInt(3, categoria.getIdCategoria());

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst!=null) pst.close();		
				FactoryConnection.getInstancia().closeConnection();	
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public ArrayList<Categoria> buscarCategorias(String texto) throws SQLException {
		
		ArrayList<Categoria> categorias_encontradas = new ArrayList<Categoria>();
		
		String texto_a_buscar = "%"+texto+"%";
		String sentencia = "select * from categorias where desc_categoria like ?";
		
		PreparedStatement pst = null;
		try {
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			pst.setString(1, texto_a_buscar);
			
			ResultSet resultSet = pst.executeQuery();
			Categoria categoria;
			while(resultSet.next()) {
				
				categoria = new Categoria(resultSet.getInt("id_categoria"),resultSet.getString("desc_categoria"));
				categoria.setImagenCategoria(resultSet.getString("imagen_categoria"));
				
				categorias_encontradas.add(categoria);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		finally {
			try {
				if(pst!=null) pst.close();		
				FactoryConnection.getInstancia().closeConnection();
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return categorias_encontradas;
	}
}
