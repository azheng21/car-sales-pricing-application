package driver;

//import model.Automobile;
import adapter.*;

public class Driver {

	
	public static void main(String[] args) {
		BuildAuto buildAutoInterface = new BuildAuto();
		buildAutoInterface.init(); 
		String FordZTWAutomobileKey = buildAutoInterface.buildAuto("FordZTW.txt", "text");
		String BMW320iAutomobileKey = buildAutoInterface.buildAuto("BMW320i.txt", "text");
		if (FordZTWAutomobileKey != null) {
			buildAutoInterface.printAuto(FordZTWAutomobileKey);
			buildAutoInterface.updateOptionSetName(FordZTWAutomobileKey, "Color", "Colors");
			buildAutoInterface.updateOptionPrice(FordZTWAutomobileKey, "Transmission", "automatic", 50);
			buildAutoInterface.setOptionChoice(FordZTWAutomobileKey, "Transmission", "manual");
			buildAutoInterface.serialize(FordZTWAutomobileKey, "FordZTW.data");
		} else {
			System.out.println("Could not build automobile");
		}
		String FordZTWAutomobileKey2 = buildAutoInterface.deserialize("FordZTW.data");
		if (FordZTWAutomobileKey2 != null) {
			buildAutoInterface.printAuto(FordZTWAutomobileKey2);
			String optionName = buildAutoInterface.getOptionChoice(FordZTWAutomobileKey, "Transmission");
			if (optionName != null) {
				System.out.println("Transmission choice: " + optionName);
			}
			Double optionPrice = buildAutoInterface.getOptionChoicePrice(FordZTWAutomobileKey, "Transmission");
			if (optionName != null) {
				System.out.println("Transmission choice price: $" + optionPrice);
			}
		} else {
			System.out.println("could not deserialize automobile");
		}
	}

}