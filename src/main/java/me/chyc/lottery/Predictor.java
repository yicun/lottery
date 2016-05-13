package me.chyc.lottery;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chyc on 8/17/14.
 */
public class Predictor {

    public static Double[] Normalize(Double[] predicts, String algo) {
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < predicts.length; i++) {
            if (predicts[i] < min)
                min = predicts[i];
            if (predicts[i] > max)
                max = predicts[i];
        }
        if (algo.contains("log")) {
            for (int i = 0; i < predicts.length; i++)
                predicts[i] = Math.log(predicts[i] - min + 1) / Math.log(max - min + 1);

            return predicts;
        }
        if (algo.contains("avg")) {
            for (int i = 0; i < predicts.length; i++)
                predicts[i] = (predicts[i] - min) / (max - min);
            return predicts;
        }

        if (algo.contains("exp")) {
            for (int i = 0; i < predicts.length; i++)
                predicts[i] = (Math.exp((predicts[i] - min) / (max - min)) - 1.0) / (Math.exp(1.0) - Math.exp(0.0));
            return predicts;
        }

        return predicts;


    }

    public static void test1() throws IOException {
        List<Number> numbers = NumberGetter.loadNumbers(new File("lottery/src/main/resources/ssq.num"));
        System.out.println(numbers.size());
        int n = 1;
        HashMap<MultiTuple<Integer>, Double[]> probability = ShuangSeQiu.Red2RedCooccurrenceProbability(numbers, n);
        int i = 0;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("lottery/src/main/resources/red2red." + n + ".num")));
        for (MultiTuple<Integer> comb : probability.keySet()) {
            Double count[] = probability.get(comb);
            Double sum = 0.0;
            for (int j = 0; j < count.length; j++)
                sum += count[j];
            for (int j = 0; j < count.length; j++) {
                if (count[j] == 0.0)
                    continue;
                bw.write(comb.toString2() + "\t" + (j + 1) + "\t" + count[j] / sum);
                bw.newLine();
            }
        }
        bw.close();
    }

    public static void test2() throws IOException {
        List<Number> numbers = NumberGetter.loadNumbers(new File("lottery/src/main/resources/ssq.num"));
        System.out.println(numbers.size());
        int n = 4;
        HashMap<MultiTuple<Integer>, Double[]> probability = ShuangSeQiu.Red2BlueCooccurrenceProbability(numbers, n);
        int i = 0;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("lottery/src/main/resources/red2blue." + n + ".num")));
        for (MultiTuple<Integer> comb : probability.keySet()) {
            Double count[] = probability.get(comb);
            Double sum = 0.0;
            for (int j = 0; j < count.length; j++)
                sum += count[j];
            for (int j = 0; j < count.length; j++) {
                if (count[j] == 0.0)
                    continue;
                bw.write(comb.toString2() + "\t" + (j + 1) + "\t" + count[j] / sum);
                bw.newLine();
            }
        }
        bw.close();
    }


    public static Double[] add(Double[] a, Double[] b) {
        if (a.length != b.length)
            return null;
        List<Double> c = new ArrayList<Double>();
        for (int i = 0; i < a.length; i++)
            c.add(a[i] + b[i]);

        return (Double[]) c.toArray();
    }


    public static Double[] avg(Double[]... predicts) {
        int len = predicts[0].length;
        for (Double[] predict : predicts) {
            if (len != predict.length)
                return null;
        }
        Double[] c = new Double[len];

        for (int i = 0; i < predicts.length; i++) {
            if (i == 0) {
                for (int j = 0; j < len; j++)
                    c[j] = predicts[i][j];
            } else {
                for (int j = 0; j < len; j++)
                    c[j] += predicts[i][j];
            }
        }
        for (int i = 0; i < c.length; i++)
            c[i] /= predicts.length;

        return c;
    }

    public static Double[] max(Double[]... predicts) {
        int len = predicts[0].length;
        for (Double[] predict : predicts) {
            if (len != predict.length)
                return null;
        }
        Double[] c = new Double[len];

        for (int i = 0; i < predicts.length; i++) {
            if (i == 0) {
                for (int j = 0; j < len; j++)
                    c[j] = predicts[i][j];
            } else {
                for (int j = 0; j < len; j++)
                    if (predicts[i][j] > c[j])
                        c[j] = predicts[i][j];
            }
        }

        return c;
    }

    public static Double[] min(Double[]... predicts) {
        int len = predicts[0].length;
        for (Double[] predict : predicts) {
            if (len != predict.length)
                return null;
        }
        Double[] c = new Double[len];

        for (int i = 0; i < predicts.length; i++) {
            if (i == 0) {
                for (int j = 0; j < len; j++)
                    c[j] = predicts[i][j];
            } else {
                for (int j = 0; j < len; j++)
                    if (predicts[i][j] < c[j])
                        c[j] = predicts[i][j];
            }
        }

        return c;
    }

    public static int[] rank(Double[] predicts) {
        int[] ranks = new int[predicts.length];
        for (int i = 0; i < predicts.length; i++) {
            int rank = 0;
            for (int j = 0; j < predicts.length; j++) {
                if (predicts[j] <= predicts[i])
                    rank++;
            }
            while (ranks[predicts.length - rank] != 0)
                rank--;
            ranks[predicts.length - rank] = i + 1;
        }

        return ranks;
    }


    public static int[] rank2(Double[] predicts) {
        int[] ranks = new int[predicts.length];
        for (int i = 0; i < predicts.length; i++) {
            int rank = 0;
            for (int j = 0; j < predicts.length; j++) {
                if (predicts[j] > predicts[i])
                    rank++;
            }

            ranks[i] = rank + 1;
        }

        return ranks;
    }

    public static void test1_1() throws IOException {
        List<Number> numbers = NumberGetter.loadNumbers(new File("lottery/src/main/resources/ssq.num"));

        Double[] predicts_1 = Normalize(ShuangSeQiu.Red2RedPredictor(numbers, 1), "exp");
        Double[] predicts_2 = Normalize(ShuangSeQiu.Red2RedPredictor(numbers, 2), "exp");
        Double[] predicts_3 = Normalize(ShuangSeQiu.Red2RedPredictor(numbers, 3), "exp");
        Double[] predicts_4 = Normalize(ShuangSeQiu.Red2RedPredictor(numbers, 4), "exp");
        Double[] predicts_avg = avg(predicts_1, predicts_2, predicts_3, predicts_4);
        Double[] predicts_max = max(predicts_1, predicts_2, predicts_3, predicts_4);
        Double[] predicts_min = min(predicts_1, predicts_2, predicts_3, predicts_4);


        for (int i = 0; i < predicts_2.length; i++)
            System.out.println((i + 1) + "\t"
                    + predicts_1[i] + "\t"
                    + predicts_2[i] + "\t"
                    + predicts_3[i] + "\t"
                    + predicts_4[i] + "\t"
                    + predicts_avg[i] + "\t"
                    + predicts_max[i] + "\t"
                    + predicts_min[i]);
        int[] rank_1 = rank(predicts_1);
        int[] rank_2 = rank(predicts_2);
        int[] rank_3 = rank(predicts_3);
        int[] rank_4 = rank(predicts_4);
        int[] rank_avg = rank(predicts_avg);
        int[] rank_max = rank(predicts_max);
        int[] rank_min = rank(predicts_min);


        for (int i = 0; i < rank_1.length; i++)
            System.out.println(rank_1[i] + "\t"
                    + rank_2[i] + "\t"
                    + rank_3[i] + "\t"
                    + rank_4[i] + "\t"
                    + rank_avg[i] + "\t"
                    + rank_max[i] + "\t"
                    + rank_min[i]);

        int[] rank2_1 = rank2(predicts_1);
        int[] rank2_2 = rank2(predicts_2);
        int[] rank2_3 = rank2(predicts_3);
        int[] rank2_4 = rank2(predicts_4);
        int[] rank2_avg = rank2(predicts_avg);
        int[] rank2_max = rank2(predicts_max);
        int[] rank2_min = rank2(predicts_min);
        for (int i = 0; i < rank2_1.length; i++)
            System.out.println((i + 1) + "\t" + rank2_1[i] + "\t"
                    + rank2_2[i] + "\t"
                    + rank2_3[i] + "\t"
                    + rank2_4[i] + "\t"
                    + rank2_avg[i] + "\t"
                    + rank2_max[i] + "\t"
                    + rank2_min[i]);
    }

    public static void test2_1() throws IOException {
        List<Number> numbers = NumberGetter.loadNumbers(new File("lottery/src/main/resources/ssq.num"));

        Double[] predicts_1 = Normalize(ShuangSeQiu.Red2BluePredictor(numbers, 1), "exp");
        Double[] predicts_2 = Normalize(ShuangSeQiu.Red2BluePredictor(numbers, 2), "exp");
        Double[] predicts_3 = Normalize(ShuangSeQiu.Red2BluePredictor(numbers, 3), "exp");
        Double[] predicts_4 = Normalize(ShuangSeQiu.Red2BluePredictor(numbers, 4), "exp");
        Double[] predicts_avg = avg(predicts_1, predicts_2, predicts_3, predicts_4);
        Double[] predicts_max = max(predicts_1, predicts_2, predicts_3, predicts_4);
        Double[] predicts_min = min(predicts_1, predicts_2, predicts_3, predicts_4);


        for (int i = 0; i < predicts_2.length; i++)
            System.out.println((i + 1) + "\t"
                    + predicts_1[i] + "\t"
                    + predicts_2[i] + "\t"
                    + predicts_3[i] + "\t"
                    + predicts_4[i] + "\t"
                    + predicts_avg[i] + "\t"
                    + predicts_max[i] + "\t"
                    + predicts_min[i]);
        int[] rank_1 = rank(predicts_1);
        int[] rank_2 = rank(predicts_2);
        int[] rank_3 = rank(predicts_3);
        int[] rank_4 = rank(predicts_4);
        int[] rank_avg = rank(predicts_avg);
        int[] rank_max = rank(predicts_max);
        int[] rank_min = rank(predicts_min);

        for (int i = 0; i < rank_1.length; i++)
            System.out.println(rank_1[i] + "\t"
                    + rank_2[i] + "\t"
                    + rank_3[i] + "\t"
                    + rank_4[i] + "\t"
                    + rank_avg[i] + "\t"
                    + rank_max[i] + "\t"
                    + rank_min[i]);

        int[] rank2_1 = rank2(predicts_1);
        int[] rank2_2 = rank2(predicts_2);
        int[] rank2_3 = rank2(predicts_3);
        int[] rank2_4 = rank2(predicts_4);
        int[] rank2_avg = rank2(predicts_avg);
        int[] rank2_max = rank2(predicts_max);
        int[] rank2_min = rank2(predicts_min);
        for (int i = 0; i < rank2_1.length; i++)
            System.out.println((i + 1) + "\t" + rank2_1[i] + "\t"
                    + rank2_2[i] + "\t"
                    + rank2_3[i] + "\t"
                    + rank2_4[i] + "\t"
                    + rank2_avg[i] + "\t"
                    + rank2_max[i] + "\t"
                    + rank2_min[i]);


    }


    public static void main(String[] args) throws IOException {
//        test1();
//        test2();
        test1_1();
        System.out.println("============================================");
        test2_1();
    }

}
