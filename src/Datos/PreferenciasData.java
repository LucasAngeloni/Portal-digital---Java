package Datos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Modelo.Categoria;
import Modelo.Preferencia;

public class PreferenciasData {

	public ArrayList<Preferencia> getPreferenciasUsuario(String nombre_usuario) throws SQLException{
		
		ArrayList<Preferencia> preferencias = new ArrayList<Preferencia>();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		try {
			String sentencia = "SELECT pre.id_categoria, valor_preferencia,desc_categoria FROM preferencias pre left join categorias cat on pre.id_categoria = cat.id_categoria where nombre_usuario=?";
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			pst.setString(1, nombre_usuario);
			
			resultSet = pst.executeQuery();
			Preferencia preferencia;
			Categoria categoria_preferencia;
			while(resultSet.next()) {
				categoria_preferencia = new Categoria(resultSet.getInt("id_categoria"), resultSet.getString("desc_categoria"));
				preferencia = new Preferencia(categoria_preferencia,resultSet.getDouble("valor_preferencia"));
				preferencias.add(preferencia);
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
		return preferencias;
	}
	
	public void insertPreferencias(ArrayList<Preferencia> preferencias, String nombre_usuario) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String sentencia = "INSERT INTO preferencias values(?,?,?)";
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			
			for(Preferencia preferencia : preferencias) {
				pst.setString(1, nombre_usuario);
				pst.setInt(2, preferencia.getIdCategoria());
				pst.setDouble(3, preferencia.getValorPreferencia());
				
				pst.execute();
			}
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally
		{
		    try {
				if(pst != null)	pst.close();
				FactoryConnection.getInstancia().closeConnection();
			}
			catch(SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	public static void updatePreferencias(ArrayList<Preferencia> preferencias, String nombre_usuario) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String sentencia = "UPDATE preferencias set valor_preferencia=? where nombre_usuario=? and id_categoria=?";
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			
			for(Preferencia preferencia : preferencias) {
				pst.setString(2, nombre_usuario);
				pst.setInt(3, preferencia.getIdCategoria());
				pst.setDouble(1, preferencia.getValorPreferencia());
				
				pst.execute();
			}
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally
		{
		    try {
				if(pst != null)	pst.close();
				FactoryConnection.getInstancia().closeConnection();
			}
			catch(SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	
	public void deletePreferencia(int categoria, String nombre_usuario) throws SQLException {
		
		PreparedStatement pst = null;
		try {
			String sentencia = "delete from preferencias where nombre_usuario=? and id_categoria=?";
			
			pst = FactoryConnection.getInstancia().getConnection().prepareStatement(sentencia);
			pst.setString(1, nombre_usuario);
			pst.setInt(2, categoria);

			pst.executeUpdate();
		}
		catch(SQLException e) {
			throw new SQLException(e);
		}
		finally
		{
		    try {
				if(pst != null)	pst.close();
				FactoryConnection.getInstancia().closeConnection();
			}
			catch(SQLException e) {
				throw new SQLException(e);
			}
		}
	}
}

