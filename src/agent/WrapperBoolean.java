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
package agent;

public class WrapperBoolean extends WrapperClass{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1515304239289393505L;
	private Boolean value;
	public WrapperBoolean(Boolean val) {
		value = val;
		enabled = true;
	}
	public WrapperBoolean(Boolean val,Boolean bl) {
		value = val;
		enabled = bl;
	}
	public Boolean getValue() {
		return value;
	}
	public void setValue(Boolean val) {
		value = val;
	}
	public Boolean equals(Boolean vl) {
		return vl==value;
	}
	@Override
	public Boolean isNull() {
		return value==null;
	}
}