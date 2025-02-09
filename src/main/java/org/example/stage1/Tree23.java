package org.example.stage1;

import ajs.printutils.PrettyPrintTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 *
 *
 * <p>This class implements a Tree 2-3 Data Structure. This kind of structure stores the
 * elements as a tree structure but balanced. So then, every leaf is at the same level in the tree.</p>
 *
 * <p>It is focused on performing fast searchs.</p>
 *
 * <p>Check this <a href="https://en.wikipedia.org/wiki/2&#8208;3_tree"> link</a> for a deepest explanation
 * of how it works.</p>
 *
 * <h1>Links</h1>
 * <a href="https://github.com/Albertpv95/Tree23/blob/master/Tree23.java">https://github.com/Albertpv95/Tree23/blob/master/Tree23.java</a>
 * <br/>
 * <a href="https://www.geeksforgeeks.org/2-3-trees-search-and-insert/">https://www.geeksforgeeks.org/2-3-trees-search-and-insert/<a>
 * <br/>
 * <a href="https://www.math.umd.edu/~immortal/CMSC420/notes/twothree.pdf">https://www.math.umd.edu/~immortal/CMSC420/notes/twothree.pdf</a></a>
 * <br/>
 * <a href="https://www.cs.princeton.edu/~dpw/courses/cos326-12/ass/2-3-trees.pdf">https://www.cs.princeton.edu/~dpw/courses/cos326-12/ass/2-3-trees.pdf</a>
 * <br/>
 * <a href="https://github.com/duchuyctlk/alg-vis">https://github.com/duchuyctlk/alg-vis</a>
 * <br/>
 * <a href="https://codeforces.com/problemset/problem/1324/C">https://codeforces.com/problemset/problem/1324/C</a>
 *
 * @param <T> Generic element
 * @author Albert Pernia Vazquez
 * @version 1.2.2 : Search enhanced and other minor improvements
 */

public class Tree23<T extends Comparable<T>> {

    PrettyPrintTree<Node> printer = new PrettyPrintTree<>(Node::getChildren, Node::getLabel);

    private Node root;              // The root of the tree

    private int size;              // Number of elements inside of the tree

    private static final int ROOT_IS_BIGGER = 1;
    private static final int ROOT_IS_SMALLER = -1;

    private boolean addition;       // A flag to know if the last element has been added correctly or not

    public Tree23() {

        this.root = new Node();

        size = 0;
    }


    /**
     * Adds a new element to the tree keeping it balanced.
     *
     * @param element The element to add
     * @return If the element has been added (true) or not because it already exists (false)
     */
    public boolean add(T element) {

        size++;

        addition = false;

        if (root == null || root.getLeftElement() == null) { // first case

            if (root == null) root = new Node();

            root.setLeftElement(element);

            addition = true;
        } else {

            Node newRoot = addElementI(root, element); // Immersion

            if (newRoot != null) root = newRoot;
        }

        if (!addition) size--;

        return addition;
    }


    /**
     * Adds all the elements into the tree. If one or more elements can't be inserted because they already
     * exists, all the elements inserted before are removed from the tree.
     *
     * @param elements the collection of elements to add
     * @return true if all the elements have been inserted, false if one or more elements could not be inserted because
     * they already exists
     */
    public boolean addAllSafe(Collection<T> elements) {
        int inserted = 0, i = 0;

        for (T e : elements) {

            if (!add(e)) {

                // for each element inserted from the collection, we remove it
                for (T a : elements) {

                    if (i >= inserted)
                        return false; // when all the elements inserted before the error have been removed, we return false

                    else remove(a);
                }
            } else inserted++;
        }

        return true;
    }

