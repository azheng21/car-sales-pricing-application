package util;

import java.io.*;
import java.util.Map;

import model.Automobile;
import exception.AutoException;

public class FileIO {

	public Automobile fileToAutomobile(String fileName) throws AutoException {
		Automobile automobileObject = new model.Automobile();
		addToAutomobile(fileName, automobileObject);
		return automobileObject;
	}


	public void addToAutomobile(String fileName, Automobile automobileObject) throws AutoException {
		String optionSetOptions, optionSetName, lineNext;

		BufferedReader reader = null;
		int optionSetObjectIndex = -1;
		optionSetName = optionSetOptions = null;

		try {
			reader = new BufferedReader(new FileReader(new File(fileName)));
			while ((lineNext = reader.readLine()) != null) {
				if (lineNext.indexOf(':') != -1) {
					String[] optionSetParts = lineNext.split(":");
					optionSetName = optionSetParts[0].trim();
					optionSetOptions = optionSetParts[1].trim();
					try {
						optionSetObjectIndex = automobileObject.addOptionSet(optionSetName);
					} catch (AutoException e) {
						optionSetObjectIndex = -1;
					}
				} else {
					
					optionSetOptions = lineNext;
				}
				if (optionSetObjectIndex != -1) {
					try {
						optionSetOptionsProcess(automobileObject, optionSetObjectIndex, optionSetOptions);
					} catch (AutoException e) {
						optionSetObjectIndex = -1;
					}
				} else if (optionSetName != null && !optionSetName.equals("") && optionSetOptions != null
					&& !optionSetOptions.equals("")) {
					automobileObject.setOptionSetOptionNameReserved(optionSetName, optionSetOptions);
				}
			}
		} catch (FileNotFoundException e) {
			throw new exception.AutoException(200);
		} catch (IOException e) {
			throw new exception.AutoException(201);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw new exception.AutoException(200);
			}
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
					automobileObject.addOptionSetOption(optionSetObjectIndex, optionName,
						Double.parseDouble(optionPrice));
				} else {
					automobileObject.addOptionSetOption(optionSetObjectIndex, optionPart.trim(), 0);
				}
			} else {
				new exception.AutoException(102, true); 
			}
		}
	}

	
	public void serialize(String fileName, Automobile automobileObject) throws AutoException {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(automobileObject);
			out.close();
			fileOut.close();
		} catch (NullPointerException e) {
			throw new exception.AutoException(300);
		} catch (IOException e) {
			throw new exception.AutoException(300);
		}
	}

	
	public Automobile deserialize(String fileName) throws AutoException {
		Automobile automobileObject = null;
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			automobileObject = (Automobile) in.readObject();
			in.close();
			fileIn.close();
		} catch (NullPointerException e) {
			throw new exception.AutoException(300);
		} catch (IOException e) {
			throw new exception.AutoException(300);
		} catch (ClassNotFoundException e) {
			throw new exception.AutoException(300);
		}
		return automobileObject;
	}

	
	public void serializeToStream(OutputStream socketStreamOut, Automobile automobileObject) throws AutoException {
		if (automobileObject == null) {
			throw new exception.AutoException(302);
		}
		try {
			ObjectOutputStream out = new ObjectOutputStream(socketStreamOut);
			out.writeObject(automobileObject);
		
		} catch (NullPointerException e) {
			throw new exception.AutoException(300);
		} catch (IOException e) {
			throw new exception.AutoException(300);
		}
	}

	
	public Automobile deserializeFromStream(InputStream socketStreamIn) throws AutoException {
		Automobile automobileObject = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socketStreamIn);
			automobileObject = (Automobile) in.readObject();
			
		} catch (NullPointerException e) {
			throw new exception.AutoException(300);
		} catch (IOException e) {
			throw new exception.AutoException(300);
		} catch (ClassNotFoundException e) {
			throw new exception.AutoException(300);
		}
		return automobileObject;
	}
	
	
	public void directorySerializeToStream(OutputStream socketStreamOut, model.AutomobileTable.Directory mapObject) throws AutoException {
		if (mapObject == null) {
			throw new exception.AutoException(300);
		}
		try {
			ObjectOutputStream out = new ObjectOutputStream(socketStreamOut);
			out.writeObject(mapObject);
			
		} catch (NullPointerException e) {
			throw new exception.AutoException(301);
		} catch (IOException e) {
			throw new exception.AutoException(301);
		}
	}

	
	public model.AutomobileTable.Directory directoryDeserializeFromStream(InputStream socketStreamIn) throws AutoException {
		model.AutomobileTable.Directory mapObject = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socketStreamIn);
			mapObject = (model.AutomobileTable.Directory) in.readObject();
			
		} catch (NullPointerException e) {
			throw new exception.AutoException(300);
		} catch (IOException e) {
			throw new exception.AutoException(300);
		} catch (ClassNotFoundException e) {
			throw new exception.AutoException(300);
		}
		return mapObject;
	}

	public void print() {
		System.out.print(toString());
	}

	public String toString() {
		return "FileIO Utility";
	}
}