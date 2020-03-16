package com.mohsinkerai.adminlte.lookups.disease;

import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(DiseaseController.URL_PATH)
@PreAuthorize("hasAuthority('ADMIN')")
public class DiseaseController extends SimpleBaseController<Disease> {

  public static final String URL_PATH = "lookup/disease";

  protected DiseaseController(
    SimpleBaseService<Disease> simpleBaseService) {
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
  protected Disease getEmptyObject() {
    return new Disease();
  }

  @Override
  protected Map<String, Object> getAttributes() {
    return Maps.newHashMap();
  }
}
