package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Geolocation;

import java.security.SignatureException;
import java.util.Date;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Servlet implementation class Test
 */
@WebServlet("/test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String timestamp = String.valueOf(new Date().getTime()).substring(0, 10);
		String data = "application_id=31318&auth_key=tukFM5K2SKpWPEt&nonce=1234&timestamp="+timestamp;
		String key = "C6XCdUk3Sy39Or3";
		String result = null;
		try {

		// get an hmac_sha1 key from the raw key bytes
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

		// get an hmac_sha1 Mac instance and initialize with the signing key
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);

		// compute the hmac on input data bytes
		byte[] rawHmac = mac.doFinal(data.getBytes());
		
		Formatter formatter = new Formatter();
		for (byte b : rawHmac) {
			formatter.format("%02x", b);
		}
		result = formatter.toString();

		} catch (Exception e) {
			try {
				throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
			} catch (SignatureException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		Geolocation.getGeometry("312,Margaret Morrison Carnegie Hall,Pittsburgh, Pennsylvania");
		
		response.getWriter().append(result).append("\n").append(timestamp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
