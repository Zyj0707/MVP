package com.zyj.base.model.util;

/**
 * Created by Zyj on 2018/5/2.
 * Email: zyj0707@outlook.com
 */

public interface CharacterDelegate {

    /** * 单字解析 * * @param str * @return */
    String convert(String str);

    /** * 词组解析 * * @param chs * @return */
    String getSelling(String chs);

}
