package MunteanuCezar.SD1.controllers;

import MunteanuCezar.SD1.dtos.UserDTO;
import MunteanuCezar.SD1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<?> getUsers(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertUser(@Valid @RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.insert(userDTO), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO){
        userService.update(userDTO);
        return new ResponseEntity<>("User " + userDTO.getUsername() + " updated!", HttpStatus.OK);

    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username){
        userService.delete(username);
        return new ResponseEntity<>("Deleted user: " + username, HttpStatus.OK);
    }
}
