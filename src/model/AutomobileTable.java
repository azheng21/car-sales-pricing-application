package model;

import java.util.*;
import java.io.Serializable;

import exception.AutoException;


public class AutomobileTable implements Serializable {
	private static final long serialVersionUID = -6524925073605314987L;
	private Map<String, Automobile> automobileTable;

	
	public AutomobileTable() {
		automobileTable = new LinkedHashMap<String, Automobile>(64);
	}

	public AutomobileTable(int capacitySize) {
		automobileTable = new LinkedHashMap<String, Automobile>(capacitySize);
	}

	public Automobile getByKey(String automobileKey) {
		return automobileTable.get(automobileKey);
	}

	
	public String getKey(Automobile automobileObject) throws AutoException {
		if (automobileObject == null)
			throw new exception.AutoException(502);
		return automobileObject.getMake() + "-" + automobileObject.getModel() + "-" + automobileObject.getYear();
	}


	public Iterator<Map.Entry<String, Automobile>> getIterator() {
		return automobileTable.entrySet().iterator();
	}

	public Directory toDirectory() {
		Directory automobileDirectory = new Directory();
		automobileDirectory.map = new LinkedHashMap<String, String>(automobileTable.size());
		Iterator<Map.Entry<String, Automobile>> mapIterator = automobileTable.entrySet().iterator();
		while (mapIterator.hasNext()) {
			Map.Entry<String, Automobile> mapEntry = mapIterator.next();
			StringBuffer carNameBuffer = new StringBuffer();
			carNameBuffer.append(mapEntry.getValue().getYear());
			carNameBuffer.append(" ").append(mapEntry.getValue().getMake()).append(" ")
				.append(mapEntry.getValue().getModel());
			automobileDirectory.map.put(mapEntry.getKey(), carNameBuffer.toString());
		}
		return automobileDirectory;
	}

	
	public String insertOverwrite(Automobile automobileObject) throws AutoException {

		String automobileKey = null;
		try {
			automobileKey = getKey(automobileObject);
			automobileTable.put(automobileKey, automobileObject);
		} catch (NullPointerException e) {
			// Automobile could not be added to database
			throw new exception.AutoException(501);
		} catch (Exception e) {
			// Automobile could not be added to database
			throw new exception.AutoException(501);
		}
		return automobileKey;
	}


	public String insertWrapper(Automobile automobileObject) throws AutoException {
		int tryNumber = 1;
		String automobileKey = null;
		automobileKey = getKey(automobileObject);
		while (tryNumber > 0) {
			try {
				insert(automobileKey, automobileObject);
			} catch (exception.AutoException e) {
				if (tryNumber > 1) {
					tryNumber = -5;
				}
				e.setAutomobile(automobileObject);
				automobileKey = e.fixString(500);
				tryNumber += 1;
			}
		}
		return automobileKey;
	}

	public boolean insert(String automobileKey, Automobile automobileObject) throws AutoException {
		boolean returnValue = false;
		if (automobileTable.containsKey(automobileKey)) {
			throw new exception.AutoException(500);
		} else {
			automobileTable.put(automobileKey, automobileObject);
			returnValue = true;
		}
		return returnValue;
	}

	public void print() {
		System.out.print(toString());
	}

	public String toString() {
		return "Automobile Table";
	}
	
	public class Directory implements Serializable {
		private static final long serialVersionUID = 8187458227654283135L;
		public Map<String, String> map;
	}
}