import java.io.IOException;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class Trail
{
  private String name;
  private String street;
  private String town;
  private String state;
  private String parkingStatus;
  private double[] latLng;

  public Trail(String name, String street, String town, String state,
    String parkingStatus)
  {
    this.name = name;
    this.street = street;
    this.town = town;
    this.state = state;
    this.parkingStatus = parkingStatus;
    latLng = new double[2];
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getAddress()
  {
    return street + ", " + town + ", " + state;
  }

  public String getParkingStatus()
  {
    return parkingStatus;
  }

  public void setParkingStatus(String parkingStatus)
  {
    this.parkingStatus = parkingStatus;
  }

  public void setLatLong() throws IOException
  {
    // get API Data from mapquest, convert to JSON
    HttpResponse<JsonNode> response =
      Unirest.post("http://www.mapquestapi.com/geocoding/v1/address")
        .header("accept", "application/json").queryString("key", Main.API_KEY)
        .field("location", getAddress()).asJson();
    String rawJson = response.getBody().toPrettyString();

    // extract latitude / longitude from rawJson text
    String latitude = rawJson.substring(rawJson.indexOf("\"lat\": "));
    latitude =
      latitude.substring(latitude.indexOf(" ") + 1, latitude.indexOf(","));
    String longitude = rawJson.substring(rawJson.indexOf("\"lng\": "));
    longitude =
      longitude.substring(longitude.indexOf(" ") + 1, longitude.indexOf("\n"));

    latLng[0] = Double.parseDouble(latitude);
    latLng[1] = Double.parseDouble(longitude);
  }

  public double[] getLatLong()
  {
    return latLng;
  }
}
