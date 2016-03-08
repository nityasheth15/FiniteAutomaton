package theoryOfComputation;
import java.io.*;
import java.util.*;

public class DesignAutomaton {
	
	//static String userInputedString = "";
	static DeterministicFA dfaSpecification = null;
	static DeterministicFA dfaSystem = null;
	static DeterministicFA dfaNew = null;
	
	
	public static void main(String[] args) throws IOException
	{
		ArrayList<String> alphabets = new ArrayList<String>();
		
		ArrayList<String> transitionsFA1 = new ArrayList<String>();
		ArrayList<String> initialStateFA1 = new ArrayList<String>();
		ArrayList<String> finalStateFA1 = new ArrayList<String>();
		
		ArrayList<String> transitionsFA2 = new ArrayList<String>();
		ArrayList<String> initialStateFA2 = new ArrayList<String>();
		ArrayList<String> finalStateFA2 = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<String> allLinesFromInputFile = new ArrayList<String>();
		String aLineFromInputFile = "";
		
		System.out.println("-----------------------------------Design Automata-----------------------------------");
		/*System.out.println("Enter the String you want to test: \n");
		userInputedString = br.readLine();	*/					//The string will be checked in DFA if it is accepted in the DFA or not.	
		System.out.println("Enter the FA Text file path: \n");
		String pathToDFATextFile = br.readLine();
				
		FileReader readFileFromUserDefinedLocation = null;
		BufferedReader fileReader = null;
		try
		{
			readFileFromUserDefinedLocation = new FileReader(pathToDFATextFile);
			fileReader = new BufferedReader(readFileFromUserDefinedLocation);
			while((aLineFromInputFile = fileReader.readLine()) != null)
			{
				allLinesFromInputFile.add(aLineFromInputFile);
			}
		}
		catch(FileNotFoundException fnf)
		{
			System.out.println("File not found");
			System.exit(0);
		}
		
		//The following logic is for processing the input and storing the tuples in different arrays.
				//These arrays will be used to call methods in Automaton and set the tuples for the DFA.
				for(int i=0; i< allLinesFromInputFile.size(); i++)
				{
					if(allLinesFromInputFile.get(i).contains("% Input alphabet"))
					{
						i++;
						while(allLinesFromInputFile.get(i).contains("%") == false)
						{
							alphabets.add(allLinesFromInputFile.get(i).trim());
							i++;
						}
					}
					if(allLinesFromInputFile.get(i).contains("% Specification automaton"))
					{
						i++; i++;
						while(allLinesFromInputFile.get(i).contains("%") == false)
						{
							transitionsFA1.add(allLinesFromInputFile.get(i));
							i++;//n++;
						}
					}
					if(allLinesFromInputFile.get(i).contains("% Initial state"))
					{
						i++;
						while(allLinesFromInputFile.get(i).contains("%") == false)
						{
							initialStateFA1.add(allLinesFromInputFile.get(i));
							i++; //o++;
						}
					}
					if(allLinesFromInputFile.get(i).contains("% Final"))
					{
						i++;
						while(allLinesFromInputFile.get(i).contains("%") == false)
						{
							finalStateFA1.add(allLinesFromInputFile.get(i));
							i++;
						}
					}
					if(allLinesFromInputFile.get(i).contains("% System automaton"))
					{ i++; i++;
						while(allLinesFromInputFile.get(i).contains("%") == false)
						{
							//finalStateFA1.add(q,allLinesFromInputFile.get(i));
							transitionsFA2.add(allLinesFromInputFile.get(i));
							i++;
						}
					}
					if(allLinesFromInputFile.get(i).contains("% Initial"))
					{i++;
						while(allLinesFromInputFile.get(i).contains("%") == false)
						{
							initialStateFA2.add(allLinesFromInputFile.get(i));
							i++;
						}
					}
					if(allLinesFromInputFile.get(i).contains("% Final"))
					{i++;
					
						for(int j = i; j<allLinesFromInputFile.size(); j++)
						{
							finalStateFA2.add(allLinesFromInputFile.get(j));
						}
					}
					//checkNFAOrDFA!
				}
				try
				{
					fileReader.close();
				}
				catch(NullPointerException e)
				{
					System.out.println("File not found");
					System.exit(0);
				}
				
				String nfaOrDfa1 = DesignAutomaton.checkFA(transitionsFA1, alphabets);
				String nfaOrDfa2 = DesignAutomaton.checkFA(transitionsFA2, alphabets);
				
				try
				{
					if(nfaOrDfa1.equalsIgnoreCase("D") == true)
					{
						System.out.println("--------------------   L(S)   --------------------");
						dfaSpecification = DesignAutomaton.contructDeterministicFA(alphabets, transitionsFA1, initialStateFA1, finalStateFA1);
						System.out.println("--------------------   END L(S)   --------------------");
					}
					else if(nfaOrDfa1.equals("N") == true)
					{
						System.out.println("\n--------------------   L(S)   --------------------");
						dfaSpecification = DesignAutomaton.constructNonDeterministicFA(alphabets, transitionsFA1, initialStateFA1, finalStateFA1);
						System.out.println("--------------------   END L(S)   --------------------");
					}
					//converts to complement.
					System.out.println("\nConvert S into its complement...");
					dfaSpecification = new ComplementFA().convertToComplement(dfaSpecification);
					System.out.println("Specification S is now converted to S'");
					
					if(nfaOrDfa2.equalsIgnoreCase("D") == true)
					{
						System.out.println("\n--------------------   L(A)   --------------------");
						dfaSystem = DesignAutomaton.contructDeterministicFA(alphabets, transitionsFA2, initialStateFA2, finalStateFA2);
						System.out.println("\n--------------------   End L(A)   --------------------");
					}
					else if(nfaOrDfa2.equals("N") == true)
					{
						System.out.println("\n--------------------   L(A)   --------------------");
						dfaSystem =  DesignAutomaton.constructNonDeterministicFA(alphabets, transitionsFA2, initialStateFA2, finalStateFA2);
						System.out.println("\n--------------------   End L(A)   --------------------");
					}
					
					IntersectFA ifa = new IntersectFA();
					dfaNew = ifa.constructIntersectionFA(dfaSpecification, dfaSystem);
					DesignAutomaton.removeUnreachableStates(dfaNew);
					System.out.println("\n--------------------   L(M) = (L(S))' [intersection] L(A)   --------------------");
					System.out.println("Initial State: " + dfaNew.getInitialState().getStateName());
					System.out.println("\nFinal States:");
					for(State s: dfaNew.getFinalStates())
					{
						System.out.println(s.getStateName());
					}
					System.out.println("\nTransitions are as follows: ");
					for(int i=0; i<dfaNew.getStatesInAutomaton().size(); i++)
					{
						for(Object a : alphabets)
						{
							System.out.print(dfaNew.getStatesInAutomaton().get(i).getStateName());
							Map <String, State> destination = dfaNew.getAllTransition().get(dfaNew.getStatesInAutomaton().get(i));
							System.out.print(" : " + a.toString().trim());
							System.out.println(" : " + destination.get(a.toString()).getStateName());
						}
					}
					System.out.println("--------------------   End L(M)   --------------------\n");
					
					System.out.println("--------------------   System specification results   --------------------");
					StringBuilder acceptString =  DesignAutomaton.createAcceptString(dfaNew);
					if((acceptString+"").trim().equals(""))
					{
						//System.out.println(dfaNew.getStatesInAutomaton().size());
						System.out.println("The system satisfies the specification!");
					}
					else 
					{
						System.out.println("The system does not satisfy the specification!");
						System.out.println("The counterexample that demonstrates that the specification does not hold is: "+ acceptString);
					}
					System.out.println("--------------------   System specification results end   --------------------");
					
					/*System.out.println("\n--------------------   String validation   --------------------");
					StringValidator validateString = new StringValidator();
					System.out.println("Status of the user input: " + validateString.checkStringForDFA(dfaNew, userInputedString));
					System.out.println("--------------------   String validation end   --------------------");*/
				}
				catch(Exception e)
				{
					System.out.println("Exception occured");
					System.out.println("Restart the program and please insert the string as per the alphabet.");
					System.out.println("Please read the 'Readme.docx' for more insight on how to run the program");
				}			
	}
	
