package theoryOfComputation;
import java.util.*;

public class DeterministicFA {
		
	private ArrayList<State> statesInAutomaton;
	private ArrayList<State> finalStates;
	private State initialState;
	private ArrayList<String> alphabet;
	private Map<State, Map<String, State>> allTransitions;
	
	
	
	public DeterministicFA()
	{
		this.initialState = new State("1"); //default initial state - 1!
		this.statesInAutomaton = new ArrayList<State>();
		//this.rawTransitions = new ArrayList<String>();
		this.finalStates = new ArrayList<State>();
		this.alphabet = new ArrayList<String>();
		this.allTransitions = new HashMap<State, Map<String, State>>();
	}
	
	//Initial State
	public void setInitailState(State initialState)
	{
		this.initialState = this.getEquviState(initialState);
	}
	
	public State getInitialState()
	{
		return this.initialState;
	}
	
	//FINAL states!
	public void setFinalStates(ArrayList<State> newFinalStates)
	{
		for(State s: newFinalStates)
		{
			this.finalStates.add(this.getEquviState(s));
		}
	}
	
	public ArrayList<State> getFinalStates()
	{
		return this.finalStates;
	}
	
	//after all states have been saved in statesInAutomaton arraylist, this method will return the similar object from stateInAutomaton
	//thus will prevent creation of new objects now and then.
	public State getEquviState(State aState)
	{
		for(State s: this.statesInAutomaton)
		{
			if(s.getStateName().equals(aState.getStateName().trim()))
			{
				return s;
			}
		}
		return null; //this line never executes!
	}
	
	//Alphabet of the DFA!
	public void setAlphabet(ArrayList<String> alphabet)
	{
		this.alphabet = alphabet;
	}
	
	public ArrayList<String> getAlphabet()
	{
		return this.alphabet;
	}
	
	
	public Map<State, Map<String, State>> getAllTransition()
	{
		return this.allTransitions;
	}
	
	//this method is specially created for use in NonDeterministicToDeterministicFA
	//the states are set using setAllTransitions method for a general DFA!
	public void setStatesInAutomaton(ArrayList<State> rawStatesInAutomaton)
	{		
		this.statesInAutomaton = rawStatesInAutomaton;
	}
	
	public ArrayList<State> getStatesInAutomaton()
	{
		return this.statesInAutomaton;
	}
	
	//set one particular transition
	public void setTransition(State sourceState, String ipAlpha, State destinationState)
	{
		Map<String, State> destinationForState = null;
		if(this.allTransitions.containsKey(sourceState) == true)
			destinationForState = allTransitions.get(sourceState);
		else
			destinationForState = new HashMap<String, State>();
		
		destinationForState.put(ipAlpha, destinationState);
		this.allTransitions.put(sourceState, destinationForState);
	}
	
	//this method iteratively calls setTransition() method to set one particular transition and hence sets all the transitions in the DFA.
	public void setAllTransitions(ArrayList<String> rawTransitions)
	{
		//ArrayList<State> tempStateArray = new ArrayList<State>();
		boolean stateExist = false;
		State tempSource = null;
		State tempDesti = null;
		
		for(Object aRawTransition : rawTransitions.toArray())
		{
			String[] transition = aRawTransition.toString().split(" ");
			if(this.statesInAutomaton.size() > 0)
			{
					for(State s : this.statesInAutomaton)
					{
						if(s.getStateName().equals(transition[0]))
						{
							stateExist = true;
							tempSource = s;
						}
					}
					if(stateExist == false)
					{
						tempSource = new State(transition[0]);
						this.statesInAutomaton.add(tempSource);
					}
			}
			else if(this.statesInAutomaton.size() == 0)
			{
				tempSource = new State(transition[0]);
				this.statesInAutomaton.add(tempSource);
			}
				
			//for destination state
			stateExist = false;
			if(this.statesInAutomaton.size() > 0)
			{
					for(State s : this.statesInAutomaton)
					{
						if(s.getStateName().equals(transition[2]))
						{
							stateExist = true;
							tempDesti = s;
						}
					}
					if(stateExist == false)
					{
						tempDesti = new State(transition[2]);
						this.statesInAutomaton.add(tempDesti);
					}
			}
			this.setTransition(tempSource, transition[1], tempDesti);
		}
	}
	
	public void setAllTransition(Map<State, Map<String,State>> allTransitions)
	{
		this.allTransitions = allTransitions;
	}
	
}
