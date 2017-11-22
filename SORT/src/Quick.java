/**
 * Created by scfro on 2017/11/9.
 * 快速排序
 */

public class Quick {
    public static void sort(Comparable[] a){
        //此处应先打乱一下a的顺序，书中为`StdRandom.shuffle(a)`
        sort(a, 0, a.length - 1);
    }
    public static void sort(Comparable[] a, int lo, int hi){
        if (hi <= lo) return;
        int j = partition(a, lo, hi); //确定一下分界的位置
        sort(a, lo, j - 1); //左右分别再递归排序
        sort(a, j + 1, hi);
    }

    public static int partition(Comparable[] a, int lo, int hi){
        int i = lo, j = hi + 1;
        Comparable v = a[lo];
        while (true){
            while (less(a[++i], v)) if(i == hi) break; //从左向右找一个比最左大的元素
            while (less(v, a[--j])) if(j == lo) break; //从右向左找一个比最左小的元素
            if (i >= j) break;
            exch(a, i ,j);                             //找到后二者交换
        }
        exch(a, lo, j);                                //跳出后将最左元素交换到分界位置
        return j;
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
