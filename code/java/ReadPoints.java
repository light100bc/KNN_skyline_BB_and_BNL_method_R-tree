import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
*read points from file
*/

public class ReadPoints {
    /**
     *
     * @param fileName
     * @return res ArrayList<CusPoint>
     * @throws FileNotFoundException
     */
    public static ArrayList<CusPoint> readPoints(String fileName) throws FileNotFoundException {
        ArrayList<CusPoint> res = new ArrayList<CusPoint>();
        CusPoint point;
        float[] pointDims;
        int numDims;
        String[] s;
        File file = new File(fileName);

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            s= sc.nextLine().toString().split("\t");
            numDims=s.length;
            pointDims = new float[numDims];
            for (int i=0;i<numDims;i++) {
                pointDims[i]=Float.valueOf(s[i]);
            }
            point=new CusPoint(pointDims,numDims);
            res.add(point);
//            System.out.println(sc.nextLine());
            //res.add(point);
        }
        return res;
    }

    /**
     * 只save点
     * @param fileName
     * @param points
     * @throws IOException
     */
    public static void savePoints(String fileName,ArrayList<CusPoint> points) throws IOException {
        File writename = new File(fileName); // 相对路径，如果没有则要建立一个新的output。txt文件
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        for(int i=0; points.size() > i; i++)
        {
                float[] s = points.get(i).getDim(); //s存这个CusPoint的dim
                for (int j = 0; j < s.length; j++) {
                    out.write(s[j]+" "); // \r\n即为换行
                    out.flush(); // 把缓存区内容压入文件
                }
            out.write("\r\n"); // \r\n即为换行
            out.flush();
        }
        out.close();
    }

    /**
     * save点和distance
     * @param fileName
     * @param points
     * @throws IOException
     */
    public static void savePoints2(String fileName,ArrayList<String> points) throws IOException {
        File writename = new File(fileName); // 相对路径，如果没有则要建立一个新的output。txt文件
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        for(int i=0; points.size() > i; i++)
        {
            out.write(points.get(i) +"\r\n"); // \r\n即为换行
            out.flush();
        }
        out.close();
    }

    /**
     * save使用时间，！！！append方法
     * @param fileName
     * @param time
     * @throws IOException
     */
    public static void saveTime(String fileName,Long time) throws IOException {
        File writename = new File(fileName); // 相对路径，如果没有则要建立一个新的output。txt文件
        if(!writename.exists()){writename.createNewFile();} // 不存在!!!才创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename,true));
        out.write(time +"\r\n"); // \r\n即为换行
        out.flush();
        out.close();
    }
}
