package com.javafullstackcourse.chatapp.controllers;

import com.javafullstackcourse.chatapp.models.ChatUser;
import com.javafullstackcourse.chatapp.utils.Command;
import com.javafullstackcourse.chatapp.models.CommonResponse;
import com.javafullstackcourse.chatapp.repository.UserRepository;
import com.javafullstackcourse.chatapp.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "The Chat App Api";
    }

    @GetMapping("/users")
    public ResponseEntity<CommonResponse> getAllUsers(HttpServletRequest request) {
        Command cmd = new Command(request);

        //process
        CommonResponse cr = new CommonResponse();
        cr.data = userRepository.findAll();
        cr.message = "All users";

        HttpStatus resp = HttpStatus.OK;

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<CommonResponse> getUserById(HttpServletRequest request, @PathVariable Integer id) {
        Command cmd = new Command(request);

        //process
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if (userRepository.existsById(id)) {
            cr.data = userRepository.findById(id);
            cr.message = "Entry with id: " + id;
            resp = HttpStatus.OK;
        } else {
            cr.data = null;
            cr.message = "Entry not found";
            resp = HttpStatus.NOT_FOUND;
        }

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

    @PostMapping("/users")
    public ResponseEntity<CommonResponse> addUser(HttpServletRequest request, @RequestBody ChatUser user) {
        Command cmd = new Command(request);

        //process
        user = userRepository.save(user);

        CommonResponse cr = new CommonResponse();
        cr.data = user;
        cr.message = "New user with id: " + user.id;

        HttpStatus resp = HttpStatus.CREATED;

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<CommonResponse> updateUser(HttpServletRequest request, @RequestBody ChatUser newUser,
                                                     @PathVariable Integer id) {
        Command cmd = new Command(request);

        //process
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if (userRepository.existsById(id)) {
            Optional<ChatUser> userRepo = userRepository.findById(id);
            ChatUser user = userRepo.get();

            if (newUser.username != null) {
                user.username = newUser.username;
            }
            if (newUser.password != null) {
                user.password = newUser.password;
            }
            if (newUser.created != null) {
                user.created = newUser.created;
            }
            if (newUser.modified != null) {
                user.modified = newUser.modified;
            }
            if (newUser.profileImage != null) {
                user.profileImage = newUser.profileImage;
            }

            userRepository.save(user);

            cr.data = user;
            cr.message = "Updated user with id: " + user.id;
            resp = HttpStatus.OK;
        } else {
            cr.message = "User not found with id: " + id;
            resp = HttpStatus.NOT_FOUND;
        }

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().

                logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

    @PostMapping("/users/login")
    public ResponseEntity<CommonResponse> loginUser(HttpServletRequest request, @RequestBody ChatUser user) {
        Command cmd = new Command(request);

        //process
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        ChatUser fetchedUser = userRepository.getByUsername(user.username);

        if(fetchedUser != null) {
            if(fetchedUser.password.equals(user.password)) {
                cr.data = user;
                cr.message = "User credential approved.";
                resp = HttpStatus.OK;
            } else {
                cr.message = "Username or password is incorrect.";
                resp = HttpStatus.NOT_FOUND;
            }
        } else {
            cr.message = "Username or password is incorrect.";
            resp = HttpStatus.NOT_FOUND;
        }

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().

                logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

    @PostMapping("/users/register")
    public ResponseEntity<CommonResponse> registerUser(HttpServletRequest request, @RequestBody ChatUser user) {
        CommonResponse cr = new CommonResponse();
        Command cmd = new Command(request);
        HttpStatus resp;

        ChatUser newUser = new ChatUser();

        if (user.username != null && user.password != null) {

            if(userRepository.getByUsername(user.username) == null) {
                /*newUser.username = user.username;
                newUser.password = user.password;

                if (user.profileImage != null) {
                    newUser.profileImage = user.profileImage;
                }

                userRepository.save(newUser);*/
                user = userRepository.save(user);

                cr.data = user;
                cr.message = "New user with id: " + user.id;

                resp = HttpStatus.CREATED;
            } else {
                cr.message = "Username must be unique";

                resp = HttpStatus.BAD_REQUEST;
            }

        } else {
            cr.message = "Something went wrong. Invalid request.";

            resp = HttpStatus.BAD_REQUEST;
        }

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }
}
