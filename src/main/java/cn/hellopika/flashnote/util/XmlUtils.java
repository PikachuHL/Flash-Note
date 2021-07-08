package cn.hellopika.flashnote.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @date: 2021/7/5
 * @author: pikachu
 * @description: 操作XML相关的工具类
 **/

public class XmlUtils {

    /**
     * 把 xml字符串解析成为一个 map
     * @param xmlString
     * @return
     */
    public static Map<String, String> parseXmlToMap(String xmlString){
        // 创建一个map，用于存放xml中解析出来的内容
        Map<String, String> map = new HashMap<>();
        try {
            Document document = DocumentHelper.parseText(xmlString);
            Element root = document.getRootElement();
            Iterator<Element> it = root.elementIterator();
            while (it.hasNext()){
                Element element = it.next();
                String key = element.getName();
                String value = element.getText();
                map.put(key, value);
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }


        return map;
    }
}
