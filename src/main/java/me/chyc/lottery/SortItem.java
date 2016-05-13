package me.chyc.lottery;

import java.util.Comparator;

/**
 * Created by chyc on 4/23/16.
 */
public class SortItem implements Comparable<SortItem> {
    int key;
    Double score;

    public SortItem(int key, Double score) {
        this.key = key;
        this.score = score;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


    public int compareTo(SortItem o) {
        return 0;
    }
}
