package com.group4.FKitShop.Mapper;


import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Request.AccountCustomerRequest;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.Request.UpdateInfoCustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountsMapper {

    @Mapping(source = "dob", target = "dob")
    Accounts toAccounts(AccountCustomerRequest request);

    Accounts toAccounts(AccountsRequest request);

    void toAccounts(UpdateInfoCustomerRequest request, @MappingTarget Accounts accounts);

    @Mapping(source = "adminID", target = "adminID")
    void toAccounts(AccountsRequest request, @MappingTarget Accounts accounts);
}
