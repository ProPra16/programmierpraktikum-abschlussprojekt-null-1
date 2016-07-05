package gui.controllers;


import java.net.URL;
import java.util.ResourceBundle;

import gui.views.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.*;

import vk.core.api.*;


public class ExerciseController implements Initializable{
	Exercise exercise;
	String valueName;
	String valueDescription;
	Test test;
	JavaStringCompiler compiler;
	
	
	@FXML
	TextArea sourceTextField;
	@FXML
	Button cancelButton;
	@FXML
	Button confirmButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void cancelAction(){
		
		AlertBox alertBox = new AlertBox("Exit", "Are you sure you want to go back to edit the tests?", 2);
		alertBox.buttonList[0].setText("Cancel");
		alertBox.buttonList[0].setOnAction(e-> alertBox.end());
		alertBox.buttonList[1].setText("Confirm");
		alertBox.buttonList[1].setOnAction(e-> {
			// TODO switch scene back to TestController
			alertBox.end();
		});
		alertBox.show();
	}
	
	@FXML
	public void confirmAction(){	
	
		exercise.getClasses().get(0).setContent(sourceTextField.getText());
		CompilationUnit compilatedData = new CompilationUnit(test.getName(), exercise.getTests().get(0).getContent(), true);
		CompilationUnit compilatedDataB = new CompilationUnit(exercise.getClasses().get(0).getName(), exercise.getClasses().get(0).getContent(), false);
		
		compiler = CompilerFactory.getCompiler(compilatedData,compilatedDataB);
				
		compiler.compileAndRunTests();
		CompilerResult comResult = compiler.getCompilerResult();
		TestResult tesResult = compiler.getTestResult();
		
		//Checks for Syntax-Errors
		if(comResult.hasCompileErrors()){
			AlertBox alertBox = new AlertBox("Error", "Die folgenden Syntax-Fehler sind aufgetreten:", 1);
			alertBox.setComResult(comResult);
			alertBox.setCompilatedData(compilatedData);
			alertBox.buttonList[0].setText("Confirm");
			alertBox.buttonList[0].setOnAction(e-> alertBox.end());
			alertBox.show();

		}
		else{
			//failed tests 
			if(tesResult.getNumberOfFailedTests() != 0){
				
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("TDDT");
				alert.setContentText("Tests failed!");
				alert.showAndWait();
				
			}
			// none failed tests
			else{
				
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("TDDT");
				alert.setContentText("none failed Tests!");
				alert.showAndWait();
				//TODO save changed code
			}
		}
	}
	
	public void setExercise(Exercise exercise){
		this.exercise = exercise;
		test = exercise.getTests().get(0);
		valueName = exercise.getClasses().get(0).getName();
		valueDescription = exercise.getClasses().get(0).getContent();
		// TODO check if TextField is able to combine name + description
		sourceTextField.setText(valueDescription);
	}
}
