package ca.ucalgary.seng300.a3.enums;

public enum OutputMethod {
	TEXT_LOG(0), DISPLAY(1), LOOPING_MESSAGE(2), CONFIG_PANEL_DISPLAY(3);
	
	private final int value;

    private OutputMethod(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
