package com.jerry.merge.deduct.demo.service;

import com.jerry.merge.deduct.demo.form.DeDuctForm;
import com.jerry.merge.deduct.demo.repositories.BalanceRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author JerryZeng
 * @date 2020/7/31
 */

@Service
public class MergeDeductService {

  @Autowired
  private BalanceRepository balanceRepository;

  private final BlockingQueue<DeDuctForm> queue = new ArrayBlockingQueue<>(4000);
  private volatile boolean stop = false;
  private final ThreadPoolExecutor DEDUCT_POLL = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2 + 1,
    Runtime.getRuntime().availableProcessors() * 4,
    5,
    TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(1024));;

  @PostConstruct
  public void init() {
    new Thread(this::start).start();
  }

  @PreDestroy
  public void destroy() {
    DEDUCT_POLL.shutdown();
  }

  private void start() {
    CountDownLatch deductBatch = null;
    while (!canStop()) {
      try {
        DeDuctForm form;
        try {
          form = queue.poll(1, TimeUnit.SECONDS);
          if(form == null) {
            continue;
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
          continue;
        }

        List<DeDuctForm> forms = new ArrayList<>(100);
        queue.drainTo(forms);
        forms.add(form);
        if(forms.isEmpty()) {
          continue;
        }
        if(deductBatch != null) {
          deductBatch.await(1, TimeUnit.SECONDS);
        }

        Map<String, List<DeDuctForm>> usernameMap = forms.stream()
          .collect(Collectors.groupingBy(DeDuctForm::getUsername));
        deductBatch = new CountDownLatch(usernameMap.size());
        final CountDownLatch finalLatch = deductBatch;
        for (Entry<String, List<DeDuctForm>> entry : usernameMap.entrySet()) {
          DEDUCT_POLL.execute(() ->{
            try {
              mergeDeduct(entry.getKey(), entry.getValue());
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
              finalLatch.countDown();
            }
          });
        }
      } catch (Exception e){
        e.printStackTrace();
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
        }
      }
    }
  }

  public boolean put(DeDuctForm form) {
    try {
      queue.put(form);
    } catch (InterruptedException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public boolean canStop() {
    return stop && queue.isEmpty();
  }

  public void stop() {
    stop = true;
  }

  private void mergeDeduct(String username, List<DeDuctForm> forms) {
    Optional<BigDecimal> optional = forms.stream().map(DeDuctForm::getDeduction)
      .reduce(BigDecimal::add);
    if(!optional.isPresent()) {
      throw new RuntimeException("deduct error");
    }

    System.out.println(String.format("user:%s deduct:%.2f", username, optional.get().doubleValue()));
    balanceRepository.deduct(username, optional.get().doubleValue());
  }
}
