package com.group4.FKitShop.mapper;


import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Request.AccountsRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountsMapper {
    Accounts toAccounts(AccountsRequest request);


}
