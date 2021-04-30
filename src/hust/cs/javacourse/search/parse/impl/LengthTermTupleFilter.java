package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import java.util.Arrays;

public class LengthTermTupleFilter extends AbstractTermTupleFilter {

    AbstractTermTuple termTuple;

    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public LengthTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 获得下一个三元组
     *
     * @return 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        int length; //单词长度
        do {
            termTuple = input.next();
            if (termTuple == null) {
                return null; //流中没有三元组
            }
            length = termTuple.term.getContent().length();
        } while (length > Config.TERM_FILTER_MAXLENGTH || length < Config.TERM_FILTER_MINLENGTH);
        return termTuple;
    }
}