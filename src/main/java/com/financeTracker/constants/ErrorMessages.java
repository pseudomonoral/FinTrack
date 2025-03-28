package com.financeTracker.constants;

public interface ErrorMessages {
    String USER_NOT_FOUND = "Пользователь с таким логином не найден.";
    String USER_BLOCKED = "Ваш аккаунт заблокирован.";
    String INVALID_PASSWORD = "Неверный пароль.";
    String USER_ALREADY_EXISTS = "Пользователь с таким логином уже существует.";
    String EMPTY_CREDENTIALS = "Логин и пароль не должны быть пустыми.";
    String WEAK_PASSWORD = "Длина пароля должна составлять не менее 5 символов.";
}

