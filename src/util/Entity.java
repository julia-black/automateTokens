package util;

public class Entity {

    String name;

    int N;

    boolean res;

    public Entity(String name, int n, boolean res) {
        this.name = name;
        N = n;
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }
}
