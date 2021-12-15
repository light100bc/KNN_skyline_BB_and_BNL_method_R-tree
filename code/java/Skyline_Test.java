import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Skyline_Test {

    /**
     * 对不同skline_test的整合
     * @param readFileName
     * @param funcName “Func”，“norm2”
     * @param skylineType “BB","BNL"
     * @param numPoints 显示top n个，目前不用
     * @param loop 是否是所有data都loop一遍，是的话关闭部分输出语句
     * @param loop2 1.是否把timeUsed存入，以不同data为一个file，用append方法 & 2.是否是同一个data多次loop，不是就每次都写入result point.txt
     * @throws IOException
     */
    @Test(timeout=1000)
    public static void Skyline_test(String readFileName,String funcName,String skylineType,int numPoints,boolean loop,boolean loop2) throws IOException {
        long start = -1; //计时
        long elapsedTime=-1;
        ArrayList<CusPoint> inputPoints;
        ArrayList<CusPoint> skylineResult = null;
        inputPoints = ReadPoints.readPoints(readFileName); //read
        System.out.println("=========start a new session========");
        //=====================skyline=====================
        if (skylineType=="BNL") {
            start = System.nanoTime(); //计时
            skylineResult = Skyline.BNL_Skyline(inputPoints,loop); // 普通BNL skyline result
            //！range skyline！ 按照设定的func排序，使用Cuspoiont.dist
            System.out.println("BNL - Rank Skyline: point ranking according to the function...");
            int n = skylineResult.get(0).getDimSize();
            CusPoint origin=new CusPoint(n); //设置原点
            //用TreeMap(默认排序): key-value存点，key: distance to origin, value: Cuspoints
            TreeMap treeMap = new TreeMap();
            for (int i=0;i<skylineResult.size();i++){
                CusPoint p= skylineResult.get(i);
                treeMap.put(p.dist(origin, funcName), p);
            }
            //排序完后从treeMap导回skylineResult
            int i=0;
            for ( Object key : treeMap.keySet()) {
                skylineResult.set(i, (CusPoint) treeMap.get(key));
                //测试用显示
                //System.out.println(key + " ：" + Arrays.toString(skylineResult.get(i).getDim()));
                i++;
            }
            elapsedTime = System.nanoTime() - start;
        }
        else if (skylineType=="BB"){
            //存入Rtree
            RTree rtree = Build_Index.RTree_Index(inputPoints,5,10,loop);//2,5; 5,10 best performance
            start = System.nanoTime(); //计时,不算index时长
            //skyline读取（带function）
            skylineResult=Skyline.BB_Skyline(rtree,funcName,loop);
            elapsedTime = System.nanoTime() - start;
        }

        //=========================show result======================
        System.out.println("==============result==============");
        int numDim = skylineResult.get(0).getDimSize();
        CusPoint origin=new CusPoint(numDim); //设置原点
        ArrayList<String> saveToFile = new ArrayList<String>(); //用于save的string
        // 按现有顺序显示到原点距离与点
        for (int i=0;i<skylineResult.size();i++){
            CusPoint p= skylineResult.get(i);
            //循环时关闭这个语句，否则太多输出
            if (!loop){System.out.println(p.dist(origin, funcName) + " ：" + Arrays.toString(skylineResult.get(i).getDim()));}
            //构造用于save的string
            String s=p.dist(origin, funcName)+" ";
            for(int j=0;j<numDim;j++){
                s=s+skylineResult.get(i).getDim()[j]+" ";
            }
            saveToFile.add(s);
        }
        System.out.println("fileName: "+readFileName);
        System.out.println("Skyline Method: "+ skylineType+", Ranking function used: "+ funcName);
        System.out.println("result_length: " + skylineResult.size());
        System.out.println("time_used: " + elapsedTime + " nano seconds");

        //=========================save======================
        //save点
        String saveFileName=readFileName.substring(0,readFileName.length()-4)+"_"+skylineType+".txt";
//        ReadPoints.savePoints(saveFileName, skylineResult); //只 save 点
        File writename = new File(saveFileName); // 相对路径，如果没有则要建立一个新的output。txt文件
        if (!writename.exists()||loop2==false){ReadPoints.savePoints2(saveFileName, saveToFile);} //如果已经存在&是同一个data循环test的情况，忽略多次写入
        //save used time
        if (loop2==true) {
            saveFileName = readFileName.substring(0, readFileName.length() - 4) + "_"+skylineType+"_timeUsed.txt";
            ReadPoints.saveTime(saveFileName, elapsedTime);
        }
    }

    public static void main(String[] args) throws IOException {
        //全为range skyline
        //dim|cov|numPoint
        //2|-0.8,-0.5,0,0.5,0.8|10w,20w,30w,40w,50w,60w,70w,80w,90w,100w
        //3|-0.5(去除),-0.3,0,0.5,0.8|10w,20w,30w,40w,50w,60w,70w,80w,90w,100w
        //4|-0.3,-0.15,0,0.5,0.8|10w,20w,30w,40w,50w,60w,70w,80w,90w,100w
        //
        //遍历data中所有目录自动进行test
            String path = "data";		//要遍历的路径
            File file = new File(path);		//获取其file对象
            File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
            for(File f:fs){					//遍历File[]数组
                if(!f.isDirectory()){   //若非目录(即文件)
                    String fileName=f.toString();
                    //也非之前result，则进行test(！！！结尾不是0的都是exclude的)
                    if ((!fileName.contains("BB"))&(!fileName.contains("BNL")) &(!fileName.contains("time"))
                            &(fileName.substring(fileName.length()-5,fileName.length()-4).equals("0"))) {
                        //para设置
                        String skylineType="BNL";
                        //判断时间记录文件夹是否存在，存在就先清除 (可以关闭这个功能，反正最后用matlab取所有实验的mean.median,max,min)
                        String saveFileName = fileName.substring(0, fileName.length() - 4) + "_"+ skylineType +"_timeUsed.txt";
                        File writeName = new File(saveFileName);
                        writeName.delete();
                        System.gc();
                        for (int i = 1; i < 20; i++) {     //同一data 19次遍历防randomness
                            System.out.println();
                            System.out.println("test count: "+ i + "th of " + fileName);
                            Skyline_test(fileName, "Func1", skylineType, 1000, true, true);
                        }
                    }
                }
            }
//        Skyline_test("data/4d_-0.3cor_1000000.txt","Func1","BNL",1000,false,false);
    }
}
