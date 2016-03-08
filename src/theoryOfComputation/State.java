package theoryOfComputation;

public class State {
	
	private String stateName;
	
	public State()
	{
		//to avoid errors
	}
	
	public State(String stateName)
	{
		this.stateName = stateName;
	}
	
	public void setStateName(String stateName)
	{
		this.stateName = stateName;
	}
	public String getStateName()
	{
		return this.stateName;
	}

}
