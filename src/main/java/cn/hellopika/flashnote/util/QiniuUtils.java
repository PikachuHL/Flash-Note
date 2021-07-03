package cn.hellopika.flashnote.util;

import cn.hellopika.flashnote.config.QiniuConfig;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @date: 2021/6/30
 * @author: pikachu
 * @description: 七牛云 工具类
 **/
@Component
public class QiniuUtils {

    @Autowired
    private QiniuConfig qiniuConfig;

    /**
     * 获取上传授权
     * @return
     */
    public String getImgUpToken(){
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());

        // 自定义上传成功后返回哪些数据
        StringMap stringMap = new StringMap();
        stringMap.put("returnBody", "{\n" +
                                    "    \"key\":\"$(key)\",\n" +
                                    "    \"hash\":\"$(etag)\",\n" +
                                    "    \"bucket\":\"$(bucket)\",\n" +
                                    "    \"url\":\""+qiniuConfig.getDomain() +"/$(key)\",\n" +
                                    "    \"fname\":\"$(fname)\",\n" +
                                    "    \"uid\":\"$(x:uid)\"\n" +
                                    "}");
        // 授权的有效期
        long expireSeconds = 3600;

        String upToken = auth.uploadToken(qiniuConfig.getBucket(), null, expireSeconds, stringMap);

        return upToken;
    }

    /**
     * 删除七牛云上的图片
     * @param key
     */
    public void delImg(String key){
        Configuration cfg = new Configuration(Region.region2());

        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);

        try {
            bucketManager.delete(qiniuConfig.getBucket(), key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
