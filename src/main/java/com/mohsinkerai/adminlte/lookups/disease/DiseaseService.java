package com.mohsinkerai.adminlte.lookups.disease;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import org.springframework.stereotype.Service;

@Service
public class DiseaseService extends SimpleBaseService<Disease> {

  protected DiseaseService(
    SimpleBaseRepository<Disease> simpleBaseRepository) {
    super(simpleBaseRepository);
  }
}
