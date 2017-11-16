package operationForAutomate;


import automates.Automate;
import automates.NotDeterminatedAutomate;
import util.Tetro;

import java.util.List;
import java.util.TreeMap;

public class Operations {

    public Automate concat(Automate automate1, Automate automate2){
        if(automate1 != null) {
            Automate resAutomate = new NotDeterminatedAutomate();
            //states
            List<String> resStates = automate1.getStates();
            resStates.addAll(automate2.getStates());
            resAutomate.setStates(resStates);

            //transactions
            List<Tetro> resTransaction = automate1.getTransaction();
            resTransaction.addAll(automate2.getTransaction());

            //signs
            List<String> resSigns = automate1.getSigns();
            resSigns.addAll(automate2.getSigns());
           // System.out.println("BEG STATE = " + automate1.getBeginState());
            resAutomate.setBeginState(automate1.getBeginState());
            resAutomate.setEndStates(automate2.getEndStates());
            resAutomate.setTransaction(resTransaction);
            resAutomate.setSigns(resSigns);

            for (int i = 0; i < automate1.getEndState().size(); i++) {
                Automate copyAutomate2 = automate2;
                copyAutomate2.setCurrentState(automate2.getBeginState());
                copyAutomate2.execute(automate2.getSigns().get(0).toString().charAt(0));
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
       // if (lastKey(this.table) >= lastKey(object2.table))
       //     object2.renameTable(lastKey(this.table));
       // else
       //     this.renameTable(lastKey(object2.table));
       // appendTable(object2);
       // this.abc = appendArray(abc, object2.abc);
       // this.begin = appendArray(begin, object2.begin);
       // this.end = appendArray(end, object2.end);
       // deleteDuplicateRows();
       // deleteEmptyState();
       // write("association");
       // return this;
    }
}
