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
package project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.consts.Constants.EnumCalc;
import com.consts.Constants.EnumStep;

import agent.InputAgent;
import agent.InputAgentGeo;
import app.viewer3d.WorkScene3D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Project implements Serializable{
	//EVRYTHING SHOULD BE ACCESSED ON THE PROJECT LEVEL, NOT ON THE CALCULATION CLASS LEVEL
	/**
	 * 
	 */
	private static final long serialVersionUID = 8311819691310573808L;
	
	private String nameProject;
	private HashMap<EnumCalc, calculationClass> calcDict;
	private ArrayList<EnumCalc> calcList;
	private HashMap<EnumStep, InputAgent> projectDefault;
	private EnumCalc activeCalcKey;
	private ArrayList<InputAgentGeo> geoList;
	private Integer activeGeoInd;
	private Boolean boolGeoActive;
	private EnumCalc calcScfDefault;
	transient private WorkScene3D viewer3D=null; //do not save it to the file!
	
	public Project(String np) {
		activeCalcKey = null;
		nameProject = np;
		calcDict = new HashMap<EnumCalc, calculationClass>();
		calcList = new ArrayList<EnumCalc>();
		projectDefault = new HashMap<EnumStep, InputAgent>();
		geoList = new ArrayList<InputAgentGeo>();
		geoList.add(new InputAgentGeo());//at least have one Geometry
		activeGeoInd = 0;
		boolGeoActive = true;
		calcScfDefault = null;
		viewer3D = new WorkScene3D();
	}
	public WorkScene3D getViewer3D() {
		if (viewer3D==null) {viewer3D = new WorkScene3D();}
		return viewer3D;
	}
	public void updateViewerPlot() {
		if (viewer3D==null) {viewer3D = new WorkScene3D();}
		viewer3D.buildGeometry(getAgentGeo());//null ok
		//viewer3D.buildSampleMolecule();
	}
	public void setGeoActive(Boolean bl) {
		boolGeoActive = bl;
	}
	public Boolean getGeoActive() {
		return boolGeoActive;
	}
	public void setProjectDefault(EnumStep es) {
		calculationClass calc = getActiveCalc();
		if (calc==null) return;
		InputAgent ia = calc.getAgent(es);
		if (projectDefault!=null && ia!=null) {
			projectDefault.put(es, ia);//only reference
			calcScfDefault = activeCalcKey;//hopefully Enum does not need deep copy...
		}
	}
	public InputAgent getProjectDefault(EnumStep es) {
		if (es!=null && projectDefault!=null && projectDefault.containsKey(es)){
			return (InputAgent) projectDefault.get(es).clone();
		}
		else return null;
	}
	public Boolean isDefault() {
		if (calcScfDefault==null || activeCalcKey==null) return false;
		return (calcScfDefault==activeCalcKey);
	}
	public void addCalculation(EnumCalc ec) {
		if (ec == null)  return;
		if (!calcDict.containsKey(ec)){
			calculationClass calc;
			switch (ec) {
			case SCF:calc = new calculationScfClass();break;
			case OPT:calc = new calculationOptClass();break;
			case DOS:calc = new calculationDosClass();break;
			default:
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("Error");
		    	alert.setContentText("Not implemented calculationClass!");
		    	alert.showAndWait();
		    	return;
			}
			calcDict.put(ec, calc);
			calcList.add(ec);
		}
		activeCalcKey = ec;//set active no matter whether already contains or not
	}
	public InputAgentGeo getAgentGeo() {
		if (activeGeoInd>=geoList.size()) return null;
		if (boolGeoActive || !existCurrentCalc()){
			return geoList.get(activeGeoInd);//use project default
		}
		else {
			Integer ind = getActiveCalc().getGeoInd();
			if (ind != null) {
				return geoList.get(getActiveCalc().getGeoInd());
			}
			else return null;
		}
	}
	public void setActiveGeoInd(int ind) {
		if (ind>=geoList.size()) return;
		activeGeoInd = ind;
	}
	public ArrayList<EnumCalc> getCalcList(){
		return calcList;
	}
	public EnumCalc getActiveCalcName() {
		return activeCalcKey;
	}
	public void setActiveCalcName(EnumCalc ec) {
		if (ec!=null && calcDict.containsKey(ec)) {
			activeCalcKey=ec;
		}
	}
	public calculationClass getActiveCalc() {
		if (activeCalcKey == null)  return null;
		if (calcDict.containsKey(activeCalcKey)) {
			return calcDict.get(activeCalcKey);
		}
		else return null;
	}
	public Boolean existCurrentCalc() {
		if (activeCalcKey == null)  return false;//no current calc
		return calcDict.containsKey(activeCalcKey);
	}
	public Boolean existCalc(EnumCalc key) {
		if (key == null)  return false;
		return calcDict.containsKey(key);
	}
	public calculationClass getCalc(EnumCalc key) {
		if (key == null)  return null;
		if (calcDict.containsKey(key)) return calcDict.get(key);
		else return null;
	}
	public String getName() {
		return nameProject;
	}
	public void genInputFromAgent() {
		calculationClass tmp = getActiveCalc();
		if (tmp==null || boolGeoActive) {
			Alert alert1 = new Alert(AlertType.INFORMATION);
	    	alert1.setHeaderText("No valid calculation!");
	    	alert1.setContentText("No calculaion or in the geometry page.");
	    	alert1.showAndWait();
		}
		else tmp.genInputFromAgent(geoList);
	}
	public EnumCalc getcalcScfDefault() {
		return calcScfDefault;
	}
	public void setcalcScfDefault(EnumCalc calcScfDefault) {
		this.calcScfDefault = calcScfDefault;
	}
}