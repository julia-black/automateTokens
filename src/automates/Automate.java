package automates;

import util.Tetro;

import java.util.List;


public abstract class Automate {
    protected String name;
    protected List<String> beginState;
    protected List<String> states;
    public List<String> signs; //входные сигналы
    protected List<String> endStates; //завершающие сигналы
    protected List<Tetro> transaction;


    public String toString(){
        String str = "\nName:  " + name + "\nbegState: " + beginState + "\nStates: " + states + "\nSigns: " + signs + "\nendStates: "+ endStates
                + "\nTransactions: " + transaction;
        return str;
    }

    public Automate(){

    }

    public Automate(Automate automate1){
        this.name = "Copy " + automate1.getName();
        this.beginState = automate1.getBeginState();
        this.states = automate1.getStates();
        this.endStates = automate1.getEndStates();
        this.signs = automate1.getSigns();
        this.transaction = automate1.getTransaction();
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
    public abstract boolean containsElem(List<String> endStates, List<String> currentStates);

    public abstract boolean execute(char input);

    public abstract List<String> getCurrentState();


    public List<String> getBeginState(){
        return beginState;
    }


    public Automate(List<String> states, List<String> signs, List<String> endStates) {
        this.states = states;
        this.signs = signs;
        this.endStates = endStates;
       // this.transaction = transaction;
    }

    public  abstract  void setCurrentState(List<String> currentState);

    public void setBeginState(List<String> beginState) {
       // System.out.println("SET BEGIN STATE");
        this.beginState = beginState;
    }


    public boolean containsSignal(String symbol){
        for(String str : signs) {
            switch (str) {
                case "\\w":
                    if (symbol.matches("\\w")) {
                       // System.out.println("This is letter");
                        return true;
                    }
                    break;
                case "\\d":
                    if (symbol.matches("\\d")) {
                       // System.out.println("This is number");
                        return true;
                    }
                default:
                    if(str.equals(symbol)){
                        return true;
                    }
            }
        }
        return false;
    }
    public void setStates(List<String> states) {
        this.states = states;
    }

    public List<String> getSigns() {
        return signs;
    }

    public List<String> getStates() {
        return states;
    }

    public List<String> getEndStates() {
        return endStates;
    }

    public void setSigns(List<String> signs) {
        this.signs = signs;
    }

    public void setEndStates(List<String> endStates) {
        this.endStates = endStates;
    }

    public void setTransaction(List<Tetro> transaction){
        this.transaction = transaction;
    }

    public List<Tetro> getTransaction() {
        return transaction;
    }

}
