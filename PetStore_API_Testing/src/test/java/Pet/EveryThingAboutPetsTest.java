package Pet;

import org.testng.annotations.Test;

import com.petStore.api.PojoClass.PetPojo;
import com.petStore.api.baseClass.BaseApiClass;
import com.petStore.api.endpoints.IendPoints;
import com.petStore.api.genericUtility.JsonUtility;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static  io.restassured.RestAssured.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class EveryThingAboutPetsTest extends BaseApiClass {

	JsonUtility jsonutil = new JsonUtility();
	public Object petId ="9223372036854760538";
	
	List<String> photo = Arrays.asList("https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?cs=srgb&dl=pexels-chevanon-1108099.jpg&fm=jpg");
	
	@Test
	public void addNewPetToStoreTest() {
		
		
		PetPojo petpojo = new PetPojo("Charile",photo);
		
		String addPet = "{\r\n"
				+ "  \"id\": 0,\r\n"
				+ "  \"category\": {\r\n"
				+ "    \"id\": 0,\r\n"
				+ "    \"name\": \""+"Charile"+"\"\r\n"
				+ "  },\r\n"
				+ "  \"name\": \"doggie\",\r\n"
				+ "  \"photoUrls\": [\r\n"
				+ "    \"string\"\r\n"
				+ "  ],\r\n"
				+ "  \"tags\": [\r\n"
				+ "    {\r\n"
				+ "      \"id\": 0,\r\n"
				+ "      \"name\": \"string\"\r\n"
				+ "    }\r\n"
				+ "  ],\r\n"
				+ "  \"status\": \"available\"\r\n"
				+ "}";
		
		Response resp = given()
			.spec(reqBuild)
			.body(petpojo)
		.when()
			.post(IendPoints.addnewPetToStore);
		resp.then()
			.spec(respBuild)
			.assertThat().statusCode(200)
			.log().all();
		
		 petId = jsonutil.getDataOnJsonPath(resp, ".id");
		 System.out.println(petId);
	}
	
	@Test
	public void updateExistingPetTest() {
		 
		PetPojo petpojo = new PetPojo("Snufy",photo);
		Response resp = given()
				.spec(reqBuild)
				.body(petpojo)
			.when()
				.put(IendPoints.addnewPetToStore);
			resp.then()
				.spec(respBuild)
				.assertThat().statusCode(200)
				.log().all();
	}
	
	@Test
	public void uploadPetImageTest() {
		
		
		Response resp = given()
				.spec(reqBuild)
				.pathParam("petId", petId )
	            .multiPart("file", new File("src/test/resources/dog.jpg")) // image path
	            .contentType("multipart/form-data") // ✅ Correct type for file upload
	            .accept(ContentType.JSON)
			.when()
				.post(IendPoints.uploadImage);
			resp.then()
				.spec(respBuild)
				.assertThat().statusCode(200)
				.log().all();
	}
	
	
	@Test
	public void findPetByStatusTest() {
		
		
		given()
			.spec(reqBuild)
			.queryParam("status", "available")
		.when()
			.get(IendPoints.findPetsStatus)
		.then()
			.assertThat().statusCode(200)
//			.assertThat().statusLine("successful operation")
			.log().all();
	}
	
	@Test
	public void findPetByIDTest() {
		
		given()
			.spec(reqBuild)
			.pathParam("petId", petId)
		.when()
			.get(IendPoints.findPetByID)
		.then()

			.spec(respBuild)
			.assertThat().statusCode(200)
			.log().all();
	}
	
	
	@Test
	public void updatePetWithFormDataTest() {
		given()
		.spec(reqBuild)
		.pathParam("petId", petId)
		  .contentType("application/x-www-form-urlencoded")
		.formParam("name", "Bairava")
		.header("Accept", "application/json")  // 👈 Force JSON
	.when()
		.post(IendPoints.updatePetInStoreByFormData)
	.then()
		.spec(respBuild)
//		.assertThat().statusCode(200)
		.log().all();
	}
	
	
	@Test
	public void deletePetTest() {
		
		given()
		.spec(reqBuild)
		.pathParam("petId", petId)
		  .header("Accept", "application/json")  // 👈 Force JSON
	.when()
		.delete(IendPoints.deletePet)
	.then()

		.log().all();
	}
	
}
