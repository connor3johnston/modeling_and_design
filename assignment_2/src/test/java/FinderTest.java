import org.junit.Assert;
import org.junit.Test;

/** Test for Finder. */
public class FinderTest {

    @Test
    public void findMax_validArrayOfLengthOne() {
        Assert.assertEquals(Finder.findMax(new int[]{1}), new Integer(1));
    }

    @Test
    public void findMax_validUnorderedArrayOfLengthGreaterThanOne() {
        Assert.assertEquals(Finder.findMax(new int[]{4, 5, 1, 2, 3}), new Integer(5));
    }

    @Test
    public void findMax_validOrderedArrayOfLengthGreaterThanOne() {
        Assert.assertEquals(Finder.findMax(new int[]{1,2,3}), new Integer(3));
    }

    @Test
    public void findMax_invalidEmptyArray() {
        Assert.assertEquals(Finder.findMax(new int[]{}), null);
    }

    @Test
    public void findMax_invalidNullArray() {
        Assert.assertEquals(Finder.findMax(null), null);
    }

    @Test
    public void findMin_validArrayOfLengthOne() {
        Assert.assertEquals(Finder.findMin(new int[]{1}), new Integer(1));
    }

    @Test
    public void findMin_validUnorderedArrayOfLengthGreaterThanOne() {
        Assert.assertEquals(Finder.findMin(new int[]{4, 5, 1, 2, 3}), new Integer(1));
    }

    @Test
    public void findMin_validOrederedArrayOfLengthGreaterThanOne() {
        Assert.assertEquals(Finder.findMin(new int[]{1,2,3}), new Integer(1));
    }

    @Test
    public void findMin_invalidEmptyArray() {
        Assert.assertEquals(Finder.findMin(new int[]{}), null);
    }

    @Test
    public void findMin_invalidNullArray() {
        Assert.assertEquals(Finder.findMin(null), null);
    }
}
