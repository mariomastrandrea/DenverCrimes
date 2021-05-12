package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnect 
{
	private static final String jdbcURL = "jdbc:mariadb://localhost/denver_crimes";
	private static final String username = "root";
	private static final String password = "root";
	private static final HikariDataSource ds;
	
	static
	{
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcURL);
		config.setUsername(username);
		config.setPassword(password);
		
		// configurazione MySQL
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		
		ds = new HikariDataSource(config);
	}
	
	public static Connection getConnection() 
	{
		try 
		{	
			return ds.getConnection();
		} 
		catch (SQLException sqle) 
		{
			System.err.println("Errore connessione al DB");
			throw new RuntimeException("Errore connessione al DB", sqle);
		}
	}
	
	public static void close(AutoCloseable... resources) throws SQLException
	{
		for(var res : resources)
		{
			try
			{
				res.close();
			}
			catch(Exception e)
			{
				throw new SQLException("Errore chiusura risorsa: " + res.toString(), e);
			}
		}
	}

}