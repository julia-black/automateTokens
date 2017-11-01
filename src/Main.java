import sun.font.FontRunIterator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {



    public static List<String> priority = new ArrayList<>();

    //Возвращает true/false и n - максимальную длину найденной подстроки
    private static Pair f(Automate automate, List<Character> chars, int index){
      //  System.out.println();
        boolean result = false;
        int count = 0;
        List<String> states;
        List<String> newStates = null;
        for (int i = index; i < chars.size(); i++) {
            if(automate.signs.contains(chars.get(i).toString())){ //если содержит такой вх. сигнал
              //  System.out.println(chars.get(i) + " State = " + automate.getCurrentState());
                automate.execute(chars.get(i));
                newStates = automate.getCurrentState();
              //  System.out.println("New state = " + newStates);
            }
            if(newStates != null){
                states = newStates;
                newStates = null;
                if(automate.containsElem(automate.getEndState(), states)){
                    count = i - index + 1;
                    result = true;
                }
            }
            else
                break;
        }
        Pair pair = new Pair(result, count);
        return pair;
    }

    public static List<Character> readInputString(){
        List<Character> chars = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("input.txt")));
            int c;
            while ((c = reader.read()) != -1) {
                chars.add((char) c);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Input string: ");
        for (Character c : chars) {
            System.out.print(c);
        }
        System.out.println();
        return chars;
    }

  // public static void readInputAutomateRN() throws IOException {

  //     List<String> lines = Files.readAllLines(Paths.get("input_automate_task2.txt"), StandardCharsets.UTF_8);
  //     for(int i = 0; i < lines.size(); i++){
  //         //Сигналы
  //         if(i == 0){
  //             String[] string = lines.get(i).split(" ");
  //             for(String ch : string){
  //                 signs.add(ch);
  //             }
  //         }
  //         //Состояния
  //         else if(i == 1){
  //             String[] string = lines.get(i).split(" ");
  //             for(String ch : string){
  //                 states.add(ch);
  //             }
  //         }
  //         //Вых. состояния
  //         else if(i==2){
  //             String[] string = lines.get(i).split(" ");
  //             for(String ch : string){
  //                 endStates.add(ch);
  //             }
  //         }
  //         //Табл. переходов
  //         else
  //         {
  //             String[] string = lines.get(i).split("/");
  //             List<String> input = new ArrayList<>();
  //             String inputState = "";
  //             String result = "";
  //             List<String> results = new ArrayList<>();
  //             for(int k = 0; k < string.length; k++){
  //                 //System.out.println(string[i]);
  //                 String[] elems = string[k].split("'");
  //                 if(k == 0){
  //                     for(String ch : elems){
  //                         input.add(ch);

  //                     }
  //                 }
  //                 if(k == 1){ //если это вх. сост
  //                     inputState = string[k];
  //                 }
  //                 else if(k == 2){ //если это вых. сост
  //                     String[] elems1 = string[k].split("'");
  //                     if(elems1.length > 1){
  //                         for(String ch: elems1){
  //                             results.add(ch);
  //                             transactions.add(new Tetro(input, inputState, results));
  //                         }
  //                     }
  //                     else {
  //                         result = string[k];
  //                         transactions.add(new Tetro(input, inputState, result));
  //                     }
  //                 }
  //             }
  //         }
  //     }

  // }

    public static void readInputAutomate(Automate automate, String nameOfFile) throws IOException {

        List<String> signs = new ArrayList<>();
        List<String> states = new ArrayList<>();
        List<Tetro> transactions = new ArrayList<>();
        List<String> endStates = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(nameOfFile), StandardCharsets.UTF_8);

        for(int i = 0; i < lines.size(); i++){
            //Сигналы
            if(i == 0){
                String[] string = lines.get(i).split(" ");
                for(String ch : string){
                    if(ch.equals("letter")){
                        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                        for (int j = 0; j < letters.length(); j++) {
                            signs.add(Character.toString(letters.charAt(j)));
                        }

                    }
                    else if(ch.equals("number")){
                        for (int j = 0; j < 10 ; j++) {
                            signs.add(j + "");
                        }
                    }
                    else if(ch.equals("slash")){
                        signs.add("/");
                    }
                    else {
                        signs.add(ch);
                    }
                }
            }
            //Состояния
            else if(i == 1){
                String[] string = lines.get(i).split(" ");
                for(String ch : string){
                    states.add(ch);
                }
            }
            //Вых. состояния
            else if(i==2){
                String[] string = lines.get(i).split(" ");
                for(String ch : string){
                    endStates.add(ch);
                }
            }
            //Табл. переходов
            else
            {
                String[] string = lines.get(i).split("/");
                List<String> input = new ArrayList<>();
                String inputState = "";
                String result = "";
                List<String> results = new ArrayList<>();
                for(int k = 0; k < string.length; k++){
                    //System.out.println(string[i]);
                    String[] elems = string[k].split("'");
                    if(k == 0){
                        for(String ch : elems){
                            if(ch.equals("letter")){
                                String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                                for (int j = 0; j < letters.length(); j++) {
                                    input.add(Character.toString(letters.charAt(j)));
                                }

                            }
                            else if(ch.equals("number")){
                                for (int j = 0; j < 10 ; j++) {
                                    input.add(j + "");
                                }
                            }
                            else if(ch.equals("slash")){
                                input.add("/");
                            }
                            else {
                                input.add(ch);
                            }

                        }
                    }
                    if(k == 1){ //если это вх. сост
                        inputState = string[k];
                    }
                    else if(k == 2){ //если это вых. сост
                        String[] elems1 = string[k].split("'");
                        if(elems1.length > 1){
                            for(String ch: elems1){
                                results.add(ch);
                                transactions.add(new Tetro(input, inputState, results));
                            }
                        }
                        else {
                            result = string[k];
                            transactions.add(new Tetro(input, inputState, result));
                        }
                    }
                }
            }
        }

        automate.setStates(states);
        automate.setSigns(signs);
        automate.setEndStates(endStates);
        automate.setTransaction(transactions);
    }

   // public static void readInputAutomateIN() throws IOException {
