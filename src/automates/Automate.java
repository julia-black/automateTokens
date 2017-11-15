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

    public List<String> getEndState(){
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
