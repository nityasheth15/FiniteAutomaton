package theoryOfComputation;

import java.util.*;

public class NonDeterministicFA {
	
	private ArrayList<State> statesInAutomaton;
	private ArrayList<State> finalStates;
	private State initialState;
	private ArrayList<String> alphabet;
	private Map<State, Map<String, HashSet<State>>> allTransitions; //because, there could be more than one possible state transitions.

	
	public NonDeterministicFA()
	{
		this.initialState = new State("1"); //default initial state - 1!
		this.statesInAutomaton = new ArrayList<State>();
		this.finalStates = new ArrayList<State>();
		this.alphabet = new ArrayList<String>();
		this.allTransitions = new HashMap<State, Map<String, HashSet<State>>>();
	}
	
	public ArrayList<State> getStatesInAutomaton()
	{//check the need for set!
		return this.statesInAutomaton;
	}
	
	public void setInitailState(State initialState)
	{
		this.initialState = this.getEquviState(initialState);
	}
	
	public State getInitialState()
	{
		return this.initialState;
	}
	
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
	
	public void setAlphabet(ArrayList<String> alphabet)
	{
		this.alphabet = alphabet;
	}
	
	public ArrayList<String> getAlphabet()
	{
		return this.alphabet;
	}
	
	
	public Map<State, Map<String, HashSet<State>>> getAllTransition()
	{
		return this.allTransitions;
	}
	
	public void setTransition(State sourceState, String ipAlpha, State destinationState)
	{
		Map<String,HashSet<State>>destinationsForAlpha = null;
		HashSet<State>destinationStates = null;
		
		if(this.allTransitions.containsKey(sourceState) == true)
		{
			destinationsForAlpha = this.allTransitions.get(sourceState);
			if(destinationsForAlpha.containsKey(ipAlpha))
				destinationStates = destinationsForAlpha.get(ipAlpha);	
			else
				destinationStates = new HashSet<State>();
			
			destinationStates.add(destinationState);
			destinationsForAlpha.put(ipAlpha, destinationStates);
		}
		else
		{
			destinationsForAlpha = new HashMap<String,HashSet<State>>();
			destinationStates = new HashSet<State>();
			destinationStates.add(destinationState);
			destinationsForAlpha.put(ipAlpha, destinationStates);
		}
		this.allTransitions.put(sourceState, destinationsForAlpha);
	}
	
	public void setAllTransitions(ArrayList<String> rawTransitions)
	{
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

	public HashSet<State> getTransition(State fromState, String alphabet) {
		if (this.allTransitions.containsKey(fromState)) {
			Map<String, HashSet<State>> stateTransition = this.allTransitions.get(fromState);
			if (stateTransition.containsKey(alphabet)) {
                return stateTransition.get(alphabet);
			}
		} 
		return null;
	}
}
