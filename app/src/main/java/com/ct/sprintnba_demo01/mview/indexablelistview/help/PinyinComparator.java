package com.ct.sprintnba_demo01.mview.indexablelistview.help;



import com.ct.sprintnba_demo01.mview.indexablelistview.IndexEntity;

import java.util.Comparator;


/**
 * Created by YoKeyword on 16/3/20.
 */
public class PinyinComparator<T extends IndexEntity> implements Comparator<T> {

    @Override
    public int compare(T lhs, T rhs) {
        return lhs.getFirstSpell().compareTo(rhs.getFirstSpell());
    }
}