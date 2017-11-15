package util;

public class Pair implements Comparable<Pair>{
    boolean res;
    int n;

    public Pair(boolean res, int n) {
        this.res = res;
        this.n = n;
    }

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    @Override
    public int compareTo(Pair o) {
        return n < o.n ? -1 : n == o.n ? 0 : 1;
    }
}