/**
 * Created by scfro on 2017/11/5.
 * 插入排序
 */
public class Insertion {
    public static void sort(Comparable[] a) {
        int N = a.length;
        for(int i = 1; i < N; i++){
            //对于每一个元素向前遍历，插入到合适的位置中
            for(int j = i; j > 0 && less(a[j], a[j-1]); j--) exch(a, j, j-1);
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
        String[] a = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
