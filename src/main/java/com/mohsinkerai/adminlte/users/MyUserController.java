package com.mohsinkerai.adminlte.users;

import com.google.common.collect.ImmutableMap;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.jamatkhana.JamatkhanaService;
import java.util.List;
import java.util.Map;

import com.mohsinkerai.adminlte.users.authority.MyAuthority;
import com.mohsinkerai.adminlte.users.authority.MyAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(MyUserController.URL_PATH)
public class MyUserController extends SimpleBaseController<MyUser> {

  public static final String URL_PATH = "users";
  private final MyUserService myUserService;
  private final JamatkhanaService jamatkhanaService;
  private final MyAuthorityService myAuthorityService;

  protected MyUserController(
    MyUserService myUserService,
    JamatkhanaService jamatkhanaService, MyAuthorityService myAuthorityService) {
    super(myUserService);
    this.myUserService = myUserService;
    this.jamatkhanaService = jamatkhanaService;
    this.myAuthorityService = myAuthorityService;
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
  protected MyUser getEmptyObject() {
    return new MyUser();
  }

  @Override
  protected Map<String, Object> getAttributes() {
    List<com.mohsinkerai.adminlte.jamatkhana.Council> councils = jamatkhanaService.findAll();
    List<MyAuthority> authorities = myAuthorityService.findAll();
    return ImmutableMap.of("jks", councils, "authorities", authorities);
  }

  @GetMapping("hello")
  public void hello() {
    log.info("Hello World");
    log.info("Role {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
  }
}
