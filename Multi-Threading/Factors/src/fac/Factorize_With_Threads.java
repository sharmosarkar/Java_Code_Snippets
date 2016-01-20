package fac;


import java.math.BigInteger;
import java.util.ArrayList;

public class Factorize_With_Threads {
    public static BigInteger number = BigMath.ZERO;
    public static BigInteger stopPoint = BigMath.ZERO;
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.out.println("Usage: java Factor using Threads");
        }
        BigInteger nn = new BigInteger(args[0]);//32281802098926944263
        ArrayList<BigInteger> factors = factorize(nn);
        System.out.println("Factors:");
        for (BigInteger xx : factors) {
            System.out.println(xx);
        }
    }
     
    static ArrayList<BigInteger> factorize(BigInteger nn) throws InterruptedException {
        BigInteger THREAD_COUNT = BigMath.TWO;
        BigInteger rn = BigMath.sqrt(nn);
        Factorize_With_Threads f = new Factorize_With_Threads();
        MyThread t;
        ArrayList<MyThread> threadList = new  ArrayList<MyThread>(); 
        System.out.println("Input: " + nn);
        System.out.println("Sqrt: "  + rn);
        ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
        while (nn.mod(BigMath.TWO).equals(BigMath.ZERO)) {
            factors.add(BigMath.TWO);
            nn = nn.divide(BigMath.TWO);
            number = nn;
            rn = BigMath.sqrt(nn);}
        BigInteger counter ;
        BigInteger start = BigMath.ZERO;
        BigInteger end = BigMath.ZERO;
        for (counter = BigMath.ZERO; counter.compareTo(THREAD_COUNT) < 0 ; counter =counter.add(new BigInteger("1")) ){
            if(counter.compareTo(BigMath.ZERO)!= 0){
                start = end.add(new BigInteger("1"));
                end = start.add(rn.divide(THREAD_COUNT));
                if (end.mod(BigMath.TWO).equals(BigMath.ZERO)) 
                    end = end.add(new BigInteger("1"));
            }
            else{
                start = new BigInteger("3");
                end = start.add(rn.divide(THREAD_COUNT));
            }
            t = new MyThread(nn,start, end, f);
            t.start();
            threadList.add(t);
            }
      
        int i=0;
        for(i =0; i<threadList.size() ;i++){
            threadList.get(i).join();}
        for(i =0; i<threadList.size() ;i++){
            factors.addAll(threadList.get(i).tempFactors);}
        factors.add(number);
        return factors;
    }
    
    public void setNN (BigInteger num){
        number = num;}
    public  void setEnd (BigInteger end){
        stopPoint = end;}
    public BigInteger getEnd (){
        return stopPoint;}
    public BigInteger getNN(){
        return number;}
}

class BigMath {
    public final static BigInteger TWO  = new BigInteger("2");
    public final static BigInteger ZERO = new BigInteger("0");

    static BigInteger sqrt(BigInteger nn) {
        return sqrtSearch(nn, TWO, nn);
    }

    static BigInteger sqrtSearch(BigInteger nn, BigInteger lo, BigInteger hi) {
        BigInteger xx = lo.add(hi).divide(TWO);

        if (xx.equals(lo) || xx.equals(hi)) {
            return xx;
        }
        
        BigInteger dy = nn.subtract(xx.multiply(xx));
        if (dy.compareTo(ZERO) < 0) {
            return sqrtSearch(nn, lo, xx);
        }
        else {
            return sqrtSearch(nn, xx, hi);
        }
    }
}

class MyThread extends Thread {
    BigInteger start , end , nn;
    ArrayList<BigInteger> tempFactors = new ArrayList<BigInteger>();
    Factorize_With_Threads object = new Factorize_With_Threads();
    MyThread(BigInteger n, BigInteger begn, BigInteger stop, Factorize_With_Threads f) {
        this.start = begn;
        this.end = stop;
        this.nn = n;
        this.object = f;}

    @Override
    public void run() {
        BigInteger ii = start,rn; 
        if (object.getNN().compareTo(BigMath.ZERO) != 0 && object.getNN().compareTo(nn) == -1)
                    nn = object.getNN(); 
        if (object.getEnd().compareTo(end) == -1 && object.getEnd().compareTo(BigMath.ZERO) != 0)
                    end = object.getEnd(); 
        while (ii.compareTo(end) <= 0) {
            if (nn.mod(ii).equals(BigMath.ZERO)) {
                tempFactors.add(ii);
                nn = nn.divide(ii);
                end = BigMath.sqrt(nn);
                rn = BigMath.sqrt(nn);
                synchronized(object){
                object.setEnd(rn);
                object.setNN(nn);}
            }
            else { ii = ii.add(BigMath.TWO);
                if (object.getEnd().compareTo(end) == -1 && object.getEnd().compareTo(BigMath.ZERO) != 0 )
                    end = object.getEnd(); 
            }
        }
    }
}