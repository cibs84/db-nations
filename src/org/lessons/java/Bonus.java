package org.lessons.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Bonus {

	public static void main(String[] args) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "root";
		Scanner scan = new Scanner(System.in);
		
		// Stampa la lista delle Nazioni con le relative Regioni e Continenti
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			
			String sql = "select c.name as 'nome_nazione', c.country_id as 'id_nazione', r.name as 'nome_regione', c2.name as 'nome_continente'\r\n"
						+ "from countries c \r\n"
						+ "join regions r on r.region_id = c.region_id \r\n"
						+ "join continents c2 on c2.continent_id  = r.continent_id \r\n"
						+ "order by c.name";
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				
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
		
		// Chiede di inserire l'id di una Nazione e stampa le lingue che in quella nazione 
		// si parlano e le sue statistiche più recenti
		Connection con_2 = DriverManager.getConnection(url, user, password);
		try {
			System.out.println("\nRicerca per ID Nazione:");
			int idNationSearch = Integer.parseInt(scan.nextLine());
			
			
			
			String sql_nation = "select c.name from countries c "
							+ "where c.country_id = ?";
			String sql_lang = "select l.`language` from countries c "
							+ "join country_languages cl on cl.country_id = c.country_id "
							+ "join languages l on l.language_id = cl.language_id "
							+ "where c.country_id = ? "
							+ "group by l.language_id";
			String sql_stats = "select cs.*  from countries c "
							+ "join country_stats cs on cs.country_id = c.country_id "
							+ "where c.country_id = ? "
							+ "order by cs.`year` desc "
							+ "limit 1";
			
			con_2.setAutoCommit(false);
			
			// Nazione
			PreparedStatement ps_1 = con_2.prepareStatement(sql_nation);
			ps_1.setInt(1, idNationSearch);
			ResultSet rs_1 = ps_1.executeQuery();
			// Lingue
			PreparedStatement ps_2 = con_2.prepareStatement(sql_lang);
			ps_2.setInt(1, idNationSearch);
			ResultSet rs_2 = ps_2.executeQuery();
			// Statistiche
			PreparedStatement ps_3 = con_2.prepareStatement(sql_stats);
			ps_3.setInt(1, idNationSearch);
			ResultSet rs_3 = ps_3.executeQuery();
			
			con_2.commit();
			
			// Nazione
			while (rs_1.next()) {
				System.out.println("\nNazione: " + rs_1.getString(1) + "\n");
			}
			// Lingue
			System.out.println("Lingue parlate: ");
			while (rs_2.next()) {
				String language = rs_2.getString("language");
				System.out.println("- " + language);
			}
			// Statistiche
			System.out.println("\nStatistiche: ");
			while (rs_3.next()) {
				String year = rs_3.getString("year");
				String population = rs_3.getString("population");
				String gdp = rs_3.getString("gdp");
				System.out.println("- Anno: " + year);
				System.out.println("- Popolazione: " + population);
				System.out.println("- Gdp: " + gdp);
			}
			
		} catch (Exception ex) {
			System.out.println("Errore esecuzione multiquery!");
			con_2.rollback();
		} finally {
			if (con_2 != null) {
				con_2.close();
			}
		}
		
		scan.close();
	}

}
