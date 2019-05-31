package com.mayikt.test;


import com.mayikt.utils.QRCodeUtil;

public class QRCodeTest {

    public static void main(String[] args) throws Exception {
        // 存放在二维码中的内容
        String text = "http://www.mayikt.com";
        // 嵌入二维码的图片路径
        String imgPath = "G:/img/test.jpg";
        // 生成的二维码的路径及名称
        String destPath = "G:/img/new_code.jpg";
        //生成二维码
        QRCodeUtil.encode(text, imgPath, destPath);
        // 解析二维码
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);
    }
}
