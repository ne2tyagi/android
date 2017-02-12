package model;

/**
 * Created by neetu on 12/02/17.
 */

public class Weather {
    public Place place;
    public String iconData;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temprature temprature = new Temprature();
    public Wind wind = new Wind();
    public Snow snow = new Snow();
    public Clouds clouds = new Clouds();
}
