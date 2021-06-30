package cn.hellopika.flashnote.util;

import cn.hellopika.flashnote.config.QiniuConfig;
import com.qiniu.util.Auth;
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

    public String getImgUpToken(){
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());


        String upToken = auth.uploadToken(qiniuConfig.getBucket());

        return upToken;
    }
}
