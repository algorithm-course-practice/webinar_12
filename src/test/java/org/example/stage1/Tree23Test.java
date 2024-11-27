package org.example.stage1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class Tree23Test {

    @Test
    void check() {

        Tree23<Integer> tree = new Tree23<>();
        for (int i = 1; i < 11; i++) {
            assertFalse(tree.contains(i));
            assertTrue(tree.add(i));
            assertTrue(tree.contains(i));
        }
        for (int i = 1; i < 11; i++) {
            System.out.println("------------------ delete "+i);
            tree.print();
            assertTrue(tree.contains(i), "i = "+i);
            if(i == 6){
                System.out.println("dbg");;
            }
            System.out.println(" after delete");
            assertTrue(tree.remove(i));
            tree.print();
            assertFalse(tree.contains(i));
        }
    }

    @Test
    void reverse() {

        Tree23<Integer> tree = new Tree23<>();
        for (int i = 1; i < 11; i++) {
            assertFalse(tree.contains(i));
            assertTrue(tree.add(i));
            assertTrue(tree.contains(i));
        }
        for (int i = 10; i >= 1; i--) {
            System.out.println("------------------ delete "+i);
            tree.print();
            assertTrue(tree.contains(i), "i = "+i);
            System.out.println(" after delete");
            assertTrue(tree.remove(i));
            tree.print();
            assertFalse(tree.contains(i));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    })
    void random(int seed) {

        Tree23<Integer> tree = new Tree23<>();
        List<Integer> items = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            items.add(i);
            assertFalse(tree.contains(i));
            assertTrue(tree.add(i));
            assertTrue(tree.contains(i));
        }

        Collections.shuffle(items, new Random(seed));

        for (int i: items) {
            System.out.println("------------------ delete "+i);
            tree.print();
            assertTrue(tree.contains(i), "i = "+i);
            System.out.println(" after delete");
            assertTrue(tree.remove(i));
            tree.print();
            assertFalse(tree.contains(i));
        }
    }


}