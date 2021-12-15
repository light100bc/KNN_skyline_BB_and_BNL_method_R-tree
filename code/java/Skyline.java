import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * return a list of skyline points
 * assume the query points is at 0,0, ONLY consider 第一象限
 */
public class Skyline {
    /**
     * BNL method
     *         1. p is dominated by a tuple within the window. In this case, p is eliminated and will not be considered
     *         in future iterations. Of course, p need not be compared to all tuples of the window in this case.
     *         2. p dominates one or more tuples in the window. In this case, these tuples are eliminated; that is, these
     *         tuples are removed from the window and will not be considered in future iterations. p is inserted into
     *         the window.
     *         3. p is incomparable with all tuples in the window. If there is enough room in the window, p is inserted
     *         into the window. Otherwise, p is written to a temporary file on disk. The tuples of the temporary file
     *         will be further processed in the next iteration of the algorithm. When the algorithm starts, the first
     *         tuple will naturally be put into the window because the window is empty
     *
     * ！！！in_memory=目前为infinity 因为控制变量，两个实验都用同意memory
     * 假设in mermory 为 1000
     * @param inputPoints ArrayList<CusPoint>
     * @return res ArrayList<CusPoint>
     * @param loop 控制是否显示运行进程,true=关闭
     */

    public static ArrayList<CusPoint> BNL_Skyline(ArrayList<CusPoint> inputPoints,boolean loop) {
        double inf = Double.POSITIVE_INFINITY;
        int memorySize= (int) inf;
        int totalPointNum=inputPoints.size();
        ArrayList<CusPoint> disk=inputPoints;
        ArrayList<CusPoint> inMemory=new ArrayList<CusPoint>();
        ArrayList<CusPoint> res = new ArrayList<CusPoint>();
        System.out.println("BNL processing...");
        //遍历取skyline直到取到足够数量或disk取/排除完
        while (!disk.isEmpty()){
            //遍历一次disk，去除点，选出(inMemory size)个点，多余可能符合条件的放回disk
            int diskSize=disk.size();
            for (int i=0;i<diskSize;i++){
                //显示还剩多少
                if(!loop) {
                int processed = totalPointNum-disk.size();
                int n = totalPointNum/10;
                if(processed%n ==0){System.out.println( "BNL: "+ processed +"is processed..."+processed/n*10+"%");}
                }
                CusPoint p=disk.remove(0);
                int flag=1;
                //遍历inMemory确定选出点符不符合条件
                for (int j = 0; j <inMemory.size(); j++) {
                    //inMemory 存在 dominant point，舍弃选出的disk point
                    if (inMemory.get(j).dominant(p)){flag=0; break;}
                    //inMemory 被dominant,就去除Inmemory 的point
                    if (p.dominant(inMemory.get(j))){inMemory.remove(j); j--;} // !!j-- 因为遍历的list长度改变，当然也可以反向遍历就不用j--
                    }
                //符合条件入memory/disk,位置不够就返回disk
                //入memory的必为skyline，返回的因为没有后续比较，从下一个遍历开始就不一定是。
                if (flag==1 & memorySize>inMemory.size()){
                    inMemory.add(p);
                }
                else if(flag==1 & memorySize<=inMemory.size()) {
                    disk.add(p);
                }
            }
            //清空inMemory读入res
            res.addAll(inMemory);
            inMemory.clear();
        }
        return res;
    }

    /**
     * BB Method
     *          Input: A Dataset D (r-tree).
     *          Output: The Set of skyline points of dataset D.
     *          1. S=∅ // list of skyline points
     *          2. insert all entries of the root D in the heap
     *          3. while heap not empty do
     *          4. remove top entry e
     *              5. if e is dominated by some point in S do discard e
     *              6. else // e is not dominated
     *                  7. if e is an intermediate entry then
     *                      8. for each child ei of e do
     *                          9. if ei is not dominated by some point in S then
     *                              10. insert ei into heap
     *                  11. else // e is a data point
     *                      12. insert ei into S
     *          13. end while
     *          !!!不需要set inMemory，because normally # retreat points << inMemory
     * @param rtree
     * @param funcName
     * @param loop 控制是否显示运行进程,true=关闭. 没用，r-tree无法显示进程
     * @return
     */
    public static ArrayList<CusPoint> BB_Skyline(RTree rtree,String funcName,boolean loop) {
        //1. S=∅ // list of skyline points
        ArrayList<CusPoint> res = new ArrayList<CusPoint>();
        System.out.println("BB processing...");
        //2.开始时把root中Node放入heap
        // 用TreeMap(默认排序): key-value存点，key: distance to origin, value: Cuspoints
        int numDim = rtree.getNumDims();
        CusPoint origin=new CusPoint(numDim); //设置原点
        TreeMap treeMap = new TreeMap();
        Node root= rtree.getRoot();
        for (int i=0;i<root.children.size();i++){
            Node child=root.children.get(i);//每个child入heap
            CusPoint recMinPoint=new CusPoint(child.coords,numDim);//算离0，0最近点 dist作为key
            treeMap.put(recMinPoint.dist(origin, funcName), child);//！！heap中存Node
        }
        //3.while heap not empty do
        while(!treeMap.isEmpty()){
            //4. remove top entry e
            Double topKey = (Double) treeMap.firstKey();
            Node topNode = (Node) treeMap.get(topKey);
            treeMap.remove(topKey);
            CusPoint recMinPoint = new CusPoint(topNode.coords, numDim);
            //判断 5.if e is dominated by some point in S
            int flag = 0;
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i).dominant(recMinPoint)) {
                    flag = 1;
                    break;
                }
            }
            //5. if e is dominated by some point in S do discard e
            if (flag == 1) {
            }
            //6. else // e is not dominated
            else if (flag == 0) {
                //7. if e is an intermediate entry then
                if (!(topNode.children.size() ==0)) {
                    //8. for each child ei of e do
                    for (int i = 0; i < topNode.children.size(); i++) {
                        Node child = topNode.children.get(i);
                        CusPoint recMinPoint2 = new CusPoint(child.coords, numDim);//算离0，0最近点 dist作为key
                        //9. if ei is not dominated by some point in S then
                        int flag2=0;
                        for (int j = 0; j < res.size(); j++) {
                            if (res.get(j).dominant(recMinPoint)) {
                                flag2 = 1;
                                break;
                            }
                        }
                        if(flag2==0) {
                            //10. insert ei into heap
                            treeMap.put(recMinPoint2.dist(origin, funcName), child);//！！heap中存Node
                        }
                    }
                }
                //11. else // e is a data point
                else if (topNode.children.size() ==0) {
                    //12. insert ei into S
                    CusPoint resPoint=new CusPoint(topNode.coords,topNode.coords.length);
                    res.add(resPoint);
                }
            }
        }
        return res ;
    }
}
