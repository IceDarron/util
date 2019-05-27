import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class T1 {

    public static void main(String[] args) throws ParseException {

        String time = "2019-03-18 03:23:33";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");//yyyy-mm-dd, 会出现时间不对, 因为小写的mm是代表: 秒
        Date date = sdf1.parse(time);
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
        System.out.println(sdf2.format(date));
    }


}
