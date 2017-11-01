import java.util.List;


public abstract class Automate {
    protected String name;
    protected List<String> beginState;
    protected List<String> states;
    protected List<String> signs; //входные сигналы
    protected List<String> endStates; //завершающие сигналы


    public Automate(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected boolean containsBeginSymbol(char begSymbol){
        if(signs.contains(begSymbol)){
            return true;
        }
        else
            return false;

    }
    protected abstract boolean containsElem(List<String> endStates, List<String> currentStates);

    protected abstract boolean execute(char input);

    protected abstract List<String> getCurrentState();


    protected abstract List<String> getBeginState();

    protected List<String> getEndState(){
        return endStates;
    }

    public Automate(List<String> states, List<String> signs, List<String> endStates) {
        this.states = states;
        this.signs = signs;
        this.endStates = endStates;
       // this.transaction = transaction;
    }

    public  abstract  void setCurrentState(List<String> currentState);

    public void setBeginState(List<String> beginState) {
        this.beginState = beginState;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public void setSigns(List<String> signs) {
        this.signs = signs;
    }

    public void setEndStates(List<String> endStates) {
        this.endStates = endStates;
    }

    public abstract void setTransaction(List<Tetro> transaction);
}
