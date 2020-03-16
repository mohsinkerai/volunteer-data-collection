package com.mohsinkerai.adminlte.base;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public abstract class SimpleBaseController<E extends BaseEntity> extends BaseController<E, Long>{

  protected SimpleBaseController(BaseService<E, Long> baseService) {
    super(baseService);
  }
}
