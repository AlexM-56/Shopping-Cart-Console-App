import java.text.DecimalFormat;

@SuppressWarnings("serial")
public class VideoGame extends Product {
	
	private String videoGamePlatform;
	private String videoGameRating;
	
	
	private static DecimalFormat currency = new DecimalFormat("€0.00");

	public VideoGame() {
		
	}


	public VideoGame(int productID, String productName, double productPrice, int productQty, String productType, String videoGamePlatform, String videoGameRating) {
		super(productID, productName, productPrice, productQty, productType);
		this.videoGamePlatform = videoGamePlatform;
		this.videoGameRating = videoGameRating;
	}

	public void printGameDetails() {
		System.out.println(getProductName());
		System.out.println(currency.format(getProductPrice()));
		System.out.println("Platform: " + getVideoGamePlatform());
		System.out.println("Age Rating: "+ getVideoGameRating());
	}

	public String getVideoGamePlatform() {
		return videoGamePlatform;
	}


	public void setVideoGamePlatform(String videoGamePlatform) {
		this.videoGamePlatform = videoGamePlatform;
	}


	public String getVideoGameRating() {
		return videoGameRating;
	}


	public void setVideoGameRating(String videoGameRating) {
		this.videoGameRating = videoGameRating;
	}
		
}