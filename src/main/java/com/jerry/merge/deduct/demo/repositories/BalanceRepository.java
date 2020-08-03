package com.jerry.merge.deduct.demo.repositories;

import com.jerry.merge.deduct.demo.domain.Balance;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author JerryZeng
 * @date 2020/7/31
 */
public interface BalanceRepository extends CrudRepository<Balance, Long> {

  @Query("select * from balance where username=:username")
  Optional<Balance> getByUsername(@Param("username") String username);

  @Modifying
  @Query("update balance set balance = balance - :deduction where username = :username")
  boolean deduct(@Param("username") String username, @Param("deduction") double deduction);
}
