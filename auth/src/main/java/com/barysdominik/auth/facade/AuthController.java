package com.barysdominik.auth.facade;

import com.barysdominik.auth.entity.http.AuthResponse;
import com.barysdominik.auth.entity.http.Code;
import com.barysdominik.auth.entity.http.ValidationMessage;
import com.barysdominik.auth.entity.user.*;
import com.barysdominik.auth.exception.*;
import com.barysdominik.auth.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @GetMapping("/get-user")
    public ResponseEntity<?> getUser(HttpServletRequest httpServletRequest) {
        try {
            return userService.getUser(httpServletRequest);
        } catch (UserDontExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.USER_DO_NOT_EXISTS_OR_ACCOUNT_NOT_ACTIVATED)
            );
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(HttpServletRequest httpServletRequest) {
        try {
            return userService.getAllUsers(httpServletRequest);
        } catch (UserDontExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.USER_DO_NOT_EXISTS_OR_ACCOUNT_NOT_ACTIVATED)
            );
        }
    }

    @GetMapping("/activate") //
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

    @GetMapping("/auto-login") //
    public ResponseEntity<?> autoLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return userService.autoLogin(httpServletRequest, httpServletResponse);
    }

    @GetMapping("/logged-in") //
    public ResponseEntity<?> isLoggedIn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return userService.isLoggedIn(httpServletRequest, httpServletResponse);
    }

    @GetMapping("/logout") //
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

    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse httpServletResponse) {
        Thread.sleep(2000);
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

    @PatchMapping("/reset-password-no-email")
    public ResponseEntity<AuthResponse> resetPasswordNoEmail(
            HttpServletRequest httpServletRequest,
            @RequestBody String newPassword
    ) {
        try {
            userService.resetPasswordNoEmail(httpServletRequest, newPassword);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDontExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.USER_DO_NOT_EXISTS_OR_ACCOUNT_NOT_ACTIVATED)
            );
        }
    }

    @PatchMapping("/change-role")
    public ResponseEntity<?> changeRole(
            HttpServletRequest httpServletRequest,
            @RequestParam String uuid,
            @RequestParam String role
    ) {
        try {
            userService.changeRole(httpServletRequest, uuid, role);
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
        } catch (CannotAuthorizeByTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(Code.INVALID_TOKEN)
            );
        }
    }

    @GetMapping("/authorize")
    public ResponseEntity<AuthResponse> authorize(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        try {
            userService.validateToken(httpServletRequest, httpServletResponse);
            userService.authorize(httpServletRequest);
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));
        }catch (IllegalArgumentException | ExpiredJwtException e){
            log.info("Token is not correct");
            return ResponseEntity.status(401).body(new AuthResponse(Code.INVALID_TOKEN));
        }catch (NoPermissionsException e1){
            log.info("User dont exist");
            return ResponseEntity.status(401).body(new AuthResponse(Code.NO_PERMISSION));
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ValidationMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}