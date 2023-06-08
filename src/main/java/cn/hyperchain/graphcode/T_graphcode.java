package cn.hyperchain.graphcode;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: sealDemo
 * @description: 图形验证码
 * @author: inkChain
 * @create: 2023-03-03 10:25
 **/
public class T_graphcode {

    public static void main(String[] args) throws IOException {

        //        String verifyCode = VerifyCodeUtils.outputVerifyImage(220, 76, response.getOutputStream(), 4);
        File file=new File("/Users/zhangrui/Downloads/graph.png");
        String verifyCode = VerifyCodeUtils.outputVerifyImage(220, 76, new FileOutputStream(file), 4);
        System.out.println(verifyCode);
    }

    //图形验证码
//
//    public BaseResult graphCodeCreate(HttpSession session, HttpServletResponse response) {
//        BaseResult baseResult = new BaseResult();
//
//          String GRAPHCODEKEY="seesionKey";
//        //设置相应类型,输出内容为图片
//        response.setContentType("image/jpeg");
//        //设置响应头信息，不缓存此内容
//        response.setHeader("Pragma", "No-cache");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setDateHeader("Expire", 0);
//        try {
//            String verifyCode = VerifyCodeUtils.outputVerifyImage(220, 76, response.getOutputStream(), 4);
//            session.removeAttribute(GRAPHCODEKEY);
//            session.setAttribute(GRAPHCODEKEY, verifyCode);
//            baseResult.returnWithoutValue(Code.SUCCESS);
//        } catch (IOException e) {
//            logger.error("获取图片验证码失败", e);
//            baseResult.returnWithoutValue(Code.GRAPH_CODE_CREATE_ERROR);
//        }
//        return baseResult;
//    }
}
