package me.chyc.lottery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chyc on 8/2/14.
 */
public class NumberGetter {
    public static String getWebSource(String url) {
        StringBuffer sb = new StringBuffer();
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url)
                    .openStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            in.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return sb.toString();
    }

    public static void getSSQ(int startPhase, int endPhase, File file) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        for (int i = startPhase; i < endPhase; i += 1000) {

            String url = "http://trend.baidu.lecai.com/ssq/redBaseTrend.action?" +
                    "startPhase=" + i + "&" +
                    "endPhase=" + (i + 1000);
            String html = getWebSource(url);
            Element element = Jsoup.parse(html);
            Element tbody = element.select("tbody").first();
            for (Element tr : tbody.children()) {
                Element chart_table_td = tr.select("td.chart_table_td").first();
                try {
                    int no = Integer.valueOf(chart_table_td.text());
                    StringBuffer sb = new StringBuffer();
                    sb.append(no + "\t");
//                    System.out.print(no + "\t");
                    for (Element td : tr.select("td")) {
                        if (td.className().contains("omission_hit"))
                            sb.append(td.text() + "\t");
//                            System.out.print(td.text() + "\t");
                    }
//                    System.out.println();
                    bw.write(sb.toString());
                    bw.newLine();
                    bw.flush();
                } catch (NumberFormatException ex) {
//                    break;
                }
            }
        }
        bw.close();
    }


    public static void getDLT(int startPhase, int endPhase, File file) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        for (int i = startPhase; i < endPhase; i += 1000) {

            String url = "http://trend.baidu.lecai.com/dlt/redBaseTrend.action?" +
                    "startPhase=" + (new Integer(100000+i)).toString().substring(1) + "&" +
                    "endPhase=" + (new Integer(100000+i+1000)).toString().substring(1);
            String html = getWebSource(url);
            Element element = Jsoup.parse(html);
            Element tbody = element.select("tbody").first();
            for (Element tr : tbody.children()) {
                Element chart_table_td = tr.select("td.chart_table_td").first();
                try {
                    int no = Integer.valueOf(chart_table_td.text());
                    StringBuffer sb = new StringBuffer();
                    sb.append((new Integer(100000+no)).toString().substring(1) + "\t");
//                    System.out.print(no + "\t");
                    for (Element td : tr.select("td")) {
                        if (td.className().contains("omission_hit"))
                            sb.append(td.text() + "\t");
//                            System.out.print(td.text() + "\t");
                    }
//                    System.out.println();
                    bw.write(sb.toString());
                    bw.newLine();
                    bw.flush();
                } catch (NumberFormatException ex) {
//                    break;
                }
            }
        }
        bw.close();
    }

    public static List<Number> loadNumbers(File file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        List<Number> nums = new ArrayList<Number>();
        while ((line = br.readLine()) != null)
            nums.add(new Number(line));
        return nums;
    }

    public static void main(String args[]) throws IOException {
        getSSQ(2003001, 2014200, new File("/Users/chyc/Workspaces/Data/lottery/ssq.num"));
        getDLT(07001, 14200, new File("/Users/chyc/Workspaces/Data/lottery/dlt.num"));
    }
}
