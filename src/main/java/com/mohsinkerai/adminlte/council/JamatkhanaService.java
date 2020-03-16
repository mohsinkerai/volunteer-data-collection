package com.mohsinkerai.adminlte.jamatkhana;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import org.springframework.stereotype.Service;

@Service
public class JamatkhanaService extends SimpleBaseService<Jamatkhana> {

  protected JamatkhanaService(
    SimpleBaseRepository<Jamatkhana> simpleBaseRepository) {
    super(simpleBaseRepository);
  }
}
