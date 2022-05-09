import java.text.DecimalFormat;

@SuppressWarnings("serial")
public class Jacket extends Product {
	private String jacketColour;
	private String jacketSize;
	
	private static DecimalFormat currency = new DecimalFormat("€0.00");
	
	public Jacket() {
		
	}

	public Jacket(int productID, String productName, double productPrice, int productQty, String productType, String jacketColour, String jacketSize) {
		super(productID, productName, productPrice, productQty, productType);
		this.jacketColour = jacketColour;
		this.jacketSize = jacketSize;
	}
	
	public void printJacketDetails() {
		System.out.println(getProductName());
		System.out.println(currency.format(getProductPrice()));
		System.out.println("Colour: " + getJacketColour());
		System.out.println("Size: "+ getJacketSize());
	}

	public String getJacketColour() {
		return jacketColour;
	}

	public void setJacketColour(String jacketColour) {
		this.jacketColour = jacketColour;
	}

	public String getJacketSize() {
		return jacketSize;
	}

	public void setJacketSize(String jacketSize) {
		this.jacketSize = jacketSize;
	}
	
}
