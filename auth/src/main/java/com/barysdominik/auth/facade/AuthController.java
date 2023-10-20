package com.barysdominik.auth.facade;

import com.barysdominik.auth.entity.http.AuthResponse;
import com.barysdominik.auth.entity.http.Code;
import com.barysdominik.auth.entity.http.ValidationMessage;
import com.barysdominik.auth.entity.user.ChangePasswordDTO;
import com.barysdominik.auth.entity.user.ResetPasswordDTO;
import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.entity.user.UserRegisterDTO;
import com.barysdominik.auth.exception.*;
import com.barysdominik.auth.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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

    @GetMapping("/auto-login")
    public ResponseEntity<?> autoLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return userService.autoLogin(httpServletRequest, httpServletResponse);
    }

    @GetMapping("/logged-in")
    public ResponseEntity<?> isLoggedIn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return userService.isLoggedIn(httpServletRequest, httpServletResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return userService.logout(httpServletRequest, httpServletResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            userService.register(userRegisterDTO);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (DuplicateUsernameException usernameException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.DUPLICATE_USERNAME));
        } catch (DuplicateMailException mailException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.DUPLICATE_EMAIL));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse httpServletResponse) {
        return userService.login(httpServletResponse, user);
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

    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            userService.passwordRecovery(resetPasswordDTO.getEmail());
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDontExistException e) {
            log.error("User with email: '" + resetPasswordDTO.getEmail() + "' does not exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.USER_DO_NOT_EXISTS_OR_ACCOUNT_NOT_ACTIVATED)
            );
        }
    }

    @PatchMapping("/change-username")
    public ResponseEntity<?> changeUsername(HttpServletRequest httpServletRequest, @RequestParam String newUsername) {
        try {
            userService.changeUsername(httpServletRequest, newUsername);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (CannotAuthorizeByTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.INVALID_TOKEN)
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

    @PatchMapping("/change-role")
    public ResponseEntity<?> changeRole(HttpServletRequest httpServletRequest, @RequestParam String role) {
        try {
            userService.changeRole(httpServletRequest, role);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (CannotAuthorizeByTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.INVALID_TOKEN)
            );
        } catch (InvalidParamException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.INVALID_PARAMETERS)
            );
        }
    }

    @PatchMapping("/change-rank")
    public ResponseEntity<?> changeRank(HttpServletRequest httpServletRequest, @RequestParam String rank) {
        try {
            userService.changeRank(httpServletRequest, rank);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (CannotAuthorizeByTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.INVALID_TOKEN)
            );
        } catch (InvalidParamException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.INVALID_PARAMETERS)
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestParam String uuid) {
        try {
            userService.deleteUser(httpServletRequest, httpServletResponse, uuid);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDontExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.USER_DO_NOT_EXISTS_OR_ACCOUNT_NOT_ACTIVATED)
            );
        } catch (NoPermissionsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.NO_PERMISSION)
            );
        } catch (CannotAuthorizeByTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.INVALID_TOKEN)
            );
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ValidationMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}