//
   //     List<String> lines = Files.readAllLines(Paths.get("input_automate_IN.txt"), StandardCharsets.UTF_8);
   //     for(int i = 0; i < lines.size(); i++){
   //         //Сигналы
   //         if(i == 0){
   //             String[] string = lines.get(i).split(" ");
   //             for(String ch : string){
   //                     signs.add(ch);
   //             }
   //         }
   //         //Состояния
   //         else if(i == 1){
   //             String[] string = lines.get(i).split(" ");
   //             for(String ch : string){
   //                 states.add(ch);
   //             }
   //         }
   //         //Вых. состояния
   //         else if(i==2){
   //             String[] string = lines.get(i).split(" ");
   //             for(String ch : string){
   //                 endStates.add(ch);
   //             }
   //         }
   //         //Табл. переходов
   //         else
   //         {
   //             String[] string = lines.get(i).split("/");
   //             List<String> input = new ArrayList<>();
   //             String inputState = "";
   //             String result = "";
   //             List<String> results = new ArrayList<>();
   //             for(int k = 0; k < string.length; k++){
   //                 //System.out.println(string[i]);
   //                 String[] elems = string[k].split("'");
   //                 if(k == 0){
   //                     for(String ch : elems){
//
   //                             input.add(ch);
//
//
   //                     }
   //                 }
   //                 if(k == 1){ //если это вх. сост
   //                     inputState = string[k];
   //                 }
   //                 else if(k == 2){ //если это вых. сост
   //                     String[] elems1 = string[k].split("'");
   //                     if(elems1.length > 1){
   //                         for(String ch: elems1){
   //                             results.add(ch);
   //                             transactions.add(new Tetro(input, inputState, results));
   //                         }
   //                     }
   //                     else {
   //                         result = string[k];
   //                         transactions.add(new Tetro(input, inputState, result));
   //                     }
   //                 }
   //             }
   //         }
   //     }
   // }

    public static void readPriority() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input_priority.txt"), StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            priority.add(lines.get(i));
        }
       // for (int i = 0; i < priority.size(); i++) {
       //     System.out.println(priority.get(i) + i);
       // }
    }

    public static Automate getAutomateForName(List<Automate> automates, String name){
        for (int i = 0; i < automates.size(); i++) {
            if(automates.get(i).getName().equals(name)) return automates.get(i);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {

        Automate automateID = new DeterminatedAutomate();
        automateID.setName("ID");
        Automate automateIN = new DeterminatedAutomate();
        automateIN.setName("IN");
        Automate automateRN = new DeterminatedAutomate();
        automateRN.setName("RN");

        readInputAutomate(automateID, "input_automate_ID.txt");
        readInputAutomate(automateIN, "input_automate_IN.txt");
        readInputAutomate(automateRN, "input_automate_task2.txt");

        readPriority();
        System.out.println(priority);

        List<Automate> automates = new ArrayList<>();
        for (int i = 0; i < priority.size(); i++) {
            if(priority.get(i).equals("IN")){
                automates.add(automateIN);
            }
            else if(priority.get(i).equals("ID")){
                automates.add(automateID);
            }
            else if(priority.get(i).equals("RN")){
                automates.add(automateRN);
            }
        }

        List<Token> tokens = new ArrayList<>();
        List<Character> chars = readInputString();

        List<String> beginState = new ArrayList<>();
        beginState.add("1");

        //String resultsIN = "";
        //String resultsID = "";
        //String resultsRN = "";
        System.out.println("\nOutput:");

        String results = "";
        for (int index = 0; index < chars.size();){
            for (int i = 0; i < automates.size() ; i++) {
                automates.get(i).setCurrentState(beginState);
            }

            List<Priority> pairs = new ArrayList<>();

           //Pair pairIN = f(getAutomateForName(automates,"IN"), chars, index);
           //pairs.add(new Priority("IN", pairIN.getN(), pairIN.isRes()));

            Pair pairRN = f(getAutomateForName(automates, "RN"), chars, index);
            pairs.add(new Priority("RN", pairRN.getN(), pairRN.isRes()));

            Pair pairID = f(getAutomateForName(automates, "ID"), chars, index);
            pairs.add(new Priority("ID", pairID.getN(), pairID.isRes()));


           // System.out.println("\n list:");
           // showList(pairs);

            sortList(pairs);

            //System.out.println("\nnew list:");

            //showList(pairs);



          for (int i = 0; i < pairs.size() ; i++) {

              if(pairs.get(i).isRes()) {
                //  if (pairs.get(i + 1).isRes() && pairs.get(i).getN() == pairs.get(i + 1).getN()) {
                //     //если совпали N, то смотрим по приоритету
                //     if (getIdxPriority(priority, pairs.get(i).getName()) != -1 &&
                //             getIdxPriority(priority, pairs.get(i).getName()) > getIdxPriority(priority, pairs.get(i + 1).getName())) {
                //         for (int j = index; j < index + pairs.get(i).getN(); j++) {
                //             System.out.println(chars.get(j));
                //             results += chars.get(j).toString();
                //         }
                //         tokens.add(new Token(pairs.get(i).getName(), results));
                //         results = "";
                //         index += pairs.get(i).getN();

                //     }
                // } else {
                      for (int j = index; j < index + pairs.get(i).getN(); j++) {
                          System.out.println(chars.get(j));
                          results += chars.get(j).toString();
                      }
                      tokens.add(new Token(pairs.get(i).getName(), results));
                      results = "";
                      index += pairs.get(i).getN();

                 // }
              }
             else {
                  System.out.println("else");
                  index++;
              }
          }

           //if(pairIN.isRes() && (pairIN.getN() >= pairID.getN())){
           //for (int i = index; i < index + pairIN.getN(); i++) {
           //        resultsIN += chars.get(i).toString();
           //    }
           //    tokens.add(new Token("IN", resultsIN));
           //    resultsIN = "";
           //    index += pairIN.getN();
           //}
           //else if(pairID.isRes() && (pairID.getN() > pairIN.getN())){
           //    for (int i = index; i < index + pairID.getN(); i++) {
           //        resultsID += chars.get(i).toString();
           //    }
           //    tokens.add(new Token("ID", resultsID));
           //    resultsID = "";
           //    index += pairID.getN();
           //}
           //else if(pairRN.isRes() ){
           //    for (int i = index; i < index + pairRN.getN(); i++) {
           //        resultsRN += chars.get(i).toString();
           //    }
           //    tokens.add(new Token("RN", resultsRN));
           //    resultsRN = "";
           //    index += pairRN.getN();
           //}
           //else {
           //    index++;
           //}
        }

        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(tokens.get(i).getName() + " - " + tokens.get(i).getString());
        }
     }

    private static void sortList(List<Priority> pairs) {
        System.out.println("Sorting...");
        Priority temp = null;
        for(int i=0; i < pairs.size(); i++){
            for(int j=1; j < (pairs.size()-i); j++){
                if(pairs.get(j-1).getN() < pairs.get(j).getN()){

                    temp = pairs.get(j-1);
                    pairs.set(j-1, pairs.get(j));
                    pairs.set(j, temp);
                }

            }
        }

    }


    public static void showList(List<Priority> list){
        for (int i = 0; i < list.size() ; i++) {
            System.out.print(list.get(i).getName() + ": " + list.get(i).getN() + " "+  list.get(i).isRes() + ",");
        }
        System.out.println();
    }
    public static int getIdxPriority(List<String> priority, String name){
        for (int i = 0; i < priority.size(); i++) {
            if(priority.get(i).equals(name)){
                return i;
            }
        }
        return -1;
    }




}
