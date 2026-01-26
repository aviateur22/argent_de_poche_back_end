package com.ctoutweb.argenDePoche.infra.controller;

import com.ctoutweb.argenDePoche.infra.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${api.version}/admin")
public class AdminController {
  private static final Logger LOGGER = LogManager.getLogger();

  private final AdminService adminService;



  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @GetMapping("/initialize-next-period")
  public Mono<ResponseEntity<String>> initializeNextPeriod() {
    LOGGER.info(() -> "Initialisation de la période suivante");
    return adminService.initializeNextPeriod()
            .map(responseDto -> {
                      return ResponseEntity
                              .ok()
                              .body("responseDto");
                    }
            );
  }

}
