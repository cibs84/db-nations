package org.lessons.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "root";
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Ricerca per nazione:");
		String nationSearch = scan.nextLine();
		
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			
			String sql = "select c.name as 'nome_nazione', c.country_id as 'id_nazione', r.name as 'nome_regione', c2.name as 'nome_continente'\r\n"
						+ "from countries c \r\n"
						+ "join regions r on r.region_id = c.region_id \r\n"
						+ "join continents c2 on c2.continent_id  = r.continent_id \r\n"
						+ "where c.name like ? \r\n"
						+ "order by c.name";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				
				ps.setString(1, "%" + nationSearch + "%");
				
				try (ResultSet rs = ps.executeQuery()) {
					
					while (rs.next()) {
						String nomeNazione = rs.getString("nome_nazione");
						int idNazione = rs.getInt("id_nazione");
						String nomeRegione = rs.getString("nome_regione");
						String nomeContinente = rs.getString("nome_continente");
						System.out.println("Nazione: " + nomeNazione + " | ID Nazione: " + idNazione + " | Regione: " + nomeRegione + " | Continente: " + nomeContinente);
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		scan.close();
	}

}
