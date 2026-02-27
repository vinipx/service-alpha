package com.ecosystem.alpha.controller;

import com.ecosystem.alpha.service.UserService;
import com.ecosystem.common.dto.ApiResponse;
import com.ecosystem.common.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(userService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> create(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(userService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> update(@PathVariable Long id,
                                                        @Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(userService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