    /**
     * The algorithm stores the new element ordered as the 'compareTo' method of the Object is done. So the tree can store
     * the data in Ascending or Descending mode.
     * <p>
     * During the top down of the recursive, the algorithm tries to find the deepest level of the tree, where the new element will be saved.
     * <p>
     * On the bottom up, if the new element has to be added inside a node with two existing elements (3-node), then we have to
     * create a new tree level elevating a new node with the element which should be in the middle of the three.
     * <p>
     * In the code, this situation happens when the Node returned by the method is not null. If it is null, the node where
     * the new element has been inserted was a 2-node (there was an element position still empty).
     * <p>
     * Also, during the bottom up, the algorithm checks if the tree is well-balanced correcting the structure if it isn't.
     *
     * @param current The child where we are
     * @param element The element to insert
     * @return If there is a new level to add (we have tried to add a new element to a 3-node) or we don't have to do nothing (node is null)
     */
    private Node addElementI(Node current, T element) {

        Node newParent = null;

        // We aren't in the deepest level yet
        if (!current.isLeaf()) {

            Node sonAscended = null;

            if (current.leftElement.compareTo(element) == 0 || (current.is3Node() && current.rightElement.compareTo(element) == 0)) {

                // Already exists. This condition can be modified for the particular needs of any programmer
            }
            // The new element is smaller than the left element
            else if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {

                sonAscended = addElementI(current.left, element);

                // Case sonAscended != null --> the element has been added on a 3-node (there were 2 elements)
                if (sonAscended != null) { // A new node comes from the left branch

                    // The new element, in this case, is always less than the current.left
                    if (current.is2Node()) {

                        current.rightElement = current.leftElement;  // shift the current left element to the right
                        current.leftElement = sonAscended.leftElement;
                        current.right = current.mid;
                        current.mid = sonAscended.mid;
                        current.left = sonAscended.left;
                    } else { // In this case we have a new split, so the current element in the left will go up

                        // We copy the right part of the subtree
                        Node rightCopy = new Node(current.rightElement, null, current.mid, current.right);

                        // Now we create the new "structure", pasting the right part
                        newParent = new Node(current.leftElement, null, sonAscended, rightCopy);
                    }
                }

                // Case: the ascended element is bigger than the left element and less than the right element
            } else if (current.is2Node() || (current.is3Node() && current.rightElement.compareTo(element) == ROOT_IS_BIGGER)) {

                sonAscended = addElementI(current.mid, element);

                if (sonAscended != null) { // A new split

                    // The right element is empty, so we can set the ascended element in the left and the existing left element into the right
                    if (current.is2Node()) {

                        current.rightElement = sonAscended.leftElement;
                        current.right = sonAscended.mid;
                        current.mid = sonAscended.left;
                    } else { // Another case we have to split again

                        Node left = new Node(current.leftElement, null, current.left, sonAscended.left);
                        Node mid = new Node(current.rightElement, null, sonAscended.mid, current.right);
                        newParent = new Node(sonAscended.leftElement, null, left, mid);
                    }
                }
                // The new element is bigger than the right element
            } else if (current.is3Node() && current.rightElement.compareTo(element) == ROOT_IS_SMALLER) {

                sonAscended = addElementI(current.right, element);

                if (sonAscended != null) { // Split, the right element goes up

                    Node leftCopy = new Node(current.leftElement, null, current.left, current.mid);
                    newParent = new Node(current.rightElement, null, leftCopy, sonAscended);
                }
            }
        } else { // We are in the deepest level

            addition = true;

            // The element already exists
            if (current.leftElement.compareTo(element) == 0 || (current.is3Node() && current.rightElement.compareTo(element) == 0)) {

                addition = false;
            } else if (current.is2Node()) { // an easy case, there is not a right element

                // if the current left element is bigger than the new one --> we shift the left element to the right
                if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {

                    current.rightElement = current.leftElement;
                    current.leftElement = element;
                }
                // if the new element is bigger, we add it in the right directly
                else if (current.leftElement.compareTo(element) == ROOT_IS_SMALLER) current.rightElement = element;
            }
            // Case 3-node: there are 2 elements in the node and we want to add another one. We have to split the node
            else newParent = split(current, element);
        }

        return newParent;
    }

