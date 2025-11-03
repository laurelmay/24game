package me.laurelmay.game24.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackageClasses = WebTemplateController.class)
public class WebTemplateControllerAdvice {
  @ModelAttribute("currentPath")
  public String currentPath(HttpServletRequest request) {
    return request.getServletPath();
  }
}
