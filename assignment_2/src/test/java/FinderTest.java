import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Test for Finder. */
public class FinderTest {

    @Test
    public void findMax_validArrayOfLengthOne() {
        assertEquals(Finder.findMax(new int[]{1}), new Integer(1));
    }

    @Test
    public void findMax_validUnorderedArrayOfLengthGreaterThanOne() {
        assertEquals(Finder.findMax(new int[]{4, 5, 1, 2, 3}), new Integer(5));
    }

    @Test
    public void findMax_validOrderedArrayOfLengthGreaterThanOne() {
        assertEquals(Finder.findMax(new int[]{1,2,3}), new Integer(3));
    }

    @Test
    public void findMax_invalidEmptyArray() {
        assertEquals(Finder.findMax(new int[]{}), null);
    }

    @Test
    public void findMax_invalidNullArray() {
        assertEquals(Finder.findMax(null), null);
    }

    @Test
    public void findMin_validArrayOfLengthOne() {
        assertEquals(Finder.findMin(new int[]{1}), new Integer(1));
    }

    @Test
    public void findMin_validUnorderedArrayOfLengthGreaterThanOne() {
        assertEquals(Finder.findMin(new int[]{4, 5, 1, 2, 3}), new Integer(1));
    }

    @Test
    public void findMin_validOrederedArrayOfLengthGreaterThanOne() {
        assertEquals(Finder.findMin(new int[]{1,2,3}), new Integer(1));
    }

    @Test
    public void findMin_invalidEmptyArray() {
        assertEquals(Finder.findMin(new int[]{}), null);
    }

    @Test
    public void findMin_invalidNullArray() {
        assertEquals(Finder.findMin(null), null);
    }
}
