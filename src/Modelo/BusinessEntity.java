package Modelo;

public class BusinessEntity {
	
	public BusinessEntity() {
		this.state = States.NEW;
	}
	
	private States state;
	
	public States getState() {
		return state;
	}
	public void setState(States stat) {
		state = stat;
	}
	
	public enum States{
		DELETED,
		NEW,
		MODIFIED,
		UNMODIFIED,
	}

}
