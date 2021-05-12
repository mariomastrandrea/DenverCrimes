package it.polito.tdp.crimes.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model 
{
	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<String> percorsoMigliore;
	
	
	public Model()
	{
		this.dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese)
	{
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiunta vertici
		Collection<String> vertici = this.dao.getVertici(categoria, mese);
		Graphs.addAllVertices(this.grafo, vertici);
		
		// aggiunta archi
		
		Collection<Adiacenza> adiacenze = this.dao.getAdiacenze(categoria, mese);
		
		for(Adiacenza a : adiacenze)
		{
			String v1 = a.getV1();
			String v2 = a.getV2();
			double peso = a.getPeso();
			
			if(!this.grafo.containsEdge(v1, v2))
			{
				Graphs.addEdge(this.grafo, v1, v2, peso);
			}
		}
		
		System.out.println("#Vertici: " + this.grafo.vertexSet().size());
		System.out.println("#Archi: " + this.grafo.edgeSet().size());
	}
	
	public Collection<Adiacenza> getArchiSuperioriAllaMedia()
	{
		double pesoMedio = 0.0;
		
		for(DefaultWeightedEdge edge : this.grafo.edgeSet())
		{
			pesoMedio += this.grafo.getEdgeWeight(edge);
		}
		
		pesoMedio /= (double)this.grafo.edgeSet().size();
		
		Collection<Adiacenza> archiConPesoSuperioreAllaMedia = new HashSet<>();
		
		for(DefaultWeightedEdge edge : this.grafo.edgeSet())
		{
			double weight = this.grafo.getEdgeWeight(edge);
			if(weight > pesoMedio)
			{
				String source = this.grafo.getEdgeSource(edge);
				String target = this.grafo.getEdgeTarget(edge);
				Adiacenza a = new Adiacenza(source, target, weight);
				archiConPesoSuperioreAllaMedia.add(a);
			}
		}
		
		return archiConPesoSuperioreAllaMedia;
	}
	
	public List<String> trovaPercorsoPiuLungo(String sorgente, String destinazione)
	{
		this.percorsoMigliore = new LinkedList<>();	
		LinkedList<String> soluzioneParziale = new LinkedList<>();
		
		soluzioneParziale.add(sorgente);
		cercaRicorsivamente(destinazione, soluzioneParziale);
		
		return this.percorsoMigliore;
	}
	
	private void cercaRicorsivamente(String destinazione, LinkedList<String> soluzioneParziale)
	{		
		String ultimoElementoInserito = soluzioneParziale.getLast();
		
		//caso terminale
		if(ultimoElementoInserito.equals(destinazione))
		{
			if(soluzioneParziale.size() > this.percorsoMigliore.size())
			{
				this.percorsoMigliore.clear();
				this.percorsoMigliore.addAll(soluzioneParziale);
			}
			return;
		}
		
		// ...altrimenti
		Collection<String> vicini = Graphs.neighborSetOf(this.grafo, ultimoElementoInserito);
		
		for(String vertice : vicini)
		{
			if(!soluzioneParziale.contains(vertice))
			{
				soluzioneParziale.addLast(vertice);
				
				cercaRicorsivamente(destinazione, soluzioneParziale);
				
				soluzioneParziale.removeLast(); //backtracking
			}
		}
	}

	public Collection<String> getAllCategorie()
	{
		return this.dao.getAllCategorie();
	}
}






