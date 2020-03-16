package com.mohsinkerai.adminlte.users.authority;

import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(MyAuthorityController.URL_PATH)
public class MyAuthorityController extends SimpleBaseController<MyAuthority> {

  public static final String URL_PATH = "authorities";

  protected MyAuthorityController(
    SimpleBaseService<MyAuthority> simpleBaseService) {
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
  protected MyAuthority getEmptyObject() {
    return new MyAuthority();
  }

  @Override
  protected Map<String, Object> getAttributes() {
    return Maps.newHashMap();
  }
}
