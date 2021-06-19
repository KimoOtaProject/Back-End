package com.kimoota.kimootaapiservice.service.user;

import com.kimoota.kimootaapiservice.domain.User;
import com.kimoota.kimootaapiservice.exception.ResourceNotFoundException;
import com.kimoota.kimootaapiservice.repository.user.UserRepository;
import com.kimoota.kimootaapiservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("user not found. email: "+ email));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user","id",id));

        return UserPrincipal.create(user);
    }

}