	public static String checkFA(ArrayList<String> transitions, ArrayList<String> alphabet)
	{
		ArrayList<String> alphaForStates = null;
		Map<String, ArrayList<String>> checkMap = new HashMap<String, ArrayList<String>>();
		String nfaOrDfa = "D";
		//test-1: checks for repeated transitions
		for(String transition : transitions)
		{
			String[] splitTransition = transition.trim().split(" ");
			if(checkMap.containsKey(splitTransition[0].trim()))
			{
				alphaForStates = checkMap.get(splitTransition[0].trim());
				if(!alphaForStates.contains(splitTransition[1].trim()))
					alphaForStates.add(splitTransition[1].trim());
				else
					return "N"; //if a single alphabet is repeated for any of the transitions; it will return NFA.
			}
			else
			{
				alphaForStates = new ArrayList<String>();
				alphaForStates.add(splitTransition[1].trim());
			}
			checkMap.put(splitTransition[0].trim(),alphaForStates);
		}
		Queue<String> q = new LinkedList<String>();
		//Set<String> states = checkMap.keySet();
		for(String s : checkMap.keySet())
		{
			q.add(s);
		}
		//test-2 : checks if there are less number of transitions for all the states. <here, we are sure that no repeated transition will be there>
		while(!q.isEmpty())
		{
			ArrayList<String> alphaList = checkMap.get(q.poll()); //states.iterator().next());
			if(alphaList.size() != alphabet.size())
			{
				return "N";
			}
		}
		return nfaOrDfa;
	}
	
