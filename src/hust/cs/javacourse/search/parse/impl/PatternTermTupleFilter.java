package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模式匹配三元组过滤器：使用前需要设置文本过滤模式Regex
 */
public class PatternTermTupleFilter extends AbstractTermTupleFilter {

    private AbstractTermTuple termTuple;
    private Pattern pattern;
    private Matcher matcher;

    /**
     * 构造函数
     * @param input Filter的输入，类型为AbstractTermTupleStream
     */
    public PatternTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 设置模式
     * @param pattern 要设置的模式
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * 获得下一个三元组
     * <p>使用前要确保模式已经设置</p>
     * @return 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        do {
            termTuple = input.next();
            if (termTuple == null) {
                return null;
            }
            matcher = pattern.matcher(termTuple.term.getContent());
        } while (!matcher.matches());
        return termTuple;
    }
}
