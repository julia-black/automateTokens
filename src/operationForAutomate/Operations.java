package operationForAutomate;


import automates.Automate;
import automates.NotDeterminatedAutomate;
import util.Tetro;

import java.util.List;

public class Operations {

    public Automate concat(Automate automate1, Automate automate2){
        if(automate1 != null) {
            Automate resAutomate = new NotDeterminatedAutomate();

            //states
            List<String> resStates = automate1.getStates();
            resStates.addAll(automate2.getStates());
            resAutomate.setStates(resStates);

            //trans
            List<Tetro> resTransaction = automate1.getTransaction();
            resTransaction.addAll(automate2.getTransaction());

            //signs
            List<String> resSigns = automate1.getSigns();
            resSigns.addAll(automate2.getSigns());
            System.out.println("BEG STATE = " + automate1.getBeginState());
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

            System.out.println("Trans: " + resAutomate.getTransaction());
            return  resAutomate;

        }
        else
            return automate2;

    }
}
