package com.barysdominik.auth.service;

import com.barysdominik.auth.entity.http.AuthResponse;
import com.barysdominik.auth.entity.http.Code;
import com.barysdominik.auth.entity.http.LoginResponse;
import com.barysdominik.auth.entity.user.*;
import com.barysdominik.auth.exception.*;
import com.barysdominik.auth.repository.UserRepository;
import com.barysdominik.auth.repository.UserResetPasswordRepository;
import com.barysdominik.auth.repository.dao.UserDeleteOperationsDAO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    private final EmailService emailService;
    private final UserResetPasswordService userResetPasswordService;
    private final UserResetPasswordRepository userResetPasswordRepository;
    private final UserDeleteOperationsDAO userDeleteOperationsDAO;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh}")
    private int refreshExp;

    public ResponseEntity<UserDTO> getUser(HttpServletRequest request) {
        User user = getUserByRefreshToken(request);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUuid(user.getUuid());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole().toString());
            userDTO.setRank(user.getRank().toString());
            userDTO.setJoinedAt(user.getJoinedAt());
            userDTO.setAmountOfCreatedTutorials(user.getAmountOfCreatedTutorials());
            return ResponseEntity.ok(userDTO);
        }
        throw new UserDontExistException();
    }

    @Transactional
    public void activate(String uuid) throws UserDontExistException {
        User user = userRepository.findUserByUuid(uuid).orElse(null);
        if (user != null) {
            user.setLock(false);
            user.setEnabled(true);
            userRepository.save(user);
            log.info("User with uuid: '" + uuid + "' activated successfully");
            return;
        }
        log.error("User with uuid: '" + uuid + "' does not exist");
        throw new UserDontExistException("User with uuid: '" + uuid + "' does not exist");
    }

    public ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            User user = getUserByRefreshToken(request);
            if (user != null) {
                log.info("User with username: '" + user.getUsername() + "' was auto logged successfully via token");
                return ResponseEntity.ok(
                        UserRegisterDTO
                                .builder()
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .rank(user.getRank())
                                .build()
                );
            }
            log.error("User with does not exist, is not enabled or is locked");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.LOGIN_FAILED));
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            log.error("User tokens are null or expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.INVALID_TOKEN));
        }
    }

    public User getUserByRefreshToken(HttpServletRequest request) {
        String refresh = null;
        for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
            if (value.getName().equals("refresh")) {
                refresh = value.getValue();
            }
        }
        String username = jwtService.getSubject(refresh);
        return userRepository.findNonLockedAndEnabledUserByUsername(username).orElse(null);
    }

    public ResponseEntity<?> isLoggedIn(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            log.info("User is logged in");
            return ResponseEntity.ok(new LoginResponse(true, Code.SUCCESS));
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            log.error("User is not logged in");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse(false, Code.LOGIN_FAILED));
        }
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = cookieService.removeCookie(Arrays.stream(request.getCookies()).toList(), "token");
        if (cookie != null) {
            response.addCookie(cookie);
        }
        cookie = cookieService.removeCookie(Arrays.stream(request.getCookies()).toList(), "refresh");
        if (cookie != null) {
            response.addCookie(cookie);
        }
        log.info("Successfully logged out");
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

    @Transactional
    public void register(UserRegisterDTO userRegisterDTO) throws DuplicateUsernameException, DuplicateMailException {
        userRepository.findUserByUsername(userRegisterDTO.getUsername()).ifPresent(value -> {
            log.error("Username '" + userRegisterDTO.getUsername() +"' already exists");
            throw new DuplicateUsernameException("Użytkownik o takiej nazwie już istnieje");
        });
        userRepository.findUserByEmail(userRegisterDTO.getEmail()).ifPresent(value -> {
            log.error("Email '" + userRegisterDTO.getEmail() +"' already exists");
            throw new DuplicateMailException("Użytkownik o takim mailu już istnieje");
        });

        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(userRegisterDTO.getPassword());
        user.setEmail(userRegisterDTO.getEmail());
        user.setJoinedAt(LocalDate.now());
        user.setRole(Role.USER);
        user.setRank(Rank.ROOKIE);
        user.setLock(true);
        user.setEnabled(false);
        saveUser(user);

        emailService.sendAccountActivationMail(user);
    }

    protected User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
        User user = userRepository.findNonLockedAndEnabledUserByUsername(authRequest.getUsername()).orElse(null);
        if (user != null) {
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
                );
            } catch (AuthenticationException e) {
                log.error("Login or password are incorrect");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.AUTHENTICATION_ERROR));
            }

            if (authentication.isAuthenticated()) {
                Cookie refresh = cookieService.generateCookie(
                        "refresh",
                        generateToken(authRequest.getUsername(), refreshExp),
                        refreshExp
                );
                Cookie token = cookieService.generateCookie(
                        "token",
                        generateToken(authRequest.getUsername(), exp),
                        exp);
                response.addCookie(refresh);
                response.addCookie(token);
                log.info("User with username: '" + user.getUsername() + "' has been logged in successfully");
                return ResponseEntity.ok(
                        UserRegisterDTO
                                .builder()
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .rank(user.getRank())
                                .build()
                );
            } else {
                log.error("User with username: '" + user.getUsername() + "' is not authenticated");
                return ResponseEntity.ok(new AuthResponse(Code.LOGIN_FAILED));
            }
        }
        log.error("User does not exist, is not enabled or is locked");
        return ResponseEntity.ok(new AuthResponse(Code.USER_NOT_FOUND));
    }

    public String generateToken(String username, int exp) {
        return jwtService.generateToken(username, exp);
    }

    public void validateToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ExpiredJwtException, IllegalArgumentException {
        String token = null;
        String refresh = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : Arrays.stream(request.getCookies()).toList()) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                } else if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        } else {
            throw new IllegalArgumentException("Token is null");
        }

        try {
            jwtService.validateToken(token);
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            //tutaj sie trafia zawsze, prowadzi tu gateway, wiec odswiezanie wystarczy tylko tu, ewentualnie jeszcze w authorize
            log.warn("Token has expired, trying to validate via refresh token");
            jwtService.validateToken(refresh);

            Cookie authorizationToken = cookieService.generateCookie(
                    "token",
                    jwtService.refreshToken(refresh, exp),
                    exp
            );
            Cookie refreshToken = cookieService.generateCookie(
                    "refresh",
                    jwtService.refreshToken(refresh, refreshExp),
                    refreshExp
            );

            response.addCookie(authorizationToken);
            response.addCookie(refreshToken);
            log.info("Token validation passed successfully and tokens have been refreshed");
        }
    }

    public void passwordRecovery(String email) throws UserDontExistException {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user != null) {
            UserResetPassword userResetPassword = userResetPasswordService.startResettingPassword(user);
            emailService.sendPasswordRecoveryMail(user, userResetPassword.getUuid());
            return;
        }
        log.error("User with email: '" + email + "' does not exist");
        throw new UserDontExistException("User with email: '" + email + "' does not exist");
    }

    @Transactional
    public void resetPassword(ChangePasswordDTO changePasswordDTO) throws UserDontExistException {
        UserResetPassword userResetPassword = userResetPasswordRepository
                .findByUuid(changePasswordDTO.getUuid())
                .orElse(null);
        if (userResetPassword != null) {
            User user = userRepository.findUserByUuid(userResetPassword.getUser().getUuid()).orElse(null);
            if (user != null) {
                user.setPassword(changePasswordDTO.getPassword());
                saveUser(user);
                userResetPasswordService.endResettingPassword(userResetPassword.getUuid());
                log.info("Password of user: '" + user.getUsername() + "' have been resetted successfully");
                return;
            }
        }
        log.error("User with uuid: '" + changePasswordDTO.getUuid() +
                "' does not exist or reset password uuid has expired");
        throw new UserDontExistException("User with uuid: '" + changePasswordDTO.getUuid() + "' does not exist");
    }

    @Transactional
    public void changeUsername(HttpServletRequest request, String newUsername) {
        User user;
        try{
            user = getUserByRefreshToken(request);
        } catch (Exception e) {
            throw new CannotAuthorizeByTokenException("Tokens are null or expired");
        }
        user.setUsername(newUsername);
        userRepository.save(user);
    }

    @Transactional
    public void changeRole(HttpServletRequest request, String uuid, String role) {
        User user;
        try{
            user = getUserByRefreshToken(request);
        } catch (Exception e) {
            throw new CannotAuthorizeByTokenException("Tokens are null or expired");
        }

        User userToChangeRole = userRepository.findUserByUuid(uuid).orElse(null);
        if(userToChangeRole != null) {
            List<Role> roles = Arrays.stream(Role.values()).toList();

            if(roles.stream().anyMatch(name -> name.toString().equals(role))) {
                userToChangeRole.setRole(Role.valueOf(role));
                userRepository.save(userToChangeRole);
                log.info("User role changed successfully");
                return;
            }
        }

        throw new InvalidParamException("Given params are incorrect");
    }

    @Transactional
    public void deleteUser(HttpServletRequest request, HttpServletResponse response, String uuid) {
        User user;
        try{
             user = getUserByRefreshToken(request);
        } catch (Exception e) {
            throw new CannotAuthorizeByTokenException("Tokens are null or expired");
        }

        User userToDelete = userRepository.findUserByUuid(uuid).orElse(null);

        if(user != null) {
            if(userToDelete != null) {
                userDeleteOperationsDAO.deleteUserOperations(userToDelete.getId());
                userResetPasswordRepository.deleteAllByUser(userToDelete);
                userRepository.delete(userToDelete);
                log.info("User with uuid: '" + uuid + "' deleted successfully");
                return;
            }
            throw new UserDontExistException("Such user don't exist");
        }
    }

    public void authorize(HttpServletRequest request) throws UserDontExistException {
        String token = null;
        String refresh = null;
        if(request.getCookies() != null) {
            for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("token")) {
                    token = value.getValue();
                } else if (value.getName().equals("refresh")) {
                    refresh = value.getValue();
                }
            }
        } else {
            throw new CannotAuthorizeByTokenException("Tokens are null or expired");
        }

        if (token != null && !token.isEmpty()){
            String subject = jwtService.getSubject(token);
            userRepository.findAdmin(subject).orElseThrow(
                    ()->new NoPermissionsException("User has no permission for this operation")
            );
        } else if (refresh != null && !refresh.isEmpty()) {
            String subject = jwtService.getSubject(refresh);
            userRepository.findAdmin(subject).orElseThrow(
                    ()->new NoPermissionsException("User has no permission for this operation")
            );
        }
    }

}