package dev.be.domain.model;

import java.math.BigInteger;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "sales_history", indexes = @Index(name = "idx_customerName", columnList = "customerName"))
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesHistoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "HISTORY_ID")
	private Long id;
	
	@Column(nullable = false)
	private String customerName;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String productName;
	
	@Column(nullable = false)
	private Long quantity;
	
	@Column(nullable = false)
	private BigInteger price;
	
	@Column(nullable = false)
	private LocalDate saleDate;
}
