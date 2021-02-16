package adapter;

import model.*;
import scale.EditOptions;

import java.io.*;
import java.util.*;

import exception.*;

public abstract class ProxyAutomobile {
	private static model.AutomobileTable automobileTable;
	private static int threadNumber;
	private util.FileIO fileIOUtil;
	private util.StreamIO streamIOUtil;
	public boolean threadAvailable = true;

	protected ProxyAutomobile() {
		fileIOUtil = new util.FileIO();
		streamIOUtil = new util.StreamIO();
	}

	public void init() {
		automobileTable = new AutomobileTable(64);
		threadNumber = 0;
	}

	
	public boolean updateOptionSetName(String automobileKey, String optionSetName, String nameNew) {
		boolean returnValue = false;
		model.Automobile automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			automobileObject.setOptionSetName(optionSetName, nameNew);
			returnValue = true;
		}
		return returnValue;
	}

	public boolean updateOptionPrice(String automobileKey, String optionSetName, String optionName, double priceNew) {
		boolean returnValue = false;
		model.Automobile automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			automobileObject.setOptionSetOptionPrice(optionSetName, optionName, priceNew);
			returnValue = true;
		}
		return returnValue;
	}

	public boolean updateOptionName(String automobileKey, String optionSetName, String optionName,
		String optionNameNew) {
		boolean returnValue = false;
		model.Automobile automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			automobileObject.setOptionSetOptionName(optionSetName, optionName, optionNameNew);
			returnValue = true;
		}
		return returnValue;
	}

	
	public String buildAuto(String fileName, String fileType) {
		String automobileKey = null;
		model.Automobile automobileObject = new model.Automobile();
		if (fileType.equals("text")) {
			try {
				fileIOUtil.addToAutomobile(fileName, automobileObject);
				automobileKey = automobileTable.insertWrapper(automobileObject);
			} catch (exception.AutoException e) {
			}
		} else if (fileType.equals("property")) {
			try {
				streamIOUtil.propertiesToAutomobile(streamIOUtil.fileToProperties(fileName), automobileObject);
				automobileKey = automobileTable.insertWrapper(automobileObject);
			} catch (exception.AutoException e) {
			}
		}
		return automobileKey;
	}

	public boolean printAuto(String automobileKey) {
		boolean returnValue = false;
		model.Automobile automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			returnValue = true;
			System.out.println(automobileObject.toString());
		}
		return returnValue;
	}

	
	public boolean serialize(String automobileKey, String fileName) {
		boolean returnValue = false;
		model.Automobile automobileObject;
		try {
			automobileObject = automobileTable.getByKey(automobileKey);
			fileIOUtil.serialize(fileName, automobileObject);
			returnValue = true;
		} catch (exception.AutoException e) {
		}
		if (returnValue) {
			System.out.println("Serialized data is saved in " + fileName);
		} else {
			System.out.println("Automobile could not be serialized");
		}
		return returnValue;
	}

	public String deserialize(String fileName) {
		String returnValue = null;
		model.Automobile automobileObject;
		try {
			automobileObject = fileIOUtil.deserialize(fileName);
			returnValue = automobileTable.insertWrapper(automobileObject);
			System.out.println("Deserialized data read from " + fileName);
		} catch (AutoException e) {
			System.out.println("Automobile could not be deserialized");
		}
		return returnValue;
	}


	public boolean setOptionChoice(String automobileKey, String optionSetName, String optionName) {
		boolean returnValue = false;
		model.Automobile automobileObject;
		automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			automobileObject.setOptionSetChoice(optionSetName, optionName);
			returnValue = true;
		}
		return returnValue;
	}

	public String getOptionChoice(String automobileKey, String optionSetName) {
		String returnValue = null;
		model.Automobile automobileObject;
		automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			returnValue = automobileObject.getOptionSetChoiceName(optionSetName);
		}
		return returnValue;
	}

	public Double getOptionChoicePrice(String automobileKey, String optionSetName) {
		Double returnValue = null;
		model.Automobile automobileObject;
		automobileObject = automobileTable.getByKey(automobileKey);
		if (automobileObject != null) {
			returnValue = automobileObject.getOptionSetChoicePrice(optionSetName);
		}
		return returnValue;
	}

	
	public void operation(int operationNumber, String[] inputArguments) {

		EditOptions editObtionsObject = new scale.EditOptions(this, operationNumber, threadNumber++, true,
			inputArguments);
		editObtionsObject.start();
	}

	
	public void operationSynchronous(int operationNumber, String[] inputArguments) {
		
		EditOptions editObtionsObject = new scale.EditOptions(this, operationNumber, threadNumber++, false,
			inputArguments);
		editObtionsObject.start();
	}

	
	public String buildAutomobileFromProperties(Properties automobileProperties) throws exception.AutoException {
		String automobileKey = null;
		model.Automobile automobileObject = new model.Automobile();
		streamIOUtil.propertiesToAutomobile(automobileProperties, automobileObject);
		automobileKey = automobileTable.insertOverwrite(automobileObject);
		return automobileKey;
	}

	public Properties propertiesFromStream(InputStream socketStreamIn) throws exception.AutoException {
		return streamIOUtil.deserializeToStream(socketStreamIn);
	}

	public String automobileFromStream(InputStream socketStreamIn) throws exception.AutoException {
		return automobileTable.insertOverwrite(fileIOUtil.deserializeFromStream(socketStreamIn));
	}

	
	public void automobileToStream(OutputStream socketStreamOut, String automobileKey) throws exception.AutoException {
		fileIOUtil.serializeToStream(socketStreamOut, automobileTable.getByKey(automobileKey));
	}

	public void directoryToStream(OutputStream socketStreamOut) throws exception.AutoException {
		fileIOUtil.directorySerializeToStream(socketStreamOut, automobileTable.toDirectory());
	}

	public String getAutomobileList() {
		StringBuffer listString = new StringBuffer();
		Iterator<Map.Entry<String, Automobile>> mapIterator = automobileTable.getIterator();
		while (mapIterator.hasNext()) {
			Map.Entry<String, Automobile> mapEntry = mapIterator.next();
			listString.append("Car ID: ").append(mapEntry.getKey()).append("\tName=")
				.append(mapEntry.getValue().getYear());
			listString.append(" ").append(mapEntry.getValue().getMake()).append(" ")
				.append(mapEntry.getValue().getModel());
			listString.append("\tRetail Price=$").append(mapEntry.getValue().getPrice()).append("\n");
		}
		return listString.toString();
	}

	public model.AutomobileTable.Directory getAutomobileDirectoryMap() {
		return automobileTable.toDirectory();
	}

	public Iterator<Map.Entry<String, Automobile>> getAutomobileIterator() {
		return automobileTable.getIterator();
	}
}