package com.mayikt.controller;

import com.mayikt.utils.QRCodeUtil;
import com.mayikt.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/5/31 0031 22:13
 * @version: V1.0
 * @Copyright:该项目“基于SpringCloud2.x构建微服务电商项目”由每特教育|蚂蚁课堂版权所有，未经过允许的情况下， 私自分享视频和源码属于违法行为。
 */
@Controller
public class QrCodeController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 提供创建二维码接口：http://127.0.0.1:8080/generateCode
     * @return
     */
    @RequestMapping("/generateCode")
    @ResponseBody
    public String generateCode() throws Exception {
        // 1.生成token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 2.存放在redis中 0未使用过 1使用过
        redisUtil.setString(token, "0");

        // 3.生成二维码
        // 存放在二维码中的内容
        String text = "http://u9jqwj.natappfree.cc/updateTokenState?token=";
        // 嵌入二维码的图片路径
        String imgPath = "G:/img/test.jpg";
        // 生成的二维码的路径及名称
        String destPath = "G:/img/" + token + ".jpg";
        //生成二维码
        QRCodeUtil.encode(text + token, imgPath, destPath);
        System.out.println(">>>生成二维码成功");
        return token;
    }

    /**
     * 页面获取二维码：http://127.0.0.1:8080/getCodeImg?token=
     * @param token
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getCodeImg", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getCodeImg(String token) throws IOException {
        File file = new File("G:/img/" + token + ".jpg");
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0, fis.available());
        System.out.println(">>>获取首页二维码" + token);
        return bytes;
    }

    /**
     * 根据token展示页面的二维码：http://127.0.0.1:8080/?token=
     *
     * @param token
     * @param request
     * @return
     */
    @RequestMapping("/")
    public String index(String token, HttpServletRequest request) {
        request.setAttribute("token", token);
        System.out.println(">>>用户访问首页二维码" + token);
        return "index";
    }

    /**
     * 用户扫码 将该token 的状态改为1：http://127.0.0.1:8080/updateTokenState?token=
     *
     * @param token
     * @return
     */
    @RequestMapping("/updateTokenState")
    @ResponseBody
    public String updateTokenState(String token) {
        if (StringUtils.isEmpty(token)) {
            return "token 不能为空!";
        }
        // 将该token的状态改为1
        redisUtil.setString(token, "1");
        System.out.println(">>>更新用户扫码token为1成功" + token);
        return "<h1>用户扫码成功!</h1>";
    }

    /**
     * 前端使用定时器检查token状态
     *
     * @param token
     * @return
     */
    @RequestMapping("/checkToken")
    @ResponseBody
    public Boolean checkToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        String redisValue = redisUtil.getString(token);
        if (StringUtils.isEmpty(redisValue)) {
            return false;
        }
        if (!redisValue.equals("1")) {
            System.out.println(">>>用户未扫码成功" + token);
            return false;
        }
        System.out.println(">>>用户扫码成功" + token);
        return true;
    }

    /**
     * @return
     */
    @RequestMapping("/sweepCode")
    public String sweepCode() {
        System.out.println(">>>跳转用户扫码成功页面");
        return "sweepCode";
    }

}
