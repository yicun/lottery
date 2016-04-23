package me.chyc.lottery;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.gc;

/**
 * Created by chyc on 8/2/14.
 */
public class ShuangSeQiu {
    public static HashMap<MultiTuple<Integer>, Double[]> Red2RedCooccurrenceProbability(List<Number> numbers, int n) {
        HashMap<MultiTuple<Integer>, Double[]> probability = new HashMap<MultiTuple<Integer>, Double[]>();
        for (int i = n; i < numbers.size(); i++) {
            // 计算i之前n期的历史趋势
            Set<MultiTuple<Integer>> combination = new HashSet<MultiTuple<Integer>>();
            for (int j = i - n; j < i; j++) {
                if (combination.size() == 0) {
                    for (int red : numbers.get(j).getReds())
                        combination.add(new MultiTuple<Integer>(red));
                } else {
                    Set<MultiTuple<Integer>> tmp = combination;
                    combination = new HashSet<MultiTuple<Integer>>();
                    for (MultiTuple<Integer> t : tmp) {
                        for (int red : numbers.get(j).getReds()) {
                            MultiTuple<Integer> c = t.copy();
                            c.insert(red);
                            combination.add(c);
                        }
                    }
                }
            }
            // 计算i之前n期到i期的转移次数
            for (MultiTuple<Integer> comb : combination) {
                Double[] count = new Double[33];
                for (int j = 0; j < count.length; j++)
                    count[j] = 0.0;
                if (probability.containsKey(comb))
                    count = probability.get(comb);
                for (int red : numbers.get(i).getReds())
                    count[red - 1] += 1.0;

                probability.put(comb, count);
            }
        }

        // 计算i之前n期到i期的转移概率
        for (MultiTuple<Integer> comb : probability.keySet()) {
            Double count[] = probability.get(comb);
            Double sum = 0.0;
            for (int j = 0; j < count.length; j++)
                sum += count[j];
            for (int j = 0; j < count.length; j++) {
                if (count[j] == 0.0)
                    continue;
                count[j] = count[j] / sum;
            }
            probability.put(comb, count);
        }

        return probability;
    }

    public static HashMap<MultiTuple<Integer>, Double[]> Red2BlueCooccurrenceProbability(List<Number> numbers, int n) {
        HashMap<MultiTuple<Integer>, Double[]> probability = new HashMap<MultiTuple<Integer>, Double[]>();
        for (int i = n; i < numbers.size(); i++) {
            // 计算i之前n期的历史趋势
            Set<MultiTuple<Integer>> combination = new HashSet<MultiTuple<Integer>>();
            for (int j = i - n; j < i; j++) {
                if (combination.size() == 0) {
                    for (int red : numbers.get(j).getReds())
                        combination.add(new MultiTuple<Integer>(red));
                } else {
                    Set<MultiTuple<Integer>> tmp = combination;
                    combination = new HashSet<MultiTuple<Integer>>();
                    for (MultiTuple<Integer> t : tmp) {
                        for (int red : numbers.get(j).getReds()) {
                            MultiTuple<Integer> c = t.copy();
                            c.insert(red);
                            combination.add(c);
                        }
                    }
                }
            }
            // 计算i之前n期到i期的转移次数
            for (MultiTuple<Integer> comb : combination) {
                Double[] count = new Double[16];
                for (int j = 0; j < count.length; j++)
                    count[j] = 0.0;
                if (probability.containsKey(comb))
                    count = probability.get(comb);

                int blue = numbers.get(i).getBlue();
                count[blue - 1] += 1.0;

                probability.put(comb, count);
            }
        }

        // 计算i之前n期到i期的转移概率
        for (MultiTuple<Integer> comb : probability.keySet()) {
            Double count[] = probability.get(comb);
            Double sum = 0.0;
            for (int j = 0; j < count.length; j++)
                sum += count[j];
            for (int j = 0; j < count.length; j++) {
                if (count[j] == 0.0)
                    continue;
                count[j] = count[j] / sum;
            }
            probability.put(comb, count);
        }

        return probability;
    }

