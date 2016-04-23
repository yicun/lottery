package me.chyc.lottery;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yicun.chen on 11/29/14.
 */
public class SSQPredictor {
    HashMap<Integer, Number> numList;
    List<Integer> noList;

    public static enum Type {MAX, MIN, SUM, AVG}


    public SSQPredictor(String filePath) {
        try {
            this.numList = loadNums(filePath);
            this.noList = new ArrayList<Integer>(this.numList.keySet());
            Collections.sort(this.noList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int get(int no, int index) {
        if (this.numList.containsKey(no))
            return this.numList.get(no).getNum(index);
        else if (no > 0 && no < this.noList.size())
            return this.numList.get(this.noList.get(no)).getNum(index);
        return 0;
    }


    public HashMap<Integer, Number> loadNums(String filePath) throws IOException {
        HashMap<Integer, Number> nums = new HashMap<Integer, Number>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        String line = null;
        while (null != (line = br.readLine())) {
            Number num = new Number(line);
//            if (nums.containsKey(num.getNo()))
//                System.out.println(line);
            nums.put(num.getNo(), num);
        }
        return nums;
    }

    public int[] subList(int start, int end, int index) {
        List<Integer> subList = new ArrayList<Integer>();
        for (int i = start; i < Math.min(end, this.noList.size()); i++) {
            int No = this.noList.get(i);
            Number num = this.numList.get(No);
            subList.add(num.getNum(index));
        }

        int[] arr = new int[subList.size()];
        for (int i = 0; i < subList.size(); i++)
            arr[i] = subList.get(i);
        return arr;
    }

    public static double calSimilarity(int[] X, int[] Y) {
        if (X == null || Y == null || X.length != Y.length)
            return 0.0;
        double sum_x = 0.0;
        double sum_y = 0.0;
        double sum_xy = 0.0;
        double sum_xx = 0.0;
        double sum_yy = 0.0;
        double N = X.length;
        for (int i = 0; i < N; i++) {
            sum_x += X[i];
            sum_y += Y[i];
            sum_xx += X[i] * X[i];
            sum_yy += Y[i] * Y[i];
            sum_xy += X[i] * Y[i];
        }
        return (sum_xy - sum_x * sum_y / N)
                / Math.sqrt((sum_xx - sum_x * sum_x / N) * (sum_yy - sum_y * sum_y / N));
    }


    public List<SortItem> calSimilarity(int index, int seg) {
        List<SortItem> simis = new ArrayList<SortItem>();
        if (seg >= this.noList.size())
            return simis;
        int N = this.noList.size();
        int[] base = subList(N - seg, N, index);
        for (int i = 0; i < N - seg; i++) {
            int[] tmp = subList(i, i + seg, index);
            double simi = calSimilarity(base, tmp);
            int num = get(i + seg, index);
            simis.add(new SortItem(num, simi));
        }
        return simis;
    }

    public double[] getScore(int index, int seg, int top, Type type) {
        double[] scores = new double[33];
        int[] counts = new int[33];
        for (int i = 0; i < scores.length; i++) {
            counts[i] = 0;
            if (type == Type.MIN)
                scores[0] = Double.MAX_VALUE;
            else if (type == Type.MAX)
                scores[0] = Double.MIN_VALUE;
            else
                scores[0] = 0.0;
        }
        List<SortItem> simis = calSimilarity(index, seg);
        Collections.sort(simis);
        for (SortItem item : simis.subList(0, Math.min(top, simis.size()))) {
            int num = (Integer) item.getKey();
            double score = item.getScore();
            if (num <= 0 || score <= 0.0)
                continue;
            if (type == Type.SUM || type == Type.AVG)
                scores[num - 1] += score;
            if (type == Type.MAX)
                scores[num - 1] = score > scores[num - 1] ? score : scores[num - 1];
            if (type == Type.MIN)
                scores[num - 1] = score < scores[num - 1] ? score : scores[num - 1];
            counts[num - 1]++;
        }

        for (int i = 0; i < scores.length; i++) {
            if (counts[i] == 0)
                scores[i] = 0.0;
            else if (type == Type.AVG)
                scores[i] /= counts[i];
        }

        return scores;
    }

    public HashMap<Integer, Number> getNumList() {
        return numList;
    }

    public void setNumList(HashMap<Integer, Number> numList) {
        this.numList = numList;
    }

    public List<Integer> getNoList() {
        return noList;
    }

    public void setNoList(List<Integer> noList) {
        this.noList = noList;
    }

    public static void main(String args[]) {
        String filePath = "/Users/chyc/Workspaces/Data/lottery/ssq.num";
        SSQPredictor predictor = new SSQPredictor(filePath);
        int seg = 10;
        int index = 5;
        double[][] scores = new double[7][];
        for (index = 0; index < 7; index++) {
            scores[index] = predictor.getScore(index, seg, 20, Type.MAX);
        }
        for (int i = 0; i < 33; i ++){
            System.out.print(i +"\t");
            double sum = 0.0;
            for (int j = 0; j < 6; j ++){
                sum += scores[j][i];
//                System.out.print("\t"+scores[j][i]);
            }
            System.out.println(sum);
        }

        for (int i= 0; i < 33; i ++)
            System.out.println(i + "\t" + scores[6][i]);

    }
}
