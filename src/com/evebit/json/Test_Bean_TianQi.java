package com.evebit.json;


import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class Test_Bean_TianQi {

	private Test_Model_TianQi  msg;

	public Test_Model_TianQi  getData() {
		return msg;
	}

	public void setData(Test_Model_TianQi  msg) {
		this.msg = msg;
	}

	/**
	 * @param data
	 * @return
	 * @throws WHT_Exception 
	 */
	public static Test_Bean_TianQi dataParser(String data) throws Y_Exception {
		Test_Bean_TianQi reigst = new Test_Bean_TianQi();

		try {
			Test_Model_TianQi  myanswerlist = (Test_Model_TianQi) JsonUtil.DataToObject(data, new TypeToken<Test_Model_TianQi >(){}.getType());
			reigst.setData(myanswerlist);
		} 
		catch(JsonParseException e) {
			throw Y_Exception.dataParser(e);
		}
		
		return reigst;
	}
}
