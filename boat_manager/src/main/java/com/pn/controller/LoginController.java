package com.pn.controller;

import com.google.code.kaptcha.Producer;
import com.pn.entity.*;
import com.pn.service.AuthService;
import com.pn.service.UserService;
import com.pn.utils.DigestUtil;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Autowired
    private Producer producer;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/captcha/captchaImage")
    public void captchaImage(HttpServletResponse response){
        ServletOutputStream out = null;
        try {
            String text = producer.createText();
            BufferedImage image = producer.createImage(text);
            redisTemplate.opsForValue().set(text,"",60*30, TimeUnit.SECONDS);//验证码过期时间30分钟
            //设置响应正文类型
            response.setContentType("image/jpeg");
            //拿到响应对象的字节输出流
            out = response.getOutputStream();
            //将验证码图片响应给前端
            ImageIO.write(image,"jpg",out);//使用响应对象的字节输出流写入验证码图片给前端
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtils tokenUtils;
    @RequestMapping("/login")
    public Result login(@RequestBody LoginUser loginUser) {
        //判断验证码是否正确  --  验证码已经存在redis里了
        String code = loginUser.getVerificationCode();
        if (!redisTemplate.hasKey(code)) {
            return Result.err(Result.CODE_ERR_BUSINESS,"验证码错误");
        }

        User user = userService.queryUserByCode(loginUser.getUserCode());
        if(user!=null){
            if (user.getUserState().equals(WarehouseConstants.USER_STATE_PASS)){

                String userPwd = DigestUtil.hmacSign(loginUser.getUserPwd());
                if (userPwd.equals(user.getUserPwd())){
                    //账号存在、已审核并且密码正确，登陆成功。需要给前端发送令牌
                    //loginUser没有id，所以不用他
                    CurrentUser currentUser = new CurrentUser(user.getUserId(),user.getUserCode(),user.getUserName());
                    String token = tokenUtils.loginSign(currentUser, user.getUserPwd());//注意token放进了redis
                    return Result.ok("登陆成功",token);//颁发token给前端，此后前端发送请求资源，都会携带token
                }else {
                    return Result.err(Result.CODE_ERR_BUSINESS,"密码错误");
                }
            }else {
                return Result.err(Result.CODE_ERR_BUSINESS,"账号未审核");
            }
        }else{
            return Result.err(Result.CODE_ERR_BUSINESS,"账号不存在");
        }

    }


    //获取登陆的用户信息
    @RequestMapping("/curr-user")
    public Result CurrentUser(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //使用工具类调用解析token的方法，别忘了token中载体封装了登录用户的信息
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //通过响应对象将数据给响应前端
        return Result.ok(currentUser);
    }

    @Autowired
    private AuthService authService;

    //获取登录的用户的菜单树，响应给前端
    @RequestMapping("/user/auth-list")
    public Result loadAuthTree(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int userId = currentUser.getUserId();
        List<Auth> authTreeList = authService.authTreeByUid(userId);
        return Result.ok(authTreeList);
    }

    //退出登录
    @RequestMapping("/logout")
    public Result logout(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        redisTemplate.delete(token);
        return Result.ok("退出系统");
    }




}
