package theoryOfComputation;
import java.util.*;


public class IntersectFA 
{
	private DeterministicFA intersectDFA; 
	
	public ArrayList<State> createStatesForAutomaton(DeterministicFA dfa1, DeterministicFA dfa2)
	{
		ArrayList<State> statesInAutomaton1 = dfa1.getStatesInAutomaton() ;
		ArrayList<State> statesInAutomaton2 = dfa2.getStatesInAutomaton();
		ArrayList<State> statesInAutomaton = new ArrayList<State>();
		String newStateName = "";
		for(State stateDFA1 : statesInAutomaton1)
		{
			for(State stateDFA2 : statesInAutomaton2)
			{
					newStateName = stateDFA1.getStateName().trim() + "_".trim() +stateDFA2.getStateName().trim();
					statesInAutomaton.add(new State(newStateName));
			}
		}
		return statesInAutomaton;
	}
	
	public State createInitialStateForAutomaton(DeterministicFA dfa1, DeterministicFA dfa2)
	{
		return this.getEquivalentState(new State(dfa1.getInitialState().getStateName().trim() + "_".trim() + dfa2.getInitialState().getStateName().trim()));
	}
	
	
	public ArrayList<State> createFinalStatesForAutomaton(DeterministicFA dfa1, DeterministicFA dfa2)
	{
		ArrayList<State> finalStatesDFA1 = dfa1.getFinalStates();
		ArrayList<State> finalStatesDFA2 = dfa2.getFinalStates();
		ArrayList<State> finalStatesNewDFA = new ArrayList<State>();
		
		for(State stateDFA1 : finalStatesDFA1)
		{
			for(State stateDFA2 : finalStatesDFA2)
			{
				finalStatesNewDFA.add(this.getEquivalentState(new State(stateDFA1.getStateName().trim() + "_".trim() + stateDFA2.getStateName().trim())));
			}
		}
		return finalStatesNewDFA;
	}
	
	
	public DeterministicFA constructIntersectionFA(DeterministicFA dfa1, DeterministicFA dfa2)
	{
		this.intersectDFA = new DeterministicFA();
		ArrayList<String> alphabet =  dfa1.getAlphabet();
		this.intersectDFA.setAlphabet(alphabet); 
		ArrayList<State> statesInNewAutomaton = this.createStatesForAutomaton(dfa1, dfa2);
		this.intersectDFA.setStatesInAutomaton(statesInNewAutomaton);
		
		Map<State, Map<String, State>> transitionDFA1 = dfa1.getAllTransition();
		Map<State, Map<String, State>> transitionDFA2 = dfa2.getAllTransition();
		Map<State, Map<String, State>> transitionNewDFA = new HashMap<State, Map<String,State>>();
		
		//produces newTransitions!
		for(State s: statesInNewAutomaton)
		{
			String[] statesOfDFA = s.getStateName().split("_".trim());
			for(String a: alphabet)
			{
				//try using loop!
				//statesOfDFA will always have size 2.
				State source1 = dfa1.getEquviState(new State(statesOfDFA[0]));
				State source2 = dfa2.getEquviState(new State(statesOfDFA[1]));
				
				//take care for the exception.
				State destinationState1 = transitionDFA1.get(source1).get(a.trim()); 
				State destinationState2 = transitionDFA2.get(source2).get(a.trim());
				
				State sourceNew = this.getEquivalentState(new State(source1.getStateName().trim() + "_".trim() + source2.getStateName().trim()));
				State destinationNew = this.getEquivalentState(new State(destinationState1.getStateName().trim() + "_".trim() + destinationState2.getStateName().trim()));
				
				Map<String, State> destination = new HashMap<String, State>();
				if(transitionNewDFA.containsKey(sourceNew))
					destination = transitionNewDFA.get(sourceNew);
				else
					destination = new HashMap<String, State>();
				
				destination.put(a, destinationNew);
				transitionNewDFA.put(sourceNew, destination);
			}
		}
		
		this.intersectDFA.setAllTransition(transitionNewDFA);
		this.intersectDFA.setInitailState(this.createInitialStateForAutomaton(dfa1, dfa2));
		this.intersectDFA.setFinalStates(this.createFinalStatesForAutomaton(dfa1, dfa2));
		return intersectDFA;
	}
	
	
	public State getEquivalentState(State s)
	{
		ArrayList<State> statesInAutomaton = this.intersectDFA.getStatesInAutomaton();
		for(State state : statesInAutomaton)
		{
			if(state.getStateName().equals(s.getStateName()))
			{
				return state;
			}
		}
		return new State();
	}
}
