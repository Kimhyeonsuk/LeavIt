package com.ram.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;
import org.springframework.stereotype.Repository;
import com.ram.server.model.entity.User;
//Entity명 + repository
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    //User Entity의 id는 Long 타입이기 때문
    /*//Select * from user where account= ? << test03, test04
    Optional<User> findByAccount(String account);

    //Select * from user where email= ? <<test03@gmail.com
    Optional<User> findByEmail(String email);

    //select * from user where account= ? and email = ?
    Optional<User> findByAccountAndEmail(String account,String email);*/
    User findFirstByPhoneNumberOrderByIdDesc(String phoneNumber);
}
