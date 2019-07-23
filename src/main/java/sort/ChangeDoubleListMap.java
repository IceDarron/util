package sort;

public class ChangeDoubleListMap {

    /**
     * 解决两个list<map>相互比较赋值的问题。
     * 从原先的嵌套for循环，时间复杂度为O（n^2）。降低到单循环，时间复杂度为O（n）。
     */
    public static void main(String[] args) {

        // 需要正排序
        int[] a = {3, 4, 5, 6, 7, 8, 9};
        int[] b = {0, 1, 2, 3, 4, 5, 6};

        int alength = a.length;
        int blength = b.length;
        int i = 0;
        int j = 0;

        int count = 0;

        while (i < alength && j < blength) {
            if (a[i] == b[j]) {
                System.out.println(a[i]);
                i++;
                j++;
            } else if (a[i] > b[j]) {
                j++;
            } else {
                i++;
            }
        }
    }
}