    public static Double[] Red2RedPredictor(List<Number> numbers, int n) {
        HashMap<MultiTuple<Integer>, Double[]> probability = Red2RedCooccurrenceProbability(numbers, n);
        Set<MultiTuple<Integer>> combination = new HashSet<MultiTuple<Integer>>();
        for (int i = numbers.size() - n; i < numbers.size(); i++) {
            if (combination.size() == 0) {
                for (int red : numbers.get(i).getReds())
                    combination.add(new MultiTuple<Integer>(red));
            } else {
                Set<MultiTuple<Integer>> tmp = combination;
                combination = new HashSet<MultiTuple<Integer>>();
                for (MultiTuple<Integer> t : tmp) {
                    for (int red : numbers.get(i).getReds()) {
                        MultiTuple<Integer> c = t.copy();
                        c.insert(red);
                        combination.add(c);
                    }
                }
                gc();
            }
        }

        Double[] predicts = new Double[33];
        for (int i = 0; i < predicts.length; i++)
            predicts[i] = 0.0;
        for (MultiTuple<Integer> comb : combination) {
            if (!probability.containsKey(comb))
                continue;
            Double[] probs = probability.get(comb);
            for (int j = 0; j < probs.length; j++) {
//                predicts[j] += Math.log(probs[j]);
                predicts[j] += probs[j];
            }
        }

        return predicts;
    }

    public static Double[] Red2BluePredictor(List<Number> numbers, int n) {
        HashMap<MultiTuple<Integer>, Double[]> probability = Red2BlueCooccurrenceProbability(numbers, n);
        Set<MultiTuple<Integer>> combination = new HashSet<MultiTuple<Integer>>();
        for (int i = numbers.size() - n; i < numbers.size(); i++) {
            if (combination.size() == 0) {
                for (int red : numbers.get(i).getReds())
                    combination.add(new MultiTuple<Integer>(red));
            } else {
                Set<MultiTuple<Integer>> tmp = combination;
                combination = new HashSet<MultiTuple<Integer>>();
                for (MultiTuple<Integer> t : tmp) {
                    for (int red : numbers.get(i).getReds()) {
                        MultiTuple<Integer> c = t.copy();
                        c.insert(red);
                        combination.add(c);
                    }
                }
                gc();
            }
        }

        Double[] predicts = new Double[16];
        for (int i = 0; i < predicts.length; i++)
            predicts[i] = 0.0;
        for (MultiTuple<Integer> comb : combination) {
            if (!probability.containsKey(comb))
                continue;
            Double[] probs = probability.get(comb);
            for (int j = 0; j < probs.length; j++) {
//                predicts[j] += Math.log(probs[j]);
                predicts[j] += probs[j];
            }
        }

        return predicts;
    }


    public static Double[][] Red2BlueCooccurrenceProbability(List<Number> numbers) {
        Double[][] probability = new Double[33][16];
        for (int i = 0; i < 33; i++) {
            for (int j = 0; j < 16; j++)
                probability[i][j] = 0.0;
        }
        for (Number number : numbers) {
            List<Integer> redNums = number.nums.subList(0, 6);
            int blueNum = number.nums.get(6);
            for (int redNum : redNums)
                probability[redNum - 1][blueNum - 1]++;
        }

        for (int i = 0; i < 33; i++) {
            Double total = 0.0;
            for (int j = 0; j < 16; j++)
                total += probability[i][j];
            for (int j = 0; j < 16; j++)
                probability[i][j] /= total;
        }
        return probability;
    }

    public static Double[][] Red2BlueTranslateProbability(List<Number> numbers) {
        Double[][] probability = new Double[33][16];
        for (int i = 0; i < 33; i++) {
            for (int j = 0; j < 16; j++)
                probability[i][j] = 0.0;
        }
        for (int i = 1; i < numbers.size(); i++) {
            Number number = numbers.get(i);
            Number number_pre = numbers.get(i - 1);
            List<Integer> redNums_pre = number_pre.nums.subList(0, 6);
            int blueNum = number.nums.get(6);
            for (int redNum_pre : redNums_pre)
                probability[redNum_pre - 1][blueNum - 1]++;
        }

        for (int i = 0; i < 33; i++) {
            Double total = 0.0;
            for (int j = 0; j < 16; j++)
                total += probability[i][j];
            for (int j = 0; j < 16; j++)
                probability[i][j] /= total;
        }
        return probability;
    }

