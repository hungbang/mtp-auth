package ch.smartlink.security.service;

import ch.smartlink.security.entity.User;
import ch.smartlink.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("customUserDetailService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(s);

        if(user == null)
            throw new UsernameNotFoundException(String.format("The username %s doesn't exits", s));

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach( role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
    }
}
