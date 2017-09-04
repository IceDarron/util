package calculateFileLines;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CalculateCode {

    /**
     * 计算一个文本文件有多少行
     * Author Darron
     * 2016.3.18
     */

    public int calculateCode(String url) {
        int count = 0;
        String str = null;
        File file = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            file = new File(url);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            if (!file.exists()) {
                return -1;
            }
            while ((str = br.readLine()) != null) {
                if (!str.equals("")) {
                    count++;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("本文件共有 " + count + " 行");
        return count;
    }
}
