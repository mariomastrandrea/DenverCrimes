package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import it.polito.tdp.crimes.model.Adiacenza;


public class EventsDao 
{		
	public Collection<String> getAllCategorie()
	{
		String sqlQuery = String.format("%s %s", 
				"SELECT DISTINCT offense_category_id",
				"FROM events");
		
		Collection<String> categorie = new HashSet<>();
		
		try
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			ResultSet queryResult = statement.executeQuery();
			
			while(queryResult.next())
				categorie.add(queryResult.getString("offense_category_id"));
			
			DBConnect.close(queryResult, statement, connection);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new RuntimeException("Errore DAO in getAllCategorie()", sqle);
		}
		
		return categorie;
	}
	
	
	public Collection<String> getVertici(String categoria, int mese)
	{
		String sqlQuery = String.format("%s %s %s", 
				"SELECT DISTINCT offense_type_id",
				"FROM events",
				"WHERE offense_category_id = ? AND MONTH(reported_date) = ?");
		
		Collection<String> vertici = new HashSet<>();
		
		try
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setString(1, categoria);
			statement.setInt(2, mese);
			ResultSet queryResult = statement.executeQuery();
			
			while(queryResult.next())
				vertici.add(queryResult.getString("offense_type_id"));
			
			DBConnect.close(queryResult, statement, connection);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new RuntimeException("Errore DAO in getVertici()", sqle);
		}
		
		return vertici;
	}
	
	public Collection<Adiacenza> getAdiacenze(String categoria, int mese)
	{
		/*
		String sqlQuery = String.format("%s %s %s %s %s %s %s", 
				"SELECT e1.offense_type_id AS v1, e2.offense_type_id AS v2, COUNT(DISTINCT e1.neighborhood_id) AS peso",
				"FROM events e1, events e2",
				"WHERE e1.offense_category_id = ? AND e1.offense_category_id = e2.offense_category_id",
					"AND MONTH(e1.reported_date) = ? AND MONTH(e1.reported_date) = MONTH(e2.reported_date)",
					"AND e1.offense_type_id < e2.offense_type_id",
					"AND e1.neighborhood_id = e2.neighborhood_id",
				"GROUP BY e1.offense_type_id, e2.offense_type_id");
		*/
		
		String sqlQuery = String.format("%s %s %s %s %s %s %s %s %s", 
				"SELECT t1.offense, t2.offense, COUNT(DISTINCT t1.neighborhood) AS weight",
				"FROM (SELECT DISTINCT offense_type_id AS offense, neighborhood_id AS neighborhood",
						"FROM events",
						"WHERE MONTH(reported_date) = ? AND offense_category_id = ?) t1,",
						"(SELECT DISTINCT offense_type_id AS offense, neighborhood_id AS neighborhood",
						"FROM events",
						"WHERE MONTH(reported_date) = ? AND offense_category_id = ?) t2",
				"WHERE t1.offense < t2.offense AND t1.neighborhood = t2.neighborhood",
				"GROUP BY t1.offense, t2.offense");
		
		Collection<Adiacenza> adiacenze = new HashSet<>();
		
		try
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setInt(1, mese);
			statement.setString(2, categoria);
			statement.setInt(3, mese);
			statement.setString(4, categoria);
			
			ResultSet queryResult = statement.executeQuery();
			
			while(queryResult.next())
			{
				String v1 = queryResult.getString("t1.offense");
				String v2 = queryResult.getString("t2.offense");
				int peso = queryResult.getInt("weight");
				
				Adiacenza newAdiacenza = new Adiacenza(v1, v2, peso);
				
				adiacenze.add(newAdiacenza);
			}
			
			DBConnect.close(queryResult, statement, connection);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new RuntimeException("Errore DAO in getAdiacenze()", sqle);
		}
		
		return adiacenze;
	}
}






