package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {


    Optional<MemberEntity> findByuserid(String userid);

    @Query("select sns from MemberEntity where userid = :userid")
    String findSnsByUserId(@Param("userid") String userid);


}
