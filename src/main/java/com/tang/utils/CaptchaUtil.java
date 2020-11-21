package com.tang.utils;//package com.cn.tianxia.admin.base.utils;
//
//import com.cn.tianxia.admin.base.consts.BaseConsts;
//import com.cn.tianxia.admin.base.dto.RR;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.imageio.ImageIO;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.util.Random;
//
//
//@RestController
//@RequestMapping(value = "/permission/captcha")
//@Api(value = "验证码-验证码", tags = "验证码-验证码")
//public class CaptchaUtil {
//
//    /**
//     * 随机字符字典
//     */
//    private static final char[] CHARS = { '2', '3', '4', '5', '6', '7', '8',
//            '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
//            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
//            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm',
//            'n', 'p', 'q', 'r', 's', 't', 'u' ,'v', 'w', 'x', 'y', 'z'};
//
//    /**
//     * 随机数
//     */
//    private static Random random = new Random();
//
//    /**
//     * 获取4位随机数
//     * @return
//     */
//    private static String getRandomString() {
//        StringBuffer buffer = new StringBuffer();
//        for(int i = 0; i < 4; i++) {
//            buffer.append(CHARS[random.nextInt(CHARS.length)]);
//        }
//        return buffer.toString();
//    }
//
//    /**
//     * 获取随机数颜色
//     * @return
//     */
//    private static Color getRandomColor() {
//        return new Color(random.nextInt(255),random.nextInt(255), random.nextInt(255));
//    }
//
//    /**
//     * 返回某颜色的反色
//     * @param c
//     * @return
//     */
//    private static Color getReverseColor(Color c) {
//        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
//    }
//
//    /**
//     * 生成验证码
//     * @param request
//     * @param response
//     */
//    @PostMapping(value = "/captcha", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
//    @ApiOperation(value = "验证码", notes = "验证码", httpMethod = BaseConsts.REQUEST_METHOD)
//    public void outputCaptcha(HttpServletRequest request, HttpServletResponse response, String rad)throws ServletException, IOException {
//        // 设置页面不缓存
//        response.setHeader("Pragma", "No-cache");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setDateHeader("Expires", 0);
//        response.setContentType("image/jpeg");
//
//        String randomString = getRandomString(); //生成的验证码
//
//        int width = 100;    //验证码图像的宽度
//        int height = 34;    //验证码图像的高度
//
//        // 在内存中创建图象
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g = bi.createGraphics();
//        for(int i=0; i<randomString.length(); i++){
//            Color color = getRandomColor();
//            Color reverse = getReverseColor(color);
//            g.setColor(color);    //设置字体颜色
//            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));    //设置字体样式
//            g.fillRect(0, 0, width, height);
//            g.setColor(reverse);
//            g.drawString(randomString, 18, 25);
//        }
//        //随机生成一些点
//        for (int i = 0, n = random.nextInt(100); i < n; i++) {
//            g.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
//        }
//        // 随机产生干扰线，使图象中的认证码不易被其它程序探测到
//        for (int i = 0; i < 10; i++) {
//            g.setColor(getRandomColor());
//            final int x = random.nextInt(width-1); // 保证画在边框之内
//            final int y = random.nextInt(height-1);
//            final int xl = random.nextInt(width);
//            final int yl = random.nextInt(height);
//            g.drawLine(x, y, x + xl, y + yl);
//        }
//        g.dispose();    //图像生效
//        ImageIO.write(bi, "JPEG", response.getOutputStream());    //输出图片到页面
//    }
//}
