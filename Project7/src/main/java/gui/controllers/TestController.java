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


public class TestController implements Initializable{
	Exercise exercise;
	String value;
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
		AlertBox alertBox = new AlertBox("Exit", "Are you sure you want to go back to the Menu?", 2);
		alertBox.buttonList[0].setText("Cancel");
		alertBox.buttonList[0].setOnAction(e-> alertBox.end());
		alertBox.buttonList[1].setText("Confirm");
		alertBox.buttonList[1].setOnAction(e-> {
			// TODO switch scene back to the menu!!!
			alertBox.end();
		});
		alertBox.show();
		
	}
	
	@FXML
	public void confirmAction(){	
	
		System.out.println(test.getName());
		CompilationUnit compilatedData = new CompilationUnit(test.getName(), sourceTextField.getText(), true);
		// TODO Save changes in the TextArea
		
		compiler = CompilerFactory.getCompiler(compilatedData);
				
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
			//One failed test which is Okay
			if(tesResult.getNumberOfFailedTests() == 1){
				// TODO switch scene
				System.out.println("Test1");
			}
			//Multiple or none failed tests
			else{
				if(tesResult.getNumberOfFailedTests() == 0){
					AlertBox alertBox = new AlertBox("Error", "Kein Test ist fehlgeschlagen! Bitte einen Test schreiben der fehlschlägt!", 1);
					alertBox.buttonList[0].setText("Confirm");
					alertBox.buttonList[0].setOnAction(e-> alertBox.end());
					alertBox.show();
					
				}else{
					AlertBox alertBox = new AlertBox("Error", "Zu viele Tests sind fehlgeschlagen! Bitte nur genau EINEN Test schreiben der fehlschlagen soll!", 1);
					alertBox.buttonList[0].setText("Confirm");
					alertBox.buttonList[0].setOnAction(e-> alertBox.end());
					alertBox.show();
				}
			}
		}
		
	}
	
	public void setExercise(Exercise exercise){
		this.exercise = exercise;
		test = exercise.getTests().get(0);
		value = test.getContent();
		sourceTextField.setText(value);
	}
}