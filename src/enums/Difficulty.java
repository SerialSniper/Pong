package enums;

public enum Difficulty {
	NOOB(500),
	EASY(300),
	NORMAL(180),
	PRO(100),
	IMPOSSIBLE(1);
	
	private int value;
	
	public int getValue() {
		return value;
	}
	
	private Difficulty(int value) {
		this.value = value;
	}
}