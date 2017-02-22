package com.wso2telco.dep.operator.onboarding.dao.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pathfinder")
public class Pathfinder implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7787701592687465114L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
    @JoinColumn(name = "routingdid", referencedColumnName = "id")
	private Routing routing;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Routing getRouting() {
		return routing;
	}

	public void setRouting(Routing routing) {
		this.routing = routing;
	}
	

}
