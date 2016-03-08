package theoryOfComputation;

import java.util.LinkedList;
import java.util.*;


public class NonDeterministicFAToDeterministicFA {

	DeterministicFA dfa = new DeterministicFA();
	Queue<State> processStates = new LinkedList<State>();
	ArrayList<String> alphabetForDFA = new ArrayList<String>();
	ArrayList<State> statesForDFA = new ArrayList<State>();
	State nullState = new State("ns");
	
	public  DeterministicFA convertNFAToDFA(NonDeterministicFA nfa)
	{
		processStates.add(nfa.getInitialState()); //add null state to processState if necessary
		statesForDFA.add(nfa.getInitialState());
		alphabetForDFA = nfa.getAlphabet();
		
		this.dfa.setAlphabet(alphabetForDFA);
		HashSet<State> destinationStates = null;
		
		Map<State,Map<String, State>> dfaTransitions = dfa.getAllTransition();
		Map<String, State> destinationForTransition = new HashMap<String, State>();
		
		Map<State, Map<String, HashSet<State>>> nfaTransitions = nfa.getAllTransition();
		
		while(processStates.isEmpty() == false)
		{
			destinationStates = null;
			State stateToProcess = processStates.poll();
			for(String a: alphabetForDFA)
			{
				String[] stateNames = stateToProcess.getStateName().split("-");
				if(stateNames.length == 1)
				{
					if(nfaTransitions.get(stateToProcess) != null)
						destinationStates = nfaTransitions.get(stateToProcess).get(a.trim());
				}
				else if(stateNames.length>1)
				{
					destinationStates = new HashSet<State>();
					for(String state : stateNames)
					{
						if(dfaTransitions.get(nfa.getEquviState(new State(state))).get(a.trim()).getStateName() != this.nullState.getStateName())
						{
							State temp = dfaTransitions.get(nfa.getEquviState(new State(state))).get(a.trim());
							destinationStates.add(temp); 
						}
					}
					//to remove the common states! 
					HashSet<String> rawStringState = new HashSet<String>();
					for(State s : destinationStates)
					{
						String[] stringState = s.getStateName().split("-");
						rawStringState.addAll(Arrays.asList(stringState));
					} 
					destinationStates.clear();
					for(String s : rawStringState)
					{
						destinationStates.add(nfa.getEquviState(new State(s)));
					}
				}
				State newState = this.createNewState(destinationStates);
				if(newState.getStateName().equals("")) //remove the code later!
					newState = this.nullState;
				if(statesForDFA.contains(newState) == false)
				{
					statesForDFA.add(newState);
					processStates.add(newState);
				}
				
				if(dfaTransitions.containsKey(stateToProcess) == true)
					destinationForTransition = dfaTransitions.get(stateToProcess);
				else
					destinationForTransition = new HashMap<String, State>();
					
				destinationForTransition.put(a.trim(), newState);
				dfaTransitions.put(stateToProcess, destinationForTransition);
			}
		}
		this.dfa.setAllTransition(dfaTransitions);
		this.dfa.setStatesInAutomaton(this.statesForDFA);
		this.dfa.setInitailState(nfa.getInitialState());
		this.dfa.setFinalStates(this.getFinalStatesForDFA(nfa));
		return this.dfa;
	}
	
	public State createNewState(HashSet<State> destinationStates)
	{
		String raw = "";
		if(destinationStates != null)
		{
			for(State s: destinationStates)
			{
				if(statesForDFA.contains(s) == false)
				{
					statesForDFA.add(s);
					processStates.add(s);
				}
			}
		}
		
		if(destinationStates == null)
		{
			return nullState;
		}
		else if(destinationStates.size() == 1)
		{
			return destinationStates.iterator().next();
		}
		else if(destinationStates.size() > 1)
		{
			for(State s: destinationStates)
			{
				if(raw == "")
					raw = s.getStateName();
				else
					raw += "-" + s.getStateName();
			}
		}
		State newState = this.getEquivalentState(new State(raw));
		return newState;
	}

	public State getEquivalentState(State newState)
	{
		ArrayList<State> states = this.statesForDFA;
		for(State s : states)
		{
			if(s.getStateName().equals(newState.getStateName()))
			{
				return s;
			}
		}
		return newState;
	}
	
	public ArrayList<State> getFinalStatesForDFA(NonDeterministicFA nfa)
	{
		ArrayList<State> allStatesOfDFA = this.dfa.getStatesInAutomaton();
		ArrayList<State> finalStatesForDFA = new ArrayList<State>();
		for(State s : allStatesOfDFA)
		{
			for(State nfaFinal : nfa.getFinalStates())
				if(s.getStateName().contains(nfaFinal.getStateName()))
				{
					if(!finalStatesForDFA.contains(s))
							finalStatesForDFA.add(s);
				}
				else if(s.getStateName().equals(nfaFinal.getStateName()))
				{
					System.out.println("This never prints");
				}
		}
		return finalStatesForDFA;
	}
	
}
