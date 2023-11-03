package com.example.security.controllers;

import com.example.security.exception.ResourceNotFoundException;
import com.example.security.model.Account;
import com.example.security.model.ApiGenericResponse;
import com.example.security.model.User;
import com.example.security.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User Management", description = "User Management APIs")
@RestController
@RequestMapping("/api/user")
public class UserManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(UserManagementController.class);

    @Autowired
    UserManagementService userManagementService;

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "All existing users",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = {@Content})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public List<User> getAllUsers() {
        return this.userManagementService.getAllUsers();
    }

    @Operation(summary = "Delete a user")
    @ApiResponse(responseCode = "200", description = "User deleted successfully",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiGenericResponse.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = {@Content})
    @DeleteMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ApiGenericResponse> deleteUser(@PathVariable("username") String username) throws ResourceNotFoundException {
        this.userManagementService.deleteUser(username);
        return ResponseEntity.ok().body(new ApiGenericResponse(true, "User '" + username + "' has been deleted successfully"));
    }
}
