package Lesson03;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person {
    private String phone;
    private String e_mail;

    Person(String phone, String email){
        this.phone = phone;
        this.e_mail = email;
    }


    public String getPhone() {
        return phone;
    }

    public String getE_mail() {
        return e_mail;
    }

    @Override
    public String toString() {
        return "Тел: "+this.phone+", "+"e-mail: "+this.e_mail;
    }
}
