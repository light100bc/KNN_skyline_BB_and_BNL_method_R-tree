

public class CusPoint {
    private final float[] pointDims;
    private final int numDims;

    public CusPoint(float[] pointDims, int numDims) {
        this.pointDims = pointDims;
        this.numDims = numDims;
    }

    //没有dim就用原点
    public CusPoint(int numDims) {
        this.pointDims = new float[numDims];
        this.numDims = numDims;
        for (int i=0;i<numDims;i++){
            pointDims[i]=0;
        }
    }

    public int getDimSize(){
        return numDims;
    }

    public float[] getDim(){
        return pointDims;
    }

    /**
     * euclidean distance
     * @param n CusPoint
     * @param type 选择的distance计算公式
     * @return res double
     */
    public double dist(CusPoint n, String type){
        double res=0;
        if (type=="norm2") {
            for (int i = 0; i < this.numDims; i++) {
                res = res + Math.pow((this.pointDims[i] - n.pointDims[i]), 2);
            }
            res = Math.sqrt(res);
        }
        else if(type=="Func1"){
            for (int i = 0; i < this.numDims; i++) {
                res = res + Math.pow((i+1)*((this.pointDims[i] - n.pointDims[i])), 2); //dim越高越重要 sqrt((x1-x2)^2+(2*(y1-y2))^2+(3*(z1-z2))^2)
            }
            res=Math.sqrt(res);
        }

        return res;
    }

    public boolean dominant(CusPoint n){
        boolean res=true;
        for (int i = 0; i < this.numDims; i++) {
            if (n.pointDims[i]<this.pointDims[i]){
                res=false;
            }
        }
        return res;
    }
}
