package me.dev.springbookstore.users;


import jakarta.validation.Valid;
import me.dev.springbookstore.users.dto.ChangeUserRoleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public void changeRole(@PathVariable Long id, @Valid @RequestBody ChangeUserRoleRequest request) {
        log.info("Changing role of user with id {}", id);
        userService.changeRole(id, request);

    }
}
