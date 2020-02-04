package Lesson05;

public class CalcThread extends Thread {
    private final float [] arr;
    private int offset;

    public CalcThread(float[] arr,int offset) {
        this.arr = arr;
        this.offset = offset;
        start();
    }

    @Override
    public void run() {
        System.out.println(getName()+" started...");
        float sum = 0;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + (i+offset) / 5) *
                    Math.cos(0.2f + (i+offset) / 5) *
                    Math.cos(0.4f + (i+offset) / 2));
            sum += arr[i];
        }
        System.out.println(getName()+" stoped..."+sum);
    }
}
