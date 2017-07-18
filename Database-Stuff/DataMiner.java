import DatabaseTables.DataPool;

import java.io.File;

/**
 * Created by knukro on 6/14/17.
 */
public class DataMiner {

    private static final String outPath = "/home/knukro/OPTAs/";

    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();

        DataPool mainPool = new DataPool();
        System.out.println("Parsing Data");

        parseF24(mainPool);
        parseF7(mainPool);

        mainPool.insertMetaInDataBase(outPath + "META");

        System.out.println("Task took totally: " + secondsPassed(startTime) + "Seconds");
    }

    private static void parseF24(DataPool metaPool)
    {
        final String path = "/home/knukro/Dropbox/Prototype group 1/Data/F24_XML";
        final File[] files = getXMLs(path);

        for (File file : files) {
            try {
                String name = file.getAbsoluteFile().getName();
                name = name.substring("f24-3-2015-".length());
                name = name.substring(0, name.indexOf("-"));

                F24_Parser f24Parser = new F24_Parser(file.getAbsolutePath());
                DataPool pool = new DataPool();
                f24Parser.addData(pool);
                pool.insertInDataBase(outPath + name);

                f24Parser.addData(metaPool);

            } catch (Exception e) {
                System.out.println("[Critical Error]\t" + e);
            }
        }
    }

    private static void parseF7(DataPool metaPool)
    {
        final String path = "/home/knukro/Dropbox/Prototype group 1/Data/F7_XML";
        final File[] files = getXMLs(path);

        for (File file : files) {
            try {
                String name = file.getAbsoluteFile().getName();
                name = name.substring("srml-3-2015-f".length());
                name = name.substring(0, name.indexOf("-"));

                F7_Parser f7Parser = new F7_Parser(file.getAbsolutePath());
                DataPool pool = new DataPool();
                f7Parser.addData(pool);
                pool.insertInDataBase(outPath + name);

                f7Parser.addData(metaPool);


            } catch (Exception e) {
                System.out.println("[Critical Error]\t" + e);
            }
        }
    }

    private static File[] getXMLs(String path)
    {
        return new File(path).listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
    }

    private static double secondsPassed(long startTime)
    {
        return (double) (System.currentTimeMillis() - startTime) / 1000;
    }

}