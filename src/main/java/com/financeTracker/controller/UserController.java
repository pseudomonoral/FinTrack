package com.financeTracker.controller;

import com.financeTracker.dto.UserDto;
import com.financeTracker.dto.UserUpdateDto;
import com.financeTracker.mapper.UserMapper;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class UserController {

    private final UserMapper dtoUserMapper = UserMapper.INSTANCE;
    private final UserService userService;

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user corresponding to the given ID.
     */
    public UserDto getUserById(Long id) {
        return dtoUserMapper.toUserDto(userService.getUserById(id));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteUser(Long id) {
        userService.deleteAccountUserById(id);
    }


    public void updateUser(Long id, UserUpdateDto dto) {
        User user = userService.getUserById(id);
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        userService.updateUser(user);
    }

    /**
     * Blocks a user by their ID.
     *
     * @param id The ID of the user to block.
     */
    public void blockedUserById(Long id) {
        userService.blockedUserById(id);
    }

    /**
     * Unblocks a user by their ID.
     *
     * @param id The ID of the user to unblock.
     */
    public void unblockedUserById(Long id) {
        userService.unblockedUserById(id);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list containing all users.
     */
    public List<UserDto> showAllUsers() {
        return userService.showAll().stream()
                .map(dtoUserMapper::toUserDto)
                .collect(Collectors.toList());
    }

}
