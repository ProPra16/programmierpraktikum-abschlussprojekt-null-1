package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import gui.views.exercises.ExercisesGrid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import services.StorageService;
import xmlParser.ParserException;

public class ExercisesViewController implements Initializable {

	@FXML
	ScrollPane mainPane;

	MenuViewController menuController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		// Try to load current workspace
		try {
			StorageService.getInstance().loadModel();
			
			// If exercises are imported, show exercises grid
			if(StorageService.getInstance().getExerciseCatalog().getExercises().size() != 0) {
				// Create exercises grid
				ExercisesGrid exercisesGrid = new ExercisesGrid(StorageService.getInstance().getExerciseCatalog().getExercises());
				exercisesGrid.addSelectExerciseHandler((exercise) -> {
					menuController.selectExercise(exercise);
				});
				mainPane.setContent(exercisesGrid);
			}
		} catch (Exception e) {
			// Do nothing - import view is already there
		}

	}

	/**
	 * Sets the menu controller
	 */
	public void setMenuController(MenuViewController menuController) {
		this.menuController = menuController;
	}

}
