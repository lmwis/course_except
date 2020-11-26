package com.fehead;

import org.junit.Test;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-07 12:03
 * @Version 1.0
 */
public class JavaTest {

    @Test
    public void Test(){
        char c = "011000".charAt(0);
        System.out.println(c);
    }

    @Test
    public void TestConvertWeeksTestFromWeeks(){
        String weeks = "11011111100000000000";
        System.out.println(convertWeeksTestFromWeeks1(weeks));
    }

    /**
     * 00011111100000000000 ->1-6
     * 11011111100000000000 -> 1-2,4-9
     * 00011111100000000011
     * @param weeks
     * @return
     */
    private String convertWeeksTestFromWeeks1(String weeks) {
        String text="";
        for (int i=0;i<20;i++){
            int start,end;
            char c = weeks.charAt(i);
            if(i>0 && i<19 && c=='1' && weeks.charAt(i-1)=='0'){
                start = i+1;
                text += start+"-";
            }
            if(i>0 && i<19 && c=='1' && weeks.charAt(i+1)=='0'){
                end = i+1;
                text +=end+",";
            }
        }
        return text.substring(0,text.length()-1);
    }

    @Test
    public void testParent(){
        Son son = new Son();
        System.out.println(son.money);
//        Plane plane = getObjetc("1");
        Plane plane = new Cat();
        Plane plane1 = new Son();
        plane.fly();
    }

    private Plane getObjetc(String w) {
        if(w.equals("动物")){
            return new Cat();
        }else{
            return new Son();
        }
    }

    private String convertWeeksTestFromWeeks(String weeks) {
        int start = 1;
        String text = "";
        boolean flag1 = false;
        boolean flag2 = false;
        for (int i = 0; i < 20; i++) {
            if (weeks.charAt(i) == '0') {
                flag1 = true;
            }
            if (weeks.charAt(i) == '1') {
                flag2 = true;
            }
        }
        if (!flag1) { // 表示全部为1
            return "1-20";
        }
        if (!flag2) { // 表示全部为0
            return "0";
        }
        for (int i = 0; i < 20; i++) {
            char c = weeks.charAt(i);
            if (i >= 1 && c == '0' && (weeks.charAt(i - 1) == '1')) { // 当前为0，上一个为1才有效

                text += (start == i ? start : start + "-" + i) + ","; // 相同如：1-1 则简化为1
            }
            if (i >= 1 && c == '1' && (weeks.charAt(i - 1) == '0')) { // 当前为1，上一个为0才有效，移动头指针
                start = i + 1;
            }

            if (i == 20 - 1 && c=='1') { // 扫描到最后若以1结尾
                text += (start == 20 ? start : start + "-" + 20) + ",";;
            }
        }

        return text.substring(0, text.length() - 1);
    }

    public void CreateQRCode(){
//        BigInteger
        final int width = 300;
        final int height = 300;
        final String format = "png";
        final String content = "我爱你，中国";

               //定义二维码的参数
//        HashMap hints = new HashMap();
//               hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//               hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
//                hints.put(EncodeHintType.MARGIN, 2);
//
//               //生成二维码
//                try{
//                         //OutputStream stream = new OutputStreamWriter();
//                      BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//                       Path file = new File("F:/img.png").toPath();
//                        MatrixToImageWriter.writeToPath(bitMatrix, format, file);//MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
//                    }catch(Exception e){
//
//                    }
    }
}