    /**
     * Creates the new node structure that will be attached during the bottom up of the addElementI method.
     *
     * @param current The node where the split takes place
     * @param element The element we are trying to add.
     * @return A 2-node structure with a non null left and mid node.
     */
    private Node split(Node current, T element) {
        Node newParent = null;

        // The left element is bigger, so it will go up letting the new element on the left
        if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {

            Node left = new Node(element, null);
            Node right = new Node(current.rightElement, null);
            newParent = new Node(current.leftElement, null, left, right);

        } else if (current.leftElement.compareTo(element) == ROOT_IS_SMALLER) {

            // The new element is bigger than the current on the right and less than the right element
            // The new element goes up
            if (current.rightElement.compareTo(element) == ROOT_IS_BIGGER) {

                Node left = new Node(current.leftElement, null);
                Node right = new Node(current.rightElement, null);
                newParent = new Node(element, null, left, right);

            } else { // The new element is the biggest one, so the current right element goes up

                Node left = new Node(current.leftElement, null);
                Node right = new Node(element, null);
                newParent = new Node(current.rightElement, null, left, right);
            }
        }

        return newParent;
    }


    /**
     * Removes all of the elements from this Tree23 instance.
     */
    public void clear() {

        this.size = 0;
        this.root = null;    // GC do the rest
    }

    /**
     * Creates a copy of this Tree23 instance.
     *
     * @return A copy of this Tree23 instance
     */
    @Override
    public Tree23<T> clone() {

        Tree23<T> clone = new Tree23<>();

        if (!isEmpty()) cloneI(root, clone);

        return clone;
    }

    // Immersion
    private void cloneI(Node current, Tree23<T> clone) {

        if (current != null) {

            if (current.isLeaf()) {

                clone.add(current.getLeftElement());
                if (current.getRightElement() != null) clone.add(current.getRightElement());
            } else {

                cloneI(current.getLeftSon(), clone);

                clone.add(current.getLeftElement());

                cloneI(current.getMidSon(), clone);

                if (current.getRightElement() != null) {

                    if (!current.isLeaf()) clone.add(current.getRightElement());

                    cloneI(current.getRightSon(), clone);
                }
            }
        }
    }

    /**
     * @param element The element to find
     * @return true if this tree contains the specified element, false if not
     */
    public boolean contains(T element) {
        if (size == 0) {
            return false;
        }
        return find(element) != null;
    }


    /**
     * Searches an element inside of the tree.
     *
     * @param element The element to find
     * @return the element found or null if it doesn't exist
     */
    public T find(T element) {

        return findI(root, element);
    }

    private T findI(Node current, T element) {

        T found = null;

        if (current != null) {

            // Trivial case, we have found the element
            if (current.leftElement != null && current.leftElement.equals(element)) found = current.leftElement;
            else {

                // Second trivial case, the element to find equals the right element
                if (current.rightElement != null && current.rightElement.equals(element)) found = current.rightElement;
                else {
                    // Recursive cases
                    if (current.leftElement.compareTo(element) == ROOT_IS_BIGGER) {

                        found = findI(current.left, element);
                    } else if (current.getRightElement() == null || current.rightElement.compareTo(element) == ROOT_IS_BIGGER) {

                        found = findI(current.mid, element);
                    } else if (current.rightElement.compareTo(element) == ROOT_IS_SMALLER) {
                        found = findI(current.right, element);
                    } else return null;
                }
            }
        }

        return found;
    }


    /**
     * @return the number of levels of the tree (max deep)
     */
    public long getLevel() {
        Node aux = root;
        int level = 0;

        while (aux != null) {

            aux = root.getLeftSon();
            level++;
        }

        return level;
    }

    /**
     * Prints the elements of the tree in order.
     */
    public List<T> inOrder() {
        List<T> list = new ArrayList<>();
        if (!isEmpty()) {

            inOrderI(root, list);    // Immersion
        }
        return list;
    }

