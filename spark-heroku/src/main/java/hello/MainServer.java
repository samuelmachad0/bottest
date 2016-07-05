package hello;

import static spark.Spark.*;
import spark.ModelAndView;
import java.util.*;
import spark.template.mustache.*;
import com.pengrad.telegrambot.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.pengrad.telegrambot.model.Chat;

public class MainServer {
    public static void main(String[] args) {

		// Get port config of heroku on environment variable
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 8080;
        }
        setPort(port);

		//Delivery static file
		staticFileLocation("/static");





		post("/readMessages", (req, res) -> {
            TelegramBot bot = TelegramBotAdapter.build("194517193:AAFR4ZZosmM2OfxD7kL-XFSulXI-YyfM5Y4");
            byte[] body = req.bodyAsBytes();
            String response = new String(body, "UTF-8");
            JSONObject obj = new JSONObject(response);

            if(obj.toString().indexOf("chat") != -1){

                Update update = BotUtils.parseUpdate(response);
                Message msg = update.message();
                Chat chat = msg.chat();

                SendResponse sendResponse = bot.execute(
                        new SendMessage(chat.id(), "Ola Amiguinho x")
                );

            } else {
                Update update = BotUtils.parseUpdate(obj.toString()); // Decodifica o JSON que vem do telegram

                InlineQuery inlineQuery = update.inlineQuery(); // Pega somente a string a ser buscado (@eng3bot dilma)
 /*
                InlineQueryResult[] resultado = new InlineQueryResult[3]; // Declarou a lista

                List<Artist> artistas  = pegarArtistaPorNome( inlineQuery.query() ); // Buscando no DB4O com parâmetro
                int i=0;
                for ( Artist o : artistas ) {
                    if(i==3)
                        break;

                    resultado[i] =  new InlineQueryResultPhoto(String.valueOf(i), o.getLink(), o.getArte()); // Popula a lista
                    i++;


                }
*/
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{
                                new InlineKeyboardButton("url").url("url"),
                                new InlineKeyboardButton("callback_data").callbackData("callback_data"),
                                new InlineKeyboardButton("switch_inline_query").switchInlineQuery("switch_inline_query")
                        });

                bot.execute(new AnswerInlineQuery(inlineQuery.id(), new InlineQueryResult[]{ new InlineQueryResultArticle("1", "title", "message") } )); // Devolve para o usuário a lista


            }


            return "";
        });
    }
}
