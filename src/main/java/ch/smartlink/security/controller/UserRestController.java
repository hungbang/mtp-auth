package ch.smartlink.security.controller;

import ch.smartlink.security.entity.User;
import ch.smartlink.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRestController {


    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAuthenticatedUser() {
        return ResponseEntity.ok(userRepository.findAll());
    }

}
