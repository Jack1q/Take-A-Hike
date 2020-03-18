import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
  public static final String API_KEY = "****************************";
  public static final double EARTH_RADIUS = 6371; // approximation
  public static final double KM_TO_MILE = 0.621371;

  public static void main(String[] args) throws IOException
  {
    Scanner s = new Scanner(System.in);
    System.out.print("Enter street: ");
    String street = s.nextLine();
    System.out.print("Enter town: ");
    String town = s.nextLine();
    System.out.print("Enter two-letter state code: ");
    String stateCode = s.nextLine();
    Trail home = new Trail("Current location", street, town, stateCode, "n/a");
    home.setLatLong();

    ArrayList<Trail> trails = getTrails("trails.csv");
    sortTrails(trails, home);
    for (int i = 0; i < trails.size(); i++)
    {
      double distance =
        (int) (getDistance(home.getLatLong()[0], home.getLatLong()[1],
          trails.get(i).getLatLong()[0], trails.get(i).getLatLong()[1]) * 100)
          / 100.0;
      System.out.println(trails.get(i).getName() + ": " + distance
        + " miles away at " + trails.get(i).getAddress() + " | Parking: "
        + trails.get(i).getParkingStatus());
    }
  }

  public static ArrayList<Trail> getTrails(String fileName) throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    ArrayList<Trail> trails = new ArrayList<>();

    String line = br.readLine();
    int count = 0;
    while (line != null)
    {
      String[] data = line.split(", ");
      trails.add(new Trail(data[0], data[1], data[2], data[3], data[4]));
      trails.get(count).setLatLong();
      line = br.readLine();
      count++;
    }
    return trails;
  }

  public static ArrayList<Trail> sortTrails(ArrayList<Trail> trails, Trail home)
  {
    trails.set(0, home);
    double homeLat = home.getLatLong()[0];
    double homeLng = home.getLatLong()[1];
    int n = trails.size();
    for (int i = 0; i < n - 1; i++)
    {
      for (int j = 0; j < n - i - 1; j++)
      {
        if (getDistance(homeLat, homeLng, trails.get(j).getLatLong()[0],
          trails.get(j).getLatLong()[1]) > getDistance(homeLat, homeLng,
            trails.get(j + 1).getLatLong()[0],
            trails.get(j + 1).getLatLong()[1]))
        {
          Trail temp = trails.get(j);
          trails.set(j, trails.get(j + 1));
          trails.set(j + 1, temp);
        }
      }
    }
    trails.remove(home);

    return trails;
  }

  public static double getDistance(double currentLat, double currentLng,
    double trailLat, double trailLng)
  {
    double distance = // haversine formula
      2 * EARTH_RADIUS
        * Math.asin(Math.sqrt(
          Math.pow(Math.sin(Math.toRadians(currentLat - trailLat) / 2.0), 2)
            + Math.pow(Math.sin(Math.toRadians(currentLng - trailLng) / 2.0), 2)
              * Math.cos(Math.toRadians(trailLat))
              * Math.cos(Math.toRadians(currentLat))));

    return distance * KM_TO_MILE; // convert to miles
  }

}