	public static DeterministicFA contructDeterministicFA(ArrayList<String> alphabets, ArrayList<String> transitions, ArrayList<String>initialState, ArrayList<String>finalState)
	{
		DeterministicFA dfa = new DeterministicFA();
		dfa.setAlphabet(alphabets);
		dfa.setAllTransitions(transitions);
		dfa.setInitailState(new State(initialState.get(0).trim()));
		ArrayList<State> finalStates = new ArrayList<State>();
		for(String rawState: finalState)
		{
			finalStates.add(new State(rawState));
		}
		dfa.setFinalStates(finalStates);
		
		//Prints the newly constructed FA!
		System.out.println("\nInitial State: " + dfa.getInitialState().getStateName());
		System.out.println("\nFinal States:");
		for(State s: dfa.getFinalStates())
		{
			System.out.println(s.getStateName());
		}
		
		System.out.println("\nTransitions are as follows: ");
		for(int i=0; i<dfa.getStatesInAutomaton().size(); i++)
		{
			for(Object a : alphabets)
			{
				System.out.print(dfa.getStatesInAutomaton().get(i).getStateName());
				Map <String, State> destination = dfa.getAllTransition().get(dfa.getStatesInAutomaton().get(i));
				System.out.print(" : " + a.toString().trim());
				System.out.println(" : " + destination.get(a.toString().trim()).getStateName());
			}
		}
		return dfa;
	}
	
	public static DeterministicFA constructNonDeterministicFA(ArrayList<String> alphabets, ArrayList<String> transitions, ArrayList<String>initialState, ArrayList<String>finalState)
	{
		NonDeterministicFA nfa = new NonDeterministicFA();
		nfa.setAlphabet(alphabets);
		nfa.setAllTransitions(transitions);
		nfa.setInitailState(new State(initialState.get(0).trim()));
		ArrayList<State> finalStates = new ArrayList<State>();
		for(String rawState: finalState)
		{
			finalStates.add(new State(rawState));
		}
		nfa.setFinalStates(finalStates);
		
		//Convert NFA to DFA
		NonDeterministicFAToDeterministicFA nfaToDfa = new NonDeterministicFAToDeterministicFA();
		DeterministicFA newDfa =  nfaToDfa.convertNFAToDFA(nfa);
		
		System.out.println("\nInitial State: " + newDfa.getInitialState().getStateName());
		System.out.println("\nFinal States");
		for(State s : newDfa.getFinalStates())
		{
			System.out.println(s.getStateName());
		}
		Map<State, Map<String, State>>allTransitionsOfDFA = newDfa.getAllTransition();
		ArrayList<State> dfaStates = newDfa.getStatesInAutomaton();
		System.out.println("\nTransitions are as follows: ");
		for(int i=0; i<dfaStates.size(); i++)
		{
			for(String a : newDfa.getAlphabet())
			{
				System.out.print(newDfa.getStatesInAutomaton().get(i).getStateName());
				Map <String, State> destination = allTransitionsOfDFA.get(dfaStates.get(i));
				System.out.print(" : "+a.trim());
				System.out.println(" : " + destination.get(a.trim()).getStateName());
			}
		}
		
		return newDfa;
	}
	
