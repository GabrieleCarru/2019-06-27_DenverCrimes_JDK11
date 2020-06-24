package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDAO;

public class Model {

	private EventsDAO dao;
	private Graph<String, DefaultWeightedEdge> graph;
	
	// Variabili ricorsione
	private List<String> camminoMax;
	private int pesoMin;
	
	public Model() {
		dao = new EventsDAO();
	}

	public void creaGrafo(Integer anno, String type) {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		
		List<Adiacenza> adiacenze = dao.getAdiacenzeVerticiArco(type, anno);
		for(Adiacenza a : adiacenze) {
			if(!graph.vertexSet().contains(a.getT1())) {
				graph.addVertex(a.getT1());
			}
			
			if(!graph.vertexSet().contains(a.getT2())) {
				graph.addVertex(a.getT2());
			}
			
			if(graph.vertexSet().contains(a.getT1()) && graph.vertexSet().contains(a.getT2())) {
				Graphs.addEdge(this.graph, a.getT1(), a.getT2(), a.getPeso());
			}
		}
	}
	
	public List<Adiacenza> getArchiPesoMax(Integer anno, String type) {
		int pesoMax = 0;
		List<Adiacenza> adiacenze = dao.getAdiacenzeVerticiArco(type, anno);
		
		for(Adiacenza a : adiacenze) {
			if(a.getPeso() > pesoMax) {
				pesoMax = a.getPeso();
			}
		}
		
		List<Adiacenza> result = new ArrayList<>();
		
		for(Adiacenza a : adiacenze) {
			if(a.getPeso() == pesoMax) {
				result.add(a);
			}
		}
		return result;
	}
	
	public void trovaCamminoMassimo(Adiacenza arco) {
		camminoMax = null;
		pesoMin = 10000000;
		
		List<String> parziale = new ArrayList<>();
		parziale.add(arco.getT1());
		
		ricorsione(parziale, 1, arco);
	}
	
	// Livello = 1 a inizio ricorsione con un elemento inserito.
	// Ultimo elemento è parziale[livello]
	public void ricorsione(List<String> parziale, int livello, Adiacenza arco) {
		
		// Caso terminale:
		if((parziale.get(livello-1).compareTo(arco.getT2()) == 0) 
				&& (parziale.size() == this.graph.vertexSet().size())) {
			
							
			if(getPeso(parziale) < pesoMin) {
				pesoMin = getPeso(parziale);
				camminoMax = new ArrayList<>(parziale);
			}
			return;
		}
		
		// Caso normale: 
		List<String> vicini = Graphs.neighborListOf(this.graph, parziale.get(livello-1));
		for(String s : vicini) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				ricorsione(parziale, livello + 1, arco);
				parziale.remove(s);
			}
		}
	}
	
	
	private int getPeso(List<String> parziale) {
		int peso = 0;
		
		for(int i = 1; i < parziale.size(); i++) {
			peso += this.graph.getEdgeWeight(this.graph.getEdge(parziale.get(i), parziale.get(i-1)));
		}
		return peso;
	}

	public int getPesoMax(Integer anno, String type) {
		int pesoMax = 0;
		List<Adiacenza> adiacenze = dao.getAdiacenzeVerticiArco(type, anno);
		
		for(Adiacenza a : adiacenze) {
			if(a.getPeso() > pesoMax) {
				pesoMax = a.getPeso();
			}
		}
		return pesoMax;
	}

	public int getNumberVertex() {
		return this.graph.vertexSet().size();
	}

	public int getNumberEdges() {
		return this.graph.edgeSet().size();
	}

	public List<String> getTypes() {
		return dao.getTypes();
	}

	public List<Integer> getAnniByType() {
		return dao.getYears();
	}

	public List<String> getCamminoMax() {
		return camminoMax;
	}

	public int getPesoMin() {
		return pesoMin;
	}

}
