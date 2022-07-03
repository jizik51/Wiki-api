import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;
import org.json.simple.parser.ParseException;


public class Wiki {
    public static void main(String[] args) throws MalformedURLException {
        String subject = " ";
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter a request ");
        URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&list=search&srsearch=\""+ CheckValidData(scanner)+"\"&srlimit=2");
        String text = "";
        text = convertUrlToString(url);
        url = new URL(("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=1&explaintext=1&titles=" + outString(text).replace(" ", "%20")));
        text = convertUrlToString(url);
        System.out.println(convertUrlToString(url).toString());
        System.out.println(outRes(text).toString());

    }

    private static JSONObject createJSONObject(String jsonString){
        JSONObject  jsonObject=new JSONObject();
        JSONParser jsonParser=new  JSONParser();
        if ((jsonString != null) && !(jsonString.isEmpty())) {
            try {
                jsonObject=(JSONObject) jsonParser.parse(jsonString);
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    private  static  JSONArray createJSONArr(String str){
        JSONArray jsonArr = new JSONArray();
        JSONParser jsonParser=new  JSONParser();
        try {
            jsonArr = (JSONArray) jsonParser.parse(str);
        }catch (org.json.simple.parser.ParseException e){
            e.printStackTrace();
        }
        return  jsonArr;
    }
    private static  String outString(String UrlApi){
        JSONParser parser = new JSONParser();
        JsonObject object = new JsonObject();
        JSONObject jsonObject = new JSONObject();

        jsonObject =  createJSONObject(UrlApi);
        UrlApi =  jsonObject.get("query").toString();
        jsonObject =  createJSONObject(UrlApi);
        UrlApi = jsonObject.get("search").toString();
        JSONArray jsonArray = new JSONArray();
        jsonArray = createJSONArr(UrlApi);
        UrlApi = jsonArray.get(0).toString();
        jsonObject = createJSONObject(UrlApi);
        return jsonObject.get("title").toString();
    }

    private static  String convertUrlToString(URL url){
        String text ="";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
            String line = null;
            while (null != (line = br.readLine())) {
                line = line.trim();
                text += line;

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    private static String outRes(String text){
        JSONParser parser = new JSONParser();
        JSONObject object = new JSONObject();

        object = convertToJSONobj(text);
        text = object.get("query").toString();

        object = convertToJSONobj(text);
        text = object.get("pages").toString();

        System.out.println(object.get("pages"));

        int firstIndex = 0;
        int secondIndex = 0;
        String strWithoutTrim  = text.trim();
        for(int i = 1; i < text.length(); i++){
            char g = text.charAt(i);
            if( g == ':'){break;}
            if( g == '"'){
                firstIndex = text.indexOf('"');
                secondIndex = text.indexOf('"', 2);
            }
        }
        strWithoutTrim = text.substring(firstIndex, secondIndex);
        strWithoutTrim =  strWithoutTrim.replace('"', ' ').trim();

        object = convertToJSONobj(text);
        text = object.get(strWithoutTrim).toString();

        object = convertToJSONobj(text);
        text = object.get("extract").toString();

        return object.get("extract").toString();
    }

    private static JSONObject convertToJSONobj(String text){
        JSONObject object = new JSONObject();
        JSONParser parser = new JSONParser();

        try {
            object=(JSONObject) parser.parse(text);
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return object;
    }

    private static String CheckValidData(Scanner scanner){
        String str = scanner.next();
        if (!str.equals(" ")){
            return str;
        }
        else{
            System.out.print("Invalid request ");
            return CheckValidData(scanner);
        }
    }
}
