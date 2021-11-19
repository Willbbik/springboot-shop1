package com.ecommerce.newshop1.utils;

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
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

        Member member = memberRepository.findByuserid(userid)
                .orElseThrow(() ->
                                new UsernameNotFoundException("User not found with userid : " + userid)
                        );

        Collection<SimpleGrantedAuthority> role = new ArrayList<SimpleGrantedAuthority>();
        role.add(new SimpleGrantedAuthority(member.getRole().getValue()));


        return new User(member.getUserid(), member.getPassword(), role);
    }


//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
//
//        Optional<MemberEntity> memberEntity = memberRepository.findByuserid(userid);
//        MemberEntity member = memberEntity.get();
//
//        Collection<SimpleGrantedAuthority> role = new ArrayList<SimpleGrantedAuthority>();
//        role.add(new SimpleGrantedAuthority(member.getRole()));
//
//        return new User(member.getUserid(), member.getPassword(), role);
//    }


}















