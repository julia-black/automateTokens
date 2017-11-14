import Automates.Automate;
import Automates.DeterminatedAutomate;
import Automates.NotDeterminatedAutomate;
import OperationForAutomate.Operations;
import Structure.*;
import Structure.Entity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static List<String> priority = new ArrayList<>();

    public static int idxState = 0;
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
                        String others = ".,{}[]:@()?<>&^= ";
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

    public static List<Lexeme> readLexemes()throws IOException{
        List<Lexeme> lexemes = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("input_lexems.txt"), StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            String[] s = lines.get(i).split(":");
            //System.out.println(s[0] +" " + Integer.parseInt(s[1]) + " " + s[2]);
            lexemes.add(new Lexeme(s[0], Integer.parseInt(s[1]), s[2]));

        }

       //for (int i = 0; i < lexems.size(); i++) {
       //    System.out.println(lexems.get(i).getName() + " " + lexems.get(i).getPrior() + " " + lexems.get(i).getRegex());
       //}
        return lexemes;
    }

    public static Automate getAutomateForName(List<Automate> automates, String name){
        for (int i = 0; i < automates.size(); i++) {
            if(automates.get(i).getName().equals(name)) return automates.get(i);
        }
        return null;
    }

    public static Automate createSimpleAutomate(String name, String symbol){ //автомат просто для 1 символа
        Automate automate = new NotDeterminatedAutomate();
        List<String> beginState = new ArrayList<>();
        List<String> states = new ArrayList<>();
        List<String> signs = new ArrayList<>();
        List<Tetro> transactions = new ArrayList<>();
        List<String> endStates = new ArrayList<>();
        beginState.add("s"+idxState);
        states.addAll(beginState);
        idxState++;
        automate.setName(name);

        //если просто символ
        if(symbol.length() == 1){
            System.out.println("!");
            signs.add(Character.toString(symbol.charAt(0)));
        }
        //если это спецсимвол
        else if(symbol.length() == 2) {
            if (symbol.charAt(0) == '\\') {
                switch (symbol.charAt(1)) {
                    case ')':
                    case '|':
                    case '?':
                    case '*':
                    case '(':
                        signs.add(Character.toString(symbol.charAt(1)));
                        break;
                }
            }
        }
        states.add("s" + idxState); //номер состояния равен кол-ву символов, которые уже добавлены
        idxState++;
        endStates.clear();
        endStates.add(states.get(states.size()-1));//то конечным состоянием будет последнее добавленное состояние

       // System.out.println(signs.size() + " " + beginState + endStates);
        transactions.add(new Tetro(signs.get(signs.size() - 1), beginState.get(0), endStates));

        automate.setStates(states);
        automate.setSigns(signs);
        automate.setBeginState(beginState);
        automate.setTransaction(transactions);
        automate.setEndStates(endStates);
        System.out.println("Signs: " + signs);
        System.out.println("All states: " + states);
        System.out.println("Begin states: " + beginState);
        System.out.println("End states: " + endStates);
        for (int i = 0; i < transactions.size() ; i++) {
            System.out.println(transactions.get(i).toString());
        }
        return automate;
    }

    public static Automate createAutomate(String name, String regex){
        Automate automate = new NotDeterminatedAutomate();
        if(regex.length() < 2 || (regex.length() == 2 && regex.charAt(0) =='\\')){
            automate = createSimpleAutomate(name, regex);
        }
        else//если автомат не из одного символа
        {
            System.out.println(regex + regex.length());

            for (int i = 0; i < regex.length(); i++) {
                if (i == 0) {
                    if (regex.charAt(i) != '(' && regex.charAt(i) != '|' && regex.charAt(i) != ')') {
                        Automate automate1 = createSimpleAutomate(name, Character.toString(regex.charAt(i)));
                        automate = automate1;
                    }
                } else {
                    if (regex.charAt(i) != '(' && regex.charAt(i) != '|' && regex.charAt(i) != ')') {
                        //System.out.println(regex.substring(i,i+1));
                        System.out.println("Regex = " + regex.substring(i, i + 1));
                        Automate automate1 = createSimpleAutomate(name, Character.toString(regex.charAt(i)));

                        System.out.println("Automate builded");
                        Operations oper = new Operations();
                        System.out.println(automate.getSigns() + " " + automate1.getSigns());
                        automate = oper.concat(automate, automate1);
                    }
                }
            }
        }
        return automate;
    }


    public static void main(String[] args) throws IOException {

        List<Lexeme> lexemes = readLexemes();
        List<Automate> automates = new ArrayList<>();

        sortLexemes(lexemes);
        for (int i = 0; i < lexemes.size(); i++) {
            automates.add(createAutomate(lexemes.get(i).getName(), lexemes.get(i).getRegex()));
        }

     //  Automate automateCM = new DeterminatedAutomate();
     //  automateCM.setName("CM");
     //  Automate automateKW = new DeterminatedAutomate();
     //  automateKW.setName("KW");
     //  Automate automateWS = new DeterminatedAutomate();
     //  automateWS.setName("WS");
     //  Automate automateCB = new DeterminatedAutomate();
     //  automateCB.setName("CB");
     //  Automate automateOB = new DeterminatedAutomate();
     //  automateOB.setName("OB");
     //  Automate automateID = new DeterminatedAutomate();
     //  automateID.setName("ID");
     //  Automate automateIN = new DeterminatedAutomate();
     //  automateIN.setName("IN");
     //  Automate automateRN = new DeterminatedAutomate();
     //  automateRN.setName("RN");

     //  readInputAutomate(automateID, "input_automate_ID.txt");
     //  readInputAutomate(automateIN, "input_automate_IN.txt");
     //  readInputAutomate(automateRN, "input_automate_RN.txt");
     //  readInputAutomate(automateCB, "input_automate_CB.txt");
     //  readInputAutomate(automateOB, "input_automate_OB.txt");
     //  readInputAutomate(automateWS, "input_automate_WS.txt");
     //  readInputAutomate(automateKW, "input_automate_KW.txt");
     //  readInputAutomate(automateCM, "input_automate_CM.txt");

     //  readPriority();

     //  List<Automate> automates = new ArrayList<>();

     //  for (int i = 0; i < priority.size(); i++) {
     //      switch (priority.get(i)){
     //          case "IN": automates.add(automateIN); break;
     //          case "ID": automates.add(automateID); break;
     //          case "RN": automates.add(automateRN); break;
     //          case "CB": automates.add(automateCB); break;
     //          case "OB": automates.add(automateOB); break;
     //          case "WS": automates.add(automateWS); break;
     //          case "KW": automates.add(automateKW); break;
     //          case "CM": automates.add(automateCM); break;
     //      }
     //  }

       List<Token> tokens = new ArrayList<>();
       List<Character> chars = readInputString();

       List<String> beginState = new ArrayList<>();
       beginState.add("1");

       System.out.println("\nOutput:");
       String results = "";
       for (int index = 0; index < chars.size();) {
           for (int i = 0; i < automates.size(); i++) {
               //System.out.println(automates.get(i).toString());
               automates.get(i).setCurrentState(beginState);
           }

           List<Entity> pairs = new ArrayList<>();

           for (int i = 0; i < automates.size(); i++) {
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

    private static void sortLexemes(List<Lexeme> lexemes) {
        Lexeme temp;
        for(int i=0; i < lexemes.size(); i++){
            for(int j=1; j < (lexemes.size()-i); j++){
                if(lexemes.get(j-1).getPrior() < lexemes.get(j).getPrior()){

                    temp = lexemes.get(j-1);
                    lexemes.set(j-1, lexemes.get(j));
                    lexemes.set(j, temp);
                }

            }
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
