package main.module;

public class Parameter {

	public enum ParameterType {
		P_INT, P_FLOAT, P_BOOL
	}

	private String name;
	private String description;
	private float floatValue;
	private boolean boolValue;
	private int intValue;
	private ParameterType type;

	public Parameter(String name, String description, float value) {
		this.name = name;
		this.description = description;
		this.floatValue = value;
		this.type = ParameterType.P_FLOAT;
	}

	public Parameter(String name, String description, int value) {
		this.name = name;
		this.description = description;
		this.intValue = value;
		this.type = ParameterType.P_INT;
	}

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
