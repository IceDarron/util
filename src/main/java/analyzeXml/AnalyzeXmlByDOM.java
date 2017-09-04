package analyzeXml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class AnalyzeXmlByDOM {

    /**
     * 通过DOM方式解析XML文件
     * Author Darron
     * 2016.3.16
     */

    public void analyzeXmlByDOM() {
        // 创建一个DocumentBuilderFactory对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // 创建一个DocumentBuilder对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            // DocumentBuilder.parse(“url”)加载文件 指向Document （导包org.w3c.dom）
            Document document = db.parse(new File("I:/Java/workspace/TestUtil/src/com/darron/analyzeXml/books.xml"));
            // 获取所有节点（返回的list）
            NodeList booklist = document.getElementsByTagName("book");
            for (int i = 0; i < booklist.getLength(); i++) {
                System.out.println("=====下面开始遍历第" + (i + 1) + "本书=====");
                // 遍历每一个节点,通过item方法获取一个book节点，索引从0开始
                Node book = booklist.item(i);
                // 获取book节点里面的所有属性
                NamedNodeMap attrs = book.getAttributes();
                System.out.println("第" + (i + 1) + "本书共有" + attrs.getLength());
                // 遍历book属性
                for (int j = 0; j < attrs.getLength(); j++) {
                    // 通过item方法获取属性 通过getNodeName获得名 通过getNodeValue获得值
                    System.out.println("属性名" + attrs.item(j).getNodeName());
                    System.out.println("属性名" + attrs.item(j).getNodeValue());
                }
                System.out.println("=====结束=====\n");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
