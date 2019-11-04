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
