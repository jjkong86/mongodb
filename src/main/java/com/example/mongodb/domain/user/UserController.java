package com.example.mongodb.domain.user;

import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.response.ApiCommonResponse;
import com.example.mongodb.domain.user.response.UserListResponse;
import com.example.mongodb.domain.user.response.UserResponse;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public ApiCommonResponse saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping(value = "/users")
    public UserListResponse getUserList() {
        return userService.findUserList();
    }

    @GetMapping(value = "/users/template")
    public UserListResponse getUserListWithTemplate() {
        return userService.findUserListWithTemplate();
    }

    @GetMapping(value = "/users/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        return userService.getByCollectionKey(userId);
    }

    @PutMapping(value = "/users")
    public UserResponse updateUser(@RequestBody User user) {
        return userService.updateUserByCollectionKey(user);
    }

    @GetMapping(value = "/rollback/{userId}")
    public ApiCommonResponse rollbackTest(@PathVariable Long userId, boolean isRollback) {
        return userService.rollbackTest(userId, isRollback);
    }

    @GetMapping(value = "/ttlIndex/{userId}")
    public ApiCommonResponse saveTtlIndex(@PathVariable Long userId) {
        return userService.saveTtlIndex(userId);
    }
}
