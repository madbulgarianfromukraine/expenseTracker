package com.expensetracker.eta.service;

import com.expensetracker.eta.dto.UserDto;
import com.expensetracker.eta.model.MyUserDetails;
import com.expensetracker.eta.model.User;
import com.expensetracker.eta.repository.UserRepository;
import com.expensetracker.eta.util.error.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.validation.Valid;
import java.util.Arrays;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private SecureRandom secureRandom;
    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private SecureRandom getSecureRandom() {
        if (secureRandom == null) {
            try {
                secureRandom = SecureRandom.getInstance("PKCS11");
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize SecureRandom", e);
            }
        }
        return secureRandom;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user);
    }


    public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        // the rest of the registration operation
        User user = new User();
        user.setUsername(userDto.getUsername());

        secureRandom.nextBytes();


        return userRepository.save(user);
    }
    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }


}
