/*******************************************************************************
 * Copyright (c) 2020 Haonan Huang.
 *
 *     This file is part of QuantumVITAS (Quantum Visualization Interactive Toolkit for Ab-initio Simulations).
 *
 *     QuantumVITAS is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     QuantumVITAS is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with QuantumVITAS.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 *******************************************************************************/
package app.input.scf;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

import com.consts.Constants.EnumMixingMode;
import com.consts.Constants.EnumNumCondition;
import com.consts.Constants.EnumOccupations;
import com.consts.Constants.EnumSmearing;
import com.consts.Constants.EnumStep;
import com.consts.Constants.EnumUnitEnergy;
import com.consts.QeDocumentation;

import agent.InputAgentScf;
import agent.WrapperDouble;
import agent.WrapperInteger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import main.MainClass;

public class InputScfStandardController implements Initializable{

    @FXML private CheckBox checkResetAll;

    @FXML private Button infoResetAll;

    @FXML
    private ToggleButton restartToggle,forceToggle,stressToggle;

    @FXML
    private TextField ecutwfcField;

    @FXML
    private ComboBox<EnumUnitEnergy> ecutwfcUnit;

    @FXML
    private TextField ecutrhoField,maxStepField;

    @FXML
    private Label ecutrhoUnit,statusInfo;

    @FXML
    private TextField convField;

    @FXML
    private Label convUnit;

    @FXML
    private ComboBox<EnumMixingMode> mixingModeCombo;

    @FXML
    private TextField mixingField;

    @FXML
    private TextField kxField,kyField,kzField;

    @FXML
    private ComboBox<EnumOccupations> occupCombo;

    @FXML
    private ComboBox<EnumSmearing> smearCombo;

    @FXML
    private TextField gaussField;

    @FXML
    private Label gaussUnit;

    @FXML private Button infoRestart,infoForce,infoStress,infoEcutwfc,infoEcutRho,infoMaxstep,infoConv;
    
    @FXML private Button infoMixMode,infoMixBeta,infoK,infoOccup,infoSmearing,infoGauss;

    @FXML private CheckBox checkRestart,checkForce,checkStress,checkEcutwfc,checkEcutrho,checkMaxStep;
    
    @FXML private CheckBox checkConv,checkMixMode,checkMixBeta,checkK,checkOccup,checkSmear,checkGauss;

    private boolean allDefault=false;
    
    private MainClass mainClass;
	
