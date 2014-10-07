package me.chyc.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by chyc on 8/2/14.
 */
public class Number {
    int no;
    List<Integer> nums;


    public Number(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, "\t\n\r ");
        this.no = Integer.valueOf(tokenizer.nextToken());
        this.nums = new ArrayList<Integer>();
        while (tokenizer.hasMoreTokens())
            this.nums.add(Integer.valueOf(tokenizer.nextToken()));
    }

    public List<Pair<Integer, Integer>> toTwoCooccurrence() {
        List<Pair<Integer, Integer>> twoCooccurrence = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < 6; i++)
            for (int j = i + 1; j < 6; j++)
                twoCooccurrence.add(new Pair<Integer, Integer>(this.nums.get(i), this.nums.get(j)));

        return twoCooccurrence;
    }

    public List<Triple<Integer, Integer, Integer>> toThreeCooccurrence() {
        List<Triple<Integer, Integer, Integer>> threeCooccurrence = new ArrayList<Triple<Integer, Integer, Integer>>();
        for (int i = 0; i < 6; i++)
            for (int j = i + 1; j < 6; j++)
                for (int k = j + 1; k < 6; k++)
                    threeCooccurrence.add(new Triple<Integer, Integer, Integer>(this.nums.get(i), this.nums.get(j), this.nums.get(k)));

        return threeCooccurrence;
    }

    public List<MultiTuple<Integer>> toMultiCooccurrence(int n) {
        List<MultiTuple<Integer>> multiCooccurrence = new ArrayList<MultiTuple<Integer>>();
        if (n < 2 || n > 5)
            return null;
        if (n == 2) {
            List<Pair<Integer, Integer>> pairCs = toTwoCooccurrence();
            for (Pair<Integer, Integer> pairC : pairCs)
                multiCooccurrence.add(new MultiTuple<Integer>(pairC.t1, pairC.t2));
        }
        if (n == 3) {
            List<Triple<Integer, Integer, Integer>> tripleCs = toThreeCooccurrence();
            for (Triple<Integer, Integer, Integer> tripleC : tripleCs)
                multiCooccurrence.add(new MultiTuple<Integer>(tripleC.t1, tripleC.t2, tripleC.t3));
        }
        if (n == 4) {
            List<Pair<Integer, Integer>> pairCs = toTwoCooccurrence();
            for (Pair<Integer, Integer> pairC : pairCs) {
                List<Integer> tmp = new ArrayList<Integer>(this.nums.subList(0,6));
                tmp.remove(pairC.t2);
                multiCooccurrence.add(new MultiTuple<Integer>(tmp.get(0), tmp.get(1), tmp.get(2), tmp.get(3)));
            }
        }
        if (n == 5) {
            for (int i = 0; i < 6; i++) {
                List<Integer> tmp = new ArrayList<Integer>(this.nums.subList(0,6));
                tmp.remove(this.nums.get(i));
                multiCooccurrence.add(new MultiTuple<Integer>(tmp.get(0), tmp.get(1), tmp.get(2), tmp.get(3), tmp.get(4)));
            }
        }

        return multiCooccurrence;
    }

    /**
     * 双色球
     */
    public List<Integer> getReds(){
        return this.nums.subList(0, 6);
    }
    public Integer getBlue(){
        return this.nums.get(6);
    }

    public List<Integer> getNums(int start, int end){
        return this.nums.subList(start, end);
    }
    public Integer getNum(int index){
        return this.nums.get(index);
    }

    @Override
    public String toString() {
        return "Number{" +
                "no=" + no +
                ", nums=" + nums +
                '}';
    }
}
