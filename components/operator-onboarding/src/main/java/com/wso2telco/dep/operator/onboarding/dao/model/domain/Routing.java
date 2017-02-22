package com.wso2telco.dep.operator.onboarding.dao.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "routing")
public class Routing {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private int id;
	
    @ManyToOne
    @JoinColumn(name = "operatordid", referencedColumnName = "ID")
	private Operator operator;
	
    @ManyToOne
    @JoinColumn(name = "routingmethodid", referencedColumnName = "id")
	private RoutingMethod routingMethod;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public RoutingMethod getRoutingMethod() {
		return routingMethod;
	}

	public void setRoutingMethod(RoutingMethod routingMethod) {
		this.routingMethod = routingMethod;
	}
    

}
