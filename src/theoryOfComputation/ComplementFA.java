package theoryOfComputation;

import java.util.*;


public class ComplementFA {
	
	public DeterministicFA convertToComplement(DeterministicFA dfa)
	{
		ArrayList<State> newFinalStates = new ArrayList<State>();
		newFinalStates.addAll(dfa.getStatesInAutomaton());
		newFinalStates.removeAll(dfa.getFinalStates());
		ArrayList<State> oldFinalStates =  dfa.getFinalStates();
		oldFinalStates.clear();
		dfa.setFinalStates(newFinalStates);
		return dfa;
	}
}
