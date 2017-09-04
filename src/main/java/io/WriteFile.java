package io;

import java.io.*;

public class WriteFile {

    public void writeFile(String url, String context) {
        File file = null;
        FileReader fr = null;
        FileWriter fw = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            file = new File(url);
            fr = new FileReader(file);
            fw = new FileWriter(file, true);// 一定要加true否则每次到这都会把文件内容删掉
            br = new BufferedReader(fr);
            bw = new BufferedWriter(fw);
            if (!file.exists()) {
                file.createNewFile();
            }
            String s = null;
            s = br.readLine();
            while (true) {
                if (s == null) {
                    bw.write(context);
                    bw.newLine();
                    bw.flush();
                    break;
                }
                s = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
