package ca.ucalgary.seng300.a3.enums;

public enum DisplayType {
FRONT_DISPLAY(0), BACK_PANEL_DISPKAY(1), UNKNOWN_DISPLAY(2);
	
	private final int value;

    private DisplayType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
	
	
}
