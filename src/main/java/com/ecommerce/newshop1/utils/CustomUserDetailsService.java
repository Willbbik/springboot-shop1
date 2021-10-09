package com.ecommerce.newshop1.utils;

import com.ecommerce.newshop1.entity.MemberEntity;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

        Optional<MemberEntity> memberEntity = memberRepository.findByuserid(userid);
        MemberEntity member = memberEntity.get();

        Collection<SimpleGrantedAuthority> role = new ArrayList<SimpleGrantedAuthority>();
        role.add(new SimpleGrantedAuthority(member.getRole()));

        return new User(member.getUserid(), member.getPassword(), role);
    }


}















