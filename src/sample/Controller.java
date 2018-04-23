package sample;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Calendar;

public class Controller {
    public Label temperatureLabel;
    public ImageView conditionImage;
    public Label conditionLabel;
    public Label todaysHighLabel;
    public Label todaysLowLabel;
    public Label windSpeedLabel;
    public Label windDirectionLabel;
    public Label tomorrowLabel;
    public Label dayAfterTomorrowLabel;
    public Label twoDaysAfterTomorrowLabel;
    public Label tomorrowsHighLabel;
    public Label tomorrowsLowLabel;
    public Label dayAfterTomorrowsLowLabel;
    public Label daysAfterTomorrowsHighLabel;
    public Label twoDaysAfterTomorrowsLowLabel;
    public Label twoDaysAfterTomorrowsHighLabel;
    public ImageView tomorrowsConditionImage;
    public ImageView dayAfterTomorrowsConditionImage;
    public ImageView twoDaysAfterTomorrowsConditionImage;
    public Label tomorrowsConditionsLabel;
    public Label dayAfterTomorrowsConditionLabel;
    public Label twoDaysAfterTomorrowsConditionLabel;

    public String condition;
    public int temp;
    public String high;
    public String low;
    public String windspeed;
    public String windDirection;
    public String tomorrowDay;
    public String dayAfterTomorrow;
    public String twoDaysAfterTomorrow;
    public String tomorrowsHigh;
    public String tomorrowsLow;
    public String tomorrowsCondition;
    public String dayAfterTomorrowsHigh;
    public String dayAfterTomorrowsLow;
    public String dayAfterTomorrowsCondition;
    public String twoDaysAfterTomorrowsHigh;
    public String twoDaysAfterTomorrowsLow;
    public String twoDaysAfterTomorrowsCondition;

    public void initialize(){
        setDayLabels();
        startWeatherService();
    }