    private void inOrderI(Node current, List<T> list) {

        if (current != null) {

            if (current.isLeaf()) {
                list.add(current.getLeftElement());
                if (current.getRightElement() != null) list.add(current.getRightElement());
            } else {

                inOrderI(current.getLeftSon(), list);
                list.add(current.getLeftElement());

                inOrderI(current.getMidSon(), list);

                if (current.getRightElement() != null) {

                    if (!current.isLeaf()) list.add(current.getRightElement());

                    inOrderI(current.getRightSon(), list);
                }
            }
        }
    }

    /**
     * @return True if the tree is empty, false if not
     */
    public boolean isEmpty() {

        if (root == null) return true;

        if (root.getLeftElement() == null) return true;

        return false;
    }

    /**
     * Deletes an element from the tree.
     *
     * @param element The element to delete
     * @return True if the element has been deleted, false if not (the element was not in the tree)
     */
    public boolean remove(T element) {
        boolean deleted;

        // We decrease the number of levels at the start, if the element is not deleted, we increase the value at the end
        this.size--;

        deleted = removeI(root, element); // Immersion

        root.rebalance();
        if (root.getLeftElement() == null && root.getLeftSon() != null) {
            root = root.getLeftSon();
        }

        if (root.getLeftElement() == null) root = null; // We have deleted the last element of the tree

        if (!deleted) this.size++;

        return deleted;
    }

