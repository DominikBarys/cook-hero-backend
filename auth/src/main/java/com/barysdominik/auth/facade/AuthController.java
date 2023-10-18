package com.barysdominik.auth.facade;

import com.barysdominik.auth.entity.http.AuthResponse;
import com.barysdominik.auth.entity.http.Code;
import com.barysdominik.auth.entity.http.ValidationMessage;
import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.entity.user.UserRegisterDTO;
import com.barysdominik.auth.exception.DuplicateMailException;
import com.barysdominik.auth.exception.DuplicateUsernameException;
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

    //write all the missing controller methods here
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
            //e.printStackTrace();
            return ResponseEntity.status(401).body(new AuthResponse(Code.INVALID_TOKEN));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse httpServletResponse) {
        return userService.login(httpServletResponse, user);
    }

    //add more exception handlers
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ValidationMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

}
