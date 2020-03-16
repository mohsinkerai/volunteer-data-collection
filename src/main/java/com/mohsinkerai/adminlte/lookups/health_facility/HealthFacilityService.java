package com.mohsinkerai.adminlte.lookups.health_facility;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import org.springframework.stereotype.Service;

@Service
public class HealthFacilityService extends SimpleBaseService<HealthFacility> {

  protected HealthFacilityService(
    SimpleBaseRepository<HealthFacility> simpleBaseRepository) {
    super(simpleBaseRepository);
  }
}
