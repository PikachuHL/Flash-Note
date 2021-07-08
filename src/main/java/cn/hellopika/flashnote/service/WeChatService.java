package cn.hellopika.flashnote.service;

/**
 * @date: 2021/7/3
 * @author: pikachu
 * @description: 对接微信使用的 业务层接口
 **/
public interface WeChatService {

    /**
     * 校验微信提交的signature
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    boolean checkSignature(String signature, String timestamp, String nonce);

    /**
     * 获取带场景值的二维码
     * @param userId
     * @return
     */
    String sceneQrCode(String userId);

    /**
     *
     * @param requestXml
     * @return
     */
    String postCallback(String requestXml);
}
