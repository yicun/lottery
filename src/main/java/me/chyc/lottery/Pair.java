package me.chyc.lottery;

/**
 * Created by chyc on 8/2/14.
 */
public class Pair<T1, T2> extends Object {
    T1 t1;
    T2 t2;

    public Pair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Pair))
            return false;

        Pair<T1, T2> p = (Pair<T1, T2>) obj;
        if (t1.equals(p.t1) && t2.equals(p.t2))
            return true;
        else
            return false;
    }

    public int hashCode() {
        return this.t1.hashCode() * this.t2.hashCode();
    }

    @Override
    public String toString() {
        return "<"+this.t1 + "," + this.t2+">";
    }

    public static void main(String args[]){
        String a = "aaaa";
        String b = "bbbb";
        String aa = "aaaa";
        String bb = "bbbb";
        Pair<String, String> p = new Pair<String, String>(a,b);
        Pair<String, String> pp = new Pair<String, String>(aa, bb);
        System.out.println(p.hashCode());
        System.out.println(pp.hashCode());
    }
}
