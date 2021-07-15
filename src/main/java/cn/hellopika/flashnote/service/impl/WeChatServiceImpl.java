package cn.hellopika.flashnote.service.impl;

import cn.hellopika.flashnote.config.WeChatConfig;
import cn.hellopika.flashnote.exception.ServiceException;
import cn.hellopika.flashnote.mapper.MemoMapper;
import cn.hellopika.flashnote.mapper.UserMapper;
import cn.hellopika.flashnote.model.entity.Memo;
import cn.hellopika.flashnote.model.entity.User;
import cn.hellopika.flashnote.service.SendSmsService;
import cn.hellopika.flashnote.service.WeChatService;
import cn.hellopika.flashnote.util.DateTimeUtils;
import cn.hellopika.flashnote.util.HttpUtils;
import cn.hellopika.flashnote.util.SysConst;
import cn.hellopika.flashnote.util.XmlUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2021/7/3
 * @author: pikachu
 * @description: 对接微信使用的 业务层实现类
 **/
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MemoMapper memoMapper;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 校验微信提交的signature
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return 校验成功返回true，失败返回false
     */
    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        String token = weChatConfig.getToken();

        List<String> paramList = Arrays.asList(token, timestamp, nonce);
        Collections.sort(paramList);

        StringBuilder sb = new StringBuilder();
        for (String s : paramList) {
            sb.append(s);
        }

        String key = DigestUtils.sha1Hex(sb.toString());
        if (StringUtils.equals(key, signature)) {
            log.info("微信signature校验通过");
            return true;
        }

        return false;
    }

    /**
     * 获取微信的 Access Token
     *
     * @return Access Token
     */
    public String getAccessToken() {
        // 先查看缓存中有没有 AccessToken，如果有就直接返回
        RBucket<String> weChatAccessTokenCache = redissonClient.getBucket(SysConst.RedisPrefix.WECHAT_ACCESSTOKEN);
        if (weChatAccessTokenCache.isExists()) {
            return weChatAccessTokenCache.get();
        }

        // 如果缓存中没有，就去获取
        String appId = weChatConfig.getAppId();
        String appSecret = weChatConfig.getAppSecret();
        String url = " https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        String result = HttpUtils.sendGet(url);

        // 把返回的json解析成map，然后根据返回内容判断如何操作
        Map resultMap = JSON.parseObject(result, Map.class);
        // 如果返回内容包含 errcode，说明请求出错，直接抛异常
        if (resultMap.containsKey("errcode")) {
            throw new ServiceException("请求微信AccessToken出错：" + resultMap.get("errmsg"));
        }
        // 如果返回内容包含 access_token，说明请求成功，把 access_token 放到缓存中
        if (resultMap.containsKey("access_token")) {
            String accessToken = resultMap.get("access_token").toString();
            weChatAccessTokenCache.set(accessToken, Long.parseLong(resultMap.get("expires_in").toString()), TimeUnit.SECONDS);
            log.info("微信AccessToken：[{}] 获取成功", accessToken);
            return accessToken;
        } else {
            throw new ServiceException("获取AccessToken错误");
        }
    }


    /**
     * 获取带场景值的二维码
     * 1. 拿着场景值（userId）获取 ticket
     * 2. 使用 ticket 换取二维码图片
     *
     * @param userId
     * @return
     */
    @Override
    public String sceneQrCode(String userId) {
        // 获取 ticket 的 url
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + getAccessToken();

        /**
         *  post请求携带的参数：
         *  {
         *    "expire_seconds":604800,
         *    "action_name":"QR_STR_SCENE",
         *    "action_info":{
         *       "scene":{
         *          "scene_str":"test"
         *        }
         *     }
         *  }
         */
        // 通过 map 的形式获取相关参数，然后直接把 map 转成 json 字符串
        Map paramMap = new HashMap<>();
        paramMap.put("expire_seconds", "86400");
        paramMap.put("action_name", "QR_STR_SCENE");

        Map actionInfoMap = new HashMap<>();
        Map sceneMap = new HashMap<>();

        sceneMap.put("scene_str", userId);
        actionInfoMap.put("scene", sceneMap);
        paramMap.put("action_info", actionInfoMap);

        String paramJson = JSON.toJSONString(paramMap);
        String result = HttpUtils.sendPost(url, paramJson);
        Map resultMap = JSON.parseObject(result, Map.class);

        // 根据返回结果做不同的操作
        if (resultMap.containsKey("errcode")) {
            throw new ServiceException("请求微信AccessToken出错：" + resultMap.get("errmsg"));
        }

        if (resultMap.containsKey("ticket")) {
            String QrCodeUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + resultMap.get("ticket").toString();
            log.info("获取二维码成功，链接：[{}]", QrCodeUrl);
            // 把获取二维码图片的链接返回给前端
            return QrCodeUrl;
        } else {
            throw new ServiceException("获取二维码错误");
        }
    }


    /**
     * 处理微信的 post请求
     * 1. 用户扫码关注公众号时，绑定微信用户和系统用户
     * 2. 用户通过公众号发送文本消息时，把文本消息保存为 memo
     *
     * @param requestXml
     * @return
     */
    @Override
    public String postCallback(String requestXml) {
        // 把 xml字符串解析成 map
        Map<String, String> map = XmlUtils.parseXmlToMap(requestXml);

        // 如果是事件消息（请求绑定的消息）
        if (StringUtils.equals(map.get("MsgType"), "event") && StringUtils.equalsAny(map.get("Event"), "subscribe", "SCAN")) {
            bindWechatIdToUser(map.get("FromUserName"), map.get("EventKey"));
            return respXmlToWeChat(map.get("FromUserName"), "绑定成功");
        }

        // 如果是普通文本消息（发送笔记内容的消息）
        if (StringUtils.equals(map.get("MsgType"), "text")) {
            saveWeChatMessage(map.get("FromUserName"), map.get("Content"));
            return respXmlToWeChat(map.get("FromUserName"), "保存Memo成功");
        }

        return null;
    }


    /**
     * 绑定 微信用户 和 系统用户
     *
     * @param openId
     * @param eventKey
     */
    private void bindWechatIdToUser(String openId, String eventKey) {
        String userId = eventKey;
        if (StringUtils.startsWith(eventKey, "qrscene_")) {
            userId = StringUtils.substringAfter(eventKey, "qrscene_");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        user.setWechatId(openId);

        userMapper.updateById(user);

        log.info("用户 [{}] 绑定微信 [{}] 成功", user.getPhone(), openId);
    }

    /**
     * 被动回复给用户的文本消息
     *
     * @param toUserName
     * @param message
     * @return
     */
    private String respXmlToWeChat(String toUserName, String message) {

        return "<xml>" +
                "<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>\n" +
                "<FromUserName><![CDATA[" + weChatConfig.getId() + "]]></FromUserName>\n" +
                "<CreateTime>" + new Date().getTime() + "</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[" + message + "]]></Content>\n" +
                "</xml>";
    }

    /**
     * 接收微信用户传来的文本消息，并保存为 memo
     *
     * @param weChatId
     * @param message
     */
    private void saveWeChatMessage(String weChatId, String message) {
        // 根据 weChatId 找到与之绑定的 系统用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("wechat_id", weChatId));
        if (user == null) {
            log.info("该微信未与任何用户绑定");
            throw new ServiceException("该微信未与任何用户绑定");
        }

        // 根据用户和内容创建 memo
        Memo memo = new Memo();
        memo.setUserId(user.getId());
        memo.setContent(message);
        memo.setDevice("wechat");
        memo.setCreateTime(DateTimeUtils.getNowString());

        memoMapper.insert(memo);

        log.info("接收到微信消息，成功创建memo：[{}]", memo.getId());

    }
}
