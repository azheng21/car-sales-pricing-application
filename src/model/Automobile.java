package model;

import java.lang.ArrayIndexOutOfBoundsException;
import java.util.*;
import java.io.Serializable;

import exception.AutoException;


public class Automobile implements Serializable {
	private static final long serialVersionUID = 1362422403381823640L;
	private String makeName, modelName, year;
	private double basePrice; 
	private ArrayList<OptionSet> optionSetList;
	private ArrayList<Integer> optionSetOptionChoice;
	private ArrayList<String> optionSetNameReserved;

	public Automobile() {
		init();
		optionSetList = new ArrayList<OptionSet>();
		optionSetOptionChoice = new ArrayList<Integer>();
	}

	public Automobile(int size) {
		init();
		optionSetList = new ArrayList<OptionSet>(size);
		optionSetOptionChoice = new ArrayList<Integer>(size);
	}

	public void init() {
		makeName = "";
		modelName = "";
		year = "";
		basePrice = 0;
		optionSetNameReserved = new ArrayList<String>();
		optionSetNameReserved.add("Make");
		optionSetNameReserved.add("Model");
		optionSetNameReserved.add("Year");
		optionSetNameReserved.add("Retail Price");
	}


	public synchronized String getMake() {
		return makeName;
	}

	public synchronized String getModel() {
		return modelName;
	}

	public synchronized String getYear() {
		return year;
	}

