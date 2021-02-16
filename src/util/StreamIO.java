package util;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

import model.Automobile;
import exception.AutoException;

public class StreamIO {
	
	public Properties fileToProperties(String fileName) throws AutoException {
		Properties automobileProperties = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(fileName);
			automobileProperties.load(input);
		} catch (FileNotFoundException e) {
			throw new exception.AutoException(900);
		} catch (IOException e) {
			throw new exception.AutoException(901);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new exception.AutoException(902);
				}
			}
		}
		return automobileProperties;
	}

	
	public void propertiesToAutomobile(Properties automobileProperties, Automobile automobileObject)
		throws AutoException {
		int optionSetObjectIndex = -1;
		Enumeration<?> enumerator = automobileProperties.propertyNames();
		while (enumerator.hasMoreElements()) {
			String optionSetName = (String) enumerator.nextElement();
			String optionSetOptions = automobileProperties.getProperty(optionSetName);
			optionSetObjectIndex = automobileObject.addOptionSet(optionSetName);
			if (optionSetObjectIndex == -1) {
				automobileObject.setOptionSetOptionNameReserved(optionSetName, optionSetOptions);
			} else {
				optionSetOptionsProcess(automobileObject, optionSetObjectIndex, optionSetOptions);
			}
		}
		if (automobileObject == null) {
			throw new exception.AutoException(901);
		}
	}

	
	private void optionSetOptionsProcess(Automobile automobileObject, int optionSetObjectIndex, String optionSetOptions)
		throws AutoException {
		if (optionSetOptions.equals("")) {
			throw new exception.AutoException(101);
		}
		String optionName, optionPrice;
		String[] optionParts;
		if (optionSetOptions.indexOf(',') != -1 && optionSetObjectIndex != -1) {
			optionParts = optionSetOptions.split(",");
		} else {
			optionParts = new String[] { optionSetOptions };
		}
		for (String optionPart : optionParts) {
			if (optionPart.trim().length() > 0) {
				if (optionPart.indexOf('/') != -1) {
					String[] optionValueParts = optionPart.split("/");
					optionName = optionValueParts[0].trim();
					optionPrice = optionValueParts[1].trim();
					if (optionName.equals("")) {
						new exception.AutoException(102, true); 
					}
					if (optionPrice.equals("")) {
						new exception.AutoException(103, true); 
					}
					try {
						automobileObject.addOptionSetOption(optionSetObjectIndex, optionName,
							Double.parseDouble(optionPrice));
					} catch (NumberFormatException e) {
						throw new exception.AutoException(105, true);
					}
				} else {
					if (optionSetObjectIndex >= 0) {
						automobileObject.addOptionSetOption(optionSetObjectIndex, optionPart.trim(), 0);
					} else {
						new exception.AutoException(104, true); 
					}
				}
			} else {
				new exception.AutoException(102, true); 
			}
		}
	}

	
	public void serializeToStream(OutputStream socketStreamOut, Properties automobileProperties) throws AutoException {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socketStreamOut);
			out.writeObject(automobileProperties);
			
		} catch (NullPointerException e) {
			throw new exception.AutoException(300);
		} catch (IOException e) {
			throw new exception.AutoException(300);
		}
	}


	public Properties deserializeToStream(InputStream socketStreamIn) throws AutoException {
		Properties automobileProperties = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socketStreamIn);
			automobileProperties = (Properties) in.readObject();
			
		} catch (NullPointerException e) {
			throw new exception.AutoException(300);
		} catch (IOException e) {
			throw new exception.AutoException(300);
		} catch (ClassNotFoundException e) {
			throw new exception.AutoException(300);
		}
		return automobileProperties;
	}

	public void print() {
		System.out.print(toString());
	}

	public String toString() {
		return "StreamIO Utility";
	}
}