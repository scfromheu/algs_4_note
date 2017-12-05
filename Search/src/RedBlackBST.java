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

    private Node rotateLeft(Node h){
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
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
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

    private Node moveRedLeft(Node h){
        flipColors(h);
        if (isRed(h.right.left)){  //如果右子结点为3-结点
            h.right = rotateRight(h.right);  
            h = rotateLeft(h);
        }
        return h;
    }

    public void deleteMin(){
        if (!isRed(root.left) && !isRed(root.right))  //如果左右链接均为黑，这是一个2-结点，根结点连接设为红，后续进行变色
            root.color = RED;
        root = deleteMin(h.left);
        if (!isEmpty()) root.color = BLACK;
    }

    public Node deleteMin(Node h){
        if (h.left == null)  //一直向左找到最后一个结点
            return null;
        if (!isRed(h.left) && !isRed(h.left.left))  //如果该结点与其左子结点均为2-结点，还要判断其右结点类型
            h = moveRedLeft(h);
        return balance(h);
    }

    private balance(Node h){
        if isRed(h.right) h = rotateLeft(h);
        //以下情况可看做在一个双键树（3-结点）中插入新建
        if (isRed(h.right) && !isRed(h.left))    h = rotateLeft(h);  //增加的结点在两值之间，挂在了较小结点的右侧，其父结点的右连接为红，左旋后变为下面的类型
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);  //增加的结点比两值都大，出现连续两个红色左连接，右旋后变为下面的类型
        if (isRed(h.left) && isRed(h.right))     flipColors(h);  //增加的新键最小，连接在较大键的右连接，其父结点左右链接均为红色，做翻转颜色处理

        h.N = 1 + size(h.left) + size(h.right);
        return h;
    }
}