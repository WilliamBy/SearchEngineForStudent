package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

public class TermTupleScanner extends AbstractTermTupleScanner {

    String line;    //文档中的一行
    Iterator<String> words; //待读单词队列（对应文章中的一行）
    static StringSplitter stringSplitter = new StringSplitter();   //分词
    int pos = -1;    //记录当前单词位置

    static {
        stringSplitter.setSplitRegex(Config.STRING_SPLITTER_REGEX); //设置单词分词正则表达式
    }
    /**
     * 缺省构造函数
     */
    public TermTupleScanner() {
    }

    /**
     * 构造函数
     *
     * @param input ：指定输入流对象，应该关联到一个文本文件
     */
    public TermTupleScanner(BufferedReader input) {
        super(input);
    }

    /**
     * 获得下一个三元组
     *
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        if (words == null || !words.hasNext()) {
            try {
                line = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) return null;
            words = stringSplitter.splitByRegex(line).iterator();
        }
        TermTuple termTuple = new TermTuple();
        termTuple.term = new Term(words.next());
        termTuple.curPos = ++pos;
        return termTuple;
    }
}
