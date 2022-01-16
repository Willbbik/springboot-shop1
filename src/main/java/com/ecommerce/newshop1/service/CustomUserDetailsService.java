package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userid : " + userId));

        Collection<SimpleGrantedAuthority> role = new ArrayList<SimpleGrantedAuthority>();
        role.add(new SimpleGrantedAuthority(member.getRole().getValue()));

        return new User(member.getUserId(), member.getPassword(), role);
    }

}















