/**

 * Created by scfro on 2017/11/9.
 * 三向切分的快速排序
 */

public class Quick3way {
    public static void sort(Comparable[] a){
        //此处应先打乱一下a的顺序，书中为`StdRandom.shuffle(a)`
        sort(a, 0, a.length - 1);
    }
    public static void sort(Comparable[] a, int lo, int hi){
        if (hi <= lo) return;
        int lt = lo, i = lo + 1, gt = hi; //分三（4？）部分，lt左边比最左小，lt到i之间等于最左，
        Comparable v = a[lo];             //i到gt之间不确定，gt右边大于最左
        while(i <= gt){
            int cmp = a[i].compareTo(v);
            if (cmp < 0)      exch(a, i++, lt++);
            else if (cmp > 0) exch(a, i, gt--);
            else              i++;
        }
        sort(a, lo, lt - 1);
        sort(a, gt + 1, hi);
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
        String[] a = {"Q", "U", "I", "C", "K", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);
        assert isSorted(a);
        show(a);
    }
}

