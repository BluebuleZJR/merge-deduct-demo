package com.jerry.merge.deduct.demo;

import com.jerry.merge.deduct.demo.domain.Balance;
import com.jerry.merge.deduct.demo.form.DeDuctForm;
import com.jerry.merge.deduct.demo.repositories.BalanceRepository;
import com.jerry.merge.deduct.demo.service.MergeDeductService;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class MergeDeductDemoApplicationTests {

  @Autowired
  private BalanceRepository balanceRepository;
  @Autowired
  private MergeDeductService mergeDeductService;

  @Test
  public void testGet() {
    Optional<Balance> optional = balanceRepository.getByUsername("jerry");
    Assert.assertTrue(optional.isPresent());
    System.out.println(optional.get());
  }

  @Test
  public void testDeduct() {
    boolean flag = balanceRepository.deduct("jerry", 100D);
    Assert.assertTrue(flag);
    Optional<Balance> optional = balanceRepository.getByUsername("jerry");
    Assert.assertTrue(optional.isPresent());
    System.out.println(optional.get());
  }

//  @Test
//  public void testMergeDeduct() {
//    ExecutorService executorService = Executors.newFixedThreadPool(100);
//    for (int i = 0; i < 1000; i++) {
//      executorService.execute(() -> mergeDeductService.put(new DeDuctForm("jerry", new BigDecimal(1))));
//    }
//
//    executorService.shutdown();
//    mergeDeductService.destroy();
//  }

}
