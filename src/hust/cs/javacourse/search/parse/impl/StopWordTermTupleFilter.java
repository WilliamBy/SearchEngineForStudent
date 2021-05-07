package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.StopWords;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StopWordTermTupleFilter extends AbstractTermTupleFilter {

    AbstractTermTuple termTuple;

    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public StopWordTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 获得下一个三元组
     *
     * @return 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        do {
            termTuple = input.next();
            //流中没有三元组时返回null
            if (termTuple == null) return null;
            //用二叉树搜索停用词表中是否包含三元组对应的词汇；
            //包含（即binarySearch返回值 >= 0）则跳过，继续从流中取出下一个三元组；
            //不包含则跳出循环，返回该三元组。
        } while (Arrays.binarySearch(StopWords.STOP_WORDS, termTuple.term.getContent()) >= 0);
        return termTuple;
    }

    public static void main(String[] args) {
        String s = null;
        do {
            s = "the";

        } while (s.equals("the"));

    }
}
