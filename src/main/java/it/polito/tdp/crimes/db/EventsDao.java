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
			
			queryResult.close();
			statement.close();
			connection.close();
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
			
			queryResult.close();
			statement.close();
			connection.close();
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
		String sqlQuery = String.format("%s %s %s %s %s %s %s", 
				"SELECT e1.offense_type_id AS v1, e2.offense_type_id AS v2, COUNT(DISTINCT e1.neighborhood_id) AS peso",
				"FROM events e1, events e2",
				"WHERE e1.offense_category_id = ? AND e1.offense_category_id = e2.offense_category_id",
					"AND MONTH(e1.reported_date) = ? AND MONTH(e1.reported_date) = MONTH(e2.reported_date)",
					"AND e1.offense_type_id < e2.offense_type_id",
					"AND e1.neighborhood_id = e2.neighborhood_id",
				"GROUP BY e1.offense_type_id, e2.offense_type_id");
		
		Collection<Adiacenza> adiacenze = new HashSet<>();
		
		try
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setString(1, categoria);
			statement.setInt(2, mese);
			ResultSet queryResult = statement.executeQuery();
			
			while(queryResult.next())
			{
				String v1 = queryResult.getString("v1");
				String v2 = queryResult.getString("v2");
				int peso = queryResult.getInt("peso");
				
				Adiacenza newAdiacenza = new Adiacenza(v1,v2,peso);
				
				adiacenze.add(newAdiacenza);
			}
			
			queryResult.close();
			statement.close();
			connection.close();
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new RuntimeException("Errore DAO in getAdiacenze()", sqle);
		}
		
		return adiacenze;
	}
	
	/*
	public List<Event> listAllEvents()
	{
		String sql = "SELECT * FROM events";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			List<Event> list = new ArrayList<>();
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) 
			{
				try 
				{
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} 
				catch (Throwable t) 
				{
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list;

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null ;
		}
	}
	*/
}






