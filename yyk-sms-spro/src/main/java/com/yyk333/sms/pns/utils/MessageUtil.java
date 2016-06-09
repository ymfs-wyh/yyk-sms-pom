package com.yyk333.sms.pns.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 消息工具类
 * 
 * @author 柴观新 2014-04-21
 */
public class MessageUtil {
    protected static Logger logger = LoggerFactory.getLogger(MessageUtil.class);
    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    /**
     * 解析微信发来的请求（XML）
     * 
     * @param request HttpServletRequest
     * @return Map<String, String> Map<String, String>
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }
    
    /**
     * parseRightsXml解析微信发来的请求（XML）特殊处理维权回调xml 里面有图片列表
     * 
     * @param request HttpServletRequest
     * @return Map<String, Object> Map<String, Object>
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseRightsXml(HttpServletRequest request) throws Exception {
       /*String s = "<?xml version='1.0' encoding='UTF-8'?><xml>"
                + "<OpenId><![CDATA[oeUXqtzJGd_3PihTpXxWkRaLBgAE]]></OpenId><AppId><![CDATA[wxf"
                + "8b4f85f3a794e77]]></AppId>"
                + "<TimeStamp>1393400471</TimeStamp>"
                + "<MsgType><![CDATA[request]]></MsgType>"
                + "<FeedBackId>7197417460812502768</FeedBackId>"
                + "<TransId><![CDATA[1900000109201402143240185685]]></TransId>"
                + "<Reason><![CDATA[ddd]]></Reason>"
                + "<Solution><![CDATA[ass]]></Solution>"
                + "<ExtInfo><![CDATA[sss 12435321321]]></ExtInfo>"
                + "<AppSignature>"
                + "<![CDATA[d60293982cc7c97a5a9d3383af761db763c07c86]]></AppSignature>"
                + "<SignMethod>"
                + "<![CDATA[sha1]]>"
                + "</SignMethod>"
                + "<PicInfo>"
                + "<item><PicUrl><![CDATA[mmbiz.qpic.cn]]></PicUrl>"
                + "</item>"
                + "<item>"
                + "<PicUrl>"
                + "<![CDATA[mmbiz.qpic.cn]]></PicUrl>"
                + "</item>"
                + "<item>"
                + "<PicUrl>"
                + "<![CDATA[]]></PicUrl></item><item><PicUrl><![CDATA[]]></PicUrl></item><item><PicUrl"
                + "><![CDATA[]]></PicUrl></item>" + "</PicInfo></xml>";//测试用
*/        String s = "<xml>"+
                    "<OpenId><![CDATA[oeUXqtzJGd_3PihTpXxWkRaLBgAE]]></OpenId>"+ 
                    "<AppId><![CDATA[wwwwb4f85f3a797777]]></AppId>"+
                    "<TimeStamp> 1369743511</TimeStamp>"+
                    "<MsgType><![CDATA[confirm]]></MsgType>"+
                    "<FeedBackId><![CDATA[5883726847655944563]]></FeedBackId>"+
                    "<Reason><![CDATA[1122]]></Reason>"+
                    "<AppSignature><![CDATA[bafe07f060f22dcda0bfdb4b5ff756f973aecffa]]>"+
                    "</AppSignature>"+
                    "<SignMethod><![CDATA[sha1]]></SignMethod>"+
                "</xml> ";
        InputStream inputStream = new ByteArrayInputStream(s.getBytes());//测试用
        
        
        // 从request中取得输入流
//        InputStream inputStream = request.getInputStream();//正式用
         
        Map<String, String> requestMap = new HashMap<String, String>();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            if (e.isTextOnly()) {
                requestMap.put(e.getName(), e.getText());
            } else {
                List<Element> itemEleList = e.elements();
                StringBuffer sb = new StringBuffer();
                for (Element item : itemEleList) {
                    List<Element> picEleList = item.elements();
                    for (Element pic : picEleList) {
                        if (StringUtils.isNotBlank(pic.getTextTrim())) {
                            sb.append(pic.getText()).append(";");
                        }
                    }
                }
                String picStr = "";
                if(sb.length()>0){
                    picStr = sb.substring(0, sb.length()-1);
                }
                requestMap.put(e.getName(), picStr);
            }
        }
        logger.debug("解析微信发来的请求requestMap:"+requestMap);
        // 释放资源
        inputStream.close();
        inputStream = null;
        
        return requestMap;
    }

    /**
     * xml转换成对象
     * 
     * @param xml xim字符串
     * @return String xml
     */
    public static <T> T xmlToObject(Class<?> clazz,String xml) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(clazz);
        @SuppressWarnings("unchecked")
        T t = (T) xstream.fromXML(xml);
        return t;
    }

}
