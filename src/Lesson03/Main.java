package Lesson03;

import java.lang.reflect.Array;
import java.util.*;

public class Main {

    //Задание 1 пункт 1
    //создадим метод который создаст список уникальных объектов,
    //будет перебирать массив и класть в себя только уникальные
    public static void sample(String[] str) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (int i = 0; i < str.length; i++) {
            set.add(str[i]);
        }
        System.out.println(set);
    }
    //аналог верхнего метода
    private static TreeSet<String> getWords(String[]arr){
        return new TreeSet<>(Arrays.asList(arr));
    }

    //Задание 1 пункт 2
    //создадим метод который считает сколько раз встречается каждое слово
    public static void countWords(String[] str) {
        HashMap<String, Integer> list = new HashMap<>();
        int index = 1;
        for (int i = 0; i < str.length; i++) {//перебираем массив
            if (list.containsKey(str[i])) {  //если в списке есть такой ключ
                list.put(str[i], index + 1);   //то добавляем его в список, перезатирая по индексу с +1
            } else
                list.put(str[i], index);// в противном случае просто добавляем с индексом == 1
        }
        System.out.println(list);//отображаем список
    }

    public static void main(String[] args) {
        String str = "ночь улица фонарь аптека бессмысленный и тусклый свет живи ещё хоть четверть века всё будет так" +
                " исхода нет умрёшь начнёшь опять сначала и повторится всё как встарь ночь ледяная рябь канала " +
                "аптека улица фонарь";
        String[] block = str.split(" ");
        countWords(block);
        PhoneBook phoneBook = new PhoneBook();

        phoneBook.add("Ivanov","89221110102", "ivanov@mai.ru");
        phoneBook.add("Ivanov","89665550105", "ivan545@list.ru");
        phoneBook.add("Ivanov","89443330104", "ivan777777@lb.ru");
        phoneBook.add("petrov","89332220103", "petrov@mai.ru");
        phoneBook.add("smirnov","89554440105", "rodionov@mai.ru");

        System.out.println("email ivanov -"+ phoneBook.getE_mail("Ivanov"));
        System.out.println("email petrov -"+ phoneBook.getE_mail("petrov"));

        System.out.println("email ivanov -"+ phoneBook.getPhones("Ivanov"));
        System.out.println("email petrov -"+ phoneBook.getPhones("petrov"));



    }
}
