package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.index.impl.Posting;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.util.*;

public class IndexSearcher extends AbstractIndexSearcher {

    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) {
        index.load(new File(indexFile));
    }

    /**
     * 根据单个检索词进行搜索
     *
     * @param queryTerm ：检索词
     * @param sorter    ：排序器
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList postingList = index.search(queryTerm);
        AbstractHit[] hits = new AbstractHit[postingList.size()];
        for (int i = 0; i < hits.length; i++) {
            AbstractPosting posting = postingList.get(i);
            Map<AbstractTerm, AbstractPosting> termPostingMap = new TreeMap<>();
            termPostingMap.put(queryTerm, posting);
            hits[i] = new Hit(posting.getDocId(),
                    index.getDocName(posting.getDocId()),
                    termPostingMap);
        }
        sorter.sort(Arrays.asList(hits));
        return hits;
    }

    /**
     * 根据二个检索词进行搜索
     *
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter     ：    排序器
     * @param combine    ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        //命中列表
        ArrayList<AbstractHit> hitList = new ArrayList<>();
        AbstractPostingList postingList1 = index.search(queryTerm1);
        AbstractPostingList postingList2 = index.search(queryTerm2);
        //存放两postingList中的posting元素
        AbstractPosting posting1;
        AbstractPosting posting2;
        //存放term到posting的映射
        Map<AbstractTerm, AbstractPosting> termPostingMap = new TreeMap<>();
        //两postingList的指针
        int i = 0, j = 0;
        while (i < postingList1.size() && j < postingList2.size()) {
            posting1 = postingList1.get(i);
            posting2 = postingList2.get(j);
            if (posting1.getDocId() == posting2.getDocId()) {
                termPostingMap.clear();
                termPostingMap.put(queryTerm1, posting1);
                termPostingMap.put(queryTerm2, posting2);
                hitList.add(new Hit(posting1.getDocId(),
                        index.getDocName(posting1.getDocId()),
                        termPostingMap));
            } else if (posting1.getDocId() < posting2.getDocId()) {
                if (combine == LogicalCombination.OR) {
                    termPostingMap.clear();
                    termPostingMap.put(queryTerm1, posting1);
                    hitList.add(new Hit(posting1.getDocId(),
                            index.getDocName(posting1.getDocId()),
                            termPostingMap));
                }
                i++;
            } else {
                if (combine == LogicalCombination.OR) {
                    termPostingMap.clear();
                    termPostingMap.put(queryTerm2, posting2);
                    hitList.add(new Hit(posting2.getDocId(),
                            index.getDocName(posting2.getDocId()),
                            termPostingMap));
                }
                j++;
            }
        }
        //对于OR关系，还要将剩下未加入的posting全部加入hitList
        if (combine == LogicalCombination.OR) {
            while (i < postingList1.size()) {
                posting1 = postingList1.get(i);
                termPostingMap.clear();
                termPostingMap.put(queryTerm1, posting1);
                hitList.add(new Hit(posting1.getDocId(),
                        index.getDocName(posting1.getDocId()),
                        termPostingMap));
            }
            while (j < postingList2.size()) {
                posting2 = postingList2.get(i);
                termPostingMap.clear();
                termPostingMap.put(queryTerm2, posting2);
                hitList.add(new Hit(posting2.getDocId(),
                        index.getDocName(posting2.getDocId()),
                        termPostingMap));
            }
        }
        return hitList.toArray(new AbstractHit[0]);
    }
}
