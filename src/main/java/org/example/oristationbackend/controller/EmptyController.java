package org.example.oristationbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.VacantApplyReqDto;
import org.example.oristationbackend.service.EmptyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vacant")
@RequiredArgsConstructor
public class EmptyController {
  private final EmptyService emptyService;

  @PostMapping()
  public void applyVacant(@RequestBody VacantApplyReqDto vacantApplyReqDto) {
    emptyService.applyVacant(vacantApplyReqDto);
  }

}
