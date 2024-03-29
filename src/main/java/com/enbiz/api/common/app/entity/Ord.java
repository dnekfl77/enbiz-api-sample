package com.enbiz.api.common.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.enbiz.api.common.app.enums.OrderState;
import com.enbiz.api.common.app.enums.TargetPg;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Ord implements Serializable {
	private static final long serialVersionUID = -7261190651232133131L;

	private Long id;
	private String orderNo;
	private String goodsName;
	private Long goodsPrice;
	private String buyerName;
	private String buyerPhone;
	private String buyerEmail;
	private TargetPg targetPg;
	private OrderState orderState;
	private LocalDateTime createDt;
	private LocalDateTime updateDt;
}