    public static HashMap<Pair<Integer, Integer>, Double[]> RedPair2BlueTranslateProbability(List<Number> numbers) {
        HashMap<Pair<Integer, Integer>, Double[]> probability_tran = new HashMap<Pair<Integer, Integer>, Double[]>();
        for (int i = 1; i < numbers.size(); i++) {
            List<Pair<Integer, Integer>> redNums_pair_pre = numbers.get(i - 1).toTwoCooccurrence();
            int blueNum = numbers.get(i).nums.get(6);
            for (Pair<Integer, Integer> redNum_pair_pre : redNums_pair_pre) {
                Double[] count = new Double[16];
                for (int j = 0; j < count.length; j++)
                    count[j] = 0.0;
                if (probability_tran.containsKey(redNum_pair_pre))
                    count = probability_tran.get(redNum_pair_pre);
                count[blueNum - 1] += 1.0;
                probability_tran.put(redNum_pair_pre, count);
            }
        }

        for (Pair<Integer, Integer> redNum_pair_pre : probability_tran.keySet()) {
            Double[] prob = probability_tran.get(redNum_pair_pre);
            Double total = 0.0;
            for (int i = 0; i < prob.length; i++)
                total += prob[i];
            for (int i = 0; i < prob.length; i++)
                prob[i] /= total;
        }

        return probability_tran;
    }

    public static HashMap<Triple<Integer, Integer, Integer>, Double[]> RedTriple2BlueTranslateProbability(List<Number> numbers) {
        HashMap<Triple<Integer, Integer, Integer>, Double[]> probability_tran = new HashMap<Triple<Integer, Integer, Integer>, Double[]>();
        for (int i = 1; i < numbers.size(); i++) {
            List<Triple<Integer, Integer, Integer>> redNums_triple_pre = numbers.get(i - 1).toThreeCooccurrence();
            int blueNum = numbers.get(i).nums.get(6);
            for (Triple<Integer, Integer, Integer> redNum_triple_pre : redNums_triple_pre) {
                Double[] count = new Double[16];
                for (int j = 0; j < count.length; j++)
                    count[j] = 0.0;
                if (probability_tran.containsKey(redNum_triple_pre))
                    count = probability_tran.get(redNum_triple_pre);
                count[blueNum - 1] += 1.0;
                probability_tran.put(redNum_triple_pre, count);
            }
        }

        for (Triple<Integer, Integer, Integer> redNum_triple_pre : probability_tran.keySet()) {
            Double[] prob = probability_tran.get(redNum_triple_pre);
            Double total = 0.0;
            for (int i = 0; i < prob.length; i++)
                total += prob[i];
            for (int i = 0; i < prob.length; i++)
                prob[i] /= total;
        }

        return probability_tran;
    }

    public static HashMap<MultiTuple<Integer>, Double[]> RedMulti2BlueTranslateProbability(List<Number> numbers, int N) {
        HashMap<MultiTuple<Integer>, Double[]> probability_tran = new HashMap<MultiTuple<Integer>, Double[]>();
        for (int i = 1; i < numbers.size(); i++) {
            List<MultiTuple<Integer>> redNumsCs_pre = numbers.get(i - 1).toMultiCooccurrence(N);
            int blueNum = numbers.get(i).nums.get(6);
            for (MultiTuple<Integer> redNumC_pre : redNumsCs_pre) {
                Double[] count = new Double[16];
                for (int j = 0; j < count.length; j++)
                    count[j] = 0.0;
                if (probability_tran.containsKey(redNumC_pre))
                    count = probability_tran.get(redNumC_pre);
                count[blueNum - 1] += 1.0;
                probability_tran.put(redNumC_pre, count);
            }
        }

        for (MultiTuple<Integer> redNumC_pre : probability_tran.keySet()) {
            Double[] prob = probability_tran.get(redNumC_pre);
            Double total = 0.0;
            for (int i = 0; i < prob.length; i++)
                total += prob[i];
            for (int i = 0; i < prob.length; i++)
                prob[i] /= total;
        }

        return probability_tran;
    }

