package com.mohsinkerai.adminlte.lookups.health_facility;

import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(HealthFacilityController.URL_PATH)
@PreAuthorize("hasAuthority('ADMIN')")
public class HealthFacilityController extends SimpleBaseController<HealthFacility> {

  public static final String URL_PATH = "lookup/health-facility";

  protected HealthFacilityController(
    SimpleBaseService<HealthFacility> simpleBaseService) {
    super(simpleBaseService);
  }

  @Override
  protected String urlPath() {
    return URL_PATH;
  }

  @Override
  protected String viewPath() {
    return URL_PATH;
  }

  @Override
  protected HealthFacility getEmptyObject() {
    return new HealthFacility();
  }

  @Override
  protected Map<String, Object> getAttributes() {
    return Maps.newHashMap();
  }
}
