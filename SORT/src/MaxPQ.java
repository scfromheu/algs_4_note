/**
 * Created by scfro on 2017/11/13.
 * 优先队列的实现
 */
public class MaxPQ<Key extends Comparable<Key>> {
    private Key[] pq; //基于堆的完全二叉树
    private int N = 0; //存储于pq中，pq[0]不使用

    public MaxPQ(int MaxN){
        pq = (Key[]) new Comparable[MaxN + 1];
    }

    public boolean isEmpty(){
        return N == 0;
    }

    public int size(){
        return N;
    }

    public void insert(Key v){
        pq[++N] = v;
        swim(N);
    }

    public Key delMax(){
        Key max = pq[1]; //最大值为顶点，指针为1
        exch(1, N--); //和最后一个节点交换，长度-1
        pq[N+1] = null; //防止越界，将原位置填充为null
        sink(1);
        return max;
    }

    private boolean less(int i, int j){
        return pq[i].compareTo(pq[j]) < 0;
    }

    private void exch(int i, int j){
        Key t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
    }

    private void swim(int k){
        while (k > 1 && less(k/2, k)){ //当该元素还未到顶并且比父节点更大
            exch(k/2, k); //和父节点交换
            k = k / 2; //指针指向父节点
        }
    }

    private void sink(int k){
        while(2 * k <= N){
            int j = 2 * k;
            if (j < N && less(j, j + 1)) j++; //找到子节点中较大的
            if (!less(k, j)) break; //比下一层较大的还要大了，说明到位了
            exch(k, j); //和子节点中较大的交换位置
            k = j; //指针指到下一层
        }
    }

}
