package main.generate;

/**
 * The Parameter class represents one parameter for a module. It is intended
 * that the parameter can be altered by the user via an input mask on the GUI.
 * Currently the Parameter class supports float, int and bool parameters.
 * 
 * @author Fredo
 *
 */
public class Parameter {

	/**
	 * The ParameterType indicates which type of parameter is stored.
	 * 
	 * @author Fredo
	 *
	 */
	public enum ParameterType {
		P_INT, P_FLOAT, P_BOOL
	}

	/** The name of the parameter that is displayed in the GUI */
	private String name;
	
	/** The description of the parameters purpose */
	private String description;
	
	/** The value of the parameter if it is a float */
	private float floatValue;
	
	/** The value of the parameter if it is a bool */
	private boolean boolValue;
	
	/** The value of the parameter if it is an int */
	private int intValue;
	
	/** The type of the parameter */
	private ParameterType type;

	/**
	 * Creates a new float type parameter.
	 * 
	 * @param name The name of the parameter
	 * @param description The description of the parameters purpose
	 * @param value The float value of the parameter
	 */
	public Parameter(String name, String description, float value) {
		this.name = name;
		this.description = description;
		this.floatValue = value;
		this.type = ParameterType.P_FLOAT;
	}

	/**
	 * Creates a new int type parameter.
	 * 
	 * @param name The name of the parameter
	 * @param description The description of the parameters purpose
	 * @param value The int value of the parameter
	 */
	public Parameter(String name, String description, int value) {
		this.name = name;
		this.description = description;
		this.intValue = value;
		this.type = ParameterType.P_INT;
	}

	/**
	 * Creates a new bool type parameter.
	 * 
	 * @param name The name of the parameter
	 * @param description The description of the parameters purpose
	 * @param value The bool value of the parameter
	 */
	public Parameter(String name, String description, boolean value) {
		this.name = name;
		this.description = description;
		this.boolValue = value;
		this.type = ParameterType.P_BOOL;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public boolean getBoolValue() {
		return boolValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public ParameterType getType() {
		return type;
	}

	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}

	public void setBoolValue(boolean boolValue) {
		this.boolValue = boolValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
}
