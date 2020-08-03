package com.jerry.merge.deduct.demo.controller;

import com.jerry.merge.deduct.demo.form.DeDuctForm;
import com.jerry.merge.deduct.demo.service.MergeDeductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JerryZeng
 * @date 2020/8/3
 */

@RestController
public class BalanceController {

  @Autowired
  private MergeDeductService mergeDeductService;

  private String result = "{\"code\":%d,\"msg\":\"%s\"}";

  @PostMapping("/deduction")
  public String deduct(@RequestBody DeDuctForm form) {
    if(mergeDeductService.put(form)) {
      return String.format(result, 0, "SUCCESS");
    } else {
      return String.format(result, -1, "FAIL");
    }
  }
}
