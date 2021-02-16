package model;

import java.util.ArrayList;

import exception.AutoException;

class OptionSet implements java.io.Serializable {
	private static final long serialVersionUID = 5846223453457830887L;
	ArrayList<Option> optionList;
	private String optionSetName;

	protected OptionSet() {
		init();
		optionList = new ArrayList<Option>();
	}

	protected OptionSet(String name, int size) {
		init();
		optionSetName = name;
		optionList = new ArrayList<Option>(12);
	}

	protected void init() {
		optionSetName = "";
	}

	protected String getName() {
		return optionSetName;
	}

	protected Option getOption(int optionIndex) throws AutoException {
		Option optionObject = null;
		try {
			optionObject = optionList.get(optionIndex);
		} catch (IndexOutOfBoundsException e) {
			throw new exception.AutoException(803);
		}
		return optionObject;
	}

	protected int length() {
		return optionList.size();
	}


	protected Option findOption(String optionName) {
		Option optionObject = null;
		for (int i = 0; i < optionList.size(); i++) {
			try {
				if (optionList.get(i).getName().equals(optionName)) {
					optionObject = optionList.get(i);
				}
			} catch (NullPointerException e) {
			
				break;
			}
		}
		return optionObject;
	}


	protected int findOptionIndex(String optionName) {
		int returnValue, i, n;
		returnValue = -1;
		n = optionList.size();
		for (i = 0; i < n; i++) {
			try {
				if (optionList.get(i).getName().equals(optionName)) {
					returnValue = i;
					break;
				}
			} catch (NullPointerException e) {
				
				break;
			}
		}
		return returnValue;
	}

	protected void setName(String name) {
		optionSetName = name;
	}


	protected int addOption(String optionName, double optionPrice) {
		optionList.add(new Option(optionName, optionPrice));
		return optionList.size();
	}

	public void print() {
		System.out.print(toString());
	}

	public String toString() {
		StringBuffer stringBufferObject;
		int i, n;
		n = length();
		stringBufferObject = new StringBuffer("");
		stringBufferObject.append(getName()).append(": ");
		for (i = 0; i < n; i++) {
			try {
				stringBufferObject.append(getOption(i).toString());
			} catch (AutoException e) {
			}
			if (i < n - 1) {
				stringBufferObject.append(", ");
			}
		}
		return stringBufferObject.toString();
	}

	
}