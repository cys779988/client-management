package dev.be.domain.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_product")
@NoArgsConstructor
public class ProductEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PRODUCT_ID")
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Long quantity;
	
	@Column(nullable = false)
	private BigInteger price;
	
	@Version
	private Long version;
	
	public void decrease(Long quantity) {
		if(this.quantity - quantity < 0) {
			throw new RuntimeException();
		}
		
		this.quantity = this.quantity - quantity;
	}
	
	@Builder
	public ProductEntity(Long id, String name, Long quantity, BigInteger price) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}

	public void update(String name, Long quantity, BigInteger price) {
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
	
}
