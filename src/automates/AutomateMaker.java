package automates;

import operationForAutomate.Operations;
import util.Tetro;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class AutomateMaker {
    static Operations operations = new Operations();
    private  static int idxState = 0;
    private ArrayDeque<Automate> automates;
    private ArrayDeque<Character> operators;
    private String name;
    private int priority;
    private String regex;

    public AutomateMaker(String name, int priority, String regex) {
        this.name = name;
        this.priority = priority;
        this.regex = regex;
        automates = new ArrayDeque<>();
        operators = new ArrayDeque<>();
    }

    public Automate createSimpleAutomate(String name, String symbol){ //автомат для 1 символа
        List<String> beginState = new ArrayList<>();
        List<String> states = new ArrayList<>();
        List<String> signs = new ArrayList<>();
        List<Tetro> transactions = new ArrayList<>();
        List<String> endStates = new ArrayList<>();
        beginState.add("s"+idxState);
        states.addAll(beginState);
        idxState++;
        signs.add(symbol);
        states.add("s" + idxState); //номер состояния равен кол-ву символов, которые уже добавлены
        idxState++;
        endStates.clear();
        endStates.add(states.get(states.size()-1));//то конечным состоянием будет последнее добавленное состояние

        // System.out.println(signs.size() + " " + beginState + endStates);
        transactions.add(new Tetro(signs.get(signs.size() - 1), beginState.get(0), endStates));

        Automate automate = new NotDeterminatedAutomate(states, signs, endStates,transactions, beginState);
        automate.setName(name);
      //System.out.println("Signs: " + signs);
      //System.out.println("All states: " + states);
      //System.out.println("Begin states: " + beginState);
      //System.out.println("End states: " + endStates);
      //for (int i = 0; i < transactions.size() ; i++) {
      //    System.out.println(transactions.get(i).toString());
      //}
        return automate;
    }

    public Automate create(){
        replaceConcat();
        for (int i = 0; i < regex.length(); i++) {
            switch (regex.charAt(i)) {
                case '(':
                    operators.addLast('(');
                    break;
                case ')':
                    while (operators.peekLast() != '(') {
                        executeOperation(operators.pollLast());
                    }
                    operators.pollLast();
                    break;
                case '*':
                case '^':
                case '|':
                    char currentOperator = regex.charAt(i);
                    while (operators.size() != 0 && getPriorityOperation(operators.peekLast()) >= getPriorityOperation(currentOperator)) {
                        executeOperation(operators.pollLast());
                    }
                    operators.addLast(currentOperator);
                    break;
                case '\\':
                    i++;
                    switch (regex.charAt(i)) {
                        case 'd':
                        case 'w':
                        case 's':
                            automates.addLast(createSimpleAutomate(name, "\\" + regex.charAt(i)));
                            break;
                        case 't':
                            automates.addLast(createSimpleAutomate(name, "\t"));
                            break;
                        case 'r':
                            automates.addLast(createSimpleAutomate(name, "\r"));
                            break;
                        case 'n':
                            automates.addLast(createSimpleAutomate(name, "\n"));
                            break;
                        case '?':
                            automates.addLast(createSimpleAutomate(name, "")); //пустой автомат
                            break;
                        default:
                            automates.addLast(createSimpleAutomate(name, regex.charAt(i) + ""));
                            break;
                    }
                    break;
                default:
                    automates.addLast(createSimpleAutomate(name, regex.charAt(i) + ""));
                    break;
            }
        }
        while (operators.size() != 0)
        {
            executeOperation(operators.pollLast());
        }
        if(automates.size()==1)
            return automates.pollLast();
        return null;
    }

    private void executeOperation(char operation) {
       switch (operation) {
           case '*': {
              // System.out.println("Iteration");
               NotDeterminatedAutomate automate = (NotDeterminatedAutomate) automates.pollLast();
               Automate resAutomate = operations.iteration(automate, idxState);
               idxState++;
               resAutomate.setName(name);
               automates.addLast(resAutomate);
           }
               break;
           case '|': {
              // System.out.println("Union");
               NotDeterminatedAutomate automate1 = (NotDeterminatedAutomate) automates.pollLast();
               NotDeterminatedAutomate automate2 = (NotDeterminatedAutomate) automates.pollLast();
               Automate resAutomate = operations.union(automate2, automate1);
               resAutomate.setName(name);
               automates.addLast(resAutomate);
           }
               break;
           case '^': {
            //   System.out.println("Concat");
               NotDeterminatedAutomate automate1 = (NotDeterminatedAutomate) automates.pollLast();
               NotDeterminatedAutomate automate2 = (NotDeterminatedAutomate) automates.pollLast();
               Automate resAutomate = operations.concat(automate2, automate1);
               resAutomate.setName(name);
               automates.addLast(resAutomate);
           }
               break;
       }
    }

    private int getPriorityOperation(char operation) {
        switch (operation) {
            case '|':
                return 0;
            case '^':
                return 1;
            case '*':
                return 2;
            default:
                return -1;
        }
    }

    private void replaceConcat() {
        for (int i = 0; i < regex.length() - 1; i++) {
            if ((regex.charAt(i) != '(') && (regex.charAt(i) != '|') && (regex.charAt(i) != '^') && (regex.charAt(i) != '\\') &&
                    (regex.charAt(i + 1) != ')') && (regex.charAt(i + 1) != '|') && (regex.charAt(i + 1) != '*')) {
                StringBuffer stringBuffer = new StringBuffer(regex);
                stringBuffer.insert(i+1,"^");
                regex = stringBuffer.toString();
               // System.out.println(regex);
            }
        }
    }
}
