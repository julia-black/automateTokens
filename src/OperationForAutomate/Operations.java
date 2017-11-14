package OperationForAutomate;


import Automates.Automate;
import Automates.NotDeterminatedAutomate;
import Structure.Tetro;

import java.util.List;

public class Operations {

    public Automate concat(Automate automate1, Automate automate2){
        if(automate1 != null) {
            Automate resAutomate = new NotDeterminatedAutomate();

            List<String> resStates = automate1.getStates();
            resStates.addAll(automate2.getStates());
            resAutomate.setStates(resStates);
            List<Tetro> resTransaction = automate1.getTransaction();
            resTransaction.addAll(automate2.getTransaction());


            List<String> resSigns = automate1.getSigns();
            resSigns.addAll(automate2.getSigns());
            resAutomate.setBeginState(automate1.getBeginState());
            resAutomate.setEndStates(automate2.getEndStates());
            resAutomate.setTransaction(resTransaction);
            resAutomate.setSigns(resSigns);
            //?? ???? ????. ???? 1?? ?????? ???? ????, ???? ????? ????????? ???????

            for (int i = 0; i < automate1.getEndState().size(); i++) {
                resTransaction.add(new Tetro(automate2.getSigns().get(0).toString(), automate1.getEndState().get(i), automate2.getBeginState()));
            }
           // resTransaction.add(new Tetro( ,automate1.getEndState().toString(), ));

            System.out.println("Trans: " + resAutomate.getTransaction());

            return  resAutomate;

        }
        else //???? ?????????? ? ?????? ?????????
            return automate2;

    }
}
