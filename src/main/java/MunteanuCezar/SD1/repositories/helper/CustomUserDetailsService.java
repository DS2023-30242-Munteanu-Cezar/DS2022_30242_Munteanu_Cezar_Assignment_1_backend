package MunteanuCezar.SD1.repositories.helper;

import MunteanuCezar.SD1.entities.Role;
import MunteanuCezar.SD1.entities.User;
import MunteanuCezar.SD1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User domainUser = userRepository.findByUsername(username);

        Set<Role> roles = null;
        roles.add(domainUser.getRole());

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return domainUser.getRole().toString();
            }
        });

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(domainUser);
        customUserDetails.setAuthorities(authorities);

        return customUserDetails;
    }
}
