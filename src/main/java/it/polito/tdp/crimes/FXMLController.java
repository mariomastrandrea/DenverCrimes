/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController 
{	
	private Model model;

    @FXML 
    private ResourceBundle resources;

    @FXML 
    private URL location;

    @FXML 
    private ComboBox<String> boxCategoria; 

    @FXML 
    private ComboBox<Integer> boxMese; 

    @FXML 
    private Button btnAnalisi; 

    @FXML 
    private ComboBox<?> boxArco; 

    @FXML 
    private Button btnPercorso; 

    @FXML 
    private TextArea txtResult; 

    @FXML
    void doCalcolaPercorso(ActionEvent event) 
    {

    }

    @FXML
    void doCreaGrafo(ActionEvent event) 
    {
    	String categoria = this.boxCategoria.getValue();
    	Integer mese = this.boxMese.getValue();
    	
    	if(categoria == null || mese == null)
    	{
    		this.txtResult.setText("Seleziona entrambi i valori di input!");
    		return;
    	}
    	
    	this.model.creaGrafo(categoria, mese);
    	
    	for(var v : this.model.getArchiSuperioriAllaMedia())
    	{
    		this.txtResult.appendText(v.toString() + "\n");
    	}
    }

    @FXML 
    void initialize() 
    {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) 
    {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(this.model.getAllCategorie());
    	
    	LinkedList<Integer> mesi = new LinkedList<>();
    	for(int i=1; i<=12; i++)
    		mesi.add(i);
    	
    	this.boxMese.getItems().addAll(mesi);
    }
}
