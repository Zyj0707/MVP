package com.zyj.base.util;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Hawk on 2015/12/31.
 */
public class ByteUtils {

    public static final byte[] EMPTY_BYTES = new byte[]{};

    public static final int BYTE_MAX = 0xff;

    public static byte[] getNonEmptyByte(byte[] bytes) {
        return bytes != null ? bytes : EMPTY_BYTES;
    }

    public static boolean isEmpty(byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }

    //16进制字符串转换byte[]
    public static byte[] stringToBytes(String text) {
        int len = text.length();
        byte[] bytes = new byte[(len + 1) / 2];
        for (int i = 0; i < len; i += 2) {
            int size = Math.min(2, len - i);
            String sub = text.substring(i, i + size);
            bytes[i / 2] = (byte) Integer.parseInt(sub, 16);
        }
        return bytes;
    }

    //byte转换为16进制字符串
    public static String byteToString(byte bytes) {
        return String.format(Locale.getDefault(), "%02X", bytes);
    }
    //byte[]转换为16进制字符串
    public static String byteToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        if (!isEmpty(bytes)) {
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format(Locale.getDefault(), "%02X", bytes[i]));
            }
        }

        return sb.toString();
    }

    public static byte[] trimLast(byte[] bytes) {
        int i = bytes.length - 1;
        for ( ; i >= 0; i--) {
            if (bytes[i] != 0) {
                break;
            }
        }
        return Arrays.copyOfRange(bytes, 0, i + 1);
    }

    public static byte[] fromShort(short n) {
        return new byte[] {
                (byte) n, (byte) (n >> 8)
        };
    }

    public static short getShort(byte[] bArray, short bOff) {
        return (short)(((short)bArray[bOff] << 8) + ((short)bArray[bOff + 1] & 255));
    }

    public static short makeShort(byte b1, byte b2) {
        return (short)(((short)b1 << 8) + ((short)b2 & 255));
    }

    public static short setShort(byte[] bArray, short bOff, short sValue) {
        bArray[bOff] = (byte)(sValue >> 8);
        bArray[bOff + 1] = (byte)sValue;
        return (short)(bOff + 2);
    }

    //小端排序
    public static byte[] fromInt(int n) {
        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (n >> (i * 8));
        }

        return bytes;
    }

    public static byte[] fromLong(long n) {
        byte[] bytes = new byte[8];

        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (n >> (i * 8));
        }

        return bytes;
    }

    /**
     * 将255之内的int类型的数据转换为byte
     * @param n int数据
     */
    public static byte intToByte(int n) {
        String hexStr = Integer.toHexString(n);
        byte[] iByte = stringToBytes(hexStr);

        return iByte[0];
    }

    public static int ubyteToInt(byte b) {
        return (int) b & 0x00FF;
    }

    public static byte[] double2Byte(double data, int len) {
        String str = String.format(Locale.getDefault(), "%" + len + "d", data);

        return str.getBytes();
    }

    /**
     * 将int类型的数据转换为byte数组 原理：将int数据中的四个byte取出，分别存储
     *
     * @param n   int数据
     * @param len 长度，不能大于4
     * @return 生成的byte数组
     */
    public static byte[] intToBytes(int n, int len) {
        byte[] ret = new byte[len];
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        for (int i = 0; i < len; i++) {
            ret[i] = b[4 - len + i];
        }
        return ret;
    }

    //小端排序
    public static int byteArrayToInt(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }


    public static boolean byteEquals(byte[] lbytes, byte[] rbytes) {
        if (lbytes == null && rbytes == null) {
            return true;
        }

        if (lbytes == null || rbytes == null) {
            return false;
        }

        int llen = lbytes.length;
        int rlen = rbytes.length;

        if (llen != rlen) {
            return false;
        }

        for (int i = 0; i < llen; i++) {
            if (lbytes[i] != rbytes[i]) {
                return false;
            }
        }

        return true;
    }

    public static byte[] arrayFillNonAtomic(byte[] bytes, short bOff, int len, byte fill) throws ArrayIndexOutOfBoundsException, NullPointerException {
        if(len < 0) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            while(len-- > 0) {
                bytes[bOff++] = fill;
            }
        }
        return bytes;
    }

    public static byte[] fillBeforeBytes(byte[] bytes, int len, byte fill) {
        byte[] result = bytes;
        int oldLen = (bytes != null ? bytes.length : 0);

        if (oldLen < len) {
            result = new byte[len];

            for (int i = len - 1, j = oldLen - 1; i >= 0; i--, j--) {
                if (j >= 0) {
                    result[i] = bytes[j];
                } else {
                    result[i] = fill;
                }
            }
        }

        return result;
    }

    public static byte[] fillAfterBytes(byte[] bytes, int len, byte fill) throws Exception {
        byte[] result = null;
        int oldLen = (bytes != null ? bytes.length : 0);

        if (oldLen < len) {
            result = new byte[len];

            for (int i = 0, j = 0; i < len; i++, j++) {
                if (j < oldLen) {
                    result[i] = bytes[j];
                } else {
                    result[i] = fill;
                }
            }
        }
        else if (oldLen > len){
            result = byteCut(bytes, 0, len);
        }
        else {
            result = bytes;
        }

        return result;
    }
    /// <summary>
    /// 将两个数组按字典序比较
    /// 如果left 大，则返回大于0的值
    /// 如果right大，则返回小于0的值
    /// 如果相等，返回0
    /// 请确保left 和 right大小一致且非空否则将产生超出索引异常
    public static int CompareBytesInDictorySort(byte[] left, byte[] right, int index, int count) {
        for (int i = index; i < index + count; i++) {
            if (left[i] < right[i]) {
                return -1;
            }
            else if (left[i] > right[i]) {
                return 1;
            }
        }
        //全部相等
        return 0;
    }

    public static void reset(byte[] lbytes, int lstart, int len, byte fill) {
        if (lbytes != null && lstart >= 0 && len >= 0) {
            for (int i = lstart, j=0; i < lbytes.length && j < len; i++, j++) {
                lbytes[i] = fill;
            }
        }
    }

    public static byte[] cutBeforeBytes(byte[] bytes, byte cut) {
        if (ByteUtils.isEmpty(bytes)) {
            return bytes;
        }

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != cut) {
                return Arrays.copyOfRange(bytes, i, bytes.length);
            }
        }

        return EMPTY_BYTES;
    }

    public static byte[] cutAfterBytes(byte[] bytes, byte cut) {
        if (ByteUtils.isEmpty(bytes)) {
            return bytes;
        }

        for (int i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] != cut) {
                return Arrays.copyOfRange(bytes, 0, i + 1);
            }
        }

        return EMPTY_BYTES;
    }

    public static byte[] getBytes(byte[] bytes, int start, int end) {
        if (bytes == null) {
            return null;
        }

        if (start < 0 || start >= bytes.length) {
            return null;
        }

        if (end < 0 || end >= bytes.length) {
            return null;
        }

        if (start > end) {
            return null;
        }

        byte[] newBytes = new byte[end - start + 1];

        for (int i = start; i <= end; i++) {
            newBytes[i - start] = bytes[i];
        }

        return newBytes;
    }

    public static boolean isAllFF(byte[] bytes) {
        int len = (bytes != null ? bytes.length : 0);

        for (int i = 0; i < len; i++) {
            if (ubyteToInt(bytes[i]) != BYTE_MAX) {
                return false;
            }
        }

        return true;
    }

    public static void byteReverse(byte[] bytes) {
        byte temp;
        for(int i=0;i<bytes.length;i++){
            temp = bytes[i];
            bytes[i] = (byte) (~temp);
        }
    }

    public static void copy(byte[] lbytes, byte[] rbytes, int lstart, int rstart) {
        if (lbytes != null && rbytes != null && lstart >= 0) {
            if(rstart < rbytes.length && lstart < lbytes.length) {
                System.arraycopy(rbytes, rstart, lbytes, lstart, rbytes.length - rstart);
            }
        }
    }

    public static void copy(byte[] lbytes, byte[] rbytes, int lstart, int rstart, int rlen) {
        if (lbytes != null && rbytes != null && lstart >= 0) {
            if(rstart < rbytes.length && lstart < lbytes.length && rstart < rlen) {
                System.arraycopy(rbytes, rstart, lbytes, lstart, rlen);
            }
        }
    }

    public static byte[] copyInverse(byte[] array) {
        if (!isEmpty(array)) {
            byte[] data = new byte[array.length];

            for (int i = array.length-1, j=0; i >= 0; i--, j++) {
                data[j] = array[i];
            }

            return data;
        }
        return array;
    }

    public static byte[] copyInverse(byte[] array, int start, int len) {
        if (!isEmpty(array) && len > 0) {
            byte[] data = new byte[len];

            for (int i = start+len-1, j=0; i >= 0 && j<len; i--, j++) {
                data[j] = array[i];
            }

            return data;
        }
        return array;
    }

    public static boolean equals(byte[] array1, byte[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return equals(array1, array2, Math.min(array1.length, array2.length));
    }

    public static boolean equals(byte[] array1, byte[] array2, int len) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length < len || array2.length < len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    /**
     * 截取byte数组指定长度
     * @param bytes
     * @param offset
     * @return
     */
    public static byte[] byteCut(byte[] bytes, int offset) throws Exception {
        return byteCut(bytes, offset, bytes.length - offset);
    }
    /**
     * 截取byte数组指定长度
     * @param bytes
     * @param offset
     * @param len
     * @return
     */
    public static byte[] byteCut(byte[] bytes, int offset, int len) throws Exception {
        byte[] result = new byte[len];
        System.arraycopy(bytes, offset, result, 0, len);
        return result;
    }

    /**
     * 合并 两个byte数据
     * @param b1
     * @param b2
     * @return
     */
    public static byte[] byteMerger(byte[] b1, byte[] b2) throws Exception {
        if (b1 == null) {
            return b2;
        }
        if (b2 == null) {
            return b1;
        }
        byte[] b = new byte[b1.length + b2.length];

        System.arraycopy(b1, 0, b, 0, b1.length);
        System.arraycopy(b2, 0, b, b1.length, b2.length);

        return b;
    }
    /**
     * 合并 两个byte数据
     * @param b1
     * @param b2
     * @return
     */
    public static byte[] byteMerger(byte[] b1, byte b2) throws Exception {
        byte[] b = new byte[b1.length + 1];

        System.arraycopy(b1, 0, b, 0, b1.length);
        b[b1.length] = b2;

        return b;
    }
    /**
     * 合并 两个byte数据
     * @param b1
     * @param b2
     * @return
     */
    public static byte[] byteMerger(byte b1, byte[] b2) throws Exception {
        byte[] b = new byte[b2.length + 1];
        b[0] = b1;

        System.arraycopy(b2, 0, b, 1, b2.length);

        return b;
    }
    /**
     * 合并 两个byte数据
     * @param b1
     * @param b2
     * @return
     */
    public static byte[] byteMerger(byte b1, byte b2) throws Exception {
        byte[] b = new byte[2];

        b[0] = b1;
        b[1] = b2;

        return b;
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        try {
            int len = asc.length();
            int mod = len % 2;
            if (mod != 0) {
                asc = "0" + asc;
                len = asc.length();
            }

            if (len >= 2) {
                len = len / 2;
            }
            byte bbt[] = new byte[len];
            byte abt[]  = asc.getBytes();
            int j, k;
            for (int p = 0; p < asc.length() / 2; p++) {
                if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                    j = abt[2 * p] - '0';
                } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                    j = abt[2 * p] - 'a' + 0x0a;
                } else {
                    j = abt[2 * p] - 'A' + 0x0a;
                }

                if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                    k = abt[2 * p + 1] - '0';
                } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                    k = abt[2 * p + 1] - 'a' + 0x0a;
                } else {
                    k = abt[2 * p + 1] - 'A' + 0x0a;
                }
                int a = (j << 4) + k;
                byte b = (byte) a;
                bbt[p] = b;
            }
            return bbt;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[6];
        }
    }
    /**
     * @功能: BCD码转为10进制串(阿拉伯数据)
     * @参数: BCD码
     * @结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }
    /**
     * 把12位数字转换成6位BCD码
     */
    public static byte[] int2BCD2(String str, int len) {
        if (str.length() < len) {
            int cha = len - str.length();
            for (int i = 0; i < cha; i++) {
                str = "0" + str;
            }
        } else if (str.length() > len) {
            str = str.substring(0, len);
        }

        return str2Bcd(str);
    }

    /**
     * 把字符串转换成相应位数,前补0
     */
    public static String str2Len(String str, int len) {
        if (str.length() > len) {
            str = str.substring(0, len);
        }
        else if (str.length() < len) {
            str = String.format("%0" + (len - str.length()) + "d", 0) + str;
        }

        return str;
    }
}
