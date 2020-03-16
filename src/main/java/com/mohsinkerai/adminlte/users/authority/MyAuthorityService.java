package com.mohsinkerai.adminlte.users.authority;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import org.springframework.stereotype.Service;

@Service
public class MyAuthorityService extends SimpleBaseService<MyAuthority> {

  protected MyAuthorityService(
    SimpleBaseRepository<MyAuthority> simpleBaseRepository) {
    super(simpleBaseRepository);
  }
}
