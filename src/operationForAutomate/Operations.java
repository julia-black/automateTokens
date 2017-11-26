package operationForAutomate;


import automates.Automate;
import automates.NotDeterminatedAutomate;
import sun.applet.Main;
import util.Tetro;

import java.util.*;

public class Operations {

    //объединить 2 списка без дубляжей
    private List<String> unionListWithOutDuble(List<String> list1, List<String> list2){

       // List<String> newList1 = Collections.unmodifiableList(list1);
        //Collections.unmodifiableList(list1);
        List<String> resList = new ArrayList<>(list1);
      //  List<String> resList = list1.subList(0,list1.size());

        for (String item : list2) {
            if(!resList.contains(item)){
                resList.add(item);
            }
        }
        return resList;
    }
    public Automate concat(Automate automate1, Automate automate2){
     //  System.out.println("\nConcat");

        if(automate1 != null) {
            Automate resAutomate = new NotDeterminatedAutomate();
            //states

            List<String> resStates = unionListWithOutDuble(automate1.getStates(), automate2.getStates());
            resAutomate.setStates(resStates);

            //transactions
            List<Tetro> resTransaction = new ArrayList<>(automate1.getTransaction());
            resTransaction.addAll(automate2.getTransaction());

            List<String> resEndStates = new ArrayList<>(automate2.getEndStates());

            //signs
            List<String> resSigns = unionListWithOutDuble(automate1.getSigns(), automate2.getSigns());

            for(String state : automate2.getBeginState()){
                if(automate2.getEndStates().contains(state)) {
                  //  System.out.println("State was is end " + state);
                    //если какое-то из нач. сост было заключ, то объявляем заключ. все заключ. сост 1го автомата
                    resEndStates = unionListWithOutDuble(automate1.getEndStates(), automate2.getEndStates());
                }

            }
            //System.out.println("BEG STATE = " + automate1.getBeginState());
            resAutomate.setBeginState(automate1.getBeginState());

            resAutomate.setEndStates(resEndStates);
            resAutomate.setTransaction(resTransaction);
            resAutomate.setSigns(resSigns);

            for (int i = 0; i < automate1.getEndStates().size(); i++) {
                Automate copyAutomate2 = automate2;
                copyAutomate2.setCurrentState(automate2.getBeginState());
                for (int j = 0; j < automate2.getSigns().size(); j++) {
                    copyAutomate2.setCurrentState(automate2.getBeginState());

                    char symbol = automate2.getSigns().get(j).charAt(0);

                    if(automate2.getSigns().get(0).equals("\\d")){
                        symbol = '2';
                    }
                    else if(automate2.getSigns().get(0).equals("\\w")){
                        symbol = 'a';
                    }
                   // System.out.println("symbol " + symbol + " state " + copyAutomate2.getCurrentState());
                    copyAutomate2.execute(symbol);

                  //  System.out.println("new state " + copyAutomate2.getCurrentState());
                    //for (int j = 0; j < automate2.getSigns().size(); j++) {
                    resTransaction.add(new Tetro(automate2.getSigns().get(j).toString(), automate1.getEndStates().get(i), copyAutomate2.getCurrentState()));
                }
            }
           // System.out.println(resAutomate.toString());
            return  resAutomate;
        }
        else
            return automate2;
    }

    public Automate union(Automate automate1, Automate automate2){
       // System.out.println("\nUnion");
        Automate resAutomate =  new NotDeterminatedAutomate();

        //1.в новый автомат переносим все сост-я и переходы исходных автоматов
        List<String> resStates = new ArrayList<>(automate1.getStates());
        resStates.addAll(automate2.getStates());
        resAutomate.setStates(resStates);

        //transactions

        List<Tetro> resTransaction = new ArrayList<>(automate1.getTransaction());

        resTransaction.addAll(automate2.getTransaction());

        //System.out.println("Trasactions: " + resTransaction);
        resAutomate.setTransaction(resTransaction);


        //signs
        List<String> resSigns = unionListWithOutDuble(automate1.getSigns(), automate2.getSigns());
        resAutomate.setSigns(resSigns);


        //2.Все началальные состояние - начальные
        List<String> resBegStates = new ArrayList<>(automate1.getBeginState());
        resBegStates.addAll(automate2.getBeginState());
        resAutomate.setBeginState(resBegStates);

        //3.Все заключ. состояния - заключ.
        List<String> resEndStates = unionListWithOutDuble(automate1.getEndStates(), automate2.getEndStates());
        resAutomate.setEndStates(resEndStates);

      //  System.out.println(resAutomate.toString());
        return resAutomate;
    }

    public Automate iteration(Automate automate, int idx){
        //System.out.println("\nIteration");

        //Из всех заключительных состояний организуем переходы туда же, куда есть переходы из нач состояний
        List<Tetro> resTransaction = new ArrayList<>(automate.getTransaction());
        for (int i = 0; i < automate.getEndStates().size(); i++) {
            Automate copyAutomate = new NotDeterminatedAutomate(automate);
            copyAutomate.setCurrentState(automate.getBeginState());

            for (int j = 0; j < copyAutomate.getBeginState().size() ; j++) {
                for (int k = 0; k < automate.getSigns().size(); k++) {
                    List<String> tmpCurrState = new ArrayList<>();
                    tmpCurrState.add(copyAutomate.getBeginState().get(j));
                    copyAutomate.setCurrentState(tmpCurrState);
                    char symbol = automate.getSigns().get(k).charAt(0);
                    if (automate.getSigns().get(0).equals("\\d")) {
                        symbol = '1';
                    } else if (automate.getSigns().get(0).equals("\\w")) {
                        symbol = 'a';
                    }
                    boolean flag = copyAutomate.execute(symbol);
                    //если автомат принимает
                    if(copyAutomate.getCurrentState().size() > 0) {
                        resTransaction.add(new Tetro(copyAutomate.getSigns().get(k).toString(), copyAutomate.getEndStates().get(i), copyAutomate.getCurrentState()));
                    }
                }
            }
        }
        //Добавляем новое состояние и делаем его заключительным и начальным
        List<String> resBeginStates = new ArrayList<>(automate.getBeginState());
        resBeginStates.add("s"+ idx);

       // System.out.println("Beg states " + resBeginStates );

        List<String> resEndStates = new ArrayList<>(automate.getEndStates());
        resEndStates.add("s"+idx);

        List<String> resStates = new ArrayList<>(automate.getStates());
        resStates.add("s"+ idx);

        //Переносим всё из исходного автомата
        Automate resAutomate =  new NotDeterminatedAutomate(resStates, automate.getSigns(),
               resEndStates, resTransaction, resBeginStates);

      //  System.out.println(resAutomate.toString());
        return resAutomate;

    }
}
