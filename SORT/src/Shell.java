/**
 * Created by scfro on 2017/11/6.
 * 希尔排序
 */
public class Shell {
    public static void sort(Comparable[] a) {
        //将a[]升序排列
        int N = a.length;
        int h = 1;
        while (h < N/3) h = h * 3 + 1; //h = 4, 13, 40,...
        while (h >= 1){
            //将数组变为h有序：每隔h间隔的元素组成的数组(比如a[1],a[5],a[9]...)有序
            for (int i = h; i < N; i++){
                //将a[i]插入到a[i-h]、a[i-2h]、a[i-3h]...中合适的位置
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h){
                    exch(a, j, j-h);
                }
            }
            h = h / 3;
        }
    }
    private static boolean less(Comparable v, Comparable w){
        return v.compareTo(w) < 0;
    }
    private static void exch(Comparable[] a, int i, int j){
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
    private static void show(Comparable[] a){
        for(int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
        System.out.println("\n");
    }
    public static boolean isSorted(Comparable[] a){
        for(int i = 1; i < a.length; i++)
            if(less(a[i], a[i-1])) return false;
        return true;
    }
    public static void main(String[] args){
        String[] a = {"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
