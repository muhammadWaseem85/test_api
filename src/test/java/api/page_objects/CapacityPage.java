package api.page_objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;


public class CapacityPage {

    public void compartments (String body, Integer compartment, Integer a, String expectedCompartments ) {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(a);
            compartment = obj2.getInt("compartments");
        }
        System.out.println("Size: "+ compartment );
        String compartments = Integer.toString(compartment);
        Assert.assertEquals(compartments, expectedCompartments);
    }

}
