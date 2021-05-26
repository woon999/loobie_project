package toyproject.loobie;

import java.text.NumberFormat;

public class RegexMain {
    public static void main(String[] args) {
//        String line = "현재 16℃  (최저 14˚/ 최고 25˚)";
//        String line = "오전 강수확률 0% / 오후 강수확률 0%";
//        String line = "[뉴스 정치]";
//        String line = ",\"달러환율 1,123.00전일대비 3.50(-0.31%) 하락\"";
//        String line = "\"나스닥지수 13,642.01전일대비 19.16(-0.14%) 하락\"";

//        int len = line.length();
//        System.out.println(len);
//        System.out.println(line.charAt(len-1));
//        if(line.charAt(0) =='"') {
//            line = line.substring(1, len--);
//        }
//        System.out.println(len);
//        System.out.println(line);
//        if(line.charAt(len-1) =='"') {
//            line = line.substring(0, len-1);
//        }
//        System.out.println(len);
//        System.out.println(line);

        String line  = "1,117.00@6.00(-0.53%) 하락";
        String[] data = line.split("@");
        System.out.println(data[0]);
        System.out.println(data[1]);
        Double indx = Double.parseDouble(data[0].replaceAll(",",""));
        System.out.println(indx);

//        String dis = "^(";
//        data[1] = data[1].replaceAll(dis,"");

        if(data[1].contains("하락")){
            String[] changeData = data[1].split("-");
            System.out.println(changeData[0].substring(0,changeData[0].length()-1));
            String ci = changeData[0].substring(0,changeData[0].length()-1);
            System.out.println("ci = " + ci);

            changeData = changeData[1].split("%");
            String cr = changeData[0].substring(0,changeData[0].length());
            System.out.println("cr = " + cr);
            
        }else{
            String[] changeData = data[1].split("\\+");
            System.out.println(changeData[0].substring(0,changeData[0].length()-1));
            Double changeIndex = Double.parseDouble(changeData[0].substring(0,changeData[0].length()-1));
            System.out.println("changeIndex = " + changeIndex);
        }
//        String[] changeData = data[1].split("(");



    }
}
