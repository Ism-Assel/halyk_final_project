package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.models.User;
import kz.halykacademy.bookstore.repositories.UserRepository;
import kz.halykacademy.bookstore.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserInfoServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new UserDetailsImpl(user.get());
    }
}