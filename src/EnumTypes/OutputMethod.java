package EnumTypes;

public enum OutputMethod {
	TEXT_LOG(0), DISPLAY(1), LOOPING_MESSAGE(2);
	
	private final int value;

    private OutputMethod(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
