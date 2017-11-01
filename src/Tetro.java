import java.util.ArrayList;
import java.util.List;

public class Tetro {
    List<String> input;
    String inputState;
    String result;
    List<String> results;

    public Tetro( List<String> input, String inputState, String result) {
        this.input = input;
        this.inputState = inputState;
        this.result = result;
        this.results = null;
    }
    public Tetro(String input, String inputState, String result) {
        this.input = new ArrayList<>();
        this.input.add(input);
        this.inputState = inputState;
        this.result = result;
        this.results = null;
    }

    public Tetro( String input, String inputState, List<String> results) {
        this.input = new ArrayList<>();
        this.input.add(input);

       // this.input = input;
        this.inputState = inputState;
        this.results = results;
        this.result = null;
    }
    public Tetro( List<String> input, String inputState, List<String> results) {
        this.input = input;
        this.inputState = inputState;
        this.results = results;
        this.result = null;
    }

    public List<String> getResults() {
        return results;
    }

    public List<String> getInput() {
        return input;
    }


    public String getInputState() {
        return inputState;
    }


    public String getResult() {
        return result;
    }

}
