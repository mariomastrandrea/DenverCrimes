package it.polito.tdp.crimes.model;

public class Adiacenza
{
	private String v1;
	private String v2;
	private double peso;
	
	
	public Adiacenza(String v1, String v2, double peso)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.peso = peso;
	}

	public String getV1()
	{
		return this.v1;
	}

	public String getV2()
	{
		return this.v2;
	}

	public double getPeso()
	{
		return this.peso;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
		result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiacenza other = (Adiacenza) obj;
		if (v1 == null)
		{
			if (other.v1 != null)
				return false;
		}
		else
			if (!v1.equals(other.v1))
				return false;
		if (v2 == null)
		{
			if (other.v2 != null)
				return false;
		}
		else
			if (!v2.equals(other.v2))
				return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s  <-->  %s", v1, v2);
	}

	public String print()
	{
		return String.format("%s  <-->  %s  |  peso = %.2f", v1, v2, peso);
	}
	
}
