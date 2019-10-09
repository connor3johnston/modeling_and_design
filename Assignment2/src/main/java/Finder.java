/** A class to find specified values within an array. */
public class Finder {
    public static Integer findMax(int[] intArray) {
        if (intArray == null || intArray.length == 0) {
            return null;
        }

        int max = intArray[0];
        for (int x = 1; x < intArray.length; x++) {
            if (intArray[x]  > max) {
                max = intArray[x];
            }
        }

        return max;
    }

    public static Integer findMin(int[] intArray) {
        if (intArray == null || intArray.length == 0) {
            return null;
        }

        int min = intArray[0];
        for (int x = 1; x < intArray.length; x++) {
            if (intArray[x] < min) {
                min = intArray[x];
            }
        }

        return min;
    }
}
