package Lesson05;

public class ThreadRoad{

    private static final int size = 10000000;
    private static final int h = size / 2;

    private static float[] arr = new float[size];//создали размер массива 10 млн//создали размер массива 10 млн
    private static float[] one = new float[h];  //создадим два массива по 5 млн
    private static float[] two = new float[h];  //создадим два массива по 5 млн

    //метод обработки основным одним потоком
    private static void oneThread(){
        System.out.println("Началась обработка - 1 потоком");
        float x = 0;
        for (int i = 0; i <arr.length ; i++) {//заполнили массив единицами
            arr[i] = 1;
        }
        long startTime = System.currentTimeMillis();//стартанули время
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            x = arr[i];
        }
        long stopTime = System.currentTimeMillis();
        float intervalTime = (stopTime - startTime)/1000f;
        System.out.println("Время обработки одним потоком: "+ intervalTime+" секунд(милисекунд)"+x);
    }

    //метод обработки двумя потоками
    private static void twoThread(){
        for (int i = 0; i <arr.length ; i++) {//заполнили массив единицами
            arr[i] = 1;
        }
        long startTime = System.currentTimeMillis();//стартанули время

        System.arraycopy(arr, 0, one, 0, h);  //скопируем содержимое первой половины массива arr в массив one
        System.arraycopy(arr, h, two, 0, h);  //скопируем содержимое второй половины массива arr в массив two
        System.out.println("Разделили массивы - Потоки начали вычисление");

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                calculationThread(one);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                calculationThread(two);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Потоки закончили вычисление");
        //запоняем обратно массив уже посчитаный в потоках(собираем его)
        System.arraycopy(one, 0, arr, 0, h);
        System.arraycopy(two, 0, arr, h, h);


        long stopTime = System.currentTimeMillis();
        float intervalTime = (stopTime - startTime)/1000f;
        System.out.println("Время обработки двумя потоками: "+ intervalTime+" секунд(милисекунд)");
    }

    //метод который получает массив и считает eго
    private static synchronized void calculationThread(float [] x){
        //получает на вход массив и считает его
        for (int i = 0; i < x.length; i++) {
            x[i] = (float)(x[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("закончил");
    }
    public static void main(String[] args) {
        oneThread();//этот метод выполняет мейн поток

        twoThread();//запустим метод обработки двумя потоками
    }
}
