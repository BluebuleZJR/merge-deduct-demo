package com.jerry.merge.deduct.demo.domain;

import java.math.BigDecimal;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author JerryZeng
 * @date 2020/7/31
 */

@Data
public class Balance {

  @Id
  private Long id;
  private String username;
  private BigDecimal balance;
}
