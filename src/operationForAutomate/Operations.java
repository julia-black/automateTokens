package operationForAutomate;


import automates.Automate;
import automates.NotDeterminatedAutomate;
import sun.applet.Main;
import util.Tetro;

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

        if(automate1 != null) {
            Automate resAutomate = new NotDeterminatedAutomate();
            //states

            List<String> resStates = unionListWithOutDuble(automate1.getStates(), automate2.getStates());
            //        automate1.getStates();
            //resStates.addAll(automate2.getStates());
            resAutomate.setStates(resStates);

            //transactions
            List<Tetro> resTransaction = automate1.getTransaction();
            resTransaction.addAll(automate2.getTransaction());

            //signs
            List<String> resSigns = unionListWithOutDuble(automate1.getSigns(), automate2.getSigns());
           //         automate1.getSigns();
           // resSigns.addAll(automate2.getSigns());

           // System.out.println("BEG STATE = " + automate1.getBeginState());
            resAutomate.setBeginState(automate1.getBeginState());
            resAutomate.setEndStates(automate2.getEndStates());
            resAutomate.setTransaction(resTransaction);
            resAutomate.setSigns(resSigns);

            for (int i = 0; i < automate1.getEndState().size(); i++) {
                Automate copyAutomate2 = automate2;
                copyAutomate2.setCurrentState(automate2.getBeginState());
                System.out.println("Aut2 signs" + automate2.getSigns());
                char symbol = automate2.getSigns().get(0).charAt(0);
                if(automate2.getSigns().get(0).equals("\\d")){
                    symbol = '1';
                }
                else if(automate2.getSigns().get(0).equals("\\w")){
                    symbol = 'a';
                }

                copyAutomate2.execute(symbol);
                resTransaction.add(new Tetro(automate2.getSigns().get(0).toString(), automate1.getEndState().get(i), copyAutomate2.getCurrentState()));
            }
            return  resAutomate;

        }
        else
            return automate2;

    }

    public Automate union(Automate automate1, Automate automate2){
        Automate resAutomate =  new NotDeterminatedAutomate();

        //1.в новый автомат переносим все сост-я и переходы исходных автоматов
        List<String> resStates = automate1.getStates();
        resStates.addAll(automate2.getStates());
        resAutomate.setStates(resStates);

        //transactions
        List<Tetro> resTransaction = automate1.getTransaction();
        resTransaction.addAll(automate2.getTransaction());
        resAutomate.setTransaction(resTransaction);

        //signs
        List<String> resSigns = automate1.getSigns();
        resSigns.addAll(automate2.getSigns());
        resAutomate.setSigns(resSigns);

        //2.Все началальные состояние - начальные
        List<String> resBegStates = automate1.getBeginState();
        resBegStates.addAll(automate2.getBeginState());
        resAutomate.setBeginState(resBegStates);

        //3.Все заключ. состояния - заключ.
        List<String> resEndStates = automate1.getEndStates();
        resEndStates.addAll(automate2.getEndStates());
        resAutomate.setEndStates(resEndStates);

        return resAutomate;
    }

    public Automate iteration(Automate automate, int idx){

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
            //copyAutomate.execute(automate.getSigns().get(0).toString().charAt(0));
           // for (int j = 0; j < automat ; j++) {
           //
           // }

            //НАДО СДЕЛАТЬ ЦИКЛ ПО ВСЕМ СИГНАЛАМ И ДОБАВЛЯТЬ ИХ ВСЕ В ТРАНЗАКЦИИ ТАКИМ СПОСОБОМ
            resTransaction.add(new Tetro(automate.getSigns().get(0).toString(), automate.getEndState().get(i), copyAutomate.getCurrentState()));
        }
       // System.out.println("NEW STATE s"+idx);
        //Добавляем новое состояние и делаем его заключительным и начальным
        List<String> resBeginStates = automate.getBeginState();
        resBeginStates.add("s"+ idx);
        List<String> resEndStates = automate.getEndState();
        resEndStates.add("s"+idx);

        List<String> resStates = automate.getStates();
        resStates.add("s"+ idx);

        //Переносим всё из исходного автомата
        Automate resAutomate =  new NotDeterminatedAutomate(resStates, automate.getSigns(),
               resEndStates, resTransaction, resBeginStates);

        return resAutomate;

    }
}