	public synchronized double getPrice() {
		return basePrice;
	}

	
	private synchronized OptionSet getOptionSet(int OptionSetIndex) {
		OptionSet optionSetObject = null;
		try {
			optionSetObject = optionSetList.get(OptionSetIndex);
		} catch (NullPointerException e) {
			System.out.println("Intentional NullPointerException from getOptionSet");
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Intentional ArrayIndexOutOfBoundsException from getOptionSet");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Intentional Exception from getOptionSet");
			e.printStackTrace();
		}
		return optionSetObject;
	}

	public synchronized String getOptionSetName(int OptionSetIndex) {
		OptionSet optionSetObject = null;
		try {
			optionSetObject = optionSetList.get(OptionSetIndex);
		} catch (NullPointerException e) {
			System.out.println("Intentional NullPointerException from getOptionSet");
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Intentional ArrayIndexOutOfBoundsException from getOptionSet");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Intentional Exception from getOptionSet");
			e.printStackTrace();
		}
		return optionSetObject.getName();
	}

	public synchronized int getOptionSetLength(int OptionSetIndex) {
		OptionSet optionSetObject = null;
		try {
			optionSetObject = optionSetList.get(OptionSetIndex);
		} catch (NullPointerException e) {
			System.out.println("Intentional NullPointerException from getOptionSet");
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Intentional ArrayIndexOutOfBoundsException from getOptionSet");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Intentional Exception from getOptionSet");
			e.printStackTrace();
		}
		return optionSetObject.length();
	}

	public synchronized int length() {
		return optionSetList.size();
	}


	public synchronized String getOptionSetChoiceName(String optionSetName) {
		String returnValue = null;
		Option optionObject = null;
		int optionSetIndex;
		try {
			optionSetIndex = findOptionSetIndex(optionSetName);
			optionObject = getOptionSetChoiceByIndex(optionSetIndex);
		} catch (exception.AutoException e) {
		}
		if (optionObject != null) {
			returnValue = optionObject.getName();
		}
		return returnValue;
	}

	public synchronized String getOptionSetChoiceName(int optionSetIndex) {
		String returnValue = null;
		Option optionObject = null;
		try {
			optionObject = getOptionSetChoiceByIndex(optionSetIndex);
		} catch (exception.AutoException e) {
		}
		if (optionObject != null) {
			returnValue = optionObject.getName();
		}
		return returnValue;
	}

	
	public synchronized Double getOptionSetChoicePrice(String optionSetName) {
		Double returnValue = null;
		Option optionObject = null;
		int optionSetIndex;
		try {
			optionSetIndex = findOptionSetIndex(optionSetName);
			optionObject = getOptionSetChoiceByIndex(optionSetIndex);
		} catch (exception.AutoException e) {
		}
		if (optionObject != null) {
			returnValue = optionObject.getPrice();
		}
		return returnValue;
	}

	public synchronized Double getOptionSetChoicePrice(int optionSetIndex) {
		Double returnValue = null;
		Option optionObject = null;
		try {
			optionObject = getOptionSetChoiceByIndex(optionSetIndex);
		} catch (exception.AutoException e) {
		}
		if (optionObject != null) {
			returnValue = optionObject.getPrice();
		}
		return returnValue;
	}


	private synchronized Option getOptionSetChoiceByIndex(int optionSetIndex) throws AutoException {
		Option returnValue = null;
		int optionIndex;
		try {
			optionIndex = optionSetOptionChoice.get(optionSetIndex).intValue();
			returnValue = getOptionSet(optionSetIndex).getOption(optionIndex);
		} catch (IndexOutOfBoundsException e) {
			throw new exception.AutoException(800);
		} catch (NullPointerException e) {
			throw new exception.AutoException(801);
		}
		return returnValue;
	}

	public synchronized Double getChoiceTotalPrice() {
		Double returnValue = null;
		double totalPrice = 0;
		totalPrice += getPrice();
		int i, n;
		n = length();
		for (i = 0; i < n; i++) {
			Double optionSetChoicePrice = getOptionSetChoicePrice(i);
			if (optionSetChoicePrice != null) {
				totalPrice += getOptionSetChoicePrice(i).doubleValue();
			}
		}
		returnValue = new Double(totalPrice);
		return returnValue;
	}


	public synchronized String getOptionSetOptionName(String optionSetName, String optionName) {
		String returnValue = null;
		OptionSet optionSetObject = findOptionSet(optionSetName);
		if (optionSetObject != null) {
			if (!isOptionSetReserved(optionSetObject)) {
				Option optionObject = optionSetObject.findOption(optionName);
				if (optionObject != null) {
					returnValue = optionObject.getName();
				}
			}
		}
		return returnValue;
	}

	public synchronized String getOptionSetOptionName(int optionSetIndex, int optionSetOptionIndex) {
		String returnValue = null;
		OptionSet optionSetObject = getOptionSet(optionSetIndex);
		if (optionSetObject != null) {
			if (!isOptionSetReserved(optionSetObject)) {
				Option optionObject = null;
				try {
					optionObject = optionSetObject.getOption(optionSetOptionIndex);
				} catch (AutoException e) {
				}
				if (optionObject != null) {
					returnValue = optionObject.getName();
				}
			}
		}
		return returnValue;
	}


	public synchronized Double getOptionSetOptionPrice(String optionSetName, String optionName) {
		Double returnValue = null;
		OptionSet optionSetObject = findOptionSet(optionSetName);
		if (optionSetObject != null) {
			if (!isOptionSetReserved(optionSetObject)) {
				Option optionObject = optionSetObject.findOption(optionName);
				if (optionObject != null) {
					returnValue = optionObject.getPrice();
				}
			}
		}
		return returnValue;
	}

	public synchronized Double getOptionSetOptionPrice(int optionSetIndex, int optionSetOptionIndex) {
		double returnValue = 0;
		OptionSet optionSetObject = getOptionSet(optionSetIndex);
		if (optionSetObject != null) {
			if (!isOptionSetReserved(optionSetObject)) {
				Option optionObject = null;
				try {
					optionObject = optionSetObject.getOption(optionSetOptionIndex);
				} catch (AutoException e) {
				}
				if (optionObject != null) {
					returnValue = optionObject.getPrice();
				}
			}
		}
		return returnValue;
	}


	public synchronized boolean isOptionSetReserved(OptionSet optionSetObject) {
		boolean returnValue = false;
		if (optionSetObject != null) {
			if (isOptionSetNameReserved(optionSetObject.getName())) {
				returnValue = true;
			}
		}
		return returnValue;
	}


	public synchronized boolean isOptionSetNameReserved(String optionSetName) {
		boolean returnValue = false;
		if (optionSetNameReserved.contains(optionSetName)) {
			returnValue = true;
		}
		return returnValue;
	}


	private synchronized OptionSet findOptionSet(String optionSetName) {
		OptionSet optionSetObject = null;
		int optionSetIndex = findOptionSetIndex(optionSetName);
		if (optionSetIndex != -1) {
			optionSetObject = getOptionSet(optionSetIndex);
		}
		return optionSetObject;
	}


	public synchronized int findOptionSetIndex(String optionSetName) {
		int returnValue, i, n;
		returnValue = -1;
		n = optionSetList.size();
		for (i = 0; i < n; i++) {
			if (optionSetList.get(i).getName().equals(optionSetName)) {
				returnValue = i;
				break;
			}
		}
		return returnValue;
	}


	public synchronized int findOptionSetOptionIndex(int OptionSetIndex, String optionName) {
		int returnIndex = -1;
		OptionSet optionSetObject = getOptionSet(OptionSetIndex);
		if (optionSetObject != null) {
			returnIndex = optionSetObject.findOptionIndex(optionName);
		}
		return returnIndex;
	}

	
	public synchronized int addOptionSet(String OptionSetName) throws AutoException {
		if (OptionSetName.equals("")) {
			throw new exception.AutoException(802);
		}
		int returnValue = -1;
		if (!isOptionSetNameReserved(OptionSetName)) {
			if (optionSetList.add(new OptionSet(OptionSetName, 10))) {
				returnValue = optionSetList.size() - 1;
				optionSetOptionChoice.add(0);
			}
		}
		return returnValue;
	}


	public synchronized int addOptionSetOption(int optionSetIndex, String optionName, double optionPrice) {
		int indexReturn = -1;
		OptionSet optionSetObject = getOptionSet(optionSetIndex);
		if (optionSetObject != null) {
			if (!setOptionSetOptionNameReserved(optionSetObject.getName(), optionName)) {
				indexReturn = optionSetObject.addOption(optionName, optionPrice);
			}
		}
		return indexReturn;
	}

	
	public synchronized void setMake(String name) {
		makeName = name;
	}

	public synchronized void setModel(String name) {
		modelName = name;
	}

	public synchronized void setYear(String name) {
		year = name;
	}

	public synchronized void setPrice(double price_) {
		basePrice = price_;
	}

	public synchronized boolean setOptionSetName(String optionSetName, String nameNew) {
		System.out.println("Method: setOptionSetName, update optionSet=" + optionSetName + " to " + nameNew);
		boolean returnValue = false;
		OptionSet optionSetObject = findOptionSet(optionSetName);
		if (optionSetObject != null) {
			optionSetObject.setName(nameNew);
			returnValue = true;
		}
		return returnValue;
	}


	public synchronized boolean setOptionSetOptionNameReserved(String optionSetName, String optionName) {
		boolean returnValue = false;
		if (optionSetNameReserved.contains(optionSetName)) {
			returnValue = true;
		}
		if (optionSetName.equals("Make")) {
			setMake(optionName);
		} else if (optionSetName.equals("Model")) {
			setModel(optionName);
		} else if (optionSetName.equals("Year")) {
			setYear(optionName);
		} else if (optionSetName.equals("Retail Price")) {
			setPrice(Double.parseDouble(optionName));
		}
		return returnValue;
	}


	public synchronized boolean setOptionSetOptionName(String optionSetName, String optionName, String nameNew) {
		System.out.println("Method: setOptionSetOptionName, optionSet=" + optionSetName + " update option=" + optionName
			+ " to " + nameNew);
		boolean returnValue = false;
		if (!setOptionSetOptionNameReserved(optionSetName, nameNew)) {
			OptionSet optionSetObject = findOptionSet(optionSetName);
			if (optionSetObject != null) {
				Option optionObject = optionSetObject.findOption(optionName);
				if (optionObject != null) {
					optionObject.setName(nameNew);
					returnValue = true;
				}
			}
		} else {
			returnValue = true;
		}
		return returnValue;
	}


	public synchronized boolean setOptionSetOptionPrice(String optionSetName, String optionName, double priceNew) {
		boolean returnValue = false;
		if (!setOptionSetOptionNameReserved(optionSetName, Double.toString(priceNew))) {
			OptionSet optionSetObject = findOptionSet(optionSetName);
			if (optionSetObject != null) {
				Option optionObject = optionSetObject.findOption(optionName);
				if (optionObject != null) {
					optionObject.setPrice(priceNew);
					returnValue = true;
				}
			}
		} else {
			returnValue = true;
		}
		return returnValue;
	}

	public synchronized boolean setOptionSetChoiceByIndex(int optionSetIndex, int optionIndex) throws AutoException {
		boolean returnValue = false;
		try {
			optionSetOptionChoice.set(optionSetIndex, optionIndex);
			returnValue = true;
		} catch (IndexOutOfBoundsException e) {
			throw new exception.AutoException(800);
		}
		return returnValue;
	}

	public synchronized boolean setOptionSetChoice(String optionSetName, String optionName) {
		System.out.println("Method: setOptionSetChoice, choose optionSet=" + optionSetName + " option=" + optionName);
		boolean returnValue = false;
		int optionSetIndex, optionIndex;
		try {
			optionSetIndex = findOptionSetIndex(optionSetName);
			optionIndex = findOptionSetOptionIndex(optionSetIndex, optionName);
			setOptionSetChoiceByIndex(optionSetIndex, optionIndex);
			returnValue = true;
		} catch (exception.AutoException e) {
		}
		return returnValue;
	}

	public synchronized void print() {
		System.out.print(toString());
	}

	public synchronized String toString() {
		StringBuffer stringBufferObject;
		int i, n;
		n = length();
		stringBufferObject = new StringBuffer("");
		stringBufferObject.append("Year Make Model: ").append(getYear()).append(" ").append(getMake()).append(" ")
			.append(getModel()).append(" with Base Price: $").append(getPrice());
		stringBufferObject.append(System.getProperty("line.separator"));
		for (i = 0; i < n; i++) {
			stringBufferObject.append(optionSetList.get(i).toString()).append(System.getProperty("line.separator"));
		}
		return stringBufferObject.toString();
	}

	
}