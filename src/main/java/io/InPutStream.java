package io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InPutStream {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        try {
            InputStream is = new FileInputStream("/Users/rongxuning/vscode/util/src/main/java/io/a.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String str = "";
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
