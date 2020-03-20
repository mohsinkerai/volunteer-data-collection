package com.mohsinkerai.adminlte.person.status;

import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(PersonStatusController.URL_PATH)
@PreAuthorize("hasAuthority('ADMIN')")
public class PersonStatusController extends SimpleBaseController<PersonStatus> {

  public static final String URL_PATH = "person-status";

  protected PersonStatusController(
    SimpleBaseService<PersonStatus> simpleBaseService) {
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
  protected PersonStatus getEmptyObject() {
    return new PersonStatus();
  }

  @Override
  protected Map<String, Object> getAttributes() {
    return Maps.newHashMap();
  }
}
