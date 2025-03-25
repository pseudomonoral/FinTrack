package com.financeTracker.controller;

import com.financeTracker.dto.UserDto;
import com.financeTracker.dto.UserUpdateDto;
import com.financeTracker.mapper.DtoUserMapper;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import static com.financeTracker.model.type.UserRole.*;

@RequiredArgsConstructor
public class UserController {

    private final DtoUserMapper dtoUserMapper = Mappers.getMapper(DtoUserMapper.class);
    private final UserService userService;

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user corresponding to the given ID.
     */
    public UserDto getUserById(Long id) {
        return dtoUserMapper.toDto(userService.getUserById(id));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteUser(Long id, boolean confirmed) {
        if (confirmed) {
            userService.deleteCascadeUserById(id);
        }
    }


    public void updateUser(Long id, UserUpdateDto dto) {
        User user = userService.getUserById(id);
        user.setName(dto.getName());
        user.setLogin(dto.getEmail());
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
                .map(dtoUserMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a user and all associated (without transactions) data by their ID.
     * This operation removes the user along with related entities,
     *
     * @param id The ID of the user to delete.
     */
    public void deleteCascadeUserById(Long id) {
        userService.deleteCascadeUserById(id);
    }

    public boolean checkAdminAccess(Long id) {
        User user = userService.getUserById(id);
        return user.getUserRole().equals(ADMIN);
    }
}
