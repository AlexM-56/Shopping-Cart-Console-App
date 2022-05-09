import java.io.Serializable;


public class Product implements Serializable {

	private int productID;
	private String productName;
	private double productPrice;
	private int productQty;
	private String productType;
	private static final long serialVersionUID = 1L;
	
	public Product() {
		
	}

	public Product(int productID, String productName, double productPrice, int productQty, String productType) {
		this.productID = productID;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productQty = productQty;
		this.productType = productType;
	}

	
	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public int getProductQty() {
		return productQty;
	}

	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
}
