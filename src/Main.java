import automates.Automate;
import automates.AutomateMaker;
import automates.NotDeterminatedAutomate;
import operationForAutomate.Operations;
import util.*;
import util.Entity;

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
           // if(automate.signs.contains(chars.get(i).toString())){ //если содержит такой вх. сигнал
            if(automate.containsSignal(chars.get(i).toString())){
             //  System.out.println(chars.get(i) + " State = " + automate.getCurrentState());
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

     //System.out.println("\n\nInput string with spec-symbols:");

     //String s = "";
     //for (int i = 0; i < chars.size(); i++) {
     //     if(chars.get(i) == '\n') {
     //         s += "\\n";
     //     }
     //     else
     //        s += chars.get(i).toString();
     //}
     //// s = s.replace("\\m\\m\\m\\m", "\\t");

     // System.out.println(s);

       //System.out.println();
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
    }

    public static List<Lexeme> readLexemes()throws IOException{
        List<Lexeme> lexemes = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("input_lexems.txt"), StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            String[] s = lines.get(i).split(":");
            lexemes.add(new Lexeme(s[0], Integer.parseInt(s[1]), s[2]));
        }
        return lexemes;
    }

    public static Automate getAutomateForName(List<Automate> automates, String name){
        for (int i = 0; i < automates.size(); i++) {
            if(automates.get(i).getName().equals(name)) return automates.get(i);
        }
        return null;
    }

    private static String getStringWithSpecSymbols(String str) {

        str = str.replace("\t","\\t");
        str = str.replace("\n", "\\n");
        str = str.replace("\r","\\r");
        str = str.replace("    ","\\t");
        str = str.replace(" ", "\\m");
        return str;
    }

    public static void main(String[] args) throws IOException {
        List<Lexeme> lexemes = readLexemes();
        List<Automate> automates = new ArrayList<>();


        sortLexemes(lexemes);
        for (int i = 0; i < lexemes.size(); i++) {
            AutomateMaker automateMaker = new AutomateMaker(lexemes.get(i).getName(), lexemes.get(i).getPrior(), lexemes.get(i).getRegex());
            Automate automateTmp = automateMaker.create(); //создаем автомат
        //    System.out.println("\nAUTOMATE" + automateTmp.toString() + "\n_______________");
            automates.add(automateTmp);
        }

     List<Token> tokens = new ArrayList<>();
     List<Character> chars = readInputString();

     System.out.println("\nOutput:");
     String results = "";
     for (int index = 0; index < chars.size();) {
         for (int i = 0; i < automates.size(); i++) {
             //устаналиваем состояния всех автоматов  в начальные
             //System.out.println(automates.get(i).toString());
             automates.get(i).setCurrentState(automates.get(i).getBeginState());
         }

         List<Entity> pairs = new ArrayList<>();

         for (int i = 0; i < automates.size(); i++) {

             automates.get(i).setCurrentState(automates.get(i).getBeginState());
             Pair pair = f(automates.get(i),chars, index);

             pairs.add(new Entity(automates.get(i).getName(), pair.getN(), pair.isRes()));
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

        String text = "";
        for (int i = 0; i < tokens.size(); i++) {
            text += "<" + tokens.get(i).getName() + "," + getStringWithSpecSymbols(tokens.get(i).getString()) + ">\n";
            System.out.println("<" + tokens.get(i).getName() + "," + getStringWithSpecSymbols(tokens.get(i).getString()) + ">");
        }
        writeResult(text);
     }

   private static void writeResult(String tokens){
       try(FileWriter writer = new FileWriter("output.txt", false))
       {
           // запись всей строки
           writer.write(tokens);

       }
       catch(IOException ex){
           System.out.println(ex.getMessage());
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
