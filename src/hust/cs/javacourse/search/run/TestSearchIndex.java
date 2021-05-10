package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
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
    public static void main(String[] args){
        Sort simpleSorter = new SimpleSorter();
        String indexFile = Config.INDEX_DIR + "index.dat";
        AbstractIndexSearcher searcher = new IndexSearcher();
        searcher.open(indexFile);
        AbstractHit[] hits = searcher.search(new Term("coronavirus"), simpleSorter);
        for(AbstractHit hit : hits){
            System.out.println(hit);
        }
    }
}