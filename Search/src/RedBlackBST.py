
class Node(object):
    def __init__(self, key, val, size, color):
        self.key = key
        self.val = val
        self.size = size
        self.color = color
        self._left = None
        self._right = None

    @property
    def left(self):
        """left node of this node"""
        return self._left

    @left.setter
    def left(self, value):
        if not isinstance(value, Node):
            raise ValueError('The type of left must be Node!')
        self._left = value

    @property
    def right(self):
        """right node of this node"""
        return self._right

    @right.setter
    def right(self, value):
        if not isinstance(value, Node):
            raise ValueError('The type of right must be Node!')
        self._right = value


class RedBlackBST(object):
    """RedBlackBST"""
    RED = True
    BLACK = False

    def __init__(self, root):
        self._RED = True
        self._BLACK = False
        self._root = root

    @property
    def root(self):
        """get root of the tree"""
        return self._root

    # @root.setter
    # def root(self, value):
    #     if not isinstance(value, Node):
    #         raise ValueError('The type of root must be Node!')
    #     self._root = value

    def size(self):
        """get size of the tree"""
        return self._size(self._root)

    def _size(self, node):
        if not node:
            return 0
        return node.size

    def _is_red(self, node):
        if not node:
            return False
        return node.color == self._RED

    def _is_empty(self, node):
        return self._size(node) == 0

    def min(self):
        """get the minimum node of the tree"""
        return self._min(self._root.key)

    def _min(self, node):
        if not node.left:
            return node
        return node.left

    def max(self):
        """get the maximum node of the tree"""
        return self._max(self._root.key)

    def _max(self, node):
        if not node.right:
            return node
        return node.right

    def floor(self, key):
        """find the largest key which is less than or equal to the given"""
        rv = self._floor(self._root, key)
        if not rv:
            return None
        return rv.key

    def _floor(self, node, key):
        if not node:
            return None
        if key == node.key:
            return node
        if key < node.key:
            return self._floor(node.left, key)
        right_node = self._floor(node.right, key)
        if right_node:
            return right_node
        return node

    def ceiling(self, key):
        """find the minimum key which is more than or equal to the given"""
        rv = self._ceiling(self._root, key)
        if not rv:
            return None
        return rv.key 

    def _ceiling(self, node, key):
        if not node:
            return None
        if key == node.key:
            return node
        if key > node.key:
            return self._ceiling(node.right, key)
        left_node = self._ceiling(node.left, key)
        if left_node:
            return left_node
        return node

    def select(self, rank):
        """select the node which ranks `rank` and return the key"""
        return self._select(self._root, rank).key

    def _select(self, node, rank):
        if not node:
            return None
        left_size = self._size(node.left)
        if left_size < rank:
            return self._select(node.left, rank)
        elif left_size > rank:
            return self._select(node.right, rank - left_size - 1)
        else:
            return node

    def rank(self, key):
        """return rank of the given key"""
        return self._rank(self._root, key)

    def _rank(self, key, node):
        if not node:
            return 0
        if key < node.key:
            return self._rank(key, node.left)
        elif key > node.key:
            return 1 + self._size(node.left) + self._rank(key, node.right)
        else:
            return self._size(node.left)

    def _rotate_left(self, node):
        right_node = node.right
        node.right = right_node.left
        right_node.left = node
        right_node.color = node.color
        node.color = self._RED
        right_node.size = node.size
        node.size = 1 + self._size(node.left) + self._size(node.right)
        return right_node

    def _rotate_right(self, node):
        left_node = node.left
        node.left = left_node.right
        left_node.right = node
        left_node.color = node.color
        node.color = self._RED
        left_node.size = node.size
        node.size = 1 + self._size(node.left) + self._size(node.right)
        return left_node

    def _flip_colors(self, node):
        node.color = not node.color
        node.left.color = not node.left.color
        node.right.color = not node.right.color

    def put(self, key, val):
        """insert a key-value pair into the tree"""
        self._root = self._put(self._root, key, val)
        self._root.color = self._BLACK

    def _put(self, node, key, val):
        if not node:
            return Node(key, val, 1, self._RED)

        if key < node.key:
            node.left = self._put(node.left, key, val)
        elif key > node.key:
            node.right = self._put(node.right, key, val)
        else:
            node.val = val
        
        if self._is_red(node.right) and not self._is_red(node.left):
            self._rotate_left(node)
        if self._is_red(node.left) and self._is_red(node.left.left):
            self._rotate_right(node)
        if self._is_red(node.left) and self._is_red(node.right):
            self._flip_colors(node)

        node.size = 1 + self._size(node.left) + self._size(node.right)
        return node

    def get(self, key):
        """get the value by a key"""
        return self._get(self._root, key)

    def _get(self, node, key):
        if not node:
            return None
        elif key < node.key:
            return self._get(node.left, key)
        elif key > node.key:
            return self._get(node.right, key)
        else:
            return node.val

    def _move_red_left(self, node):
        self._flip_colors(node)
        if node.right.left is self._RED:
            node.right = self._rotate_right(node.right)
            node = self._rotate_left(node)
            self._flip_colors(node)
        return node

    def _move_red_right(self, node):
        self._flip_colors(node)
        if node.left.left is self._RED:
            node = self._rotate_right(node)
            self._flip_colors(node)
        return node

    def _balance(self, node):
        if node.right is self._RED:
            node = self._rotate_left(node)
        if self._is_red(node.left) and self._is_red(node.left.left):
            self._rotate_right(node)
        if self._is_red(node.left) and self._is_red(node.right):
            self._flip_colors(node)
    
        node.size = 1 + self._size(node.left) + self._size(node.right)
        return node
