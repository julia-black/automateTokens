public class Token {
    String name;
    String string;

    public Token(String name, String string) {
        this.name = name;
        this.string = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
