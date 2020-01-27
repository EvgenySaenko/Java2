package Lesson03;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.util.*;

public class PhoneBook {

    ArrayList<Person> list = new ArrayList<>();
    private Person ivanov = new Person("89221110102", "ivanov@mai.ru");
    private Person ivanov2 = new Person("89665550105", "ivan545@list.ru");
    private Person ivanov3 = new Person("89443330104", "ivan777777@lb.ru");
    private Person petrov = new Person("89332220103", "petrov@mai.ru");
    private Person smirnov = new Person("89443330104", "smirnov@mai.ru");
    private Person rodionov = new Person("89554440105", "rodionov@mai.ru");

    Map<String, Person> phonebook = new HashMap<>();


    PhoneBook() {
        this.phonebook.put("Ivanov",ivanov);
        this.phonebook.put("Petrov", petrov);
        this.phonebook.put("Smirnov", smirnov);
        this.phonebook.put("Rodionov", rodionov);
    }


    @Override
    public String toString() {
        return "PhoneBook\n" +
                "Ivanov = " + ivanov + " " + ivanov2 + " " + ivanov3 + " \n" +
                "Petrov = " + petrov + " \n" +
                "Smirnov = " + smirnov + " \n" +
                "Rodionov = " + rodionov + " \n" +
                '}';
    }

    //метод для поиска телефона по фамилии(ключу)нужно учесть однофамильцев
    public void searchPhone(String str) {
        if (phonebook.containsKey(str)) {
            System.out.println(phonebook.get(str).getPhone());//говорим, книга дай мне обьект по ключу, и вызываем у него гетер
        } else
            System.out.println("Такого человека в системе не обнаружено");
    }

    //метод для поиска е-мейла по фамилии(ключу)нужно учесть однофамильцев
    public void searcheMail(String str) {
        if (phonebook.containsKey(str)) {
            System.out.println(phonebook.get(str).getE_mail());//говорим, книга дай мне обьект по ключу, и вызываем у него гетер
        } else
            System.out.println("Такого человека в системе не обнаружено");
    }
}
