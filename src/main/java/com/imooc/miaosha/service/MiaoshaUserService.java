package com.imooc.miaosha.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.dao.MiaoshaUserDao;
import com.imooc.miaosha.model.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;

import java.util.Date;

@Service
public class MiaoshaUserService {


    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id) {
        //取缓存
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.getById, "" + id, MiaoshaUser.class);
        if (miaoshaUser != null)
            return miaoshaUser;
        //取数据库
        miaoshaUser = miaoshaUserDao.getById(id);
        if (miaoshaUser != null)//放入缓存
            redisService.set(MiaoshaUserKey.getById,""+id,miaoshaUser);
        return miaoshaUser;
    }

    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        MiaoshaUser user = getById(id);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getById, ""+id);//删除
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);//更新
        return true;
    }

    /**
     * 从cookie中的token中获取MiaoshaUser对象，MiaoshaUser存储在缓存中
     * @param response
     * @param token
     * @return
     */
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        //向缓存添加MiaoshaUser详见本类addCookie方法
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(user != null) {
            //重新设置Cookie
            addCookie(response, token, user);
        }
        return user;
    }


    /**
     * 登录
     * @param response
     * @param loginVo
     * @return
     */
    public String login(HttpServletResponse response, LoginVo loginVo) {
        //抛出的异常在GlobalExceptionHandler捕获处理
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        //密码不匹配，抛出错误
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();//UUID：唯一通用识别码的缩写
        addCookie(response, token, user);
        return token;
    }

    /**
     * 向缓存中添加MiaoshaUser，设置Cookie
     * @param response
     * @param token
     * @param user
     */
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        //把个人信息存放到缓存中，高效
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 注册方法
     * @param response
     * @param userName
     * @param passWord
     * @param salt
     * @return
     */
    public boolean register(HttpServletResponse response , String userName ,String nickname, String passWord , String salt) {
        MiaoshaUser miaoShaUser =  new MiaoshaUser();
        miaoShaUser.setId(Long.valueOf(userName));
        miaoShaUser.setNickname(nickname);
        String DBPassWord =  MD5Util.formPassToDBPass(passWord , salt);
        miaoShaUser.setPassword(DBPassWord);
        miaoShaUser.setRegisterDate(new Date());
        miaoShaUser.setSalt(salt);
        miaoShaUser.setNickname(userName);
        try {
            miaoshaUserDao.insertMiaoshaUser(miaoShaUser);
            MiaoshaUser user = miaoshaUserDao.getById(miaoShaUser.getId());
            if(user == null){
                return false;
            }
            //生成cookie 将session返回游览器 分布式session
            String token= UUIDUtil.uuid();
            addCookie(response, token, user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
