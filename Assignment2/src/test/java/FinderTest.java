import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertNull(Finder.findMax(new int[]{}));
    }

    @Test
    public void findMax_invalidNullArray() {
        assertNull(Finder.findMax(null));
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
        assertNull(Finder.findMin(new int[]{}));
    }

    @Test
    public void findMin_invalidNullArray() {
        assertNull(Finder.findMin(null));
    }
}