    public static void main(String args[]) throws IOException {
        List<Number> numbers = NumberGetter.loadNumbers(new File("src/me/chyc/lottery/source/ssq.num"));
//        Double[][] probability_cooc= Red2BlueCooccurrenceProbability(numbers);
//        for (int i = 0; i < 33; i++) {
//            for (int j = 0; j < 16; j++)
//                System.out.println((i+1) + " -> " + (j+1) + "\t" + probability_cooc[i][j]);
//        }

//        Double[][] probability_tran= Red2BlueTranslateProbability(numbers);
//        for (int i = 0; i < 33; i++) {
//            for (int j = 0; j < 16; j++)
//                System.out.println((i+1) + " -> " + (j+1) + "\t" + probability_tran[i][j]);
//        }
//        HashMap<Pair<Integer, Integer>, Double[]> pair_probability_tran= RedPair2BlueTranslateProbability(numbers);
//        for (Pair<Integer, Integer> redNum_pair_pre: pair_probability_tran.keySet()){
//            Double[] prob = pair_probability_tran.get(redNum_pair_pre);
//            for (int i = 0; i < prob.length; i ++)
//                System.out.println(redNum_pair_pre.toString() + " -> " + (i+1) + "\t" + prob[i]);
//        }
//
//        HashMap<Triple<Integer, Integer, Integer>, Double[]> triple_probability_tran= RedTriple2BlueTranslateProbability(numbers);
//        for (Triple<Integer, Integer, Integer> redNum_triple_pre: triple_probability_tran.keySet()){
//            Double[] prob = triple_probability_tran.get(redNum_triple_pre);
//            for (int i = 0; i < prob.length; i ++)
//                System.out.println(redNum_triple_pre.toString() + " -> " + (i+1) + "\t" + prob[i]);
//        }


        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/me/chyc/lottery/source/ssq.tran")));
        HashMap<MultiTuple<Integer>, Double[]> probability_tran_2 = RedMulti2BlueTranslateProbability(numbers, 2);
        for (MultiTuple<Integer> redNumC_pre : probability_tran_2.keySet()) {
            Double[] prob = probability_tran_2.get(redNumC_pre);
            for (int i = 0; i < prob.length; i++)
                if (prob[i] != 0.0) {
//                    System.out.println(redNumC_pre.toString() + " -> " + (i + 1) + "\t" + prob[i]);
                    bw.write(redNumC_pre.toString2() + "\t" + (i + 1) + "\t" + prob[i]);
                    bw.newLine();
                    bw.flush();
                }
        }

        HashMap<MultiTuple<Integer>, Double[]> probability_tran_3 = RedMulti2BlueTranslateProbability(numbers, 3);
        for (MultiTuple<Integer> redNumC_pre : probability_tran_3.keySet()) {
            Double[] prob = probability_tran_3.get(redNumC_pre);
            for (int i = 0; i < prob.length; i++)
                if (prob[i] != 0.0) {
//                    System.out.println(redNumC_pre.toString() + " -> " + (i + 1) + "\t" + prob[i]);
                    bw.write(redNumC_pre.toString2() + "\t" + (i + 1) + "\t" + prob[i]);
                    bw.newLine();
                    bw.flush();
                }
        }
        HashMap<MultiTuple<Integer>, Double[]> probability_tran_4 = RedMulti2BlueTranslateProbability(numbers, 4);
        for (MultiTuple<Integer> redNumC_pre : probability_tran_4.keySet()) {
            Double[] prob = probability_tran_4.get(redNumC_pre);
            for (int i = 0; i < prob.length; i++)
                if (prob[i] != 0.0) {
//                    System.out.println(redNumC_pre.toString() + " -> " + (i + 1) + "\t" + prob[i]);
                    bw.write(redNumC_pre.toString2() + "\t" + (i + 1) + "\t" + prob[i]);
                    bw.newLine();
                    bw.flush();
                }
        }
        HashMap<MultiTuple<Integer>, Double[]> probability_tran_5 = RedMulti2BlueTranslateProbability(numbers, 5);
        for (MultiTuple<Integer> redNumC_pre : probability_tran_5.keySet()) {
            Double[] prob = probability_tran_5.get(redNumC_pre);
            for (int i = 0; i < prob.length; i++)
                if (prob[i] != 0.0) {
//                    System.out.println(redNumC_pre.toString() + " -> " + (i + 1) + "\t" + prob[i]);
                    bw.write(redNumC_pre.toString2() + "\t" + (i + 1) + "\t" + prob[i]);
                    bw.newLine();
                    bw.flush();
                }
        }
        bw.close();
    }

}
