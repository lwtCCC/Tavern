package com.lwt.tavern.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class ShopQuery extends PageQuery {
    private String shopName;
    private String shopDescription;
}
