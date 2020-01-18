package cn.atool.distributor.util;

/**
 * @author darui.wu
 * @create 2020/1/15 2:57 下午
 */
public class StringHelper {

    public static boolean isBlank(String input) {
        if (input == null) {
            return true;
        } else {
            return input.trim().isEmpty();
        }
    }

    final static char[] digits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private static final int RADIX = 62;

    public static String toString(long num) {
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (num < 0);

        if (!negative) {
            num = -num;
        }
        while (num <= -RADIX) {
            buf[charPos--] = digits[(int) (-(num % RADIX))];
            num = num / RADIX;
        }
        buf[charPos] = digits[(int) (-num)];
        if (negative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (65 - charPos));
    }
}
