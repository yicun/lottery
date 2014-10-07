package me.chyc.lottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chyc on 8/2/14.
 */
public class MultiTuple<T> extends Object {
    List<T> values;

    public MultiTuple(T... values) {
        this.values = new ArrayList<T>();
        for (T value : values)
            this.values.add(value);
    }

    public MultiTuple(List<T> values){
        this.values = new ArrayList<T>();
        for (T value: values)
            this.values.add(value);
    }


    public void insert(T value){
        if(this.values == null)
            this.values = new ArrayList<T>();
        this.values.add(value);
    }

    public void insert(int index, T value){
        if (this.values == null)
            this.values = new ArrayList<T>();
        if (this.values.size() < index || index < 0)
            return;

        this.values.add(index, value);
    }

    public MultiTuple<T> copy(){
        return new MultiTuple<T>(this.values);

    }
    public static void main(String args[]) {
        int[] num = {1, 2, 3, 4};

        HashMap<MultiTuple<Integer>, Integer> test = new HashMap<MultiTuple<Integer>, Integer>();
        MultiTuple<Integer> mt = new MultiTuple<Integer>(1, 2, 3, 4);
        MultiTuple<Integer> mt1 = new MultiTuple<Integer>(1, 2, 3, 4);

        test.put(mt, 1);
        test.put(mt1, 2);

        System.out.println(mt.hashCode());
        System.out.println(mt1.hashCode());
        if (mt.equals(mt1))
            System.out.println("Equals");
        else
            System.out.println("Not Equals");


        System.out.println(test.size());
        System.out.println(test.get(mt));


        Integer i = new Integer(30);
        Integer i1 = new Integer(30);
        System.out.println(i.hashCode());
        System.out.println(i1.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MultiTuple))
            return false;
        MultiTuple<T> p = (MultiTuple<T>) obj;

//        System.out.println(this.values.size() + "\t" + p.values.size());
        if (this.values.size() != p.values.size())
            return false;
        for (int i = 0; i < this.values.size(); i++) {
//            System.out.println(this.values.get(i) +"\t" + p.values.get(i));
            if (!((T) this.values.get(i)).equals((T) p.values.get(i)))
                return false;
        }
        return true;
    }

    public int hashCode() {
        int code = 1;
        for (int i = 0; i < this.values.size(); i++)
            code *= this.values.get(i).hashCode();
        return code;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<" + this.values.get(0));
        for (int i = 1; i < this.values.size(); i++)
            sb.append("," + this.values.get(i));
        sb.append(">");
        return sb.toString();
    }

    public String toString2() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.values.get(0));
        for (int i = 1; i < this.values.size(); i++)
            sb.append("," + this.values.get(i));
        return sb.toString();
    }
}
