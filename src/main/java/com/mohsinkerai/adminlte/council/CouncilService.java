package com.mohsinkerai.adminlte.council;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import org.springframework.stereotype.Service;

@Service
public class CouncilService extends SimpleBaseService<Council> {

  protected CouncilService(
    SimpleBaseRepository<Council> simpleBaseRepository) {
    super(simpleBaseRepository);
  }
}
