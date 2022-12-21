package dev.be.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="CUSTOMER_ID")
	private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private CustomerType type;
    
    @Column(nullable = false)
    private String name;
    
    private String englishName;
    
    private String nationality;
    
    private String birthDate;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String contact;
    
	@OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<RepresentiveEntity> representive;
}