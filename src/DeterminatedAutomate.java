import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DeterminatedAutomate extends Automate {

    private String beginState;
    private static String currentState; //тек. состояние для детерминированного автомата
    protected List<Tetro> transaction; //переходы состояний


    public DeterminatedAutomate(){
        super();
    }

    public DeterminatedAutomate(List<String> states, List<String> signs, List<String> endStates, List<Tetro> transaction, String beginState) {
        super(states, signs, endStates);
        List<String> beginStates = new ArrayList<>();
        beginStates.add(beginState);
        super.beginState = beginStates;
        this.transaction = transaction;
        this.currentState = beginState;
        this.beginState = beginState;
    }

    @Override
    public List<String> getCurrentState() {
        List<String> arr = new ArrayList<>();
        arr.add(currentState);
        return arr;
    }


    @Override
    protected List<String> getBeginState() {
        List<String> arr = new ArrayList<>();
        arr.add(beginState);
        return arr;
    }

    @Override
    public void setCurrentState(List<String> currentState) {
        this.currentState = currentState.get(0);
    }



    @Override
    public void setTransaction(List<Tetro> transaction) {
        this.transaction = transaction;
    }




    @Override
    protected boolean containsElem(List<String> endStates, List<String> currentStates) {
           if(endStates.contains(currentState))      {
               return true;
           }
           else
               return false;
    }

    @Override
    public boolean execute(char input) {
        if(super.signs.contains(Character.toString(input))){
            if(super.states.contains(currentState)) {
                currentState = searchItemInTransaction(input + "", currentState);
                if(endStates.contains(currentState)){
                    return true;
                }
            }
            else{
                return false;
            }
        }
        else {
            currentState = beginState;
            System.out.println("No such sign");
            return false;
        }

        return false;
    }

    private String searchItemInTransaction(String input, String state) {
        for (int i = 0; i < transaction.size(); i++) {
            if(transaction.get(i).getInput().contains(input) && transaction.get(i).getInputState().equals(state)){
                return transaction.get(i).getResult();
            }
        }
        return null;
    }

}
