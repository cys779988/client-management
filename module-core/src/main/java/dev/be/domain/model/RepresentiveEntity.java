package dev.be.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RepresentiveEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="REPRESENTIVE_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID", foreignKey = @ForeignKey(name = "fk_representive_customer"))
	private CustomerEntity customer;
	
	private String name;
	
	private String contact;
}
