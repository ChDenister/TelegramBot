import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    /**
     * Метод получает на вход сообщение и модель. Возвращает ответ в виде набора (город, температура, влажность, картинка).
     *
     * @param message Сообщение, которое пришло боту.
     * @param model   Модель
     */
    public static String getWeather(String message, Model model) throws IOException {

        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=f8bc106e2e018462071f0d7920c2def9");
        //Простой текстовый сканер, который может анализировать примитивные типы и строки с использованием регулярных выражений.
        //Получает содержимое этого URL-адреса.
        // Scanner(String source), где source – источник входных данных
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        // считывает целое число с потока ввода и сохраняем в переменную
        while (in.hasNext()) {
            //Имеется также метод nextLine(), позволяющий считывать целую последовательность символов,
            // т.е. строку, а, значит, полученное через этот метод значение нужно сохранять в объекте класса String
            result += in.nextLine();
        }
        System.out.println(result);

//        Кастим в объект наш ответ
        JSONObject jobj = new JSONObject(result);
//      Забиваем имя
        model.setName(jobj.getString("name"));

        JSONObject main = jobj.getJSONObject("main");
//        Забиваем температура и влажность
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

//        Пробегаем по массиву weather
        JSONArray getArray = jobj.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject objects = getArray.getJSONObject(i);
            //Забиваем иконку
            model.setIcon((String) objects.get("icon"));
            model.setMain((String) objects.get("main"));

        }

        return "City: " + model.getName() + "\n" +
                "Temperature: " + model.getTemp() + "C" + "\n" +
                "Humidity: " + model.getHumidity() + "%" + "\n" +
                "Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + model.getIcon() + ".png";
    }
}
