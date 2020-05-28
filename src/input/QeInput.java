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
package input;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

import com.consts.Constants.EnumStep;
import com.error.InvalidKeyException;
import com.error.InvalidTypeException;

import agent.InputAgentDos;
import agent.InputAgentGeo;
import agent.InputAgentNscf;
import agent.InputAgentOpt;
import agent.InputAgentScf;
import agent.WrapperBoolean;
import agent.WrapperDouble;
import agent.WrapperInteger;
import agent.WrapperString;

public abstract class QeInput implements Serializable{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = -8331487638839992617L;
	
	protected LinkedHashMap<String, InputSection> sectionDict;
	
	public QeInput() {
		sectionDict = new LinkedHashMap<String, InputSection> ();
	}
	public abstract String addParameter(InputValue val);
	public abstract void print(); 
	public ContainerInputString genInput(String startingMsg) {
		ContainerInputString ci = new ContainerInputString();
		ci.appendInput(startingMsg);
		Set<String> keys = sectionDict.keySet();
        for(String key: keys){
        	ContainerInputString ciTmp = sectionDict.get(key).toStringWrapper();
        	if (!ciTmp.isEmpty()) {
        		ci.append(ciTmp);
        	}
        }
		return ci;
	}  
	public ContainerInputString genInput(EnumStep es) {
		return genInput("!"+es.toString()+"\n");
	} 
	public abstract void setValue(String keySec, String keyPara) throws InvalidKeyException, InvalidTypeException;//set null
	public abstract void setValue(String keySec, String keyPara,WrapperDouble para) throws InvalidKeyException, InvalidTypeException;
	public abstract void setValue(String keySec, String keyPara,WrapperInteger para) throws InvalidKeyException, InvalidTypeException;
	public abstract void setValue(String keySec, String keyPara,WrapperString para) throws InvalidKeyException, InvalidTypeException;
	public abstract void setValue(String keySec, String keyPara,WrapperBoolean para) throws InvalidKeyException, InvalidTypeException;
	//not abstract method, because no need to define all of them in the inherited class
	public void loadAgent(InputAgentDos ia1) {}
	public void loadAgent(InputAgentGeo ia1) {}
	public void loadAgent(InputAgentNscf ia1) {}
	public void loadAgent(InputAgentOpt ia1) {}
	public void loadAgent(InputAgentScf ia1) {} 
}