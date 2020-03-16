package com.mohsinkerai.adminlte.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {

  @RequestMapping("/")
  public String index() {
    return "redirect:/dashboard";
  }

  @RequestMapping("/dashboard")
  public String dashboard() {
    return "dashboard/index";
  }
}
