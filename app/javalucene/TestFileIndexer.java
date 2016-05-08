package javalucene;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Date;

public class TestFileIndexer {
    public static void foundIndex(String name) throws Exception {
            /* 指明要索引文件夹的位置,这里是C盘的source文件夹下 */
        File fileDir = new File("C:\\Users\\caoshuai\\wangyetest\\public\\source\\"+name);

            /* 这里放索引文件的位置 */
        //File indexDir = new File("c://index");
        String indexPath = "C:\\Users\\caoshuai\\wangyetest\\public\\index\\mm"+name;

        // Directory dir = FSDirectory.open(indexDir);    //v3.6.0
        Directory dir = FSDirectory.open(Paths.get(indexPath));

        //Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_3_6_0);
        Analyzer luceneAnalyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(luceneAnalyzer);
        iwc.setOpenMode(OpenMode.CREATE);
        IndexWriter indexWriter = new IndexWriter(dir,iwc);
        File[] textFiles = fileDir.listFiles();
        long startTime = new Date().getTime();

        //增加document到索引去
        for (int i = 0; i < textFiles.length; i++) {
            if (textFiles[i].isFile()
                    ) {
                System.out.println("File " + textFiles[i].getCanonicalPath()
                        + "is reading....");
                String temp = FileReaderAll(textFiles[i].getCanonicalPath(),
                        "UTF-8");
               // System.out.println(temp);
                Document document = new Document();
                //  Field FieldPath = new Field("path", textFiles[i].getPath(), Field.Store.YES, Field.Index.NO);   //v3.6.0的写法
                // Field FieldBody = new Field("body", temp, Field.Store.YES,  Field.Index.ANALYZED,  Field.TermVector.WITH_POSITIONS_OFFSETS);

                Field FieldPath = new StringField("path", textFiles[i].getPath(), Field.Store.YES);
                Field FieldBody = new TextField("body", temp, Field.Store.YES);
                document.add(FieldPath);
                document.add(FieldBody);
                indexWriter.addDocument(document);
            }
        }
        indexWriter.close();

        //测试一下索引的时间
        long endTime = new Date().getTime();
        System.out
                .println("this waste"
                        + (endTime - startTime)
                        + " to read"
                        + fileDir.getPath());
    }

    public static String FileReaderAll(String FileName, String charset)
            throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(FileName), charset));

        String line = new String();
        String temp = new String();

        while ((line = reader.readLine()) != null) {
            temp += line;
        }
        reader.close();
        return temp;
    }
}

//val fileDir: File = new File("C:\\Users\\caoshuai\\wangyetest\\public\\source\\"+name)
//
//            /* 这里放索引文件的位置 */
//        //File indexDir = new File("c://index");
//        val indexPath: String = "C:\\Users\\caoshuai\\wangyetest\\public\\index\\"+name
//
//        // Directory dir = FSDirectory.open(indexDir);    //v3.6.0
//        val dir: Directory = FSDirectory.open(Paths.get(indexPath))
//
//        //Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_3_6_0);
//        val luceneAnalyzer: Analyzer = new StandardAnalyzer
//        val iwc: IndexWriterConfig = new IndexWriterConfig(luceneAnalyzer)
//        iwc.setOpenMode(OpenMode.CREATE)
//        val indexWriter: IndexWriter = new IndexWriter(dir, iwc)
//        val textFiles: Array[File] = fileDir.listFiles
//        val startTime: Long = new Date().getTime
//
//        //增加document到索引去
//        {
//        var i: Int = 0
//        while (i < textFiles.length)
//        {
//        if (textFiles(i).isFile) {
//        System.out.println("File " + textFiles(i).getCanonicalPath + "is reading....")
//        val temp: String = FileReaderAll(textFiles(i).getCanonicalPath, "GBK")
//        System.out.println(temp)
//        val document: Document = new Document
//        val FieldPath: Field = new StringField("path", textFiles(i).getPath, Field.Store.YES)
//        val FieldBody: Field = new TextField("body", temp, Field.Store.YES)
//        document.add(FieldPath)
//        document.add(FieldBody)
//        indexWriter.addDocument(document)
//        }
//        }
//        i += 1
//
//        }
//        indexWriter.close()
//
//        //测试一下索引的时间
//        val endTime: Long = new Date().getTime
//        System.out.println("this waste" + (endTime - startTime) + " to read" + fileDir.getPath)
//        }
//
//        def FileReaderAll(FileName:String, charset:String):String= {
//        val reader = new BufferedReader(new InputStreamReader(
//        new FileInputStream(FileName), charset))
//
//
//        var temp = new String();
//        val  line= reader.readLine()
//        while (line != null) {
//        temp += line
//        }
//        reader.close()
//        return temp
//        }