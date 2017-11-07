import Automates.Automate;
import Automates.DeterminatedAutomate;
import Structure.Entity;
import Structure.Pair;
import Structure.Entity;
import Structure.Tetro;
import Structure.Token;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static List<String> priority = new ArrayList<>();

    //Возвращает true/false и n - максимальную длину найденной подстроки
    private static Pair f(Automate automate, List<Character> chars, int index){

        boolean result = false;
        int count = 0;
        List<String> states;
        List<String> newStates = null;
        for (int i = index; i < chars.size(); i++) {
            if(automate.signs.contains(chars.get(i).toString())){ //если содержит такой вх. сигнал
              // System.out.println(chars.get(i) + " State = " + automate.getCurrentState());
                automate.execute(chars.get(i));
                newStates = automate.getCurrentState();
             // System.out.println("New state = " + newStates);
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

    public static List<Character> readInputString() throws IOException {
        List<Character> chars = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get("input.txt"), StandardCharsets.UTF_8);

        for (int i = 0; i < lines.size() ; i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                chars.add(lines.get(i).charAt(j));
            }
           // chars.add(lines.get(i));
            if(i < lines.size() - 1)
                chars.add('\n');
        }
        System.out.println("Input string: ");

        for (Character c : chars) {
            System.out.print(c);
        }

        System.out.println("\n\nInput string with spec-symbols:");

        String s = "";
        for (int i = 0; i < chars.size(); i++) {
             if(chars.get(i) == '\n') {
                 s += "\\n";
             }
             else if(chars.get(i) == ' '){
                 s+="\\m";
             }
             else
                s += chars.get(i).toString();
        }

        s = s.replace("\\m\\m\\m\\m", "\\t");

        System.out.println(s);
        chars.clear();
        for (int i = 0; i < s.length(); i++) {
            chars.add(s.charAt(i));
        }
        System.out.println();
        return chars;
    }

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
                    else if(ch.equals("other")){
                        String others = ".,{}[]:@()?<>&^=";
                        for (int j = 0; j < others.length(); j++) {
                            signs.add(Character.toString(others.charAt(j)));
                        }
                    }
                    else if(ch.equals("\\m")){
                        signs.add(" ");
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
                            else if(ch.equals("other")){
                                String others = ".,{}[]:@()?<>&^=";
                                for (int j = 0; j < others.length(); j++) {
                                    input.add(Character.toString(others.charAt(j)));
                                }
                                //System.out.println(signs);
                            }

                            else if(ch.equals("\\m")){
                                input.add(" ");
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
        Automate automateCM = new DeterminatedAutomate();
        automateCM.setName("CM");
        Automate automateKW = new DeterminatedAutomate();
        automateKW.setName("KW");
        Automate automateWS = new DeterminatedAutomate();
        automateWS.setName("WS");
        Automate automateCB = new DeterminatedAutomate();
        automateCB.setName("CB");
        Automate automateOB = new DeterminatedAutomate();
        automateOB.setName("OB");
        Automate automateID = new DeterminatedAutomate();
        automateID.setName("ID");
        Automate automateIN = new DeterminatedAutomate();
        automateIN.setName("IN");
        Automate automateRN = new DeterminatedAutomate();
        automateRN.setName("RN");

        readInputAutomate(automateID, "input_automate_ID.txt");
        readInputAutomate(automateIN, "input_automate_IN.txt");
        readInputAutomate(automateRN, "input_automate_RN.txt");
        readInputAutomate(automateCB, "input_automate_CB.txt");
        readInputAutomate(automateOB, "input_automate_OB.txt");
        readInputAutomate(automateWS, "input_automate_WS.txt");
        readInputAutomate(automateKW, "input_automate_KW.txt");
        readInputAutomate(automateCM, "input_automate_CM.txt");

        readPriority();
       // System.out.println(priority);

        List<Automate> automates = new ArrayList<>();

        for (int i = 0; i < priority.size(); i++) {
            switch (priority.get(i)){
                case "IN": automates.add(automateIN); break;
                case "ID": automates.add(automateID); break;
                case "RN": automates.add(automateRN); break;
                case "CB": automates.add(automateCB); break;
                case "OB": automates.add(automateOB); break;
                case "WS": automates.add(automateWS); break;
                case "KW": automates.add(automateKW); break;
                case "CM": automates.add(automateCM); break;
            }
        }

        List<Token> tokens = new ArrayList<>();
        List<Character> chars = readInputString();

       // List<String> chars = readInputString();
        List<String> beginState = new ArrayList<>();
        beginState.add("1");

        System.out.println("\nOutput:");
        String results = "";
        for (int index = 0; index < chars.size();) {
            for (int i = 0; i < automates.size(); i++) {
                automates.get(i).setCurrentState(beginState);
            }

            List<Entity> pairs = new ArrayList<>();

            for (int i = 0; i < automates.size(); i++) {
               // System.out.println("AUTOMATE " + automates.get(i).getName());
                Pair pair = f(automates.get(i),chars, index);
                pairs.add(new Entity(automates.get(i).getName(), pair.getN(), pair.isRes()));
                automates.get(i).setCurrentState(beginState);
            }
            //System.out.println("\n list:");
            //showList(pairs);
            sortList(pairs);
            //System.out.println("\nnew list:");
            //showList(pairs);

            Entity entity = pairs.get(0); //берем первый токен
            if (entity.isRes()) {
                for (int i = index; i < index + entity.getN(); i++) {
                    results += chars.get(i).toString();
                }

                tokens.add(new Token(entity.getName(), results));
                results = "";
                index += entity.getN();
              //  System.out.println("result " + tokens.get(tokens.size() -1).getName() + " " +  tokens.get(tokens.size() -1).getString());
            } else {
                index++;
            }
        }

           for (int i = 0; i < tokens.size(); i++) {
               System.out.println(tokens.get(i).getName() + " - " + tokens.get(i).getString());
           }
     }

    private static void sortList(List<Entity> pairs) {
      //System.out.println("Sorting...");
        Entity temp;
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

    public static void showList(List<Entity> list){
        for (int i = 0; i < list.size() ; i++) {
            System.out.print(list.get(i).getName() + ": " + list.get(i).getN() + " "+  list.get(i).isRes() + ",");
        }
        System.out.println();
    }

}
