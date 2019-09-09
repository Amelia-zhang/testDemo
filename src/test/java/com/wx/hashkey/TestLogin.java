package com.wx.hashkey;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wx.hashkey.util.EncrypUtils;
import com.wx.hashkey.util.HttpRequest;
import com.wx.hashkey.util.Tokens;


public class TestLogin {
	String loginUrl="https://qa-api.hub.hashkey.com/passport/login/app";
	@Test(dataProvider = "testLogin",groups = "login")
	public void checkLoginResult(String deviceId,String phone,String source,
			String password,String platFormId,String reponseMessage) {
		
		String encryp = EncrypUtils.encryp(password);
		
		JSONObject  requestObj= new JSONObject();
		try {
			requestObj.put("deviceId", deviceId);
			requestObj.put("username", phone);
			requestObj.put("source", source);
			requestObj.put("password", encryp);
			requestObj.put("platFormId", platFormId);
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//System.out.println(requestObj.toString());
		String sendPost = HttpRequest.sendPost(loginUrl,requestObj.toString());
		
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(sendPost);
			String returnMessage = jsonObject.getString("message");
			//System.out.println("login接口返回"+sendPost);
			String accessToken = jsonObject.getJSONObject("body").getString("accessToken");
			
			Tokens.saveToken(accessToken);
			Assert.assertEquals(returnMessage, reponseMessage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@DataProvider(name = "testLogin")
	public Object[][] provideObject() {
		return new Object[][] { { "67C05B1B-711A-441F-A62C-5EABB74338B3", "18616771581","1",
			"Shaoser123=","1","SUCCESS"} };
	}

}