    private void setDayLabels() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day){
            case Calendar.SUNDAY:
                tomorrowDay = "Monday";
                dayAfterTomorrow = "Tuesday";
                twoDaysAfterTomorrow = "Wednesday";
                break;
            case Calendar.MONDAY:
                tomorrowDay = "Tuesday";
                dayAfterTomorrow = "Wednesday";
                twoDaysAfterTomorrow = "Thursday";
                break;
            case Calendar.TUESDAY:
                tomorrowDay = "Wednesday";
                dayAfterTomorrow = "Thursday";
                twoDaysAfterTomorrow = "Friday";
                break;
            case Calendar.WEDNESDAY:
                tomorrowDay = "Thursday";
                dayAfterTomorrow = "Friday";
                twoDaysAfterTomorrow = "Saturday";
                break;
            case Calendar.THURSDAY:
                tomorrowDay = "Friday";
                dayAfterTomorrow = "Saturday";
                twoDaysAfterTomorrow = "Sunday";
                break;
            case Calendar.FRIDAY:
                tomorrowDay = "Saturday";
                dayAfterTomorrow = "Sunday";
                twoDaysAfterTomorrow = "Monday";
                break;
            case Calendar.SATURDAY:
                tomorrowDay = "Sunday";
                dayAfterTomorrow = "Monday";
                twoDaysAfterTomorrow = "Tuesday";
                break;
        }

        tomorrowLabel.setText(tomorrowDay + ":");
        dayAfterTomorrowLabel.setText(dayAfterTomorrow + ":");
        twoDaysAfterTomorrowLabel.setText(twoDaysAfterTomorrow + ":");

    }

    private void startWeatherService() {
        Timeline getWeather = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            JsonNode jn = null;
            try {
                jn = Unirest
                        .get(" https://query.yahooapis.com/v1/public/yql")
                        .queryString("format", "json")
                        .queryString("q", "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""+"Kensington, md"+"\")")
                        .asJson()
                        .getBody();
            } catch (UnirestException e1) {
                e1.printStackTrace();
            }
            org.json.JSONObject Condition = jn.getObject()
                    .getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("channel")
                    .getJSONObject("item")
                    .getJSONObject("condition");
            condition = Condition.getString("text");
            temp = Integer.parseInt(Condition.getString("temp"));
            System.out.println("Condition: " + condition);
            System.out.println("Temperature: " + temp);

            org.json.JSONObject Wind = jn.getObject()
                    .getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("channel")
                    .getJSONObject("wind");
            windspeed = Wind.getString("speed");
            windDirection = Wind.getString("direction");
            System.out.println("Windspeed: " + windspeed);
            System.out.println("Wind Direction: " + windDirection);

            org.json.JSONObject todaysForecast = jn.getObject()
                    .getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("channel")
                    .getJSONObject("item")
                    .getJSONArray("forecast")
                    .getJSONObject(0);
            high = todaysForecast.getString("high");
            low = todaysForecast.getString("low");

            System.out.println("High: " + high);
            System.out.println("Low: " + low);

            todaysHighLabel.setText("Todays High: " + high + "° F");
            todaysLowLabel.setText("Todays Low: " + low + "° F");
            windSpeedLabel.setText("Wind Speed: " + windspeed + " MPH");
            windDirectionLabel.setText("Wind Direction: " + windDirection + "°");
            conditionLabel.setText(condition);

            org.json.JSONObject tomorrowsForecast = jn.getObject()
                    .getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("channel")
                    .getJSONObject("item")
                    .getJSONArray("forecast")
                    .getJSONObject(1);
            tomorrowsHigh = tomorrowsForecast.getString("high");
            tomorrowsLow = tomorrowsForecast.getString("low");
            tomorrowsCondition = tomorrowsForecast.getString("text");

            org.json.JSONObject dayAfterTomorrowsForecast = jn.getObject()
                    .getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("channel")
                    .getJSONObject("item")
                    .getJSONArray("forecast")
                    .getJSONObject(2);
            dayAfterTomorrowsHigh = dayAfterTomorrowsForecast.getString("high");
            dayAfterTomorrowsLow = dayAfterTomorrowsForecast.getString("low");
            dayAfterTomorrowsCondition = dayAfterTomorrowsForecast.getString("text");

            org.json.JSONObject twoDaysAfterTomorrowsForecast = jn.getObject()
                    .getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("channel")
                    .getJSONObject("item")
                    .getJSONArray("forecast")
                    .getJSONObject(3);
            twoDaysAfterTomorrowsHigh = twoDaysAfterTomorrowsForecast.getString("high");
            twoDaysAfterTomorrowsLow = twoDaysAfterTomorrowsForecast.getString("low");
            twoDaysAfterTomorrowsCondition = twoDaysAfterTomorrowsForecast.getString("text");

            tomorrowsHighLabel.setText("High: " + tomorrowsHigh);
            tomorrowsLowLabel.setText("Low: " + tomorrowsLow);
            tomorrowsConditionsLabel.setText(tomorrowsCondition);
            daysAfterTomorrowsHighLabel.setText("High: " + dayAfterTomorrowsHigh);
            dayAfterTomorrowsLowLabel.setText("Low: " + dayAfterTomorrowsLow);
            dayAfterTomorrowsConditionLabel.setText(dayAfterTomorrowsCondition);
            twoDaysAfterTomorrowsHighLabel.setText("High: " + twoDaysAfterTomorrowsHigh);
            twoDaysAfterTomorrowsLowLabel.setText("Low: " + twoDaysAfterTomorrowsLow);
            twoDaysAfterTomorrowsConditionLabel.setText(twoDaysAfterTomorrowsCondition);
            String icon = "";
            String tomorrowsIcon = "";
            String dayAfterTomorrowsIcon = "";
            String twoDaysAfterTomorrowsIcon = "";

            switch(condition.toLowerCase()){
                case "partly cloudy": icon = "partly-cloudy-day-xxl";
                    break;
                case "mostly cloudy": icon = "partly-cloudy-day-xxl";
                    break;
                case "thunderstorms": icon = "storm-xxl";
                    break;
                case "severe thunderstorms": icon = "storm-xxl";
                    break;
                case "scattered thunderstorms": icon = "storm-xxl";
                    break;
                case "hurricane": icon = "storm-xxl";
                    break;
                case "drizzle": icon = "rain-xxl";
                    break;
                case "showers": icon = "rain-xxl";
                    break;
                case "snow flurries": icon = "snow-storm-xxl";
                    break;
                case "blowing snow": icon = "snow-storm-xxl";
                    break;
                case "light snow showers": icon = "snow-storm-xxl";
                    break;
                case "snow": icon = "snow-storm-xxl";
                    break;
                case "heavy snow": icon = "snow-storm-xxl";
                    break;
                case "foggy": icon = "fog-day-xxl";
                    break;
                case "cloudy": icon = "cloud-4-512";
                    break;
                case "clear": icon = "sun-512-xxl";
                    break;
                case "sunny": icon = "sun-512-xxl";
                    break;
                default: icon = "sun-512";
                    break;
            }

            switch(tomorrowsCondition.toLowerCase()){
                case "partly cloudy": tomorrowsIcon = "partly-cloudy-day-xxl";
                    break;
                case "mostly cloudy": tomorrowsIcon = "partly-cloudy-day-xxl";
                    break;
                case "thunderstorms": tomorrowsIcon = "storm-xxl";
                    break;
                case "severe thunderstorms": tomorrowsIcon = "storm-xxl";
                    break;
                case "scattered thunderstorms": tomorrowsIcon = "storm-xxl";
                    break;
                case "hurricane": tomorrowsIcon = "storm-xxl";
                    break;
                case "drizzle": tomorrowsIcon = "rain-xxl";
                    break;
                case "showers": tomorrowsIcon = "rain-xxl";
                    break;
                case "snow flurries": tomorrowsIcon = "snow-storm-xxl";
                    break;
                case "blowing snow": tomorrowsIcon = "snow-storm-xxl";
                    break;
                case "light snow showers": tomorrowsIcon = "snow-storm-xxl";
                    break;
                case "snow": tomorrowsIcon = "snow-storm-xxl";
                    break;
                case "heavy snow": tomorrowsIcon = "snow-storm-xxl";
                    break;
                case "foggy": tomorrowsIcon = "fog-day-xxl";
                    break;
                case "cloudy": tomorrowsIcon = "cloud-4-512";
                    break;
                case "clear": tomorrowsIcon = "sun-512-xxl";
                    break;
                case "sunny": tomorrowsIcon = "sun-512-xxl";
                    break;
                default: tomorrowsIcon = "sun-512";
                    break;
            }

            switch(dayAfterTomorrowsCondition.toLowerCase()){
                case "partly cloudy": dayAfterTomorrowsIcon = "partly-cloudy-day-xxl";
                    break;
                case "mostly cloudy": dayAfterTomorrowsIcon = "partly-cloudy-day-xxl";
                    break;
                case "thunderstorms": dayAfterTomorrowsIcon = "storm-xxl";
                    break;
                case "severe thunderstorms": dayAfterTomorrowsIcon = "storm-xxl";
                    break;
                case "scattered thunderstorms": dayAfterTomorrowsIcon = "storm-xxl";
                    break;
                case "hurricane": dayAfterTomorrowsIcon = "storm-xxl";
                    break;
                case "drizzle": dayAfterTomorrowsIcon = "rain-xxl";
                    break;
                case "showers": dayAfterTomorrowsIcon = "rain-xxl";
                    break;
                case "snow flurries": dayAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "blowing snow": dayAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "light snow showers": dayAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "snow": dayAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "heavy snow": dayAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "foggy": dayAfterTomorrowsIcon = "fog-day-xxl";
                    break;
                case "cloudy": dayAfterTomorrowsIcon = "cloud-4-512";
                    break;
                case "clear": dayAfterTomorrowsIcon = "sun-512-xxl";
                    break;
                case "sunny": dayAfterTomorrowsIcon = "sun-512-xxl";
                    break;
                default: dayAfterTomorrowsIcon = "sun-512";
                    break;
            }

            switch(twoDaysAfterTomorrowsCondition.toLowerCase()){
                case "partly cloudy": twoDaysAfterTomorrowsIcon = "partly-cloudy-day-xxl";
                    break;
                case "mostly cloudy": twoDaysAfterTomorrowsIcon = "partly-cloudy-day-xxl";
                    break;
                case "thunderstorms": twoDaysAfterTomorrowsIcon = "storm-xxl";
                    break;
                case "severe thunderstorms": twoDaysAfterTomorrowsIcon = "storm-xxl";
                    break;
                case "scattered thunderstorms": twoDaysAfterTomorrowsIcon = "storm-xxl";
                    break;
                case "hurricane": twoDaysAfterTomorrowsIcon = "storm-xxl";
                    break;
                case "drizzle": twoDaysAfterTomorrowsIcon = "rain-xxl";
                    break;
                case "showers": twoDaysAfterTomorrowsIcon = "rain-xxl";
                    break;
                case "snow flurries": twoDaysAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "blowing snow": twoDaysAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "light snow showers": twoDaysAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "snow": twoDaysAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "heavy snow": twoDaysAfterTomorrowsIcon = "snow-storm-xxl";
                    break;
                case "foggy": twoDaysAfterTomorrowsIcon = "fog-day-xxl";
                    break;
                case "cloudy": twoDaysAfterTomorrowsIcon = "cloud-4-512";
                    break;
                case "clear": twoDaysAfterTomorrowsIcon = "sun-512-xxl";
                    break;
                case "sunny": twoDaysAfterTomorrowsIcon = "sun-512-xxl";
                    break;
                default: twoDaysAfterTomorrowsIcon = "sun-512";
                    break;
            }

            System.out.println(tomorrowsCondition);
            System.out.println(dayAfterTomorrowsCondition);
            System.out.println(twoDaysAfterTomorrowsCondition);

            File file = new File("Resources/Icons/Weather/" +icon+".png");
            try {
                Image iconImage = new Image(file.toURI().toURL().toString());
                conditionImage.setImage(iconImage);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }

            File file1 = new File("Resources/Icons/Weather/" +tomorrowsIcon+".png");
            try {
                Image iconImage = new Image(file1.toURI().toURL().toString());
                tomorrowsConditionImage.setImage(iconImage);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }

            File file2 = new File("Resources/Icons/Weather/" +dayAfterTomorrowsIcon+".png");
            try {
                Image iconImage = new Image(file2.toURI().toURL().toString());
                dayAfterTomorrowsConditionImage.setImage(iconImage);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }

            File file3 = new File("Resources/Icons/Weather/" +twoDaysAfterTomorrowsIcon+".png");
            try {
                Image iconImage = new Image(file3.toURI().toURL().toString());
                twoDaysAfterTomorrowsConditionImage.setImage(iconImage);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        }),
                new KeyFrame(Duration.seconds(60))
        );
        getWeather.setCycleCount(Animation.INDEFINITE);
        getWeather.play();
    }
}
