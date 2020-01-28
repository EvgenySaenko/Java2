package Lesson03;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.util.*;
import java.util.stream.Collectors;

public class PhoneBook {

    private final HashMap<String, ArrayList<Person>> entries = new HashMap<>();

    public void add(String name, String phone, String mail) {
        if (entries.containsKey(name)) {//есть ли пара с таким именем в хэшмапе
            ArrayList<Person> persons = entries.get(name);//создаем список и присваеваем этому имени
            // (буквально достает всех людей с таким именем из хэшмапа)

            persons.add(new Person(phone, mail));         //добавляем в список с этим именем еще данные
        } else {
            ArrayList<Person> persons = new ArrayList<>();//если нету с таким именем в хашмапе
            persons.add(new Person(phone, mail));        //создаем новый список и добавляем данные
            entries.put(name, persons);                  //добавляем в хэшмап, имя это и список с данными которые только что создали
        }
    }


    public ArrayList<String> getPhones(String name) {
        if (!entries.containsKey(name)) return null;
        ArrayList<Person> persons = entries.get(name);//достать существующих людей из телефонной книги
        ArrayList<String> result = new ArrayList<>();//создать пустой список и переложить в него
        for (int i = 0; i < persons.size(); i++) {    //все телефоны которые мы достали кладем в result
            result.add(persons.get(i).phone);
        }
        return result;
    }

    //метод тоже самое делает что и верхник
    public ArrayList<String> getE_mail(String name) {
        if (!entries.containsKey(name)) return null;
        return entries.get(name).stream().map(person -> person.e_mail).collect(Collectors.toCollection(ArrayList::new));
        //у хэшмапа мы берем(name). превращаем его в поток,сопоставляем  из этого потока берем каждого персона и берем у него емейл
        //собираем(коллект) в коллецию с новым листом)
        //map забирает из каждого персона -свойство емайл
    }


}




