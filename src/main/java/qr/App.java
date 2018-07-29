package qr;

public class App {

    public static void main(String[] args) throws Exception {

        //生成带logo 的二维码
        String text = "可以了解下，微博链接下评论内容，关注sd1206，长春长生";
        QRCodeUtil.encode(text, "./src/main/java/qr/V.jpg", "./src/main/java/QR/", true);

//        //生成不带logo 的二维码
//        String textt = "http://www.baidu.com";
//        QRCodeUtil.encode(textt, "", "./src/main/java/QR/", true);
//
//        //指定二维码图片，解析返回数据
//        System.out.println(QRCodeUtil.decode("./src/main/java/QR/QR-20180717112532.jpg"));

    }
}
