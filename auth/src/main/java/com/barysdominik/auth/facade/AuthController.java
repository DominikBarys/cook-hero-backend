package com.barysdominik.auth.facade;

import com.barysdominik.auth.entity.http.AuthResponse;
import com.barysdominik.auth.entity.http.Code;
import com.barysdominik.auth.entity.http.ValidationMessage;
import com.barysdominik.auth.entity.user.ChangePasswordDTO;
import com.barysdominik.auth.entity.user.ResetPasswordDTO;
import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.entity.user.UserRegisterDTO;
import com.barysdominik.auth.exception.DuplicateMailException;
import com.barysdominik.auth.exception.DuplicateUsernameException;
import com.barysdominik.auth.exception.UserDontExistException;
import com.barysdominik.auth.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try{
            userService.register(userRegisterDTO);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (DuplicateUsernameException usernameException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.DUPLICATE_USERNAME));
        } catch (DuplicateMailException mailException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.DUPLICATE_EMAIL));
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<AuthResponse> activate(@RequestParam String uuid) {
        try {
            userService.activate(uuid);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDontExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.USER_DO_NOT_EXISTS_OR_ACCOUNT_NOT_ACTIVATED)
            );
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        try {
            userService.validateToken(httpServletRequest, httpServletResponse);
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));
            //illegal odpowiada za to ze jezeli token bedzie nullem to zostanie on rzucony
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.INVALID_TOKEN));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse httpServletResponse) {
        return userService.login(httpServletResponse, user);
    }

    @GetMapping("/auto-login")
    public ResponseEntity<?> autoLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return userService.loginViaToken(httpServletRequest, httpServletResponse);
    }

    @GetMapping("/logged-in")
    public ResponseEntity<?> loggedIn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return userService.isLoggedIn(httpServletRequest, httpServletResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try{
            userService.passwordRecovery(resetPasswordDTO.getEmail());
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        }catch (UserDontExistException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.USER_DO_NOT_EXISTS_OR_ACCOUNT_NOT_ACTIVATED)
            );
        }
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<AuthResponse> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            userService.resetPassword(changePasswordDTO);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDontExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.USER_DO_NOT_EXISTS_OR_ACCOUNT_NOT_ACTIVATED)
            );
        }
    }

    //add more exception handlers
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ValidationMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

}