	public InputScfStandardController(MainClass mc) {
		mainClass = mc;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initialize();
    }
    public void initialize(){
    	if (occupCombo.getItems().isEmpty()) {
    		//restart mode
    		restartToggle.setText("from scratch"); restartToggle.setSelected(false);
    		restartToggle.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
    			if (newValue) 
    			{ 
    				restartToggle.setText("restart");
    				if (iScf!=null)  iScf.boolRestart.setValue(true);
    			}
    			else 
    			{ 
    				restartToggle.setText("from scratch"); 
    				if (iScf!=null)  iScf.boolRestart.setValue(false);
    			}
    		});
    		//force
    		forceToggle.setText("off"); forceToggle.setSelected(false);
    		forceToggle.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
    			if (newValue) 
    			{ 
    				forceToggle.setText("on");
    				if (iScf!=null)  iScf.boolForce.setValue(true);
    			}
    			else 
    			{ 
    				forceToggle.setText("off"); 
    				if (iScf!=null)  iScf.boolForce.setValue(false);
    			}
    		});
    		//stress
    		stressToggle.setText("off"); stressToggle.setSelected(false);
    		stressToggle.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
    			if (newValue) 
    			{ 
    				stressToggle.setText("on");
    				if (iScf!=null)  iScf.boolStress.setValue(true);
    			}
    			else 
    			{ 
    				stressToggle.setText("off"); 
    				if (iScf!=null)  iScf.boolStress.setValue(false);
    			}
    		});
    		//ecutwfc
    		ObservableList<EnumUnitEnergy> ecutUnit = 
	    		    FXCollections.observableArrayList(EnumUnitEnergy.values());
    		ecutwfcUnit.setItems(ecutUnit);
    		ecutwfcUnit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
    		{ 
				InputAgentScf ia = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (ia!=null && newValue!=null) {
					ia.enumEnergyUnit.setValue(newValue);
				}
			});
    		//setDoubleFieldListener(ecutwfcField, "ecutWfc",EnumNumCondition.positive);
    		ecutwfcField.textProperty().addListener((observable, oldValue, newValue) -> {
    			InputAgentScf ia = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
    			if (ia==null) return;
    			Double tmp = str2double(newValue);
				if (tmp!=null) {
					if(tmp<=0) {statusInfo.setText("Must be positive!");return;}
				
    				statusInfo.setText("");
    				ia.ecutWfc.setValue(tmp);
    				if (checkEcutrho.isSelected()) {
    					ecutrhoField.setText(String.valueOf(tmp*4.0));
    				}
				}
    		});
    		setDoubleFieldListener(ecutrhoField, "ecutRho",EnumNumCondition.positive);
    		convUnit.textProperty().bind(ecutwfcUnit.valueProperty().asString());
    		
    		setIntegerFieldListener(maxStepField, "nElecMaxStep",EnumNumCondition.positive);
    		setDoubleFieldListener(convField, "elecConv",EnumNumCondition.positive);
    		
    		//mixing mode
			ObservableList<EnumMixingMode> mixi = 
	    		    FXCollections.observableArrayList(EnumMixingMode.values());
			mixingModeCombo.setItems(mixi);
			mixingModeCombo.setOnAction((event) -> {
				InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf!=null && mixingModeCombo.getValue()!=null) {
					iScf.enumMixing.setValue(mixingModeCombo.getValue());
				}
			});
			
			setDoubleFieldListener(mixingField, "mixBeta",EnumNumCondition.positive);
			setIntegerFieldListener(kxField, "nkx",EnumNumCondition.positive);
			setIntegerFieldListener(kyField, "nky",EnumNumCondition.positive);
			setIntegerFieldListener(kzField, "nkz",EnumNumCondition.positive);
			
    		gaussUnit.textProperty().bind(ecutwfcUnit.valueProperty().asString());
    		
    		//occupation list
			ObservableList<EnumOccupations> occup = 
	    		    FXCollections.observableArrayList(EnumOccupations.values());
			occupCombo.setItems(occup);
			occupCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
    		{ 
				InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf!=null && newValue!=null) {
					iScf.enumOccupation.setValue(newValue);
					if (newValue.equals(EnumOccupations.smearing)) {
						smearCombo.setDisable(false);checkSmear.setDisable(false);
						gaussField.setDisable(false);//checkGauss.setDisable(false);
					}
					else {
						smearCombo.setDisable(true);checkSmear.setDisable(true);
						gaussField.setDisable(true);checkGauss.setDisable(true);
					}
				}
			});
			
			//smearing list
			ObservableList<EnumSmearing> smearList = 
	    		    FXCollections.observableArrayList(EnumSmearing.values());
			smearCombo.setItems(smearList);
			smearCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf!=null && newValue!=null) {
					iScf.enumSmearing.setValue(newValue);
				}
			});
			
			setDoubleFieldListener(gaussField, "degauss",EnumNumCondition.nonNegative);
			
			//reset 
			checkRestart.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {restartToggle.setSelected(iScf.resetboolRestart());restartToggle.setDisable(true);iScf.boolRestart.setEnabled(false);}//****not so efficient, double executing
				else {restartToggle.setDisable(false);iScf.boolRestart.setEnabled(true);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkForce.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {forceToggle.setSelected(iScf.resetboolForce());forceToggle.setDisable(true);iScf.boolForce.setEnabled(false);}//****not so efficient, double executing
				else {forceToggle.setDisable(false);iScf.boolForce.setEnabled(true);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkStress.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {stressToggle.setSelected(iScf.resetboolStress());stressToggle.setDisable(true);iScf.boolStress.setEnabled(false);}//****not so efficient, double executing
				else {stressToggle.setDisable(false);iScf.boolStress.setEnabled(true);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkEcutwfc.setDisable(true);
			checkEcutrho.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {
					ecutrhoField.setDisable(true);
					if(!iScf.ecutWfc.isNull()){
						iScf.ecutRho.setValue(iScf.ecutWfc.getValue()*4.0);
						ecutrhoField.setText(String.valueOf(iScf.ecutWfc.getValue()*4.0));
					}
				}
				else {ecutrhoField.setDisable(false);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkMaxStep.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {maxStepField.setText(Integer.toString(iScf.resetnElecMaxStep()));maxStepField.setDisable(true);iScf.nElecMaxStep.setEnabled(false);}
				else {maxStepField.setDisable(false);iScf.nElecMaxStep.setEnabled(true);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkConv.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {convField.setText(Double.toString(iScf.resetelecConv()));convField.setDisable(true);
				ecutwfcUnit.getSelectionModel().select(EnumUnitEnergy.Ry);ecutwfcUnit.setDisable(true);iScf.elecConv.setEnabled(false);}
				else {convField.setDisable(false);checkEcutwfcAvailable();iScf.elecConv.setEnabled(true);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkMixMode.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {mixingModeCombo.getSelectionModel().select(iScf.resetenumMixing());mixingModeCombo.setDisable(true);iScf.enumMixing.setEnabled(false);}
				else {mixingModeCombo.setDisable(false);iScf.enumMixing.setEnabled(true);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkMixBeta.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {mixingField.setText(Double.toString(iScf.resetmixBeta()));mixingField.setDisable(true);iScf.mixBeta.setEnabled(false);}
				else {mixingField.setDisable(false);iScf.mixBeta.setEnabled(true);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkK.setDisable(true);
			checkOccup.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {occupCombo.getSelectionModel().select(iScf.resetenumOccupation());occupCombo.setDisable(true);iScf.enumOccupation.setEnabled(false);}
				else {occupCombo.setDisable(false);iScf.enumOccupation.setEnabled(true);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkSmear.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
				if (iScf==null || newValue==null) return;
				if (newValue) {smearCombo.getSelectionModel().select(iScf.resetenumSmearing());smearCombo.setDisable(true);iScf.enumSmearing.setEnabled(false);}
				else {smearCombo.setDisable(false);iScf.enumSmearing.setEnabled(true);if(checkResetAll.isSelected()) {allDefault=false;checkResetAll.setSelected(false);}}
			});
			checkGauss.setDisable(true);
			checkResetAll.selectedProperty().addListener((observable, oldValue, newValue) ->
    		{ 
    			if(newValue!=null && newValue!=allDefault) {
    				checkRestart.setSelected(newValue);checkForce.setSelected(newValue);
    				checkStress.setSelected(newValue);checkEcutrho.setSelected(newValue);
    				checkMaxStep.setSelected(newValue);checkConv.setSelected(newValue);
    				checkMixMode.setSelected(newValue);checkMixBeta.setSelected(newValue);
    				checkOccup.setSelected(newValue);checkSmear.setSelected(newValue);
    				allDefault = newValue;
    			}
			});
			//context help
			setInfoButton(infoRestart,"infoRestart");
			setInfoButton(infoForce,"infoForce");
    	}
    }
    private void checkEcutwfcAvailable() {
    	if (checkConv.isSelected()) return;// || checkGauss.isSelected()
    	else {ecutwfcUnit.setDisable(false);}
    }
    private void setInfoButton(Button bt,String key) {
    	bt.setTooltip(new Tooltip(QeDocumentation.pwShortDoc.get(key)));
    	bt.setOnAction((event) -> {
			Alert alert1 = new Alert(AlertType.INFORMATION);
	    	alert1.setTitle("Info");
	    	alert1.setContentText(QeDocumentation.pwDoc.get(key));
	    	alert1.showAndWait();
		});
    }
    private void setIntegerFieldListener(TextField tf, String fieldName, EnumNumCondition cond) {	
		tf.textProperty().addListener((observable, oldValue, newValue) -> {
			InputAgentScf ia = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
			if (ia==null) return;
			try {
				Field fd = InputAgentScf.class.getField(fieldName);
				Integer tmp = str2int(newValue);
				if (tmp!=null) {
					switch(cond) {
						case no:{break;}
						case positive:{if(tmp<=0) {statusInfo.setText("Must be positive!");return;} break;}
						case nonNegative:{if(tmp<0) {statusInfo.setText("Must not be negative!");return;} break;}
						default:{}
					}
					statusInfo.setText("");
					((WrapperInteger) fd.get(ia)).setValue(tmp);
				}
			} catch (Exception e) {
				Alert alert1 = new Alert(AlertType.INFORMATION);
		    	alert1.setTitle("Error");
		    	alert1.setContentText("Cannot set listener! "+fieldName+e.getMessage());
		    	alert1.showAndWait();
				e.printStackTrace();
			}
		});
    }
    private void setDoubleFieldListener(TextField tf, String fieldName, EnumNumCondition cond) {	
		tf.textProperty().addListener((observable, oldValue, newValue) -> {
			InputAgentScf ia = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
			if (ia==null) return;
			try {
				Field fd = InputAgentScf.class.getField(fieldName);
				Double tmp = str2double(newValue);
				if (tmp!=null) {
					switch(cond) {
						case no:{break;}
						case positive:{if(tmp<=0) {statusInfo.setText("Must be positive!");return;} break;}
						case nonNegative:{if(tmp<0) {statusInfo.setText("Must not be negative!");return;} break;}
						default:{}
					}
					statusInfo.setText("");
					((WrapperDouble) fd.get(ia)).setValue(tmp);
				}
			} catch (Exception e) {
				Alert alert1 = new Alert(AlertType.INFORMATION);
		    	alert1.setTitle("Error");
		    	alert1.setContentText("Cannot set listener! "+e.getMessage());
		    	alert1.showAndWait();
				e.printStackTrace();
			}
		});
    }
    private Integer str2int(String str) {
    	try {
    		statusInfo.setText("");
    		return Integer.parseInt(str);
    	}
    	catch(Exception e) {
    		statusInfo.setText("Error! Input is not integer. "+e.getMessage());
    		return null;
    	}
    }
    private Double str2double(String str) {
    	try {
    		statusInfo.setText("");
    		return Double.parseDouble(str);
    	}
    	catch(Exception e) {
    		statusInfo.setText("Error! Input is not double. "+e.getMessage());
    		return null;
    	}
    }
    private void setField(TextField tf, WrapperDouble val) {
    	if(val.getValue()==null) tf.setText("");
    	else tf.setText(val.getValue().toString());
    }
    private void setField(TextField tf, WrapperInteger val) {
    	if(val.getValue()==null) tf.setText("");
    	else tf.setText(val.getValue().toString());
    }
    public void loadProjectParameters() {
    	if (!occupCombo.getItems().isEmpty()) {
    		InputAgentScf iScf = (InputAgentScf) mainClass.projectManager.getStepAgent(EnumStep.SCF);
    		if (iScf!=null) {
    			occupCombo.setValue((EnumOccupations)iScf.enumOccupation.getValue());
    			restartToggle.setSelected(iScf.boolRestart.getValue());
    			forceToggle.setSelected(iScf.boolForce.getValue());
    			stressToggle.setSelected(iScf.boolStress.getValue());
    			ecutwfcUnit.setValue((EnumUnitEnergy)iScf.enumEnergyUnit.getValue());
    			setField(ecutwfcField, iScf.ecutWfc);
    			setField(ecutrhoField, iScf.ecutRho);
        		setField(maxStepField, iScf.nElecMaxStep);
    			setField(convField, iScf.elecConv);
    			mixingModeCombo.setValue((EnumMixingMode)iScf.enumMixing.getValue());
    			setField(mixingField, iScf.mixBeta);
    			setField(kxField, iScf.nkx);
    			setField(kyField, iScf.nky);
    			setField(kzField, iScf.nkz);
    			smearCombo.setValue((EnumSmearing) iScf.enumSmearing.getValue());
    			setField(gaussField, iScf.degauss);
    		}
    	}
    }

}