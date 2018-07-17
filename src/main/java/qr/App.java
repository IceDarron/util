package qr;

public class App {

    public static void main(String[] args) throws Exception {

        //生成带logo 的二维码
        String text = "https://www.oschina.net/p/springboot-vue-";
        QRCodeUtil.encode(text, "./src/main/java/QR/捕11获.PNG", "./src/main/java/QR/", true);

        //生成不带logo 的二维码
        String textt = "http://www.baidu.com";
        QRCodeUtil.encode(textt, "", "./src/main/java/QR/", true);

        //指定二维码图片，解析返回数据
        System.out.println(QRCodeUtil.decode("./src/main/java/QR/QR-20180717112532.jpg"));

    }
}
