package com.group4.FKitShop.Mapper;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Request.AddCustomerRequest;
import com.group4.FKitShop.Request.UpdateInfoCustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountsMapper {

    AccountsMapper INSTANCE = Mappers.getMapper(AccountsMapper.class);

    void toAccounts(AddCustomerRequest request, @MappingTarget Accounts accounts);

    void toAccounts(UpdateInfoCustomerRequest request, @MappingTarget Accounts accounts);
}
