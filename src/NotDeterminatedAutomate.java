import java.util.ArrayList;
import java.util.List;

public class NotDeterminatedAutomate extends Automate {

   // private  List<String> beginStates = new ArrayList<>();
    private  List<String> currentStates = new ArrayList<>(); //список тек. состояний для недетерминированного автомата

    protected List<Tetro> transaction; //переходы состояний

    public NotDeterminatedAutomate(List<String> states, List<String> signs, List<String> endStates, List<Tetro> transaction,List<String> beginStates) {
        super(states, signs,endStates);
        this.transaction = transaction;
        super.beginState = beginStates;
        this.currentStates = beginStates;
    }

    //Проверяет, хотя бы одно состояние есть в списке вых. состояний
    @Override
    protected boolean containsElem(List<String> endStates, List<String> currentStates){
        for(String str : currentStates){
            if(endStates.contains(str))
                return true;
        }
        return false;
    }

    @Override
    protected boolean execute(char input) {
        if(super.signs.contains(Character.toString(input))) {
            if (states.containsAll(currentStates)) {
                List<String> newStates = searchItemsInTransaction(input + "", currentStates);
                currentStates.clear();
                currentStates = newStates;
                if(containsElem(endStates, currentStates)){
                        return true;
                }
            } else {
                System.out.println("No such states");
                return false;
            }
        }
        else {
            System.out.println("No such sign");
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
    protected List<String> getCurrentState() {
        return currentStates;
    }

   @Override
   protected List<String> getBeginState() {
       return beginState;
   }

    @Override
    public void setCurrentState(List<String> currentState) {
        this.currentStates = currentState;
    }

    @Override
    public void setTransaction(List<Tetro> transaction) {

    }


}