    /**
     * In a recursive way, the algorithm tries to find the element to delete from the tree.
     * <p>
     * When it finds the element, we can have one of this two situations:
     * <p>
     * <p>
     * A. The element we have to delete was in the deepest level of the tree, where we know all the rebalance patterns
     * (see the method "rebalance" below implemented in the private class Node) so we will not have many troubles in
     * this case because there are not more levels below of the current one.
     * <p>
     * B. The element to delete is not in the deepest level of the tree. In this situation we must force a swap.
     * What we have to do is:
     * <p>
     * - If we are deleting an element in the mid side, we are gonna replace it with the min value of the branch causing
     * an unbalanced case but in the deepest level.
     * <p>
     * - If the element to delete is in the right side of the tree, we replace that element with the max value of
     * the branch. Then, we have a unbalanced case but in the deepest level.
     * <p>
     * These processes achieves easy rebalance cases, excepting the critical case (full explained in the "rebalance" method):
     * <p>
     * - If after the deletion of the element a node has been empty and we don't have enough elements in the deepest level
     * of the tree to rebalance it, the tree will be reorganized from a higher level which increases the cost.
     *
     * @param current The current node where we are
     * @param element The element to delete
     * @return True if the element has been deleted or false if not
     */
    private boolean removeI(Node current, T element) {
        boolean deleted = true;

        // Trivial case, we are in the deepest level of the tree but we have not found the element (it does not exist)
        if (current == null) deleted = false;

        else {
            // Recursive case, we are still finding the element to delete
            if (!current.getLeftElement().equals(element)) {

                // If there is no element in the right (2 Node) or the element to delete is less than the right element
                if (current.getRightElement() == null || current.getRightElement().compareTo(element) == ROOT_IS_BIGGER) {

                    // The left element is bigger than the element to delete, so we go through the left child
                    if (current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {

                        deleted = removeI(current.left, element);
                    } else { // If not, we go through the mid child

                        deleted = removeI(current.mid, element);
                    }
                } else {

                    // If the element to delete does not equals the right element, we go through the right child
                    if (!current.getRightElement().equals(element)) {

                        deleted = removeI(current.right, element);
                    } else { // If not, we have found the element

                        // Situation A, the element equals the right element of a leaf so we just have to delete it
                        if (current.isLeaf()) current.setRightElement(null);

                        else { // We found the element but it is not in the leaf, this is the situation B

                            // We get the min element of the right branch, remove it from its current position and put it
                            // where we found the element to delete
                            T replacement = current.getRightSon().replaceMin();

                            current.setRightElement(replacement);
                        }
                    }
                }
            }
            // The left element equals the element to delete
            else {

                if (current.isLeaf()) { // Situation A

                    // The left element, the element to remove, is replaced by the right element
                    if (current.getRightElement() != null) {

                        current.setLeftElement(current.getRightElement());

                        current.setRightElement(null);
                    } else { // If there is no element in the right, a rebalance will be necessary

                        current.setLeftElement(null); // We let the node empty

                        // We warn on the bottom up that a node has been deleted (is empty) and a rebalance is necessary
                        // in THAT level of the tree
                        return true;
                    }
                } else { // Situation B

                    // We move the max element of the left branch where we have found the element
                    T replacement = current.getLeftSon().replaceMax();

                    current.setLeftElement(replacement);
                }
            }
        }

        if (current != null && !current.isBalanced()) {

            current.rebalance();  // The bottom level have to be rebalanced
        } else if (current != null && !current.isLeaf()) {

            boolean balanced = false;

            while (!balanced) {

                if (current.getRightSon() == null) {

                    // Critical case of the situation B at the left child
                    if (current.getLeftSon().isLeaf() && !current.getMidSon().isLeaf()) {

                        T replacement = current.getMidSon().replaceMin();

                        T readdition = current.getLeftElement();

                        current.setLeftElement(replacement);

                        add(readdition);

                        // Critical case of hte situation B at the right child
                    } else if (!current.getLeftSon().isLeaf() && current.getMidSon().isLeaf()) {

                        if (current.getRightElement() == null) {

                            T replacement = current.getLeftSon().replaceMax();

                            T readdition = current.getLeftElement();

                            current.setLeftElement(replacement);

                            add(readdition);
                        }
                    }
                }
                // It is important to note that we can't use the 'else' sentence because the situation could have changed in the if above
                if (current.getRightSon() != null) {

                    if (current.getMidSon().isLeaf() && !current.getRightSon().isLeaf()) {

                        current.getRightSon().rebalance();
                    }
                    if (current.getMidSon().isLeaf() && !current.getRightSon().isLeaf()) {

                        T replacement = current.getRightSon().replaceMin();

                        T readdition = current.getRightElement();

                        current.setRightElement(replacement);

                        add(readdition);
                    } else balanced = true;
                }
                if (current.isBalanced()) balanced = true;
            }
            if (current.getRightElement() == null && current.getRightSon() != null) {
                current.setRightSon(null);
            }
        }


        return deleted;
    }


    /**
     * @return The number of elements inside of the tree
     */
    public int size() {

        return size;
    }

    public void print() {
        if (root != null) {
            printer.display(root);
        }
    }

    /**
     * The 2-3 tree is formed by nodes that stores the elements of the structure.
     * <p>
     * Each node contains at most two elements and one at least. In case there is only one element
     * in a node, this is <b>always in the left</b>, so in this case the <b>right element is null</b>.
     * <p>
     * The 2-3 Tree structure defines two type of Nodes/childs:
     * <p>
     * - 2 Node : This node only has two childs, <b>always left and mid</b>. The right element is empty (null) and the
     * right node/child is also null.
     * <p>
     * - 3 Node : This node has the two elements, so it also has 3 childs: left, mid and right. It is full.
     */
    private class Node {

        private Node left;
        private Node mid;
        private Node right;
        private T leftElement;
        private T rightElement;


        /**
         * Creates an empty node/child
         */
        private Node() {

            left = null;
            mid = null;
            right = null;
            leftElement = null;
            rightElement = null;
        }

        /**
         * Constructor of a 3 Node without the childs defined yet (null references).
         *
         * @param leftElement  the element in the left
         * @param rightElement the element in the right
         */
        // Precondition: The left element must be less than the right element
        //               This is a private class but if you want to make it externally accessible
        //               I recommend to use the IllegalArgumentException
        private Node(T leftElement, T rightElement) {

            this.leftElement = leftElement;
            this.rightElement = rightElement;
            left = null;
            mid = null;
            right = null;
        }

        /**
         * Constructor of a 3 Node with the left and mid nodes/childs defined.
         *
         * @param leftElement  the element in the left
         * @param rightElement the element in the right
         * @param left         the left child
         * @param mid          the mid child
         */
        // Precondition: The left element must be less than the right element
        //               This is a private class but if you want to make it externally accessible
        //               I recommend to use the IllegalArgumentException
        private Node(T leftElement, T rightElement, Node left, Node mid) {

            this.leftElement = leftElement;
            this.rightElement = rightElement;
            this.left = left;
            this.mid = mid;
        }

        private T getLeftElement() {

            return leftElement;
        }

        private T getRightElement() {

            return rightElement;
        }

        private void setLeftElement(T element) {

            this.leftElement = element;
        }

        private void setRightElement(T element) {

            this.rightElement = element;
        }

        private void setLeftSon(Node son) {

            this.left = son;
        }

        private Node getLeftSon() {

            return left;
        }

        private void setMidSon(Node son) {

            this.mid = son;
        }

        private Node getMidSon() {

            return mid;
        }

        private void setRightSon(Node son) {

            this.right = son;
        }

        private Node getRightSon() {

            return right;
        }

        /**
         * @return true if we are on the deepest level of the tree (a leaf) or false if not
         */
        private boolean isLeaf() {

            return left == null && mid == null && right == null;
        }

        private boolean is2Node() {

            return rightElement == null; // also, right node is null but this will be always true if rightElement == null
        }

        private boolean is3Node() {

            return rightElement != null; // also, right node is not null but this will be always true if rightElement <> null
        }

        /**
         * Checks if the tree is well-balanced or not
         *
         * @return true if the tree is well-balanced, false if not
         */
        private boolean isBalanced() {

            boolean balanced = false;

            if (isLeaf()) { // If we are at the deepest level (the leaf), it is well-balanced for sure

                balanced = true;
            } else if (leftElement == null && rightElement == null) {
                return true;
            }
            // There are two cases: 2 Node or 3 Node
            else if (left.getLeftElement() != null && mid.getLeftElement() != null) {

                if (rightElement != null) { // 3 Node

                    if (right.getLeftElement() != null) {

                        balanced = true;
                    }
                } else {  // 2 Node

                    balanced = true; //getLeftSon().isLeaf() == getMidSon().isLeaf();
                }
            }

            return balanced;
        }


        private T replaceMax() {

            T max = null;

            if (!isLeaf()) { // Recursive case, we are not on the deepest level

                if (getRightElement() != null) {

                    max = right.replaceMax(); // If there is an element on the right, we continue on the right
                } else max = mid.replaceMax();  // else, we continue on the mid

            } else {    // Trivial case, we are on the deepest level of the tree

                if (getRightElement() != null) {

                    max = getRightElement();

                    setRightElement(null);
                    // We are luck, we don't need to rebalance anything
                } else {

                    max = getLeftElement();

                    setLeftElement(null);

                    // On the first up of the recursive function, there will be a rebalance
                }
            }

            if (!isBalanced()) rebalance(); // Keep calm and rebalance

            return max;
        }

        private T replaceMin() {

            T min = null;

            if (!isLeaf()) { // Cas recursiu, mentre no arribem al nivell mes profund anem baixant per l'esquerra sempre

                min = left.replaceMin();

            } else { // Cas trivial, agafem l'element i ho intentem deixar tot maco

                min = leftElement;

                leftElement = null;

                if (rightElement != null) { // Hi havia element a la dreta, el passem a l'esquerra i aqui no ha passat res!

                    leftElement = rightElement;

                    rightElement = null;

                }
            }

            if (!isBalanced()) { // Aquesta situacio es dona quan a la dreta no hi havia element, en la 1a pujada rebalancejara

                rebalance();
            }

            return min;
        }


        /**
         * Rebalances the deepest level of the tree from the second deepest.
         * <p>
         * The algorithm tries to put one element in each child, but there is a critical case where we must balance the
         * tree from a higher level removing the current level.
         */
        private void rebalance() {

            while (!isBalanced()) {

                if (getLeftSon().getLeftElement() == null) { // The unbalance is in the left child


                    // We put the left element of current node as the left element of the left child
                    getLeftSon().setLeftElement(getLeftElement());

                    // Now we replace the left element of the mid child as the left element of the current node
                    setLeftElement(getMidSon().getLeftElement());

                    // If a right element on the mid child exists, we shift it to the left
                    if (getMidSon().getRightElement() != null) {

                        getMidSon().setLeftElement(getMidSon().getRightElement());

                        getMidSon().setRightElement(null);
                        getLeftSon().setMidSon(getMidSon().getLeftSon());
                        getMidSon().shiftLeft();
                    }

                    // Else, we let the mid child EMPTY, so the next iteration may solve this situation
                    // if not, the party of the critical case starts here!
                    else {

                        getMidSon().setLeftElement(null);
                    }

                } else if (getMidSon().getLeftElement() == null) { // The unbalance is in the right child

                    // CRITICAL CASE, each node (child) of the deepest level have just one element (the right is empty)
                    // the algorithm will have to rebalance from a higher level of the tree
                    if (getRightElement() == null) {

                        if (getLeftSon().getLeftElement() != null && getLeftSon().getRightElement() == null && getMidSon().getLeftElement() == null) {

                            getLeftSon().setRightElement(getLeftElement());
                            setLeftElement(null);
                            if (!getMidSon().isLeaf()) {
                                if (getMidSon().getMidSon() != null) {
                                    getLeftSon().setMidSon(getMidSon().getLeftSon());
                                    getLeftSon().setRightSon(getMidSon().getMidSon());
                                } else {
                                    getLeftSon().setRightSon(getMidSon().getLeftSon());
                                }

                            }
                            setMidSon(null);
                        } else {
                            getMidSon().setLeftElement(getLeftElement());

                            if (getLeftSon().getRightElement() == null) {

                                setLeftElement(getLeftSon().getLeftElement());

                                getLeftSon().setLeftElement(null);

                            } else {

                                setLeftElement(getLeftSon().getRightElement());

                                getLeftSon().setRightElement(null);
                            }

                            if (getLeftSon().getLeftElement() == null && getMidSon().getLeftElement() == null) {

                                // The other but same case the party starts
                                setLeftSon(null);
                                setMidSon(null);
                                setRightSon(null);
                            }
                        }
                    } else {

                        // We put the right element of the current node as the left element of the mid son
                        getMidSon().setLeftElement(getRightElement());

                        // We put the left element of the right child as the right element of the current node
                        setRightElement(getRightSon().getLeftElement());

                        // If the right child, where we have taken the last element has a right element, we move it
                        // into the left of the same child
                        if (getRightSon().getRightElement() != null) {

                            getRightSon().setLeftElement(getRightSon().getRightElement());

                            getRightSon().setRightElement(null);
                        } else { // Else, we let the right child EMPTY

                            getRightSon().setLeftElement(null);
                        }
                    }

                    // Unbalance in the right child
                } else if (getRightElement() != null && getRightSon().getLeftElement() == null) {


                    /*
                     * In this case we can have two situations:
                     *
                     * (1) The mid child is full, so we have to do a shift of the elements to the right
                     *
                     * (2) The mid child only has the left element, then we have to put the right element
                     * 	   of the current node as the right element of the mid child
                     */
                    if (getMidSon().getRightElement() != null) { // (1)

                        getRightSon().setLeftElement(getRightElement());

                        setRightElement(getMidSon().getRightElement());

                        getMidSon().setRightElement(null);
                    } else { // (2)

                        getMidSon().setRightElement(getRightElement());

                        setRightElement(null);
                    }
                }
            }
        }

        private void shiftLeft() {
            left = mid;
            mid = right;
            right = null;
        }


        public List<Node> getChildren() {
            return asList(getLeftSon(), getMidSon(), getRightSon());
        }


        public String getLabel() {
            return String.format("(%s, %s)", getLeftElement(), getRightElement());
        }
    }
}