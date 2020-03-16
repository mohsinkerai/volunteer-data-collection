package com.mohsinkerai.adminlte.users.authority;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MyAuthorityRepository extends SimpleBaseRepository<MyAuthority> {

  Optional<MyAuthority> findByName(String name);
}
