package theoryOfComputation;
import java.util.*;

public class StringValidator 
{
	
	public String checkStringForDFA(DeterministicFA dfa, String userInputedString)
	{
		Map<String, State>destinationState = new HashMap<String, State>();
		State currentState = dfa.getInitialState();
		boolean stringAccepted = false;
		
		for(char a : userInputedString.toCharArray())
		{
			destinationState = dfa.getAllTransition().get(currentState);
			if(destinationState != null)
			{
				currentState = destinationState.get(a+"");
			}
		}
		for(State s:  dfa.getFinalStates())
		{
			if((currentState != null) && currentState.getStateName().equals(s.getStateName()))
			{
				stringAccepted = true;
				return "The string entered by you is accepted by L(M)"; //string accepted in L(M) [intersection of L(S') and L(A)].
			}
		}
		if(stringAccepted == false)
		{
			return "The string entered by you not accepted by L(M)"; //string rejected in the complement of given DFA [intersection of L(S') and L(A)].
		}
		return "Never Print";
	}
	
	
}
