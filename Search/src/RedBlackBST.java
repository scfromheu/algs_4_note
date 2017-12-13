public class RedBlackBST<Key extends Comparable<Key>, Value>{
    private Node root;

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node{
        Key key;
        Value val;
        Node left, right;
        int N;
        boolean color;

        Node(Key key, Value val, int N, boolean color){
            this.key = key;
            this.val = val;
            this.N = N;
            this.color = color;
        }
    }

    public int size(){
        return size(root);
    }

    public  int size(Node x){
        if (x == null) return 0;
        else           return x.N;
    }

    private boolean isRed(Node x){
        if (x == null) return false;
        return x.color == RED;
    }

    private boolean isEmpty(){
        return size(root) == 0;
    }

    //----以下区域里代码和二叉树没区别--------
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
    //----以上区域里代码和二叉树没区别--------

    private Node rotateLeft(Node h){  //主要目的是把左面的红连接搞到右边去，红连接两边的结点不变
        Node x = h.right;             //            (E)h                      (S)
        h.right = x.left;             //           /   \\                    //  \
        x.left = h;                   //        (<E)     (S)x       ==>    (E)   (>S)
        x.color = h.color;            //                /   \              /  \
        h.color = RED;                //             (E-S)  (>S)         (<E) (E-S)
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    private Node rotateRight(Node h){
        Node x = h.left;
        h.left = h.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    private void flipColors(Node h){
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    public void put(Key key, value val){
        root = put(root, key, val);
        root.color = BLACK;  //根结点总是黑色的
    }

    private Node put(Node h, Key key, Value val){
        if (h == null) return new Node(key, val, 1, RED);

        int cmp = key.compareTo(h.key);
        if      (cmp < 0) h.left = put(h.left, key, val);
        else if (cmp > 0) h.right = put(h.right, key, val);
        else              h.val = val
        //以下情况可看做在一个双键树（3-结点）中插入新建
        if (isRed(h.right) && !isRed(h.left))    h = rotateLeft(h);  //增加的结点在两值之间，挂在了较小结点的右侧，其父结点的右连接为红，左旋后变为下面的类型
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);  //增加的结点比两值都大，出现连续两个红色左连接，右旋后变为下面的类型
        if (isRed(h.left) && isRed(h.right))     flipColors(h);  //增加的新键最小，连接在较大键的右连接，其父结点左右链接均为红色，做翻转颜色处理

        h.N = 1 + size(h.left) + size(h.right);
        return h;
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

    private Node moveRedLeft(Node h){  //主要目的是把右子结点的红连接转移到左边，保证左子结点至少是3-结点，不会删光
        flipColors(h); //左右节点均为黑（2-节点）的话，则将其转化为一个4-结点，
        if (isRed(h.right.left)){  //如果右子结点为3-结点
            h.right = rotateRight(h.right);  //右边的3或4-结点的最左边变换到顶部，h变为顶部的左结点
            h = rotateLeft(h);  //h再换到左边的节点中，左边变为3-结点
            flipColors(h);  //颜色再变回来，这里原书答案似乎有误
        }
        return h;
    }

    public void deleteMin(){
        if (!isRed(root.left) && !isRed(root.right))  //如果左右链接均为黑，这是一个2-结点，根结点连接设为红，后续进行变色
            root.color = RED;
        root = deleteMin(root); 
        if (!isEmpty()) root.color = BLACK;
    }

    private Node deleteMin(Node h){
        if (h.left == null)  //一直向左找到最后一个结点
            return null;
        if (!isRed(h.left) && !isRed(h.left.left))  //如果该结点与其左子结点均为2-结点，还要判断其右结点类型
            h = moveRedLeft(h);
        return balance(h); 
    }

    private balance(Node h){  //向上分解4-结点，方法与put()相同
        if isRed(h.right) h = rotateLeft(h);  // 如果右连接为红，往左转
        
        //if (isRed(h.right) && !isRed(h.left))    h = rotateLeft(h);  
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);  
        if (isRed(h.left) && isRed(h.right))     flipColors(h); 

        h.N = 1 + size(h.left) + size(h.right);
        return h;
    }

    private Node moveRedRight(Node h){
        flipColors(h);
        if (isRed(h.left.left))  //此处原书中似乎有误，当左侧是3-结点时，将多出的结点转移给右边，右边变为右红连接的3-结点
            h = rotateRight(h);
            flipColors(h);
        return h;
    }

    public void deleteMax(){
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
    }

    private Node deleteMax(Node h){
        if (!isRed(h.left))  //对称于deleteMin(), 这里将右边遇到的红色左连接都转化为红色右连接，逻辑上保持一致
            h = rotateRight(h);
        if (h.right == null)
            return null;
        if (!isRed(h.right) && !isRed(h.right.left))  //该结点和右子结点都是2-结点，变成4-结点或把左侧的红连接右移
            h = moveRedRight(h)
        h.right = deleteMax(h.right);
        return(balance(h));
    }

    public void delete(Key key){  //和前面的一样
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
    }

    private Node delete(Node h, Key key){
        if (key.compareTo(h.key) < 0){  //如果比当前的key小，向左继续寻找，处理方法和deleteMin()类似
            if (!isRed(h.left) && !isRed(h.right))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        }
        else{  //如果不比当前小，那么考虑删除该结点或向右发展
            if (isRed(h.left))  //类似于deleteMax()
                h = rotateRight(h);
            if (key.compareTo(h.key = 0) && (h.right == null))  //键相等时，还要考虑是否是最后一个结点
                return null;  //是最后一个结点则直接删除
            if (!isRed(h.right) && !isRed(h.right.left))  //该结点和右子结点都是2-结点
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0){  //不是最小的，但右侧结点都已调整过，可以放心删除了
                h.val = get(h.right, min(h.right).key);  //用其右结点下最小的结点替换它，保证右边的都比它大
                h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            }
            else h.right = delete(h.right, key);  //如果不等（比该结点大），那就继续向右寻找
        }
        return balance(h);
    }
}