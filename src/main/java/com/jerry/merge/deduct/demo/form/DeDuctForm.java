package com.jerry.merge.deduct.demo.form;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author JerryZeng
 * @date 2020/8/3
 */

@Data
@AllArgsConstructor
public class DeDuctForm {

  private String username;
  private BigDecimal deduction;
}
