import com.alibaba.fastjson.JSONObject;


public class Test {
	
	public static void main(String[] args) {
		String str = "{\"dbb211c4adf4eecac8d08d4ea39474a8\":\"{\"node_length\":4,\"node_location\":[{\"w\":39.95053617975741,\"t\":116.34074032226562,\"lng\":116.34074,\"lat\":39.950536}]}\"}";
		JSONObject json = JSONObject.parseObject(str);
		
		System.out.println(json);
		
		
	}
}
