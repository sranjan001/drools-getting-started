package com.vmware.drools.misc.agendagroup;

import lombok.Data;

import java.util.List;

@Data
public class Account {

    private String accountNo;
    private long balance;

    private List<String> types;
    private String status;

}