	public static StringBuilder createAcceptString(DeterministicFA dfa)
	{
		Map<State, Map<String, State>> allTransitionsOfDFA = dfa.getAllTransition();
		Map<String, State> destinationMap = new HashMap<String, State>();
		ArrayList<State> statesInAutomaton = dfa.getStatesInAutomaton();
		ArrayList<State> finalStates = dfa.getFinalStates();
		StringBuilder acceptString = new StringBuilder("");
		State currentFinalState = null;

		//if there are no final states!
		if(finalStates.size() == 0)
		{
			return acceptString;
		}

		for(State s : finalStates)
		{
			if(dfa.getInitialState().equals(s))
			{
				return new StringBuilder("epsilon"); //if the init state is final state!
			}
		}
		boolean currentStateChanged = false;
		int count = 0;
		currentFinalState = finalStates.get(0); //choosing the first final State from the list!
		for(int i=0; i<finalStates.size(); i++)
		{ count = 0;
			while(!currentFinalState.equals(dfa.getInitialState()))
			{ count++;
				for(State s : statesInAutomaton)
				{	
					destinationMap = allTransitionsOfDFA.get(s);
					for(String a : dfa.getAlphabet())
					{ 
						State tempState = destinationMap.get(a.trim());
						if(currentFinalState.equals(tempState))
						{
							currentFinalState = s;
							acceptString.append(a.trim());
							currentStateChanged = true;
							break;
						}
					}
					if(currentStateChanged == true)
					{
						currentStateChanged = false;
						break;
					}
				}
				if(count > dfa.getStatesInAutomaton().size())
				{
					i++; count =0; acceptString = new StringBuilder();
					if(i < dfa.getFinalStates().size())
						currentFinalState = dfa.getFinalStates().get(i);
					else
						break;
				}
			}
		}	
		return acceptString.reverse();
	}
	
	public static void removeUnreachableStates(DeterministicFA dfa)
	{
		int count = 0;
		ArrayList<State> statesOfDFA = dfa.getStatesInAutomaton();
		Map<State, Map<String, State>> allTransitions = dfa.getAllTransition();
		Map<String, State> destinationMap = new HashMap<String, State>();
		
		for(int i=0; i<statesOfDFA.size(); i++)
		{
			count = 0; //State tempState = statesOfDFA.get(i);
			if(statesOfDFA.get(i).equals(dfa.getInitialState()))
				continue;
			for(State dfaState2 : statesOfDFA)
			{	
				destinationMap = allTransitions.get(dfaState2);
				if(destinationMap.containsValue(statesOfDFA.get(i)))
				{
					if(!dfaState2.equals(statesOfDFA.get(i))) //!dfaState2.equals(dfa.getInitialState()))// && !dfaState2.equals(statesOfDFA.get(i)))
						count++;
				}
			}
			if(count == 0)
			{
				dfa.getAllTransition().remove(statesOfDFA.get(i));
				if(dfa.getFinalStates().contains(statesOfDFA.get(i)))
				{
					dfa.getFinalStates().remove(statesOfDFA.get(i));
				}
				dfa.getStatesInAutomaton().remove(i);
				i = -1;
			}
		}
	}
	
}
