package EnumTypes;


public enum OutputDataType {
	WELCOME_MESSAGE_TEXT(0), CREDIT_INFO(1), BUTTON_PRESSED(2), EXCEPTION_HANDELING(3), VALID_COIN_INSERTED(4), COIN_REFUNDED(5); 
	
	private final int value;

    private OutputDataType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
	

