package com.unfoldlabs.redgreen.model;

import java.io.Serializable;

public class AddressInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int addressId;

	private String state;

	private String city;

	private String address;

	private Double lon;

	private Double lat;

	private int zipCode;

	/**
	 * @return the addressId
	 */
	public Integer getAddressId() {
		return addressId;
	}

	/**
	 * @param addressId
	 *            the addressId to set
	 */
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return longitude
	 */
	public Double getLon() {
		return lon;
	}

	/**
	 * the longitude to set
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 * @return latitude
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * the latitude to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return zipcode
	 */
	public int getZipCode() {
		return zipCode;
	}

	/**
	 * the zipcode to set
	 */
	public void setZipCode(int i) {
		this.zipCode = i;
	}

}
