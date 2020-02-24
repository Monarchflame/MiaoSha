package com.imooc.miaosha.controller;

import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.MiaoshaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class RegisterController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private MiaoshaService miaoshaService ;

    @RequestMapping("/do_register")
    public String registerIndex(){
        return "register";
    }

    /**
     * 注册
     * @param userName
     * @param passWord
     * @param salt
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public Result<String> register(@RequestParam("username") String userName ,
                                   @RequestParam("nickname") String nickname ,
                                   @RequestParam("password") String passWord,
                                   @RequestParam("verifyCode") String verifyCode,
                                   @RequestParam("salt") String salt, HttpServletResponse response ){

        //校验验证码
        boolean check = miaoshaService.checkVerifyCodeRegister(Integer.valueOf(verifyCode));
        if(!check){
            return Result.error(CodeMsg.CODE_FAIL);
        }
        boolean registerInfo  = miaoshaUserService.register(response , userName,nickname,passWord,salt);
        if(!registerInfo){
            return Result.error(CodeMsg.REGISTER_FAIL);
        }
        return Result.success(null);
    }

    /**
     * 获取登录验证码
     * @param response
     * @return
     */
    @RequestMapping("/verifyCodeRegister")
    @ResponseBody
    public Result<String> verifyCodeRegister(HttpServletResponse response) {
        try {
            BufferedImage image  = miaoshaService.createVerifyCodeRegister();
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.REGISTER_FAIL);
        }
    }
}
