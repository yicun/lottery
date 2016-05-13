package me.chyc.linkedin.lottery;

/**
 * Created by chyc on 8/2/14.
 */
public class Triple<T1,T2,T3> extends Object{
    T1 t1;
    T2 t2;
    T3 t3;

    public Triple(T1 t1, T2 t2, T3 t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Triple))
            return false;

        Triple<T1, T2, T3> p = (Triple<T1, T2, T3>) obj;
        if (t1.equals(p.t1) && t2.equals(p.t2) && t3.equals(p.t3))
            return true;
        else
            return false;
    }

    public int hashCode() {
        return this.t1.hashCode() * this.t2.hashCode() * this.t3.hashCode();
    }

    @Override
    public String toString() {
        return "<"+this.t1 + "," + this.t2+ "," + this.t3+">";
    }
}
