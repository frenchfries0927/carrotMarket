package com.carrotMarket.carrotMarket.NewProject.board.mapper;

import com.carrotMarket.carrotMarket.NewProject.board.entity.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TransactionMapper {

    @Insert("INSERT INTO transactions (item_id, buyer_id, status, transaction_date) " +
            "VALUES (#{itemId}, #{buyerId}, #{status}, #{transactionDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTransaction(Transaction transaction);

    @Select("SELECT * FROM transactions WHERE id = #{id}")
    Transaction selectTransactionById(Long id);

    @Select("SELECT * FROM transactions")
    List<Transaction> selectAllTransactions();

    @Update("UPDATE transactions SET item_id = #{itemId}, buyer_id = #{buyerId}, status = #{status}, transaction_date = #{transactionDate} WHERE id = #{id}")
    void updateTransaction(Transaction transaction);

    @Delete("DELETE FROM transactions WHERE id = #{id}")
    void deleteTransaction(Long id);
}
