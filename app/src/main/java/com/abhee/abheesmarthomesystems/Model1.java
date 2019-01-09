package com.abhee.abheesmarthomesystems;

/**
 * 
 * @author anfer
 * 
 */
public class Model1 {


	private String Market;
	private String Commodity;
	private String Model;
	private String Id;


	public Model1(String category, String companyname, String productmodelname, String id) {
		this.Market=category;
		this.Commodity=companyname;
		this.Model=productmodelname;
		this.Id=id;
	}


	public String getMarket() {
		return Market;
	}

	public void setMarket(String market) {
		Market = market;
	}

	public String getCommodity() {
		return Commodity;
	}

	public void setCommodity(String commodity) {
		Commodity = commodity;
	}

	public String getModel() {
		return Model;
	}

	public void setModel(String model) {
		Model = model;
	}


	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
}
