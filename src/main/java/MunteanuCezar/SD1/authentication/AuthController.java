package MunteanuCezar.SD1.authentication;

import MunteanuCezar.SD1.JWT.JwtTokenUtil;
//import MunteanuCezar.SD1.entities.User;
import MunteanuCezar.SD1.entities.User;
import MunteanuCezar.SD1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//            User user = (User) authentication.getPrincipal();
            User user = userRepository.findByUsername(authRequest.getUsername());
            String accesToken = jwtUtil.generateAccesToken(user);
            AuthResponse response = new AuthResponse(user.getUsername(), accesToken);
            return ResponseEntity.ok(response);
        }
        catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
//        return  new ResponseEntity<>("Sal", HttpStatus.OK);
    }
}
