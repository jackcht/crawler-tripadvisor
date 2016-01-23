package com.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity
public class Location {
	private String _locationId;
	private String _name;
	
	private Set<Location> _subLocations;
	private Set<Attraction> _attractions;

	
	public Location(){
		super();
		_subLocations = new HashSet<Location>();
		_attractions = new HashSet<Attraction>();
	}
	
	public Location(String locationId,String name){
		this();
		_locationId = locationId;
		_name = name;
	}
	@Id
	@Column(name="LocationId")
	public String getLocationId() {
		return _locationId;
	}
	
	public void setLocationId(String locationId) {
		_locationId = locationId;
	}
	
	@Column(name="LocationName")
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	@OneToMany(fetch= FetchType.EAGER)
	@JoinTable(name="SubSuperLocations",joinColumns=@JoinColumn(name="subLocation"),
			inverseJoinColumns=@JoinColumn(name="superLocation"))
	public Set<Location> getSubLocations() {
		return _subLocations;
	}
	
	public void setSubLocations(Set<Location> subLocations) {
		_subLocations = subLocations;
	}
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name="LocationAttractions", joinColumns=@JoinColumn(name="LocationId"),
			inverseJoinColumns=@JoinColumn(name="AttractionId"))
	
	public Set<Attraction> getAttractions() {
		Set<Attraction> attractions = new HashSet<Attraction>();
		for (Location l: _subLocations)
			attractions.addAll(l.getAttractions());
		attractions.addAll(_attractions);
		return attractions;
	}

	public void setAttractions(Set<Attraction> attractions) {
		_attractions = attractions;
	}

}
