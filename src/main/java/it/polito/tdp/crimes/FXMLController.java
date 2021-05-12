/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController 
{	
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> categoriaComboBox;

    @FXML
    private ComboBox<Integer> meseComboBox;

    @FXML
    private Button analisiQuartieriButton;

    @FXML
    private ComboBox<Adiacenza> arcoComboBox;

    @FXML
    private Button calcoloPercorsoButton;

    @FXML
    private TextArea resultTextArea;
    
	private Model model;
	
	
	@FXML
    void handleCategoriaSelected(ActionEvent event) 
	{
		this.checkCategoriaEMese();
    }
	
	@FXML
    void handleMeseSelected(ActionEvent event) 
	{
		this.checkCategoriaEMese();
    }
	
	private void checkCategoriaEMese()
	{
		String categoria = this.categoriaComboBox.getValue();
    	Integer mese = this.meseComboBox.getValue();
    	
    	if(categoria == null || mese == null)
    		this.analisiQuartieriButton.setDisable(true);
    	else
    		this.analisiQuartieriButton.setDisable(false);
	}
	
	@FXML
    void handleAnalisiQuartieri(ActionEvent event) 
	{
    	String categoria = this.categoriaComboBox.getValue();
    	Integer mese = this.meseComboBox.getValue();
    	
    	if(categoria == null || mese == null)
    	{
    		this.resultTextArea.setText("Errore di input (categoria o mese)");
    		return;
    	}
    	
    	this.model.creaGrafo(categoria, mese);
    	
    	StringBuilder output = new StringBuilder();
    	
    	String infoGrafo = this.printInfoGrafo();
    	output.append(infoGrafo).append("\n\n");
    	
    	Collection<Adiacenza> archiSuperioriAllaMedia = this.model.getArchiSuperioriAllaMedia();
    	
    	if(archiSuperioriAllaMedia.isEmpty())
    	{
    		output.append("Errore: non esistono archi con peso superiore alla media");
    		this.resultTextArea.setText(output.toString());
    		return;
    	}
    	
    	String infoArchi = this.printArchi(archiSuperioriAllaMedia);
    	output.append(infoArchi);
   
    	
    	//update UI
    	this.resultTextArea.setText(output.toString());
    	
    	this.arcoComboBox.setDisable(false);
    	this.arcoComboBox.setValue(null);
    	this.arcoComboBox.getItems().clear();
    	this.arcoComboBox.getItems().addAll(archiSuperioriAllaMedia);
    }
	
	private String printArchi(Collection<Adiacenza> archi)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Gli archi con peso superiore alla media sono ").append(archi.size()).append(":\n");
		
		for(Adiacenza a : archi)
		{
			sb.append("•  ");
			sb.append(a.print()).append("\n");
		}
		
		if(!archi.isEmpty())
			sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}

	private String printInfoGrafo()
	{
		int numVertici = this.model.getNumVertici();
		int numArchi = this.model.getNumArchi();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Grafo creato!\n");
		sb.append("# Vertici: ").append(numVertici).append("\n");
		sb.append("# Archi: ").append(numArchi);
		
		return sb.toString();
	}

	@FXML
    void handleArcoSelected(ActionEvent event) 
	{
		Adiacenza selected = this.arcoComboBox.getValue();
		
		if(selected == null)
			this.calcoloPercorsoButton.setDisable(true);
		else
			this.calcoloPercorsoButton.setDisable(false);
    }

	@FXML
	void handleCalcolaPercorso(ActionEvent event) 
	{
		Adiacenza selected = this.arcoComboBox.getValue();
		
		String cat1 = selected.getV1();
		String cat2 = selected.getV2();
		
		List<String> percorsoPiuLungo = this.model.trovaPercorsoPiuLungo(cat1, cat2);
		
		if(percorsoPiuLungo.isEmpty() || percorsoPiuLungo.size() == 1)
		{
			this.resultTextArea.setText("Errore: percorso piu' lungo non trovato.");
			return;
		}
		
		StringBuilder output = new StringBuilder();
		output.append("Trovato il percorso aciclico più lungo da \"");
		output.append(cat1).append("\" a \"").append(cat2).append("\":\n\n");
		
		String percorsoText = this.printPercorso(percorsoPiuLungo);
		output.append(percorsoText);
		
		this.resultTextArea.setText(output.toString());
    }

	private String printPercorso(List<String> percorsoPiuLungo)
	{
		StringBuilder sb = new StringBuilder();
		
		int count = 0;
		for(String categoria : percorsoPiuLungo)
		{
			if(count > 0)
				sb.append("\n");
			
			sb.append(++count).append(") ");
			sb.append(categoria);
		}
		
		return sb.toString();
	}

	@FXML
	void initialize() 
	{
		assert categoriaComboBox != null : "fx:id=\"categoriaComboBox\" was not injected: check your FXML file 'Scene_DenverCrimes.fxml'.";
		assert meseComboBox != null : "fx:id=\"meseComboBox\" was not injected: check your FXML file 'Scene_DenverCrimes.fxml'.";
		assert analisiQuartieriButton != null : "fx:id=\"analisiQuartieriButton\" was not injected: check your FXML file 'Scene_DenverCrimes.fxml'.";
		assert arcoComboBox != null : "fx:id=\"arcoComboBox\" was not injected: check your FXML file 'Scene_DenverCrimes.fxml'.";
		assert calcoloPercorsoButton != null : "fx:id=\"calcoloPercorsoButton\" was not injected: check your FXML file 'Scene_DenverCrimes.fxml'.";
		assert resultTextArea != null : "fx:id=\"resultTextArea\" was not injected: check your FXML file 'Scene_DenverCrimes.fxml'.";
	}
    
    public void setModel(Model model) 
    {
    	this.model = model;
    	
    	Collection<String> categorie = this.model.getAllCategorie();
    	this.categoriaComboBox.getItems().addAll(categorie);
    	
    	Collection<Integer> mesi = this.model.getMesi();
    	this.meseComboBox.getItems().addAll(mesi);
    }
}
