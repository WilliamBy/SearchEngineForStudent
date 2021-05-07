package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;
import java.util.*;

public class Index extends AbstractIndex {
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        StringBuffer strBuff = new StringBuffer("* INDEX ******************\n");
        strBuff.append("\n- DocId To DocPath Mapping -------------------\n");
        for (Map.Entry<Integer, String> entry : docIdToDocPathMapping.entrySet()) {
            strBuff.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        strBuff.append("----------------------------------------------\n");
        strBuff.append("\n- Term To PostingList Mapping ----------------\n");
        for (Map.Entry<AbstractTerm, AbstractPostingList> entry : termToPostingListMapping.entrySet()) {
            strBuff.append("\n# ").append(entry.getKey()).append("\n").append(entry.getValue()).append("\n");
        }
        strBuff.append("----------------------------------------------\n");
        strBuff.append("**********************************************\n");
        return strBuff.toString().trim();
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        List<AbstractTermTuple> tupleList = document.getTuples();
        for (AbstractTermTuple tuple : tupleList) {
            //对文档中每个三元组的term，查询其在index中对应的postingList，并新建与三元组对应的posting；
            //如果无对应postingList，则先新建一个term-postingList对，然后再包含该posting；
            //如果有对应的postingList，则直接将新建的posting加入到对应的postingList中去。
            AbstractPostingList postingList = termToPostingListMapping.get(tuple.term);
            if (postingList == null) {
                postingList = new PostingList();
                termToPostingListMapping.put(tuple.term, postingList);
            }
            AbstractPosting posting = new Posting();
            posting.setDocId(document.getDocId());
            posting.setFreq(tuple.freq);
            posting.getPositions().add(tuple.curPos);
            postingList.add(posting);
        }
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        try {
            readObject(new ObjectInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try {
            writeObject(new ObjectOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return termToPostingListMapping.get(term);
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return termToPostingListMapping.keySet();
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        for (Map.Entry<AbstractTerm, AbstractPostingList> next : termToPostingListMapping.entrySet()) {
            AbstractPostingList list = next.getValue();
            list.sort();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).sort();
            }
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            Set<Map.Entry<AbstractTerm, AbstractPostingList>> t2pEntrySet = termToPostingListMapping.entrySet();
            out.writeObject(t2pEntrySet.size());
            for (Map.Entry<AbstractTerm, AbstractPostingList> entry : t2pEntrySet) {
                entry.getKey().writeObject(out);
                entry.getValue().writeObject(out);
            }
            Set<Map.Entry<Integer, String>> i2pEntrySet = docIdToDocPathMapping.entrySet();
            out.writeObject(i2pEntrySet.size());
            for (Map.Entry<Integer, String> entry : i2pEntrySet) {
                out.writeInt(entry.getKey());
                out.writeObject(entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            termToPostingListMapping.clear();
            int sizeOft2p = (int) in.readObject();
            for (int i = 0; i < sizeOft2p; i++) {
                AbstractTerm term = new Term();
                AbstractPostingList postingList = new PostingList();
                term.readObject(in);
                postingList.readObject(in);
                termToPostingListMapping.put(term, postingList);
            }
            docIdToDocPathMapping.clear();
            int sizeOfi2p = (int) in.readObject();
            for (int i = 0; i < sizeOfi2p; i++) {
                docIdToDocPathMapping.put(in.readInt(), (String) in.readObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Deserialization failed: class Index not found!\n");
        }
    }
}
