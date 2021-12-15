import java.util.ArrayList;

public class Build_Index {
    /**
     *
     * @param inputPoints
     * @param minEntries
     * @param maxEntries
     * @param loop 控制显示process，,true=关闭
     * @return
     */
    public static RTree RTree_Index (ArrayList<CusPoint> inputPoints,int minEntries,int maxEntries,boolean loop){
            int numDim=inputPoints.get(0).getDimSize();
            System.out.println("building Rtree...");
            RTree rtree=new RTree(maxEntries,minEntries,numDim);
            for(int i=0;i<inputPoints.size();i++){
                CusPoint p=inputPoints.get(i);
                float[] dim=p.getDim();
                rtree.insert(dim,p);
                int n = inputPoints.size()/10;
                if(i%n ==0&loop==false){System.out.println( i +" of " +inputPoints.size() +" is processed..."+ i/n*10 +"%");}
            }
        return rtree;
    }
}
