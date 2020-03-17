package com.mohsinkerai.adminlte.base;

import java.io.Serializable;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
public abstract class BaseController<E extends BaseEntity, I extends Serializable> {

  protected static final int PAGE_SIZE = 10;
  protected final BaseService<E, I> baseService;

  protected BaseController(BaseService<E, I> baseService) {
    this.baseService = baseService;
  }

  protected abstract String urlPath();

  protected abstract String viewPath();

  protected abstract E getEmptyObject();

  /**
   * Return Static Values for Lookup Purpose - Such as List of Departments for Employee Controller
   * List of Lookups, etc.
   *
   * It is needed for individual record (add/edit)
   */
  protected abstract Map<String, Object> getAttributes();

  @GetMapping("")
  public String index() {
    return "redirect:/" + urlPath() + "/1";
  }

  @RequestMapping(value = "{pageNumber}", method = RequestMethod.GET)
  public String list(@PathVariable Integer pageNumber, Model model) {
    PageRequest pageRequest =
      new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "id");
    Page<E> page = baseService.findAll(pageRequest);

    int current = page.getNumber() + 1;
    int totalPages = page.getTotalPages();
    int begin = Math.max(1, current - PAGE_SIZE);
    int end = Math.min(begin + PAGE_SIZE, totalPages == 0 ? 1 : totalPages);

    model.addAttribute("urlPath", urlPath());

    model.addAttribute("list", page);
    model.addAttribute("beginIndex", begin);
    model.addAttribute("endIndex", end);
    model.addAttribute("currentIndex", current);

    return viewPath() + "/list";
  }

  @RequestMapping("add")
  public String add(Model model) {
    model.addAttribute("urlPath", urlPath());

    model.addAllAttributes(getAttributes());
    model.addAttribute("data", getEmptyObject());
    return viewPath() + "/form";
  }

  @RequestMapping("edit/{id}")
  public String edit(@PathVariable I id, Model model) {
    model.addAttribute("urlPath", urlPath());

    model.addAllAttributes(getAttributes());
    model.addAttribute("data", baseService.findOne(id).get());
    return viewPath() + "/form";
  }

  @RequestMapping(value = "save", method = RequestMethod.POST)
  public String save(E e, BindingResult bindingResult, Model model, final RedirectAttributes ra) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("data", e);
      model.addAttribute("org.springframework.validation.BindingResult.data", bindingResult);
      return viewPath() + "/form";
    }

    E save = baseService.save(e);
    ra.addFlashAttribute("successFlash", "Data was successfully saved.");
    return "redirect:/" + urlPath();
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
  public String delete(@PathVariable I id) {
    log.info("deleting form method {} urlPath {}", id, urlPath());
    baseService.delete(id);
    return "redirect:/" + urlPath();
  }
}
