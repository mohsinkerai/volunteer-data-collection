package com.mohsinkerai.adminlte.users;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepository extends SimpleBaseRepository<MyUser> {

  Optional<MyUser> findByUsername(String username);
}
