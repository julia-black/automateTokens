package automates;

import util.Tetro;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class NotDeterminatedAutomate extends Automate {

    private  List<String> currentStates = new ArrayList<>(); //список тек. состояний для недетерминированного автомата

    public NotDeterminatedAutomate(List<String> states, List<String> signs, List<String> endStates, List<Tetro> transaction, List<String> beginStates) {
        super(states, signs,endStates);
        this.transaction = transaction;
        super.beginState = beginStates;
        this.currentStates = beginStates;
    }

    public NotDeterminatedAutomate(String name) {
            this.name = name;
    }

    public NotDeterminatedAutomate() {
    }

    //Проверяет, хотя бы одно состояние есть в списке вых. состояний
    @Override
    public boolean containsElem(List<String> endStates, List<String> currentStates){
        for(String str : currentStates){
            if(endStates.contains(str))
                return true;
        }
        return false;
    }

    @Override
    public boolean execute(char input) {
        //System.out.println((Character.toString(input)));
        if(containsSignal((Character.toString(input)))){
            if (states.containsAll(currentStates)) {

                List<String> newStates;
                if(signs.contains("\\d") && Character.toString(input).matches("\\d")){
                    newStates = searchItemsInTransaction("\\d" + "", currentStates);
                }
                else if(signs.contains("\\w") && Character.toString(input).matches("\\w")){
                    newStates = searchItemsInTransaction("\\w" + "", currentStates);
                }
                else {
                   newStates = searchItemsInTransaction(input + "", currentStates);
                }
                //currentStates.clear();
                currentStates = newStates;
                if(containsElem(endStates, currentStates)){
                        return true;
                }
            } else {
                currentStates = beginState;
             //   System.out.println("No such states");
                return false;
            }
        }
        else {
          //  System.out.println("No such sign");
            return false;
        }
        return false;
    }

    private List<String> searchItemsInTransaction(String input, List<String> states) {

        List<String> newStates = new ArrayList<>();
        for (int i = 0; i < transaction.size(); i++) {
            if(transaction.get(i).getInput().contains(input) && states.contains(transaction.get(i).getInputState())){
                //если выходной символ - один
                if(transaction.get(i).getResult() != null && transaction.get(i).getResults() == null){
                    if(!newStates.contains(transaction.get(i).getResult())) {
                        newStates.add(transaction.get(i).getResult());
                    }
                }
                else {
                    for (int j = 0; j < transaction.get(i).getResults().size(); j++) {
                        //проверяем, встречался ли уже такой элемент
                        if(!newStates.contains(transaction.get(i).getResults().get(j))) {
                            newStates.add(transaction.get(i).getResults().get(j));
                        }
                    }
                }
            }
        }
        return newStates;
    }

    @Override
    public List<String> getCurrentState() {
        return currentStates;
    }


    @Override
    public void setCurrentState(List<String> currentState) {
        this.currentStates = currentState;
    }


}
