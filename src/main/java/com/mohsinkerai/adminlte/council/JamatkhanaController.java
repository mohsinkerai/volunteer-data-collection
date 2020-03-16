package com.mohsinkerai.adminlte.jamatkhana;

import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(JamatkhanaController.URL_PATH)
@PreAuthorize("hasAuthority('ADMIN')")
public class JamatkhanaController extends SimpleBaseController<Jamatkhana> {

  public static final String URL_PATH = "jamatkhana";

  protected JamatkhanaController(
    SimpleBaseService<Jamatkhana> simpleBaseService) {
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
  protected Jamatkhana getEmptyObject() {
    return new Jamatkhana();
  }

  @Override
  protected Map<String, Object> getAttributes() {
    return Maps.newHashMap();
  }
}
