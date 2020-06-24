package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

//controller turno A --> switchare al branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> boxCategoria;

    @FXML
    private ComboBox<Integer> boxAnno;

    @FXML
    private Button btnAnalisi;

    @FXML
    private ComboBox<Adiacenza> boxArco;

    @FXML
    private Button btnPercorso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaPercorso(ActionEvent event) {

    	txtResult.appendText("\n");
    	
    	Adiacenza arco = boxArco.getValue();
    	
    	if(arco == null) {
    		txtResult.appendText("Errore: selezionare un arco per proseguire. \n");
    		return;
    	}
    	
    	this.model.trovaCamminoMassimo(arco);
    	
    	List<String> camminoMax = this.model.getCamminoMax();
    	int pesoMin = this.model.getPesoMin();
    	
    	if(camminoMax == null || pesoMin == 10000000) {
    		txtResult.appendText("Errore nella ricorsione. \n");
    		return;
    	}
    	
    	txtResult.appendText(String.format("Cammino di peso minimo trovato con "
    			+ "peso pari a %d \ne lunghezza del cammino uguale a %d: \n", pesoMin, camminoMax.size()));
    	
    	for(String s : camminoMax) {
    		txtResult.appendText(" - " + s.toString() + "\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
  
    	String type = boxCategoria.getValue(); 
    	Integer anno = boxAnno.getValue();
    	
    	if(type == null) {
    		txtResult.appendText("Errore: inserire una categoria per proseguire. \n");
    		return;
    	}
    	
    	if(anno == null) {
    		txtResult.appendText("Errore: inserire un anno per proseguire. \n");
    		return;
    	}
    	
    	txtResult.appendText("Sto creando il grafo... \n");
    	this.model.creaGrafo(anno, type);
    	txtResult.appendText(String.format("Grafo creato! [#Vertici %d, #Archi %d] \n", 
    								this.model.getNumberVertex(), this.model.getNumberEdges()));
    	
    	txtResult.appendText("\n");
    	
    	int pesoMax = this.model.getPesoMax(anno, type);
    	List<Adiacenza> archiPesoMax = this.model.getArchiPesoMax(anno, type);
    	
    	txtResult.appendText(String.format("Il peso massimo nel grafo è %d. \n"
    			+ "Trovati %d archi con tale peso: \n", pesoMax, archiPesoMax.size()));
    	
    	txtResult.appendText("\n");
    	for(Adiacenza a : archiPesoMax) {
    		txtResult.appendText(a.toString() + "\n");
    	}
    	
    	boxArco.getItems().addAll(archiPesoMax);
    }

    @FXML
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		boxCategoria.getItems().addAll(this.model.getTypes());
		boxAnno.getItems().addAll(this.model.getAnniByType());
	}
}
