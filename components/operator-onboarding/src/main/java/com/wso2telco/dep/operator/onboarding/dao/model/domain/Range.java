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
@Table(name = "range")
public class Range implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7183795365460903100L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
    @JoinColumn(name = "routingdid", referencedColumnName = "id")
	private Routing routing;
	
	@Column(name="from")
	private String from;
	
	@Column(name="to")
	private String to;

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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	

}
