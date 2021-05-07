package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.util.Config;

import java.io.File;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args){
        AbstractIndex index = new IndexBuilder(new DocumentBuilder()).buildIndex(Config.DOC_DIR);
        System.out.println(index);
        index.save(new File(Config.INDEX_DIR + "/origin"));
        AbstractIndex indexCpy = new Index();
        indexCpy.load(new File(Config.INDEX_DIR + "/origin"));
        System.out.println(indexCpy);
    }
}
