package txt;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class BinaryTreeTester {
    List<Integer> list;
    BinarySearchTree bst;

    @Before
    public void setup() {
        list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 0, -1, 2, 3));
        bst = new BinarySearchTree();
        for (int i : list) {
            bst.add(i);
        }
    }

    @Test
    public void insertFound() {
        for (int i : list) {
            assertNotNull(bst.find(i));
        }
    }

    @Test
    public void othersNotFound() {
        assertNull(bst.find(6));
        assertNull(bst.find(-3));
        assertNull(bst.find(-4));
    }

    @Test
    public void elemsFound() {
        List<Integer> elems = bst.getElems();
        for (int i : elems) {
            assertNotNull(bst.find(i));
        }
    }

    @Test
    public void elemsWereAdded() {
        List<Integer> elems = bst.getElems();
        assertEquals(7, elems.size());
        for (int i : elems) {
            assertNotNull(list.contains(i));
        }
    }

    @Test
    public void elemsCorrect() {
        List<Integer> elems = bst.getElems();
        for (int i : list) {
            assertTrue(elems.contains(i));
        }
    }

    @Test
    public void elemsInSortedOrder() {
        List<Integer> elems = bst.getElems();
        for (int i = 1; i < elems.size(); i++) {
            assertTrue(elems.get(i-1) < elems.get(i));
        }
    }
}