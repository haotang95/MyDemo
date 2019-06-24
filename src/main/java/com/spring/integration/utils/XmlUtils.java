package com.spring.integration.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class XmlUtils {


    public static JSONObject xml2Json(String xmlStr) throws JDOMException, IOException {
        if (StringUtils.isEmpty(xmlStr)) {
            return null;
        }
        xmlStr = xmlStr.replaceAll("\\\n", "");
        byte[] xml = xmlStr.getBytes("UTF-8");
        JSONObject json = new JSONObject();
        InputStream is = new ByteArrayInputStream(xml);
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(is);
        Element root = doc.getRootElement();
        json.put(root.getName(), iterateElement(root));

        return json;
    }

    private static JSONObject iterateElement(Element element) {
        List<Element> node = element.getChildren();
        JSONObject obj = new JSONObject();
        List list = null;
        for (Element child : node) {
            list = new LinkedList();
            String text = child.getTextTrim();
            if (StringUtils.isBlank(text)) {
                if (child.getChildren().size() == 0) {
                    continue;
                }
                if (obj.containsKey(child.getName())) {
                    list = (List) obj.get(child.getName());
                }
                //遍历child的子节点
                list.add(iterateElement(child));
                obj.put(child.getName(), list);
            } else {
                if (obj.containsKey(child.getName())) {
                    Object value = obj.get(child.getName());
                    try {
                        list = (List) value;
                    } catch (ClassCastException e) {
                        list.add(value);
                    }
                }
                    //child无子节点时直接设置text
                if (child.getChildren().size() == 0) {
                    obj.put(child.getName(), text);
                } else {
                    list.add(text);
                    obj.put(child.getName(), list);
                }
            }
        }
        return obj;
    }

    public static void main(String[] args) {
        try (InputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\csad.txt");
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] bytes = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(bytes)) != -1){
                bos.write(bytes,0,length);
            }
            bos.close();
            String xml = bos.toString();
            System.out.println(xml);
            JSONObject jsonObject = xml2Json(xml);
            System.out.println(jsonObject.toJSONString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }

    }
}
