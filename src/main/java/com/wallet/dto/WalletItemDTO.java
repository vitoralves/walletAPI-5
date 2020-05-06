package com.wallet.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class WalletItemDTO {

	private Long id;

	@NotNull(message = "Insira o id da carteira")
	private Long wallet;

	@NotNull(message = "Informe uma data")
	private Date date;

	@NotNull(message = "Informe um tipo")
	private String type;

	@NotNull(message = "Informe uma descrição")
	@Length(min = 5, message = "A descrição deve conter no minimo 5 caracteres")
	private String description;

	@NotNull(message = "Informe um valor")
	private BigDecimal value;

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWallet() {
		return wallet;
	}

	public void setWallet(Long wallet) {
		this.wallet = wallet;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
