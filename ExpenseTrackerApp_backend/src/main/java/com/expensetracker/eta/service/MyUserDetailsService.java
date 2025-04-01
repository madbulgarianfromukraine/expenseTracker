package com.expensetracker.eta.service;

import com.expensetracker.eta.dto.UserDto;
import com.expensetracker.eta.model.MyUserDetails;
import com.expensetracker.eta.model.User;
import com.expensetracker.eta.repository.UserRepository;
import com.expensetracker.eta.util.error.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;

@Primary
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private SecureRandom secureRandom;

    // Initialize SecureRandom instance
    public SecureRandom getSecureRandom(){
        if(secureRandom != null) return secureRandom;

        try {
            secureRandom = secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return secureRandom;
    }


    public static MessageDigest digest;

    public MessageDigest getMessageDigest(){
        if(digest != null) return digest;

        try {
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return digest;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        System.out.println("I am not a lazy ass and I really was here!");
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
        user.setEmail(userDto.getEmail());
        user.setDateOfRegistration(LocalDate.now());
        byte[] salt = new byte[16];
        this.getSecureRandom().nextBytes(salt);
        user.setRandomSalt(new String(salt, Charset.defaultCharset()));
        byte[] hashedPassword = this.getMessageDigest().digest((userDto.getPassword() + user.getRandomSalt()).getBytes());
        user.setHashedPassword(new String(salt, Charset.defaultCharset()));

        System.out.println(user.toString());
        return userRepository.save(user);
    }
    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }


}
