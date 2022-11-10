package MunteanuCezar.SD1.services;

import MunteanuCezar.SD1.dtos.UserDTO;
import MunteanuCezar.SD1.entities.User;
import MunteanuCezar.SD1.repositories.RoleRepository;
import MunteanuCezar.SD1.repositories.UserRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User dtoToUser(UserDTO userDTO){
        User user = User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .role(roleRepository.findByRoleCode(userDTO.getRoleCode()))
                .build();
        return user;
    }

    public UserDTO userToDto(User user){
        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .roleCode(user.getRole().getRoleCode())
                .build();
        return userDTO;
    }

    public List<UserDTO> findAll(){
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOList = users.stream().map(user -> userToDto(user)).collect(Collectors.toList());
        log.info("Show all users!");
        return userDTOList;
    }

    public void update(UserDTO userDTO){
        User user = userRepository.findByUsername(userDTO.getUsername());
        if(!userDTO.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if(!userDTO.getRoleCode().isEmpty()){
            user.setRole(roleRepository.findByRoleCode(userDTO.getRoleCode()));
        }
        if(!userDTO.getEmail().isEmpty()){
            user.setEmail(userDTO.getEmail());
        }

        userRepository.save(user);
    }

    public UUID insert(UserDTO userDTO){
        User user = dtoToUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("User " + user.getUsername() + " inserted!");
        return userRepository.save(user).getId();
    }

    public void delete(String username){
        User user = userRepository.findByUsername(username);
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException(username + " not found!");
        }
//        UserDetails userDetails;
        return user;
    }
}
