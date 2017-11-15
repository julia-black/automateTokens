package util;

public class Lexeme {
    String name;
    int prior;
    String regex;

    public Lexeme(String name, int prior, String regex) {
        this.name = name;
        this.prior = prior;
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrior() {
        return prior;
    }

    public void setPrior(int prior) {
        this.prior = prior;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
