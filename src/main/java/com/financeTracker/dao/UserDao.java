package com.financeTracker.dao;

import com.financeTracker.model.entity.User;

import java.util.Optional;

/**
 * - Поиск пользователей по логину для администрирования
 * - Возможность редактирования профиля пользователя (имя, email, пароль).
 * - Возможность удаления аккаунта.
 *   Общее:
 * - Возможность регистрации новых пользователей с уникальным email и паролем.
 * - Поиск пользователя по id
 */

public interface UserDao extends MainDao<Long, User>{

    Optional<User> findByLogin(String login);

    User update(User user);

    User delete(User user);

}
