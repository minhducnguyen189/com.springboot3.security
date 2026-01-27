package com.springboot.project.mapper;

import com.springboot.project.entity.TransactionDetailEntity;
import com.springboot.project.generated.model.TransactionDetailModel;
import com.springboot.project.generated.model.TransactionFilterRequestModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionDetailMapper {

    TransactionDetailMapper MAPPER = Mappers.getMapper(TransactionDetailMapper.class);

    TransactionDetailEntity toTransactionDetailEntityFromExample(
            TransactionFilterRequestModel filterRequestModel);

    TransactionDetailModel toTransactionDetail(TransactionDetailEntity transactionDetailEntity);

    List<TransactionDetailModel> toTransactionDetails(
            List<TransactionDetailEntity> transactionDetailEntities);
}
