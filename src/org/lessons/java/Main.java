package org.lessons.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "root";
		
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			
			String sql = "select c.name as 'nome_nazione', c.country_id as 'id_nazione', r.name as 'nome_regione', c2.name as 'nome_continente'\r\n"
						+ "from countries c \r\n"
						+ "join regions r on r.region_id = c.region_id \r\n"
						+ "join continents c2 on c2.continent_id  = r.continent_id\r\n"
						+ "order by c.name";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					
					while (rs.next()) {
						String nomeNazione = rs.getString("nome_nazione");
						int idNazione = rs.getInt("id_nazione");
						String nomeRegione = rs.getString("nome_regione");
						String nomeContinente = rs.getString("nome_continente");
						System.out.println(nomeNazione + " | " + idNazione + " | " + nomeRegione + " | " + nomeContinente);
					}
				}
			}
		} catch (SQLException ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}
	}

}
