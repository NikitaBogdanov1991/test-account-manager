package ru.effectivemobile.model;

import java.time.LocalDate;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class AccountStorage {
    private static Set<Account> accounts = new HashSet<>();

    static {
        accounts.add(new Account(
                UUID.randomUUID().toString(),
                "Иван",
                "Иванов",
                "ivan.ivanov@mail.ru",
                "89171237801",
                "15.01.1986",
                "ivan",
                "ivanov",
                4000)
        );
        accounts.add(new Account(
                UUID.randomUUID().toString(),
                "Петр",
                "Петров",
                "petr.petrov@gmail.com",
                "89607395018",
                "20.08.1991",
                "petr",
                "petrov",
                4000)
        );
        accounts.add(new Account(
                UUID.randomUUID().toString(),
                "Анна",
                "Степанова",
                "anna.stepanova@gmail.com",
                "89887690987",
                "26.03.1994",
                "anna",
                "stepanova",
                4000)
        );
    }

    public static Set<Account> getAccounts() {
        return accounts;
    }

}
