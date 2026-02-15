package com.springboot.project.mapper;

import com.springboot.project.entity.BankAccountEntity;
import com.springboot.project.generated.model.BankAccountDetailModel;
import com.springboot.project.generated.model.BankAccountFilterRequestModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountMapper {

    BankAccountMapper MAPPER = Mappers.getMapper(BankAccountMapper.class);

    BankAccountEntity toBankAccountEntityFromExample(
            BankAccountFilterRequestModel filterRequestModel);

    BankAccountDetailModel toBankAccountDetail(BankAccountEntity bankAccountEntity);

    List<BankAccountDetailModel> toBankAccountDetails(
            List<BankAccountEntity> bankAccountEntities);
}
