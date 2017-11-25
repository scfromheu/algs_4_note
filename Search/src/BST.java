/**
 * Created by scfro on 2017/11/22.
 */
public class BST<Key extends Comparable<Key>, Value> {
    private Node root; //根节点

    public class Node {
        private Key key;  //键
        private Value val;  //值
        private Node left, right;  //左右节点
        private int N;  //以该节点为根的子树节点总数

        public Node(Key key, Value val, int N){
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }

    public int size(){
        return size(root);
    }

    public  int size(Node x){
        if (x == null) return 0;
        else           return x.N;
    }

    public Value get(Key key){
        return get(root, key);
    }

    private Value get(Node x, Key key){
        //在以x为根节点的子树中查找并返回key对应的值
        //如果找不到则返回null
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);  //如果key比x的键小，则返回x的左子树，反之返回右子树
        else if (cmp > 0) return get(x.right, key);
        else              return x.val;  //相等则表示查询到了，返回value
    }

    public void put(Key key, Value val){
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val){
        //如果key存在于以x为根节点的子树中则更新它的值
        //否则将以key, value为键值对的新结点插入到该子树中
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left = put(x.left, key, val);   //结点不为空，且当前键不对应时，只用来判断向左或向右寻找
        else if (cmp > 0) x.right = put(x.right, key, val);  //    返回的仍是原来的左右子树根节点，若为空则表示查找
        else              x.val = val;                       //    至最底层的结点仍未找到，说明没有匹配的键
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public Key min(){
        return min(root).key;
    }

    private Node min(Node x){
        if (x.left == null) return x;
        return min(x.left);
    }

    public Key max(){
        return max(root).key;
    }

    private Node max(Node x){
        if (x.right == null) return x;
        return max(x.right);
    }

    public Key floor(Key key){
        //找到小于等于key的最大键
        Node x = floor(root, key);
        if (x == null) return null;
        return x.key;
    }

    private Node floor(Node x, Key key){
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);  //比当前结点键还小，则继续向左子树中寻找
        Node t = floor(x.right, key);  //比当前结点大，同时比父结点小，则应返回当前右子树根节点，右为空则返回自身
        if (t != null) return t;
        else           return x;
    }

    public Key ceiling(Key key){
        //找到大于等于key的最大键
        Node x = ceiling(root, key);
        if (x == null) return null;
        return x.key;
    }

    private Node ceiling(Node x, Key key){
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp > 0) return ceiling(x.right, key);  //比当前结点键还大，则继续向右子树中寻找
        Node t = ceiling(x.left, key);  //比当前结点小，同时比父结点大，则应返回当前左子树根节点，右为空则返回自身
        if (t != null) return t;
        else           return x;
    }

    public Key select(int k){
        return select(root, k).key;
    }

    private Node select(Node x, int k){
        if (x == null) return null;
        int t = size(x.left);
        if      (t > k) return select(x.left, k);
        else if (t < k) return select(x.right, k-t-1);
        else            return x;
    }

    public int rank(Key key){
        return rank(key, root);
    }

    private int rank(Key key, Node x){
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else              return size(x.left);
    }


    public void deleteMin(){
        root = deleteMin(root);
    }

    private Node deleteMin(node x){
        if (x.left == null) return x.right; //左子树为空说明已经是最小，将其右子树替换到它原来的位置
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void deleteMax(){
        root = deleteMax(root);
    }

    private Node deleteMax(Node x){
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void delete(Key key){
        root = delete(root, key);
    }

    private Node delete(Node x, Key key){
        if (x == null) return null;  //找到最后，如果没找到，就算了？
        int cmp = key.compareTo(x.key);
        if (cmp < 0)      x.left  = delete(x.left,  key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else{
            if (x.left == null) return x.right;
            if (x.right == null) return x.left;
            Node t = x;
            x = min(t.right);  //找到该结点右子树中最小的，取代该节点
            x.right = deleteMin(t.right);  //原来的右子树中去掉被作为新的根结点的结点，作为新的右子树
            x.left = t.left; //左子树保持不变
        }
    }

    private void print(Node x){
        if (x == null) return;
        print(x.left);
        System.out.println(x.key);
        print(x.right);
    }

    public Iterable<Key> keys(){
        return keys(min(), max());
    }

    private Iterable<Key> keys(Key lo, Key hi){
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        retern queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi){
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key)
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }
}
