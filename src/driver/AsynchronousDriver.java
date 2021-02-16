package driver;

import adapter.*;
import scale.EditOptions;

public class AsynchronousDriver {


	public static void main(String[] args) {
		BuildAuto buildAutoInterface = new BuildAuto();
		buildAutoInterface.init(); 
		String FordZTWAutomobileKey = buildAutoInterface.buildAuto("FordZTW.txt", "text");
		if (FordZTWAutomobileKey != null) {
			buildAutoInterface.printAuto(FordZTWAutomobileKey);
			
			String input[] = { FordZTWAutomobileKey, "Color", "French Blue Clearcoat Metallic",
				"Cool California Blue" };
			String input2[] = { FordZTWAutomobileKey, "Color", "French Blue Clearcoat Metallic",
				"Sunshine Hawaiian Gold" };
			String input3[] = { FordZTWAutomobileKey, "Color", "Colors" };
			String input4[] = { FordZTWAutomobileKey, "Color", "French Blue Clearcoat Metallic" };
			buildAutoInterface.operation(0, input); 
			buildAutoInterface.operation(0, input2); 
			buildAutoInterface.operation(1, input3); 
			buildAutoInterface.operation(3, input4); 
		} else {
			System.out.println("Could not build automobile");
		}
	}

}