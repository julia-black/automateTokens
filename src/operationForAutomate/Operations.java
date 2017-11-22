package operationForAutomate;


import automates.Automate;
import automates.NotDeterminatedAutomate;
import sun.applet.Main;
import util.Tetro;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Operations {

    //объединить 2 списка без дубляжей
    private List<String> unionListWithOutDuble(List<String> list1, List<String> list2){
        List<String> resList = list1;
        for (String item : list2) {
            if(!resList.contains(item)){
                resList.add(item);
            }
        }
        return resList;
    }
    public Automate concat(Automate automate1, Automate automate2){
       System.out.println("\nConcat");

        if(automate1 != null) {
            Automate resAutomate = new NotDeterminatedAutomate();
            //states

            List<String> resStates = unionListWithOutDuble(automate1.getStates(), automate2.getStates());
            resAutomate.setStates(resStates);

            //transactions
            List<Tetro> resTransaction = automate1.getTransaction();
            resTransaction.addAll(automate2.getTransaction());

            List<String> resEndStates = automate2.getEndStates();

            //signs
            List<String> resSigns = unionListWithOutDuble(automate1.getSigns(), automate2.getSigns());

            for(String state : automate2.getBeginState()){
                if(automate2.getEndState().contains(state)) {
                    //если какое-то из нач. сост было заключ, то объявляем заключ. все заключ. сост 1го автомата
                    resEndStates = unionListWithOutDuble(automate1.getEndStates(), automate2.getEndStates());
                }

            }
            //System.out.println("BEG STATE = " + automate1.getBeginState());
            resAutomate.setBeginState(automate1.getBeginState());

            resAutomate.setEndStates(resEndStates);
            resAutomate.setTransaction(resTransaction);
            resAutomate.setSigns(resSigns);

            for (int i = 0; i < automate1.getEndState().size(); i++) {
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
                    resTransaction.add(new Tetro(automate2.getSigns().get(j).toString(), automate1.getEndState().get(i), copyAutomate2.getCurrentState()));
                }
            }
            System.out.println(resAutomate.toString());
            return  resAutomate;
        }
        else
            return automate2;
    }

    public Automate union(Automate automate1, Automate automate2){
        System.out.println("\nUnion");
        Automate resAutomate =  new NotDeterminatedAutomate();

        //1.в новый автомат переносим все сост-я и переходы исходных автоматов
        List<String> resStates = automate1.getStates();
        resStates.addAll(automate2.getStates());
        resAutomate.setStates(resStates);

        //transactions
        System.out.println("Trans. 1: " + automate1.getTransaction());
        System.out.println("Trans. 2: " + automate2.getTransaction());
        List<Tetro> resTransaction = automate1.getTransaction();
        resTransaction.addAll(automate2.getTransaction());


        //НЕПРавильно здесь трензакции соединяются
        System.out.println("Trans. : " + resAutomate.getTransaction());
        resAutomate.setTransaction(resTransaction);

        //signs
        List<String> resSigns = unionListWithOutDuble(automate1.getSigns(), automate2.getSigns());
                //automate1.getSigns();
       // resSigns.addAll(automate2.getSigns());
        resAutomate.setSigns(resSigns);

        //2.Все началальные состояние - начальные
        List<String> resBegStates = automate1.getBeginState();
        resBegStates.addAll(automate2.getBeginState());
        resAutomate.setBeginState(resBegStates);

        //3.Все заключ. состояния - заключ.
        List<String> resEndStates = automate1.getEndStates();
        resEndStates.addAll(automate2.getEndStates());
        resAutomate.setEndStates(resEndStates);

        System.out.println(resAutomate.toString());
        return resAutomate;
    }

    public Automate iteration(Automate automate, int idx){
        System.out.println("\nIteration");

        //Из всех заключительных состояний организуем переходы туда же, куда есть переходы из нач состояний
        List<Tetro> resTransaction = automate.getTransaction();
        for (int i = 0; i < automate.getEndState().size(); i++) {
            Automate copyAutomate = automate;
            copyAutomate.setCurrentState(automate.getBeginState());
            char symbol = automate.getSigns().get(0).charAt(0);
            if(automate.getSigns().get(0).equals("\\d")){
                symbol = '1';
            }
            else if(automate.getSigns().get(0).equals("\\w")){
                symbol = 'a';
            }
            copyAutomate.execute(symbol);

            for (int j = 0; j < automate.getSigns().size(); j++) {
                resTransaction.add(new Tetro(automate.getSigns().get(j).toString(), automate.getEndState().get(i), copyAutomate.getCurrentState()));
            }

        }
        //Добавляем новое состояние и делаем его заключительным и начальным
        List<String> resBeginStates = automate.getBeginState();
        resBeginStates.add("s"+ idx);

       // System.out.println("Beg states " + resBeginStates );

        List<String> resEndStates = automate.getEndState();
        resEndStates.add("s"+idx);

        List<String> resStates = automate.getStates();
        resStates.add("s"+ idx);

        //Переносим всё из исходного автомата
        Automate resAutomate =  new NotDeterminatedAutomate(resStates, automate.getSigns(),
               resEndStates, resTransaction, resBeginStates);

        System.out.println(resAutomate.toString());
        return resAutomate;

    }
}
