/**
 * Created by scfro on 2017/11/13.
 * 堆排序
 */
public class HeapSort {
    public static void sort(Comparable[] a){
        int N = a.length - 1;
        for (int k = N/2; k >= 1; k--){ //从倒数第二级节点开始，利用sink排序
            //System.out.println(k);
            sink(a, k, N);
            //show(a);
        }
        //System.out.println("--------------------------");
        while (N > 1){ //从顶部节点开始，将最大值与剩余的尾部交换，并将顶点下沉
            exch(a, 1, N--);
            sink(a, 1, N);
        }
    }

    private static void exch(Comparable[] a, int i, int j){
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void sink(Comparable[] a, int i, int max) {

        while (i * 2 < max) {
            int j = i * 2;
            if (j < max && less(a[j], a[j + 1])) j++;
            if (!less(a[i], a[j])) break;
            exch(a, i, j);
            i = j;
        }
    }

    private static boolean less(Comparable i, Comparable j){
        return i.compareTo(j) < 0;
    }

    private static void show(Comparable[] a){
        for(int i = 1; i < a.length; i++)
            System.out.print(a[i] + " ");
        System.out.println("\n");
    }
    public static boolean isSorted(Comparable[] a){
        for(int i = 1; i < a.length; i++)
            if(less(a[i], a[i-1])) return false;
        return true;
    }

    public static void main(String[] args){
        String[] a = {null, "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"}; //a[0]空缺，从1开始    索引
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
