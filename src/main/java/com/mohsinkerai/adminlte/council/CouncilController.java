package com.mohsinkerai.adminlte.council;

import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(CouncilController.URL_PATH)
@PreAuthorize("hasAuthority('ADMIN')")
public class CouncilController extends SimpleBaseController<Council> {

  public static final String URL_PATH = "council";

  protected CouncilController(
    SimpleBaseService<Council> simpleBaseService) {
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
  protected Council getEmptyObject() {
    return new Council();
  }

  @Override
  protected Map<String, Object> getAttributes() {
    return Maps.newHashMap();
  }
}
