package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.io.File;
import java.util.Arrays;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     * 搜索程序入口
     *
     * @param args ：命令行参数
     */
    public static void main(String[] args) {
        AbstractIndexSearcher searcher = new IndexSearcher();
        searcher.open(Config.INDEX_DIR + "/index");
        Term termA = new Term("aaa");
        Term termB = new Term("bbb");
        Term termF = new Term("fff");
        Term termThe = new Term("the");
        SimpleSorter simpleSorter = new SimpleSorter();
        AbstractHit[] hits = null;
//        hits = searcher.search(termB, simpleSorter);
//        hits = searcher.search(termA, termB, simpleSorter, AbstractIndexSearcher.LogicalCombination.AND);
        for (AbstractHit hit : hits) {
            System.out.println("--------------------------------------\n");
            System.out.println(hit);
        }
    }
}
